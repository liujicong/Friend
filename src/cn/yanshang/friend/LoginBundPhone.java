package cn.yanshang.friend;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

//import cn.smssdk.EventHandler;
//import cn.smssdk.SMSSDK;
import cn.yanshang.friend.common.Constants;
import cn.yanshang.friend.connect.BaseInfo;
import cn.yanshang.friend.connect.BaseListener;
import cn.yanshang.friend.connect.HttpConnect;
import cn.yanshang.friend.info.BindPhoneInfo;
import cn.yanshang.friend.utils.CommonUtils;
import cn.yanshang.friend.utils.ProgressUtil;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
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

public class LoginBundPhone extends Activity implements OnClickListener, BaseListener{

	private Button btnBund;
	private Button btnCheck;
	private EditText inputPhoneNum;
	private EditText inputCheck;
	
	private Activity mActivity;
	private ProgressDialog mProgress;
	
//	private EventHandler mEventHandler;
	
	private int recLen = 2;

	final Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				recLen--;
				// txtView.setText("" + recLen);

				if (recLen > 0) {
					btnCheck.setText(mActivity.getString(R.string.tip_check_send)+"(" + recLen + ")");
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
		
//		initSMS();	
	}

	private void initBase() {

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
	
	
//	private void initSMS() {
//		SMSSDK.initSDK(this, Constants.SHARESDK_APPKEY,
//				Constants.SHARESDK_APPSECRET);
//
//		mEventHandler = new EventHandler() {
//			public void afterEvent(final int event, final int result,
//					final Object data) {
//				runOnUiThread(new Runnable() {
//					public void run() {
//						if (result == SMSSDK.RESULT_COMPLETE) {
//							if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
//								Toast.makeText(mActivity, "RESULT_COMPLETE",
//										Toast.LENGTH_SHORT).show();
//							} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
//								// 请求验证码后，跳转到验证码填写页面
//								// afterVerificationCodeRequested();
//								Toast.makeText(mActivity, "EVENT_GET_CODE",
//										Toast.LENGTH_SHORT).show();
//							}else if(event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
//								//验证成功 跳转请求服务器
//								connectServer();
//								Toast.makeText(mActivity, "EVENT_SUBMIT_CODE",
//										Toast.LENGTH_SHORT).show();
//							}
//						} else {
//							
//							ProgressUtil.dismiss(mProgress);
//							try {
//								((Throwable) data).printStackTrace();
//								Throwable throwable = (Throwable) data;
//
//								JSONObject object = new JSONObject(
//										throwable.getMessage());
//								String des = object.optString("detail");
//								if (!TextUtils.isEmpty(des)) {
//									Toast.makeText(mActivity, des,
//											Toast.LENGTH_SHORT).show();
//									return;
//								}
//							} catch (JSONException e) {
//								e.printStackTrace();
//							}
//						}
//					}
//				});
//			}
//		};
//		
//		SMSSDK.registerEventHandler(mEventHandler);
//	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		SMSSDK.unregisterEventHandler(mEventHandler);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnTitleCancel:
			this.finish();
			break;
		case R.id.btnCheck:
			RequestVerificationCode();
			break;
		case R.id.btnBund:
			goBundPhoneNum();
			//connectServer();
			break;
		default:
			Toast.makeText(this, "00", Toast.LENGTH_SHORT).show();
			break;
		}
	}
	
	private void RequestVerificationCode() {

		if (!CommonUtils.isPhoneNumberValid(inputPhoneNum.getText().toString())) {
			ProgressUtil.showDialog(this,
					getResources().getString(R.string.dialog_title_1),
					getResources().getString(R.string.dialog_phone_num_1));
			return;
		}
		
		countDown();

		final String phone = inputPhoneNum.getText().toString();
		final String code = "86";

//		SMSSDK.getVerificationCode(code, phone.trim());
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

		if(TextUtils.isEmpty(verifyCode)){
			Toast.makeText(this, this.getString(R.string.tip_check_num), Toast.LENGTH_SHORT).show();
		}else{

			mProgress = ProgressUtil.show(this, R.string.dialog_title_1,
					R.string.dialog_connect_1, new OnCancelListener() {

						@Override
						public void onCancel(DialogInterface dialog) {
							// if (mTokenTask != null) {
							// mTokenTask.doCancel();
							// }
						}
					});
			
//			SMSSDK.submitVerificationCode("86", phone, verifyCode);
		}	
	}

	private void connectServer()
	{
    	Map<String,String> keyValueMap = new HashMap<String,String>();
    	keyValueMap.put("phone", inputPhoneNum.getText().toString());
    	keyValueMap.put("uid", "987654321");
		
		BindPhoneInfo bInfo = new BindPhoneInfo();

		HttpConnect.newInstance().doPost(this, bInfo.getJsonString(keyValueMap), bInfo,Constants.URL_POST_BIND_PHONE, this);
	}


	@Override
	public void onGotInfo(BaseInfo objInfo) {
		// TODO Auto-generated method stub
		//objInfo instanceOf 
		BindPhoneInfo bInfo = (BindPhoneInfo)objInfo;
		
		ProgressUtil.dismiss(mProgress);
		
		if (bInfo!=null && bInfo.getStatus()== Constants.HTTP_STATUS_OK) {
			// 解析数据跳转页面
			Toast.makeText(this, "onGotInfo="+bInfo.getPhoneNum(), Toast.LENGTH_SHORT).show();
		}else {
			Toast.makeText(this, "onGotInfoErr="+bInfo.getPhoneNum(), Toast.LENGTH_SHORT).show();
		}
	}
	
}










