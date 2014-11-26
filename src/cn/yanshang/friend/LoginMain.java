package cn.yanshang.friend;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
//import com.tencent.connect.common.Constants;

//import cn.sharesdk.facebook.Facebook;
//import cn.sharesdk.demo.tpl.R;
//import cn.sharesdk.framework.Platform;
//import cn.sharesdk.framework.PlatformActionListener;
//import cn.sharesdk.framework.ShareSDK;
//import cn.sharesdk.tencent.qq.QQ;
//import cn.sharesdk.wechat.friends.Wechat;
import cn.yanshang.friend.common.MyConstants;
import cn.yanshang.friend.common.Shared;
import cn.yanshang.friend.connect.BaseInfo;
import cn.yanshang.friend.connect.BaseListener;
import cn.yanshang.friend.connect.HttpConnect;
import cn.yanshang.friend.info.BindPhoneInfo;
import cn.yanshang.friend.info.QQRegisterInfo;
import cn.yanshang.friend.info.RegisterUserInfo;
import cn.yanshang.friend.utils.CommonUtils;
import cn.yanshang.friend.utils.ProgressUtil;
import cn.yanshang.friend.utils.Util;
import android.R.integer;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences.Editor;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
//import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class LoginMain extends Activity implements OnClickListener, Callback,
		BaseListener {

//	private static final int MSG_USERID_FOUND = 11;
//	private static final int MSG_LOGIN = 12;
//	private static final int MSG_AUTH_CANCEL = 13;
//	private static final int MSG_AUTH_ERROR = 14;
//	private static final int MSG_AUTH_COMPLETE = 15;

	//private Handler handler;

	private ProgressDialog mProgress;
	public static Tencent mTencent;
	private String mOpenId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.login_main);

		findViewById(R.id.btnQQ).setOnClickListener(this);
		findViewById(R.id.lgPhoneNum).setOnClickListener(this);

		// TextView TextPhoneNum = (TextView) findViewById(R.id.lgPhoneNum);
		// TextPhoneNum.setOnClickListener(this);
		// TextPhoneNum.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		// TextPhoneNum.getPaint().setAntiAlias(true);

		initQQ();
	}

	private void initQQ() {
		if (mTencent == null) {
			mTencent = Tencent.createInstance(MyConstants.APP_ID_QQ, this);
		}

		// /handler = new Handler(this);
	}

	public void initOpenidAndToken(JSONObject jsonObject) {
		try {
			String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
			String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
			mOpenId = jsonObject.getString(Constants.PARAM_OPEN_ID);
			if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
					&& !TextUtils.isEmpty(mOpenId)) {
				mTencent.setAccessToken(token, expires);
				mTencent.setOpenId(mOpenId);

				// 链接应用服务器
				Map<String, String> keyValueMap = new HashMap<String, String>();
				keyValueMap.put("service", "3");
				keyValueMap.put("openid", mOpenId);
				keyValueMap.put("access_token", token);

				mProgress = ProgressUtil.show(this, R.string.dialog_title_1,
						R.string.dialog_connect_1, new OnCancelListener() {
							@Override
							public void onCancel(DialogInterface dialog) {
							}
						});

				QQRegisterInfo bInfo = new QQRegisterInfo();
				String qqString = bInfo.getJsonString(keyValueMap);
				HttpConnect.newInstance().doPost(this, qqString, bInfo,
						MyConstants.URL_POST_SAVE_AUTH, this);
			} else {
				Util.showResultDialog(LoginMain.this, "返回为空", "获取授权失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void onClickLogin() {
		// QQ登录实现接口
		IUiListener loginListener = new BaseUiListener() {
			@Override
			protected void doComplete(JSONObject values) {
				Log.d("SDKQQAgentPref",
						"AuthorSwitch_SDK:" + SystemClock.elapsedRealtime());
				initOpenidAndToken(values);
				// updateUserInfo();
				// updateLoginButton();
			}
		};

		// if (!mTencent.isSessionValid()) {
		mTencent.login(this, "all", loginListener);
		Log.d("SDKQQAgentPref",
				"FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
		// } else {
		// mTencent.logout(this);
		// //updateUserInfo();
		// //updateLoginButton();
		// }
	}

	private class BaseUiListener implements IUiListener {

		@Override
		public void onComplete(Object response) {
			if (null == response) {
				Util.showResultDialog(LoginMain.this, "返回为空", "登录失败");
				return;
			}
			JSONObject jsonResponse = (JSONObject) response;
			if (null != jsonResponse && jsonResponse.length() == 0) {
				Util.showResultDialog(LoginMain.this, "返回为空", "登录失败");
				return;
			}
			// Util.showResultDialog(LoginMain.this, response.toString(),
			// "登录成功");
			doComplete((JSONObject) response);
		}

		protected void doComplete(JSONObject values) {

		}

		@Override
		public void onError(UiError e) {
			Util.toastMessage(LoginMain.this, "onError: " + e.errorDetail);
			Util.dismissDialog();
		}

		@Override
		public void onCancel() {
			Util.toastMessage(LoginMain.this, "onCancel: ");
			Util.dismissDialog();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.lgPhoneNum: {
			Intent it = new Intent();
			it.setClass(this, LoginPhoneRegister.class);// LoginBundPhone
			startActivity(it);
			// this.finish();
		}
			break;
		case R.id.btnQQ:
			onClickLogin();
			break;
		default:
			break;
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
//		switch (msg.what) {
//		case MSG_USERID_FOUND: {
//
//		}
//			break;
//		case MSG_LOGIN: {
//			// 调用注册页面
//			// AuthManager.showDetailPage(this,
//			// ShareSDK.platformNameToId(String.valueOf(msg.obj)));
//			Toast.makeText(this, "MSG_LOGIN", Toast.LENGTH_SHORT).show();
//		}
//			break;
//		case MSG_AUTH_CANCEL: {
//			// 取消授权
//			Toast.makeText(this, "MSG_AUTH_CANCEL", Toast.LENGTH_SHORT).show();
//		}
//			break;
//		case MSG_AUTH_ERROR: {
//			// 授权失败
//			Toast.makeText(this, "MSG_AUTH_ERROR", Toast.LENGTH_SHORT).show();
//		}
//			break;
//		case MSG_AUTH_COMPLETE: {
//			// 授权成功
//			Toast.makeText(this, "MSG_AUTH_COMPLETE", Toast.LENGTH_SHORT)
//					.show();
//		}
//			break;
//		}
		return false;
	}

	@Override
	public void onGotInfo(BaseInfo objInfo) {
		// TODO Auto-generated method stub

		QQRegisterInfo bInfo = (QQRegisterInfo) objInfo;

		if (bInfo == null) {
			ProgressUtil.dismiss(mProgress);
			Toast.makeText(this, "onGotInfoErr=" + "bInfo==null",
					Toast.LENGTH_SHORT).show();
			return;
		} else if (bInfo.getStatus() == MyConstants.HTTP_STATUS_OK) {
			// 保存登录信息
			saveLoginInfo(bInfo);

			Intent intent = new Intent();
			//mOpenId
			if (bInfo.getSid() == null || bInfo.getSid().equals("null")) {
				intent.setClass(this, LoginBundPhone.class);
				intent.putExtra("openid", mOpenId);
			} else {
				intent.setClass(this, cn.yanshang.friend.mainui.MainActivity.class);
			}
			startActivity(intent);

			Toast.makeText(this, "onGotInfo=" + bInfo.getStatus(),
					Toast.LENGTH_SHORT).show();
			// this.finish();
		}else {
			CommonUtils.statusError(bInfo.getStatus(), this);
		}

		ProgressUtil.dismiss(mProgress);
	}

	private void saveLoginInfo(QQRegisterInfo bInfo) {

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
