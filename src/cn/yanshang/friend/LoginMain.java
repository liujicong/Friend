package cn.yanshang.friend;

import java.util.HashMap;
import java.util.Map;

//import cn.sharesdk.facebook.Facebook;
//import cn.sharesdk.demo.tpl.R;
//import cn.sharesdk.framework.Platform;
//import cn.sharesdk.framework.PlatformActionListener;
//import cn.sharesdk.framework.ShareSDK;
//import cn.sharesdk.tencent.qq.QQ;
//import cn.sharesdk.wechat.friends.Wechat;
import cn.yanshang.friend.common.Constants;
import cn.yanshang.friend.connect.BaseInfo;
import cn.yanshang.friend.connect.BaseListener;
import cn.yanshang.friend.connect.HttpConnect;
import cn.yanshang.friend.info.BindPhoneInfo;
import cn.yanshang.friend.info.QQRegisterInfo;
import cn.yanshang.friend.utils.ProgressUtil;
import android.R.integer;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
//import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class LoginMain extends Activity implements OnClickListener,Callback, BaseListener {

	private static final int MSG_USERID_FOUND = 11;
	private static final int MSG_LOGIN = 12;
	private static final int MSG_AUTH_CANCEL = 13;
	private static final int MSG_AUTH_ERROR = 14;
	private static final int MSG_AUTH_COMPLETE = 15;

	private Handler handler;
	private Integer iType;
	private ProgressDialog mProgress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.login_main);
		// findViewById(R.id.tvMsgRegister).setOnClickListener(this);
		findViewById(R.id.btnQQ).setOnClickListener(this);
		findViewById(R.id.btnWechat).setOnClickListener(this);

		TextView TextPhoneNum = (TextView) findViewById(R.id.lgPhoneNum);
		TextPhoneNum.setOnClickListener(this);
		TextPhoneNum.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		TextPhoneNum.getPaint().setAntiAlias(true);

		AuthManager.setSignupListener(new SignupListener() {

			@Override
			public boolean isSignup() {
				// TODO Auto-generated method stub
				return false;
			}

//			@Override
//			public boolean doSignup(Platform platform) {
//				// TODO Auto-generated method stub
//				return false;
//			}
		});
		
//		initSDK();
	}

//	private void initSDK() {
//		ShareSDK.initSDK(this);
//		handler = new Handler(this);
//	}

//	private void authorize(Platform plat) {
//		if (plat == null) {
//			// popupOthers();
//			return;
//		}
//		
//		mProgress = ProgressUtil.show(this, R.string.dialog_title_1,
//				R.string.dialog_connect_1, new OnCancelListener() {
//
//					@Override
//					public void onCancel(DialogInterface dialog) {
//						// if (mTokenTask != null) {
//						// mTokenTask.doCancel();
//						// }
//					}
//				});
//		
//		//plat.removeAccount();
//		if (plat.isValid()) {
//			String userId = plat.getDb().getUserId();
//			if (!TextUtils.isEmpty(userId)) {
//				handler.sendEmptyMessage(MSG_USERID_FOUND);
//				login(plat.getName(), userId, null);
//				return;
//			}
//		}
//
//		plat.setPlatformActionListener(this);
//		// 关闭SSO授权
//		plat.SSOSetting(true);
//		plat.showUser(null);
//	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.lgPhoneNum: {
			iType = 1;
			Intent it = new Intent();
			it.setClass(this, LoginPhoneRegister.class);//LoginBundPhone LoginPhoneRegister
			startActivity(it);
			// this.finish();
		}
			break;
//		case R.id.btnQQ:
//			iType = 3;
//			Platform qq = ShareSDK.getPlatform(QQ.NAME);
//			
//			authorize(qq);
//			Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
//			break;
//		case R.id.btnWechat:
//			iType = 2;
//			Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
//			authorize(wechat);
//			Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();
//			break;
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
		switch (msg.what) {
		case MSG_USERID_FOUND:{
			
		}
			break;
		case MSG_LOGIN: {
			// 调用注册页面
			// AuthManager.showDetailPage(this,
			// ShareSDK.platformNameToId(String.valueOf(msg.obj)));
			 Toast.makeText(this, "MSG_LOGIN",
			 Toast.LENGTH_SHORT).show();
		}
			break;
		case MSG_AUTH_CANCEL: {
			// 取消授权
			 Toast.makeText(this, "MSG_AUTH_CANCEL",
			 Toast.LENGTH_SHORT).show();
		}
			break;
		case MSG_AUTH_ERROR: {
			// 授权失败
			 Toast.makeText(this, "MSG_AUTH_ERROR",
			 Toast.LENGTH_SHORT).show();
		}
			break;
		case MSG_AUTH_COMPLETE: {
			// 授权成功
			 Toast.makeText(this, "MSG_AUTH_COMPLETE",
			 Toast.LENGTH_SHORT).show();
		}
			break;
		}
		return false;
	}
	
	private void login(String plat, String userId, HashMap<String, Object> userInfo) {

//		Platform platDb = ShareSDK.getPlatform(plat);
		
//		connectServer(platDb);	
	}
	
//	@Override
//	public void onCancel(Platform platform, int action) {
//		// TODO Auto-generated method stub
//
//		if (action == Platform.ACTION_USER_INFOR) {
//			handler.sendEmptyMessage(MSG_AUTH_CANCEL);
//		}
//		ProgressUtil.dismiss(mProgress);
//	}
//
//	@Override
//	public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
//		// TODO Auto-generated method stub
//		if (action == Platform.ACTION_USER_INFOR) {
//			handler.sendEmptyMessage(MSG_AUTH_COMPLETE);
//			login(platform.getName(), platform.getDb().getUserId(), res);
//		}
//	}
//
//	@Override
//	public void onError(Platform platform, int action, Throwable t) {
//		// TODO Auto-generated method stub
//		if (action == Platform.ACTION_USER_INFOR) {
//			handler.sendEmptyMessage(MSG_AUTH_ERROR);
//		}
//		ProgressUtil.dismiss(mProgress);
//	}

//	private void connectServer(Platform platDb)
//	{
//		//3=>QQ
//		if (iType == 3) {
//	    	Map<String,String> keyValueMap = new HashMap<String,String>();
//	    	keyValueMap.put("service", iType.toString());
//	    	keyValueMap.put("nickname", platDb.getDb().getUserName());
//	    	keyValueMap.put("openid", platDb.getDb().getUserId());
//	    	keyValueMap.put("headimgurl", platDb.getDb().getUserIcon());
//			
//	    	QQRegisterInfo bInfo = new QQRegisterInfo();
//	    	HttpConnect.newInstance().doPost(this, bInfo.getJsonString(keyValueMap), bInfo,Constants.URL_POST_SAVE_AUTH, this);
//		}else if (iType == 2) {
//			//2=>wechat
//			
//		}else {
//			ProgressUtil.dismiss(mProgress);
//		}
//	}
	
	@Override
	public void onGotInfo(BaseInfo objInfo) {
		// TODO Auto-generated method stub
		
		ProgressUtil.dismiss(mProgress);
		
		if (iType == 3) {
			QQRegisterInfo bInfo = (QQRegisterInfo)objInfo;
			
			if (bInfo!=null && bInfo.getStatus()== Constants.HTTP_STATUS_OK) {
				// 解析数据跳转页面
				//Toast.makeText(this, "onGotInfo="+bInfo.getUid()+" server="+bInfo.getType(), Toast.LENGTH_SHORT).show();

			}else {
				Toast.makeText(this, "onGotInfoErr="+"nonono", Toast.LENGTH_SHORT).show();
			}
		}else{
			Toast.makeText(this, "onGotInfoErr="+"YYYYYY", Toast.LENGTH_SHORT).show();
		}
		
		iType = 0;
	}

}


