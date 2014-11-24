package cn.yanshang.friend.mainui;

import cn.yanshang.friend.R;
import android.R.integer;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements OnClickListener{

	private static final int MAIN_LENDER = 1;
	private static final int MAIN_FRIEND = 2;
	private static final int MAIN_ME = 3;
	
	private static FragmentManager fMgr;

	private RadioButton rbMainLender;
	private RadioButton rbMainFriend;
	private RadioButton rbMainMe;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.main_activity);
		
		fMgr = getSupportFragmentManager();
		
		initFragment();
		dealBottomButtonsClickEvent();
	}

	private void initFragment() {
		FragmentTransaction ft = fMgr.beginTransaction();
		FragmentMainLender weiXinFragment = new FragmentMainLender();
		ft.add(R.id.fragmentRoot, weiXinFragment, "FragmentMainLender");
		ft.addToBackStack("FragmentMainLender");
		ft.commit();		
	}
	
	private void dealBottomButtonsClickEvent() {
		rbMainLender = (RadioButton)findViewById(R.id.rbMainLender);
		rbMainLender.setOnClickListener(this);
		
		rbMainFriend = (RadioButton)findViewById(R.id.rbMainFriend);
		rbMainFriend.setOnClickListener(this);
		
		rbMainMe = (RadioButton)findViewById(R.id.rbMainMe);
		rbMainMe.setOnClickListener(this);
		
		setCurrectTable(MAIN_LENDER);
	}
	
//	private void setTest()
//	{
//		//Resources res = this.getResources();
//		Drawable myImage = getResources().getDrawable(R.drawable.img_main_friend);
//		rbMainLender.setCompoundDrawablesWithIntrinsicBounds(null, myImage, null, null);
//	}
	
	private void setCurrectTable(int key ) {
		switch (key) {
		case MAIN_LENDER:
			rbMainLender.setChecked(true);
			
//			Drawable myImage = getResources().getDrawable(R.drawable.img_main_friend);
//			rbMainLender.setCompoundDrawablesWithIntrinsicBounds(null, myImage, null, null);
			
			rbMainFriend.setChecked(false);
			rbMainMe.setChecked(false);
			break;
		case MAIN_FRIEND:
			rbMainLender.setChecked(false);
			rbMainFriend.setChecked(true);
			rbMainMe.setChecked(false);
			break;
		case MAIN_ME:
			rbMainLender.setChecked(false);
			rbMainFriend.setChecked(false);
			rbMainMe.setChecked(true);
			break;

		default:
			rbMainLender.setChecked(true);
			rbMainFriend.setChecked(false);
			rbMainMe.setChecked(false);
			break;
		}
	}
	
	public static void popAllFragmentsExceptTheBottomOne() {
		for (int i = 0, count = fMgr.getBackStackEntryCount() - 1; i < count; i++) {
			fMgr.popBackStack();
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(fMgr.findFragmentByTag("FragmentMainLender")!=null && fMgr.findFragmentByTag("FragmentMainLender").isVisible()) {
			//MainActivity.this.finish();
		} else {
			super.onBackPressed();
		}
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
		case R.id.rbMainLender:{
			setCurrectTable(MAIN_LENDER);
			
			if(fMgr.findFragmentByTag("FragmentMainLender")!=null && fMgr.findFragmentByTag("FragmentMainLender").isVisible()) {
				return;
			}
			popAllFragmentsExceptTheBottomOne();
		}
			break;
		case R.id.rbMainFriend:{
			setCurrectTable(MAIN_FRIEND);
			
			popAllFragmentsExceptTheBottomOne();
			
			FragmentTransaction ft = fMgr.beginTransaction();
			ft.hide(fMgr.findFragmentByTag("FragmentMainLender"));
			
			FragmentMainFriend mainFd = new FragmentMainFriend();
			ft.add(R.id.fragmentRoot, mainFd, "FragmentMainFriend");
			ft.addToBackStack("FragmentMainFriend");
			ft.commit();
		}
			break;
		case R.id.rbMainMe:{
			setCurrectTable(MAIN_ME);
			
			popAllFragmentsExceptTheBottomOne();
			
			FragmentTransaction ft = fMgr.beginTransaction();
			ft.hide(fMgr.findFragmentByTag("FragmentMainLender"));
			
			FragmentMainMe mainMe = new FragmentMainMe();
			ft.add(R.id.fragmentRoot, mainMe, "FragmentMainMe");
			ft.addToBackStack("FragmentMainMe");
			ft.commit();
		}
			break;

		default:
			//Toast.makeText(this, "00", Toast.LENGTH_SHORT).show();
			break;
		}
	}
	
	
}






