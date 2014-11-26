package cn.yanshang.friend.borrow;

import cn.yanshang.friend.R;
import cn.yanshang.friend.common.Data;
import cn.yanshang.friend.common.Shared;
import cn.yanshang.friend.utils.ProgressUtil;
import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class BorrowShroffAccount extends Activity implements OnClickListener {

	private EditText mInputAlipayAccount;
	private EditText mInputBankAccountName;
	private EditText mInputBankName;
	private EditText mInputBankCardNum;

	private Data mAppData;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.borrow_shroff_account);

		mAppData = (Data) getApplication();
		initBase();

		checkHistory();
	}

	private void initBase() {
		ImageButton TitleBarCancel = (ImageButton) findViewById(R.id.btnTitleCancel);
		TitleBarCancel.setOnClickListener(this);
		Drawable myImage = getResources().getDrawable(
				R.drawable.img_cancel_back);
		TitleBarCancel.setBackgroundDrawable(myImage);

		TextView contentText = (TextView) findViewById(R.id.customTitleContent);
		contentText.setText(R.string.borrow_shroff_title);

		findViewById(R.id.btnNextStep2).setOnClickListener(this);

		mInputAlipayAccount = (EditText) findViewById(R.id.inputAlipayAccount);
		mInputBankAccountName = (EditText) findViewById(R.id.inputBankAccountName);
		mInputBankName = (EditText) findViewById(R.id.inputBankName);
		mInputBankCardNum = (EditText) findViewById(R.id.inputBankCardNum);
	}

	private void nextUnanimous() {

		int count = 0;
		if (mInputBankAccountName.getText().toString().length() > 0) {
			count++;
		}
		if (mInputBankName.getText().toString().length() > 0) {
			count++;
		}
		if (mInputBankCardNum.getText().toString().length() > 0) {
			count++;
		}

		if (count != 3 && count != 0) {
			ProgressUtil.showDialog(this,
					getResources().getString(R.string.dialog_title_1),
					getResources().getString(R.string.dialog_borrow_shroff));
		} else {
			dataBackup();

			Intent it = new Intent();
			it.setClass(this, BorrowReason.class);
			startActivity(it);
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnTitleCancel: {
			dataBackup();
			this.finish();
		}
			break;
		case R.id.btnNextStep2: {
			nextUnanimous();
		}
			break;
		default:
			break;
		}
	}

	private void dataBackup() {
		mAppData.setAlipayAccount(mInputAlipayAccount.getText().toString());
		mAppData.setBankAccountName(mInputBankAccountName.getText().toString());
		mAppData.setBankName(mInputBankName.getText().toString());
		mAppData.setBankCardNum(mInputBankCardNum.getText().toString());
	}

	private void checkHistory() {
		mInputAlipayAccount.setText(mAppData.getAlipayAccount());
		mInputBankAccountName.setText(mAppData.getBankAccountName());
		mInputBankName.setText(mAppData.getBankName());
		mInputBankCardNum.setText(mAppData.getBankCardNum());
	}

}
