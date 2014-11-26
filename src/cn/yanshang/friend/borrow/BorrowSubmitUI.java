package cn.yanshang.friend.borrow;

import java.util.HashMap;
import java.util.Map;

import cn.yanshang.friend.R;
import cn.yanshang.friend.common.MyConstants;
import cn.yanshang.friend.common.Data;
import cn.yanshang.friend.connect.BaseInfo;
import cn.yanshang.friend.connect.BaseListener;
import cn.yanshang.friend.connect.HttpConnect;
import cn.yanshang.friend.info.BindPhoneInfo;
import cn.yanshang.friend.info.BindPhoneInfotest;
import cn.yanshang.friend.utils.Base64AES;
import android.R.integer;
import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class BorrowSubmitUI extends Activity implements OnClickListener,
		BaseListener {

	private Data mAppData;

	private ImageButton mShareTitle;
	private TextView mBorrowReason;

	private TextView mMoney;
	private TextView mRepaymentDate;
	private TextView mYearInterest;
	private TextView mDeadline;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.borrow_submit_ui);

		mAppData = (Data) getApplication();
		// mActivity = this;

		initBase();
	}

	private void initBase() {

		findViewById(R.id.btnTitleCancel).setVisibility(View.GONE);
		// TitleBarCancel.setVisibility(View.GONE);.setVisibility(View.GONE)
		// Drawable myImage = getResources().getDrawable(
		// R.drawable.img_cancel_back);
		// TitleBarCancel.setBackgroundDrawable(myImage);

		TextView contentText = (TextView) findViewById(R.id.customTitleContent);
		contentText.setText(R.string.borrow_submit_title);

		mShareTitle = (ImageButton) findViewById(R.id.btnTitleSet);
		mShareTitle.setOnClickListener(this);
		mShareTitle.setVisibility(View.VISIBLE);

		findViewById(R.id.btnSubmitOk).setOnClickListener(this);
		findViewById(R.id.btnBackModify).setOnClickListener(this);

		mBorrowReason = (TextView) findViewById(R.id.tvBorrowReason);
		mBorrowReason.setVisibility(View.GONE);

		mMoney = (TextView) findViewById(R.id.textMoney);
		mRepaymentDate = (TextView) findViewById(R.id.textRepaymentDate);
		mYearInterest = (TextView) findViewById(R.id.textYearInterest);
		mDeadline = (TextView) findViewById(R.id.textDeadline);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		boolean isVisible = false;

		if (mAppData.getBorrowReasonStr() != null
				&& mAppData.getBorrowReasonStr().length() > 0) {
			mBorrowReason.setText(mAppData.getBorrowReasonStr());
			isVisible = true;
		}

		if (mAppData.getBorrowReasonImg() != null) {
			Drawable drawable = new BitmapDrawable(this.getResources(),
					mAppData.getBorrowReasonImg());
			mBorrowReason.setBackgroundDrawable(drawable);

			DisplayMetrics outMetrics = new DisplayMetrics();
			this.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
			int heigh = (int) outMetrics.density * 300;
			mBorrowReason.setHeight(heigh);

			isVisible = true;
		}

		if (isVisible) {
			mBorrowReason.setVisibility(View.VISIBLE);
		} else {
			mBorrowReason.setVisibility(View.GONE);
		}

		if (mAppData.getInfoMoney() != null) {
			mMoney.setText(mAppData.getInfoMoney()
					+ getResources().getString(R.string.borrow_yuan));
			mRepaymentDate.setText(mAppData.getInfoDate());
			mYearInterest.setText(mAppData.getInfoInerest() + "%");
			mDeadline.setText(mAppData.getInfoDeadline()
					+ getResources().getString(R.string.borrow_day));
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnBackModify: {
			this.finish();
		}
			break;
		case R.id.btnSubmitOk: {
			connectServer();
		}
			break;
		case R.id.btnTitleSet: {
			// choosePopup();
		}
			break;
		default:
			break;
		}
	}

	private void connectServer() {
		// byte[] btInput = Base64AES.inputStream2byte(content);
		// byte[] btBase = Base64AES.decodeBase64(btInput);
		// byte[] btAES = Base64AES.decryptAES(btBase,
		// Constants.AES_CODE_KEY_ROAD);
		//
		//
		// byte[] btBase123 = Base64AES.encodeBase64(btInput);
		Toast.makeText(this, "00 base64", Toast.LENGTH_SHORT).show();

		if (mAppData.getBorrowReasonImg() != null) {
			String ddString = Base64AES.bitmapToBase64(mAppData
					.getBorrowReasonImg());

			Map<String, String> keyValueMap = new HashMap<String, String>();
			keyValueMap.put("imgName", "imageOK");
			keyValueMap.put("img", ddString);

			BindPhoneInfotest bInfo = new BindPhoneInfotest();

			HttpConnect.newInstance().doPost(this,
					bInfo.getJsonString(keyValueMap), bInfo,
					MyConstants.URL_POST_test, this);
		}

	}

	@Override
	public void onGotInfo(BaseInfo objInfo) {
		// TODO Auto-generated method stub

	}
}
