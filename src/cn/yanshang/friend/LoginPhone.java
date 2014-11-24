package cn.yanshang.friend;

import java.util.HashMap;
import java.util.Map;

import cn.yanshang.friend.common.Constants;
import cn.yanshang.friend.common.Shared;
import cn.yanshang.friend.connect.BaseInfo;
import cn.yanshang.friend.connect.BaseListener;
import cn.yanshang.friend.connect.HttpConnect;
import cn.yanshang.friend.info.BindPhoneInfo;
import cn.yanshang.friend.info.LoginNowInfo;
import cn.yanshang.friend.info.RegisterUserInfo;
import cn.yanshang.friend.utils.CommonUtils;
import cn.yanshang.friend.utils.ProgressUtil;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class LoginPhone extends Activity implements OnClickListener, BaseListener {

	private Button btnlogin;
	//private Button btnCheck;
	private TextView texLoginFind; 
	
	private EditText inputPhoneNum;
	private EditText inputPassword;
	
	private ProgressDialog mProgress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_phone_now);
		
		initBase();
//		mActivity = this;
	}
	
	
	private void initBase() {

		btnlogin = (Button) findViewById(R.id.btnLogin);
		btnlogin.setOnClickListener(this);
		btnlogin.setEnabled(false);

		ImageButton TitleBarCancel = (ImageButton)findViewById(R.id.btnTitleCancel);
		TitleBarCancel.setOnClickListener(this);
		Drawable myImage = getResources().getDrawable(R.drawable.img_cancel_back);
		TitleBarCancel.setBackgroundDrawable(myImage);
		
		TextView contentText = (TextView) findViewById(R.id.customTitleContent);
		contentText.setText(R.string.login_text);
		
		texLoginFind = (TextView) findViewById(R.id.findPasswordText);
		texLoginFind.setOnClickListener(this);
		
		inputPassword = (EditText) findViewById(R.id.inputPassword);
		
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
					btnlogin.setEnabled(true);
				} else {
					btnlogin.setEnabled(false);
				}
			}
		});
	}
	
	@Override
	public void onGotInfo(BaseInfo objInfo) {
		// TODO Auto-generated method stub
		LoginNowInfo bInfo = (LoginNowInfo)objInfo;
		
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
		
		switch (bInfo.getStatus()) {
		case Constants.HTTP_STATUS_OK:{
			saveLoginInfo(bInfo);
			
			Intent it = new Intent();
			it.setClass(this, cn.yanshang.friend.mainui.MainActivity.class);
			startActivity(it);
		}
			break;
		case Constants.HTTP_STATUS_ACCOUNT_ERROR:
			Toast.makeText(this, "onGotInfoErr=账号不正确", Toast.LENGTH_SHORT).show();
			break;
		case Constants.HTTP_STATUS_PASSWORD_ERROR:
			Toast.makeText(this, "onGotInfoErr=密码错误", Toast.LENGTH_SHORT).show();
			break;
		case Constants.HTTP_STATUS_FREQUENTLY_ERROR:
			Toast.makeText(this, "onGotInfoErr=登录频繁", Toast.LENGTH_SHORT).show();
			break;
			
		default:
			Toast.makeText(this, "bInfo.getStatus()="+bInfo.getStatus(), Toast.LENGTH_SHORT).show();
			break;
		}
		
		ProgressUtil.dismiss(mProgress);
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnTitleCancel:
			this.finish();
			break;
		case R.id.btnLogin:
			//RequestVerificationCode();
			loginConnectServer();
			break;
		case R.id.findPasswordText:
		{
			Intent it=new Intent();
			it.setClass(this, LoginFindPassword.class);
			startActivity(it);
		}
			break;
			
		default:
			Toast.makeText(this, "00", Toast.LENGTH_SHORT).show();
			break;
		}
	}
	
	private void loginConnectServer()
	{
		String phone = inputPhoneNum.getText().toString();
		String password = inputPassword.getText().toString();
		
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

    	Map<String,String> keyValueMap = new HashMap<String,String>();
    	keyValueMap.put("phone", inputPhoneNum.getText().toString());
    	keyValueMap.put("password", inputPassword.getText().toString());
		
    	LoginNowInfo bInfo = new LoginNowInfo();

		mProgress = ProgressUtil.show(this, R.string.dialog_title_1,
				R.string.dialog_connect_1, new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						// if (mTokenTask != null) {
						// mTokenTask.doCancel();
						// }
					}
				});
		
		HttpConnect.newInstance().doPost(this, bInfo.getJsonString(keyValueMap), bInfo, Constants.URL_POST_USER_LOGIN, this);
		
	}
	
	private void saveLoginInfo(LoginNowInfo bInfo){
		
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









