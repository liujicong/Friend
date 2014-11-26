package cn.yanshang.friend.borrow;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.yanshang.friend.R;
import cn.yanshang.friend.common.Data;
import cn.yanshang.friend.utils.ProgressUtil;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BorrowReason extends Activity implements
		FragmentReasonScroll.OnClickIconItemAddedListener, OnClickListener {

	private static final String IMAGE_FILE_NAME = "imageBg.jpg";
	private static final int IMAGE_REQUEST_CODE = 100;
	private static final int CAMERA_REQUEST_CODE = 200;
	private static final int RESULT_REQUEST_CODE = 300;

	private Activity mActivity;
	private Data mAppData;
	// private ItemClickEvent itemListener;

	BorrowReasonSelectPopup menuWindow;
	private EditText imageViewEdit;
	private View borrowImgBg;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.borrow_reason_ui);

		mAppData = (Data) getApplication();
		mActivity = this;
		initBase();
	}

	private void initBase() {

		ImageButton TitleBarCancel = (ImageButton) findViewById(R.id.btnTitleCancel);
		TitleBarCancel.setOnClickListener(this);
		Drawable myImage = getResources().getDrawable(
				R.drawable.img_cancel_back);
		TitleBarCancel.setBackgroundDrawable(myImage);

		TextView contentText = (TextView) findViewById(R.id.customTitleContent);
		contentText.setText(R.string.borrow_reason_title);

		findViewById(R.id.btnImageChoose).setOnClickListener(this);

		imageViewEdit = (EditText) findViewById(R.id.tvBorrowReason);
		imageViewEdit.setOnClickListener(this);

		findViewById(R.id.btnNextStep3).setOnClickListener(this);

		borrowImgBg = (View) findViewById(R.id.tvBorrowImgBg);

		if (mAppData.getBorrowReasonStr() != null) {
			imageViewEdit.setText(mAppData.getBorrowReasonStr());
		}
		if (mAppData.getBorrowReasonImg() != null) {
			// imageViewEdit.set//setText(mAppData.getBorrowReasonStr());
			Drawable drawable = new BitmapDrawable(this.getResources(),
					mAppData.getBorrowReasonImg());
			borrowImgBg.setBackgroundDrawable(drawable);
			// imageViewEdit.setBackgroundDrawable(drawable);
		}
	}

	@Override
	public void onNewItemAdded(String newItem) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_CANCELED) {
			switch (requestCode) {
			case IMAGE_REQUEST_CODE:
				startPhotoZoom(data.getData());
				break;
			case CAMERA_REQUEST_CODE:
				// 判断存储卡是否可以用，可用进行存储
				String state = Environment.getExternalStorageState();
				if (state.equals(Environment.MEDIA_MOUNTED)) {
					File path = Environment
							.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
					File tempFile = new File(path, IMAGE_FILE_NAME);
					startPhotoZoom(Uri.fromFile(tempFile));
				} else {
					Toast.makeText(getApplicationContext(), "未找到存储卡，无法存储照片！",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case RESULT_REQUEST_CODE:
				if (data != null) {
					getImageToView(data);
				}
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnTitleCancel: {
			this.finish();
		}
			break;
		case R.id.btnNextStep3: {
			mAppData.setBorrowReasonStr(imageViewEdit.getText().toString());
			// mAppData.setBorrowReasonImg(imgUrl)

			if (mAppData.getIdentity() != null
					&& mAppData.getIdentity().length() > 0) {
				// 判断
				// 跳转到输入身份信息界面
			} else {
				Intent it = new Intent();
				it.setClass(this, BorrowSubmitUI.class);
				startActivity(it);
			}
		}
			break;
		case R.id.btnImageChoose: {
			choosePopup();
		}
			break;
		default:
			break;
		}
	}

	private void choosePopup() {

		OnClickListener itemsOnClick1 = new OnClickListener() {
			public void onClick(View v) {
				menuWindow.dismiss();
				switch (v.getId()) {
				case R.id.btn_take_photo: {
					takePhoto();
				}
					break;
				case R.id.btn_pick_photo: {
					Intent intentFromGallery = new Intent();
					intentFromGallery.setType("image/*");
					intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
					startActivityForResult(intentFromGallery,
							IMAGE_REQUEST_CODE);
				}
					break;
				default:
					break;
				}
			}
		};

		menuWindow = new BorrowReasonSelectPopup(this, itemsOnClick1);
		menuWindow.showAtLocation(this.findViewById(R.id.btnImageChoose),
				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
	}

	private void takePhoto() {
		Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			File path = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
			File file = new File(path, IMAGE_FILE_NAME);
			intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(file));
		}
		startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
	}

	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");

		intent.putExtra("crop", "true");

		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);

		intent.putExtra("outputX", 340);
		intent.putExtra("outputY", 340);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, RESULT_REQUEST_CODE);
	}

	private void getImageToView(Intent data) {
		Bundle extras = data.getExtras();
		// Toast.makeText(getApplicationContext(), "getImageToView",
		// Toast.LENGTH_SHORT).show();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			Drawable drawable = new BitmapDrawable(this.getResources(), photo);
			// imageViewEdit.setBackgroundDrawable(drawable);
			borrowImgBg.setBackgroundDrawable(drawable);

			mAppData.setBorrowReasonImg(photo);
		}
	}

}
