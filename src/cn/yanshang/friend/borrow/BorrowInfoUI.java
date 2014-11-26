package cn.yanshang.friend.borrow;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.yanshang.friend.R;
import cn.yanshang.friend.common.MyConstants;
import cn.yanshang.friend.common.Data;
import cn.yanshang.friend.common.Shared;
import cn.yanshang.friend.utils.CommonUtils;
import cn.yanshang.friend.utils.ProgressUtil;
import android.R.integer;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.TextView;

public class BorrowInfoUI extends Activity implements OnClickListener,
		DialogInterface.OnClickListener {

	private EditText mInputBorrowItem1;
	private EditText mInputBorrowItem2;
	private EditText mInputBorrowItem3;
	private EditText mInputBorrowItem4;
	private TextView mForecastTipNum;
	private Button mBtNextStep;
	private Activity mActivity;

	private String mStrDay;
	private Data mAppData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.borrow_info_ui);

		mAppData = (Data) getApplication();
		mActivity = this;
		initBase();

		checkHistory();
	}

	private void showpickerD() {

		// DatePickerDialog datePickerDialog3 = new DatePickerDialog();
		DatePickerDialog datePickerDialog = new DatePickerDialog(this,
				new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						// TODO Auto-generated method stub

						Calendar calendar = Calendar.getInstance();
						calendar.set(year, monthOfYear, dayOfMonth);
						SimpleDateFormat format = new SimpleDateFormat(
								"yyyy-MM-dd");

						mInputBorrowItem2.setText(format.format(calendar
								.getTime()));
					}
				}, 2015, 12, 15);

		DatePicker datePicker = datePickerDialog.getDatePicker();

		datePicker.setMinDate(System.currentTimeMillis());
		datePicker.setMaxDate(CommonUtils.getMillisThreeYear());

		datePickerDialog.show();
	}

	private void initBase() {

		findViewById(R.id.btnTitleCancel).setOnClickListener(this);

		TextView contentText = (TextView) findViewById(R.id.customTitleContent);
		contentText.setText(R.string.borrow_info_title);

		mStrDay = getResources().getString(R.string.borrow_day);

		mForecastTipNum = (TextView) findViewById(R.id.forecastTipNum);

		mInputBorrowItem1 = (EditText) findViewById(R.id.inputBorrowItem1);
		CommonUtils.setPricePoint(mInputBorrowItem1);
		mInputBorrowItem1.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				String getStr = mInputBorrowItem1.getText().toString();
				if (!hasFocus && getStr.length() > 0) {
					mBtNextStep.setEnabled(true);
					float fl = Float.parseFloat(getStr);
					if (fl > MyConstants.BORROW_MAX_VALUE) {
						Toast.makeText(mActivity, "借款金额最大为10万",
								Toast.LENGTH_SHORT).show();
					} else {
						CountInterest();
					}
				}
			}
		});

		mInputBorrowItem2 = (EditText) findViewById(R.id.inputBorrowItem2);

		mInputBorrowItem2.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					hideIM(v);
					showpickerD();
				} else {
					CountInterest();
				}
			}
		});

		mInputBorrowItem3 = (EditText) findViewById(R.id.inputBorrowItem3);
		// CommonUtils.setPricePoint(mInputBorrowItem3);
		mInputBorrowItem3.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.toString().contains("%")) {
					return;
				}

				if (s.toString().contains(".")) {
					if (s.length() - 1 - s.toString().indexOf(".") > 2) {
						s = s.toString().subSequence(0,
								s.toString().indexOf(".") + 3);
						mInputBorrowItem3.setText(s);
						mInputBorrowItem3.setSelection(s.length());
					}
				}
				if (s.toString().trim().substring(0).equals(".")) {
					s = "0" + s;
					mInputBorrowItem3.setText(s);
					mInputBorrowItem3.setSelection(2);
				}

				if (s.toString().startsWith("0")
						&& s.toString().trim().length() > 1) {
					if (!s.toString().substring(1, 2).equals(".")) {
						mInputBorrowItem3.setText(s.subSequence(0, 1));
						mInputBorrowItem3.setSelection(1);
						return;
					}
				}

				if (!s.toString().contains(".")
						&& s.toString().trim().length() > 2) {
					mInputBorrowItem3.setText(s.toString().substring(0, 2));
					mInputBorrowItem3.setSelection(s.toString().substring(0, 2)
							.length());
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}
		});

		mInputBorrowItem3.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				String inText = mInputBorrowItem3.getText().toString();
				// TODO Auto-generated method stub
				if (!hasFocus) {
					InputFilter[] filters = { new InputFilter.LengthFilter(6) };
					mInputBorrowItem3.setFilters(filters);

					if (inText.length() > 0) {
						mInputBorrowItem3.setText(inText + "%");
					}

					CountInterest();
				} else {
					if (inText.contains("%")) {
						inText = inText.substring(0, inText.indexOf("%"));
						mInputBorrowItem3.setText(inText);
					}
					InputFilter[] filters = { new InputFilter.LengthFilter(5) };
					mInputBorrowItem3.setFilters(filters);
				}
			}
		});

		mInputBorrowItem4 = (EditText) findViewById(R.id.inputBorrowItem4);
		mInputBorrowItem4.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				String inText = mInputBorrowItem4.getText().toString();
				// TODO Auto-generated method stub
				if (!hasFocus) {
					InputFilter[] filters = { new InputFilter.LengthFilter(5) };
					mInputBorrowItem4.setFilters(filters);

					if (inText.length() > 0) {
						mInputBorrowItem4.setText(inText + mStrDay);
					}

					CountInterest();
				} else {
					if (inText.contains(mStrDay)) {
						inText = inText.substring(0, inText.indexOf(mStrDay));
						mInputBorrowItem4.setText(inText);
					}
					InputFilter[] filters = { new InputFilter.LengthFilter(2) };
					mInputBorrowItem4.setFilters(filters);
				}
			}
		});

		mBtNextStep = (Button) findViewById(R.id.btnNextStep);
		mBtNextStep.setOnClickListener(this);
		mBtNextStep.setEnabled(false);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnTitleCancel: {
			ProgressUtil.showDialog(this,
					getResources().getString(R.string.dialog_title_1),
					getResources().getString(R.string.dialog_borrow_cancel),
					this);
		}
			break;
		case R.id.btnNextStep: {
			try {
				submitRecord();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			break;
		default:
			// Toast.makeText(this, "00", Toast.LENGTH_SHORT).show();
			break;
		}

	}

	private void submitRecord() throws ParseException {
		String Item1 = mInputBorrowItem1.getText().toString();
		String Item2 = mInputBorrowItem2.getText().toString();
		String Item3 = mInputBorrowItem3.getText().toString();
		String Item4 = mInputBorrowItem4.getText().toString();

		if (Item4.length() > 1) {
			String itTextSt = Item4;
			if (itTextSt.contains(mStrDay)) {
				itTextSt = itTextSt.substring(0, itTextSt.indexOf(mStrDay));
			}

			if (Integer.parseInt(itTextSt) > 30) {
				ProgressUtil.showDialog(
						this,
						getResources().getString(R.string.dialog_title_1),
						getResources().getString(
								R.string.dialog_borrow_info_4_1));
				return;
			}
		}

		// if (Item2.length() > 2) {
		// SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		// Date curDate = new Date(System.currentTimeMillis());
		// String start = formatter.format(curDate);
		// long days = CommonUtils.getDayOfBorrow(start, Item2);
		// if (days < 1 || days > 1095) {
		// ProgressUtil.showDialog(
		// this,
		// getResources().getString(R.string.dialog_title_1),
		// getResources().getString(
		// R.string.dialog_borrow_info_2_1));
		// return;
		// }
		// }

		if (Item3.length() > 1) {
			String itTextSt = Item3;
			if (itTextSt.contains("%")) {
				itTextSt = itTextSt.substring(0, itTextSt.indexOf("%"));
			}

			if (Float.parseFloat(itTextSt) > 24.0f) {
				ProgressUtil.showDialog(
						this,
						getResources().getString(R.string.dialog_title_1),
						getResources().getString(
								R.string.dialog_borrow_info_3_1));
				return;
			}
		}

		if (!(Item1.length() > 0 && Float.parseFloat(Item1) > 0.00001)) {
			ProgressUtil.showDialog(this,
					getResources().getString(R.string.dialog_title_1),
					getResources().getString(R.string.dialog_borrow_info_1));
		} else if (Item1.length() > 0 && Float.parseFloat(Item1) > 100000.0f) {
			ProgressUtil.showDialog(this,
					getResources().getString(R.string.dialog_title_1),
					getResources().getString(R.string.dialog_borrow_info_1_1));
		} else if (Item2.length() < 1) {
			ProgressUtil.showDialog(this,
					getResources().getString(R.string.dialog_title_1),
					getResources().getString(R.string.dialog_borrow_info_2));
		} else if (Item3.length() < 1) {
			ProgressUtil.showDialog(this,
					getResources().getString(R.string.dialog_title_1),
					getResources().getString(R.string.dialog_borrow_info_3));
		} else if (Item4.length() < 1) {
			ProgressUtil.showDialog(this,
					getResources().getString(R.string.dialog_title_1),
					getResources().getString(R.string.dialog_borrow_info_4));
		} else {

			if (Item3.contains("%")) {
				Item3 = Item3.substring(0, Item3.indexOf("%"));
			}
			if (Item4.contains(mStrDay)) {
				Item4 = Item4.substring(0, Item4.indexOf(mStrDay));
			}

			if (CommonUtils.isDeadlineError(Item2, Integer.valueOf(Item4))) {
				ProgressUtil.showDialog(
						this,
						getResources().getString(R.string.dialog_title_1),
						getResources().getString(
								R.string.dialog_borrow_info_4_2));
				return;
			}

			// SharedPreferences preferences = getSharedPreferences(
			// Shared.SHARE_BORROW_INFO, Context.MODE_PRIVATE);
			// Editor editor = preferences.edit();
			// editor.putString(Shared.SHARE_BORROW_INFO_MONEY, Item1);
			// editor.putString(Shared.SHARE_BORROW_INFO_DATE, Item2);
			// editor.putString(Shared.SHARE_BORROW_INFO_INTEREST, Item3);
			// editor.putString(Shared.SHARE_BORROW_INFO_DEADLINE, Item4);
			// editor.commit();

			mAppData.setInfoMoney(Item1);
			mAppData.setInfoDate(Item2);
			mAppData.setInfoInerest(Item3);
			mAppData.setInfoDeadline(Item4);
			mAppData.setInfoInerestTotal(mForecastTipNum.getText().toString());

			Intent it = new Intent();
			it.setClass(this, BorrowShroffAccount.class);
			startActivity(it);
		}
	}

	private void checkHistory() {

		if (mAppData.getInfoMoney() != null) {
			mInputBorrowItem1.setText(mAppData.getInfoMoney());
			mInputBorrowItem2.setText(mAppData.getInfoDate());
			mInputBorrowItem3.setText(mAppData.getInfoInerest());
			mInputBorrowItem4.setText(mAppData.getInfoDeadline());
			mForecastTipNum.setText(mAppData.getInfoInerestTotal());
		}

		// SharedPreferences preferences = getSharedPreferences(
		// Shared.SHARE_BORROW_INFO, Context.MODE_PRIVATE);
		//
		// String isBack = preferences.getString(Shared.IS_BACK, Shared._NO);
		//
		// if (Shared._YES.equals(isBack)) {
		// mInputBorrowItem1.setText(preferences.getString(
		// Shared.SHARE_BORROW_INFO_MONEY, Shared._DEFAULT));
		// mInputBorrowItem2.setText(preferences.getString(
		// Shared.SHARE_BORROW_INFO_DATE, Shared._DEFAULT));
		// mInputBorrowItem3.setText(preferences.getString(
		// Shared.SHARE_BORROW_INFO_INTEREST,
		// Shared._DEFAULT));
		// mInputBorrowItem4.setText(preferences.getString(
		// Shared.SHARE_BORROW_INFO_DEADLINE,
		// Shared._DEFAULT));
		// }
		//
		// Shared.setBackBorrowYes(this, false);
	}

	private String CountInterest() {
		String result = "0";

		String Item1 = mInputBorrowItem1.getText().toString();
		String Item2 = mInputBorrowItem2.getText().toString();
		String Item3 = mInputBorrowItem3.getText().toString();

		if (Item1.length() > 0 && Item2.length() > 0 && Item3.length() > 0) {
			if (Float.parseFloat(Item1) > 0.00001) {
				result = interestCount(Item1, Item2, Item3);
			}
		}

		mForecastTipNum.setText(result);
		return result;
	}

	private String interestCount(String sMoney1, String endDate2, String inText3) {
		String strInterest = "0";

		if (inText3.contains("%")) {
			inText3 = inText3.substring(0, inText3.indexOf("%"));
		}

		if (Float.parseFloat(inText3) < 0.00001) {
			return strInterest;
		}

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date curDate = new Date(System.currentTimeMillis());
		String start = formatter.format(curDate);

		// String end = mInputBorrowItem2.getText().toString().trim();
		// String sMoney = mInputBorrowItem1.getText().toString().trim();

		float fMoney = Float.parseFloat(sMoney1);
		double fInText = Float.parseFloat(inText3) * 0.01;

		try {
			long days = CommonUtils.getDayOfBorrow(start, endDate2);
			double allInterest = fMoney * fInText * days / 365;

			DecimalFormat decimalFormat = new DecimalFormat("0.00");
			strInterest = decimalFormat.format(allInterest);// format 返回的是字符串
			// mForecastTipNum.setText(strInterest);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return strInterest;
	}

	private void hideIM(View edt) {
		try {
			InputMethodManager im = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
			IBinder windowToken = edt.getWindowToken();
			if (windowToken != null) {
				im.hideSoftInputFromWindow(windowToken, 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		this.finish();

	}

}
