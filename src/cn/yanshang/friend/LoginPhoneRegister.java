package cn.yanshang.friend;

//import static cn.smssdk.framework.utils.R.getStringRes;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

//import cn.smssdk.EventHandler;
//import cn.smssdk.SMSSDK;
import cn.yanshang.friend.R.string;
import cn.yanshang.friend.common.Constants;
import cn.yanshang.friend.common.Shared;
import cn.yanshang.friend.connect.BaseInfo;
import cn.yanshang.friend.connect.BaseListener;
import cn.yanshang.friend.connect.HttpConnect;
import cn.yanshang.friend.info.RegisterUserInfo;
import cn.yanshang.friend.utils.CommonUtils;
import cn.yanshang.friend.utils.ProgressUtil;
import android.app.ActionBar;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginPhoneRegister extends Activity implements OnClickListener,
		BaseListener {

	private Button btnRegister;
	private Button btnCheck;
	private TextView btnLoginText;

	private EditText inputPhoneNum;
	private EditText inputPassword;
	private EditText inputCheck;

	private ProgressDialog mProgress;
	private Activity mActivity;
	// private EventHandler mEventHandler;
	private String mPhoneNum;

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
		recLen = 60;

		btnCheck.setEnabled(false);
		// btnRegister.setEnabled(false);

		Message message = handler.obtainMessage(1);
		handler.sendMessageDelayed(message, 1000);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_phone_register);
		setActionBarLayout(R.layout.custom_actionbar_layout);

		findViewById(R.id.TitleBarCancel).setOnClickListener(this);

		mActivity = this;

		initBase();

	}

	private void initBase() {

		btnRegister = (Button) findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(this);
		btnRegister.setEnabled(false);

		btnCheck = (Button) findViewById(R.id.btnCheck);
		btnCheck.setOnClickListener(this);
		btnCheck.setEnabled(false);

		btnLoginText = (TextView) findViewById(R.id.loginText);
		btnLoginText.setOnClickListener(this);

		inputPassword = (EditText) findViewById(R.id.inputPassword);

		inputPassword.addTextChangedListener(new TextWatcher() {
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
				// String temp = s.toString();
				// if(s.toString().getBytes().length != s.length()){
				// s.delete(temp.length()-1, temp.length());
				// }
			}
		});

		inputCheck = (EditText) findViewById(R.id.inputCheck);

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
					// btnRegister.setEnabled(true);
					btnCheck.setEnabled(true);
				} else {
					// btnRegister.setEnabled(false);
					btnCheck.setEnabled(false);
				}
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	public void setActionBarLayout(int layoutId) {
		ActionBar actionBar = getActionBar();
		if (null != actionBar) {
			actionBar.setDisplayShowHomeEnabled(false);
			actionBar.setDisplayShowCustomEnabled(true);
			// actionBar.setDisplayHomeAsUpEnabled(true);////

			LayoutInflater inflator = (LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = inflator.inflate(layoutId, null);
			ActionBar.LayoutParams layout = new ActionBar.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			actionBar.setCustomView(v, layout);
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.TitleBarCancel:
			Toast.makeText(this, "0", Toast.LENGTH_SHORT).show();
			this.finish();
			break;
		case R.id.btnCheck:
			// Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
			connectServerIsValid();
			// RequestVerificationCode();
			break;
		case R.id.btnRegister:{
			goRegister();
		}
			break;
		case R.id.loginText: {
			Intent it = new Intent();
			it.setClass(this, LoginPhone.class);
			startActivity(it);
		}
			break;
		default:
			Toast.makeText(this, "00", Toast.LENGTH_SHORT).show();
			break;
		}
	}

	private void goRegister() {

		String phone = inputPhoneNum.getText().toString();
		String password = inputPassword.getText().toString();
		String verifyCode = inputCheck.getText().toString();

		//密码判断
		if (password.length() < 8 || password.length()>20 || !CommonUtils.isPasswordValid(password)) {
			ProgressUtil.showDialog(this,
					getResources().getString(R.string.dialog_title_1),
					getResources().getString(R.string.dialog_password_1));
			return;
		}

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
								// if (mTokenTask != null) {
								// mTokenTask.doCancel();
								// }
							}
						});

				connectServerRegister(phone, password, verifyCode);
			}
		} else {
			ProgressUtil.showDialog(this,
					getResources().getString(R.string.dialog_title_1),
					getResources().getString(R.string.dialog_phone_num_2));
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
						// if (mTokenTask != null) {
						// mTokenTask.doCancel();
						// }
					}
				});

		RegisterUserInfo bInfo = new RegisterUserInfo();

		countDown();
		// 获取验证码
		HttpConnect.newInstance().doPost(this,
				bInfo.getJsonString(keyValueMap, "register-with-phone"), bInfo,
				Constants.URL_POST_SEND_PHONE_CODE, this);
	}

	private void connectServerRegister(String phoneNum, String password,String verifyCode) {
		
		Map<String, String> keyValueMap = new HashMap<String, String>();
		keyValueMap.put("phone", phoneNum);
		keyValueMap.put("password", password);
		keyValueMap.put("code", verifyCode);

		RegisterUserInfo bInfo = new RegisterUserInfo();
		
		//登录
		HttpConnect.newInstance().doPost(this,
				bInfo.getJsonString(keyValueMap, null), bInfo,
				Constants.URL_POST_USER_REGISTER, this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// SMSSDK.unregisterEventHandler(mEventHandler);
	}

	@Override
	public void onGotInfo(BaseInfo objInfo) {
		// TODO Auto-generated method stub
		RegisterUserInfo bInfo = (RegisterUserInfo) objInfo;

		if (bInfo == null) {
			ProgressUtil.dismiss(mProgress);
			Toast.makeText(this, "onGotInfoErr=" + "bInfo==null",
					Toast.LENGTH_SHORT).show();
			return;
		} else if (bInfo.getStatus() == Constants.HTTP_STATUS_TIMEOUT) {
			ProgressUtil.dismiss(mProgress);
			Toast.makeText(this, "onGotInfoErr=网络链接超时", Toast.LENGTH_SHORT)
					.show();
			return;
		}//网络异常
 
		if (bInfo.getStatus() != Constants.HTTP_STATUS_OK) {
			if (bInfo.getStatus() == Constants.HTTP_STATUS_VALID_FALSE) {
				Toast.makeText(this, "此号码已经注册过", Toast.LENGTH_SHORT).show();
			} else if (bInfo.getStatus() == Constants.HTTP_STATUS_VALID_TURE) {
				btnRegister.setEnabled(true);

				mPhoneNum = bInfo.getPhoneNum();
				
				//测试用
				inputCheck.setText(bInfo.getCode());
			} else {
				Toast.makeText(this, "onGotInfoErr=" + bInfo.getStatus(),
						Toast.LENGTH_SHORT).show();
			}

			ProgressUtil.dismiss(mProgress);
			return;
		}

		

		if (bInfo.getStatus() == Constants.HTTP_STATUS_OK) {
			//保存登录信息
			saveLoginInfo(bInfo);
			
			Intent it = new Intent();
			it.setClass(this, cn.yanshang.friend.mainui.MainActivity.class);
			startActivity(it);

			Toast.makeText(this, "onGotInfo=" + bInfo.getPhoneNum(),
					Toast.LENGTH_SHORT).show();
			//this.finish();
		} else {
			Toast.makeText(this, "onGotInfoErr=" + bInfo.getStatus(),
					Toast.LENGTH_SHORT).show();
		}
		
		ProgressUtil.dismiss(mProgress);
	}
	
	private void saveLoginInfo(RegisterUserInfo bInfo){
		
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


