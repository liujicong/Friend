package cn.yanshang.friend;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

//import cn.smssdk.EventHandler;
//import cn.smssdk.SMSSDK;
import cn.yanshang.friend.common.MyConstants;
import cn.yanshang.friend.common.Shared;
import cn.yanshang.friend.connect.BaseInfo;
import cn.yanshang.friend.connect.BaseListener;
import cn.yanshang.friend.connect.HttpConnect;
import cn.yanshang.friend.info.BindPhoneInfo;
import cn.yanshang.friend.info.RegisterUserInfo;
import cn.yanshang.friend.utils.CommonUtils;
import cn.yanshang.friend.utils.ProgressUtil;
import android.R.integer;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginBundPhone extends Activity implements OnClickListener,
		BaseListener {

	private Button btnBund;
	private Button btnCheck;
	private EditText inputPhoneNum;
	private EditText inputCheck;

	private Activity mActivity;
	private ProgressDialog mProgress;

	private String mPhoneNum;
	private String mOpenId;

	private int recLen = 2;

	final Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				recLen--;
				// txtView.setText("" + recLen);

				if (recLen > 0) {
					btnCheck.setText(mActivity
							.getString(R.string.tip_check_send)
							+ "("
							+ recLen
							+ ")");
					Message message = handler.obtainMessage(1);
					handler.sendMessageDelayed(message, 1000);
				} else {
					// txtView.setVisibility(View.GONE);
					btnCheck.setEnabled(true);
					// btnRegister.setEnabled(true);
					btnCheck.setText(R.string.get_phone_check_num);
				}
			}

			super.handleMessage(msg);
		}
	};

	private void countDown() {
		recLen = 12;

		btnCheck.setEnabled(false);

		Message message = handler.obtainMessage(1);
		handler.sendMessageDelayed(message, 1000);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_bund_phone);

		mActivity = this;
		initBase();
	}

	private void initBase() {

		Intent intent = this.getIntent();
		mOpenId = intent.getStringExtra("openid");

		btnBund = (Button) findViewById(R.id.btnBund);
		btnBund.setOnClickListener(this);
		btnBund.setEnabled(false);

		btnCheck = (Button) findViewById(R.id.btnCheck);
		btnCheck.setOnClickListener(this);
		btnCheck.setEnabled(false);

		findViewById(R.id.btnTitleCancel).setOnClickListener(this);

		inputCheck = (EditText) findViewById(R.id.inputCheck);
		TextView contentText = (TextView) findViewById(R.id.customTitleContent);
		contentText.setText(R.string.login_bundphone);

		inputPhoneNum = (EditText) findViewById(R.id.inputPhoneNum);
		inputPhoneNum.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (s.length() > 0) {
					btnBund.setEnabled(true);
					btnCheck.setEnabled(true);
				} else {
					btnBund.setEnabled(false);
					btnCheck.setEnabled(false);
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnTitleCancel:
			this.finish();
			break;
		case R.id.btnCheck:
			connectServerIsValid();
			break;
		case R.id.btnBund:
			goBundPhoneNum();
			break;
		default:
			Toast.makeText(this, "00", Toast.LENGTH_SHORT).show();
			break;
		}
	}

	private void connectServerIsValid() {
		String phone = inputPhoneNum.getText().toString();

		if (!CommonUtils.isPhoneNumberValid(phone)) {
			ProgressUtil.showDialog(this,
					getResources().getString(R.string.dialog_title_1),
					getResources().getString(R.string.dialog_phone_num_1));
			return;
		}

		Map<String, String> keyValueMap = new HashMap<String, String>();
		keyValueMap.put("phone", phone);
		// keyValueMap.put("password", "");

		mProgress = ProgressUtil.show(this, R.string.dialog_title_1,
				R.string.dialog_connect_1, new OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
					}
				});

		BindPhoneInfo bInfo = new BindPhoneInfo();

		countDown();
		// 获取验证码
		HttpConnect.newInstance().doPost(this,
				bInfo.getJsonString(keyValueMap, "bind-with-phone"), bInfo,
				MyConstants.URL_POST_SEND_PHONE_CODE, this);
	}

	private void goBundPhoneNum() {

		String phone = inputPhoneNum.getText().toString();
		String verifyCode = inputCheck.getText().toString();

		if (!CommonUtils.isPhoneNumberValid(phone)) {
			ProgressUtil.showDialog(this,
					getResources().getString(R.string.dialog_title_1),
					getResources().getString(R.string.dialog_phone_num_1));
			return;
		}
		if (mPhoneNum != null && mPhoneNum.equals(phone)) {
			if (TextUtils.isEmpty(verifyCode)) {
				Toast.makeText(this, this.getString(R.string.tip_check_num),
						Toast.LENGTH_SHORT).show();
			} else {
				mProgress = ProgressUtil.show(this, R.string.dialog_title_1,
						R.string.dialog_connect_1, new OnCancelListener() {

							@Override
							public void onCancel(DialogInterface dialog) {
							}
						});

				connectServerBund(phone, verifyCode);
			}
		} else {
			ProgressUtil.showDialog(this,
					getResources().getString(R.string.dialog_title_1),
					getResources().getString(R.string.dialog_phone_num_2));
		}

	}

	private void connectServerBund(String phoneNum, String verifyCode) {

		Map<String, String> keyValueMap = new HashMap<String, String>();
		keyValueMap.put("phone", phoneNum);
		keyValueMap.put("openid", mOpenId);
		keyValueMap.put("code", verifyCode);
		keyValueMap.put("service", "3");

		RegisterUserInfo bInfo = new RegisterUserInfo();

		// 登录
		HttpConnect.newInstance().doPost(this,
				bInfo.getJsonString(keyValueMap, null), bInfo,
				MyConstants.URL_POST_BIND_PHONE, this);
	}

	@Override
	public void onGotInfo(BaseInfo objInfo) {
		// TODO Auto-generated method stub
		BindPhoneInfo bInfo = (BindPhoneInfo) objInfo;

		if (bInfo == null) {
			ProgressUtil.dismiss(mProgress);
			Toast.makeText(this, "onGotInfoErr=" + "bInfo==null",
					Toast.LENGTH_SHORT).show();
			return;
		} else if (bInfo.getStatus() == MyConstants.HTTP_STATUS_VALID_TURE) {
			btnBund.setEnabled(true);
			mPhoneNum = bInfo.getPhoneNum();

			// 测试用
			inputCheck.setText(bInfo.getCode());
		}else if (bInfo.getStatus() == MyConstants.HTTP_STATUS_OK) {
			// 保存登录信息
			saveLoginInfo(bInfo);

			Intent it = new Intent();
			it.setClass(this, cn.yanshang.friend.mainui.MainActivity.class);
			startActivity(it);

			Toast.makeText(this, "onGotInfo=" + bInfo.getStatus(),
					Toast.LENGTH_SHORT).show();
			// this.finish();
		} else {
			CommonUtils.statusError(bInfo.getStatus(), this);
		}

		ProgressUtil.dismiss(mProgress);
	}

	private void saveLoginInfo(BindPhoneInfo bInfo) {

		SharedPreferences preferences = getSharedPreferences(
				Shared.SHARE_LOGIN_ALL_GET, Context.MODE_PRIVATE);

		Editor editor = preferences.edit();
		editor.putInt(Shared.SHARE_LOGIN_UID, bInfo.getUid());
		editor.putString(Shared.SHARE_LOGIN_SID, bInfo.getSid());
		editor.putInt(Shared.SHARE_LOGIN_SERVICE, bInfo.getService());
		editor.putString(Shared.SHARE_LOGIN_REALNAME, bInfo.getRealName());
		editor.putString(Shared.SHARE_LOGIN_NICKNAME, bInfo.getNickName());
		editor.putString(Shared.SHARE_LOGIN_IDCARD, bInfo.getIdCard());
		editor.putString(Shared.SHARE_LOGIN_HEADIMGURL, bInfo.getHeadimgurl());
		editor.putString(Shared.SHARE_LOGIN_SIGNATURE, bInfo.getSignature());

		editor.commit();
	}

}
