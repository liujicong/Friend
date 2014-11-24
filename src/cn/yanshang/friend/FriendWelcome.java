package cn.yanshang.friend;


import cn.yanshang.friend.borrow.BorrowInfoUI;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.ImageView;

public class FriendWelcome extends Activity{

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//final Window win = getWindow();
		//win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		this.setContentView(R.layout.friendwelcome);
//		ImageView iv=(ImageView)this.findViewById(R.id.wpic);
//		iv.setImageResource(R.drawable.yan_logo);
		welcome();
	}

	
	public void welcome() {
		new Thread(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try{
					Thread.sleep(1000);
					Message m = new Message();
					logHandler.sendMessage(m);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
			
		}).start();
		
	}
	
	
	private Handler logHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			toLogin();
		}
	};
	
	
	public void toLogin() {
		
		Intent it=new Intent();
		it.setClass(this, LoginPhone.class);
		//LoginPhoneRegister
		//cn.yanshang.friend.borrow.BorrowInfoUI.class
		// CropPictureActivity.class);//cn.yanshang.friend.borrow.BorrowReason.class);
		//BorrowInfoUI.class);//LoginMain.class //LoginPhone.class
		//cn.yanshang.friend.borrow.BorrowReason.class
//		it.setAction("cn.yanshang.friend.mainui"); //cn.yanshang.friend.mainui.MainActivity
//		it.setClass(this, cn.yanshang.friend.mainui.MainActivity.class);
		
		startActivity(it);
		
		this.finish();
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		if(keyCode == 4){
			android.os.Process.killProcess(android.os.Process.myPid());
		}
		
		return super.onKeyDown(keyCode, event);	
	}
	
}
