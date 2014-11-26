package cn.yanshang.friend.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.yanshang.friend.common.MyConstants;

import android.R.bool;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

public abstract class CommonUtils {

	public static boolean isPhoneNumberValid(String phoneNumber) {
		boolean isValid = false;

		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(phoneNumber);
		// System.out.println(m.matches() + "---");
		isValid = m.matches();

		// String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$";
		//
		// String expression2 = "^\\(?(\\d{2})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";
		//
		// CharSequence inputStr = phoneNumber;
		//
		// Pattern pattern = Pattern.compile(expression);
		//
		// Matcher matcher = pattern.matcher(inputStr);
		//
		// Pattern pattern2 = Pattern.compile(expression2);
		//
		// Matcher matcher2 = pattern2.matcher(inputStr);
		// if (matcher.matches() || matcher2.matches()) {
		// isValid = true;
		// }
		return isValid;
	}

	public static boolean isPasswordValid(String password) {
		boolean isValid = false;

		Pattern p1 = Pattern.compile(".*[a-zA-Z].*[0-9]|.*[0-9].*[a-zA-Z]");// 同时有数字有字母
		// .*[a-zA-Z].*[0-9]|.*[0-9].*[a-zA-Z]
		// "^(?![0-9]+$)(?![a-zA-Z]+$){8,20}$"
		// ^(?=.*?[A-Z])(?=.*?[0-9])[a-zA-Z0-9]{7,}$
		// (?![!@#$%()&\'*+\\/=?^_`{|}~-]+$)
		// Pattern p2 = Pattern.compile("^[0-9]*$");
		// "/^\d*$/"
		// ^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$
		// /^[!@#$%()&\'*+\\/=?^_`{|}~-]*$/i"

		Matcher m = p1.matcher(password);

		isValid = m.matches();

		return isValid;
	}

	public static long getDayOfBorrow(String start, String end)
			throws ParseException {

		long days = 0;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date0 = sdf.parse(start);// ("2014-01-23")//sdf.format(start)
		Date date1 = sdf.parse(end); // ("2014-04-05")
		Calendar cal0 = Calendar.getInstance();
		cal0.setTime(date0);
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		long time0 = cal0.getTimeInMillis();
		long time1 = cal1.getTimeInMillis();

		long diff = time1 - time0;
		if (diff > 0) {
			days = diff / (1000 * 60 * 60 * 24);
		}

		return days;
	}

	public static boolean isDeadlineError(String endDate, int Deadline) {

		long endTime = 0;
		boolean isError = false;
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			Calendar cal = Calendar.getInstance();
			cal.setTime(format.parse(endDate));
			endTime = cal.getTimeInMillis();

			long diff = endTime - System.currentTimeMillis();
			if (diff > 0) {
				long days = diff / (1000 * 60 * 60 * 24);
				if (Deadline > days) {
					isError = true;
				}
			} else {
				isError = true;
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return isError;
	}

	public static long getMillisFormat(String yyyyMMdd) {

		long millis = 0;
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			Calendar cal = Calendar.getInstance();
			cal.setTime(format.parse(yyyyMMdd));
			millis = cal.getTimeInMillis();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return millis;
	}

	public static long getMillisThreeYear() {
		long millis = 0;

		long time = System.currentTimeMillis();
		Date date = new Date(time);

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, 3);
		millis = cal.getTimeInMillis();

		return millis;
	}

	public static void setPricePoint(final EditText editText) {
		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.toString().contains(".")) {
					if (s.length() - 1 - s.toString().indexOf(".") > 2) {
						s = s.toString().subSequence(0,
								s.toString().indexOf(".") + 3);
						editText.setText(s);
						editText.setSelection(s.length());
					}
				}
				if (s.toString().trim().substring(0).equals(".")) {
					s = "0" + s;
					editText.setText(s);
					editText.setSelection(2);
				}

				if (s.toString().startsWith("0")
						&& s.toString().trim().length() > 1) {
					if (!s.toString().substring(1, 2).equals(".")) {
						editText.setText(s.subSequence(0, 1));
						editText.setSelection(1);
						return;
					}
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
	}
	
	
	public static void statusError(int status ,Context context){
		switch (status) {
		case MyConstants.HTTP_STATUS_FREQUENTLY_2_ERROR:
			Toast.makeText(context, "onGotInfoErr=验证码频繁", Toast.LENGTH_SHORT)
					.show();
			break;
		case MyConstants.HTTP_STATUS_FORMAT_ERROR:
			Toast.makeText(context, "onGotInfoErr=数据格式错误", Toast.LENGTH_SHORT)
					.show();
			break;
		case MyConstants.HTTP_STATUS_REQUEST_ERROR:
			Toast.makeText(context, "onGotInfoErr=错误的请求", Toast.LENGTH_SHORT)
					.show();
			break;
		case MyConstants.HTTP_STATUS_CHECK_ERROR:
			Toast.makeText(context, "onGotInfoErr=手机验证码错误", Toast.LENGTH_SHORT)
					.show();
			break;
		case MyConstants.HTTP_STATUS_VALID_FALSE:
			Toast.makeText(context, "onGotInfoErr=此号码已经注册过", Toast.LENGTH_SHORT)
					.show();
			break;
		case MyConstants.HTTP_STATUS_ACCOUNT_ERROR:
			Toast.makeText(context, "onGotInfoErr=账号不正确", Toast.LENGTH_SHORT)
					.show();
			break;
		case MyConstants.HTTP_STATUS_PASSWORD_ERROR:
			Toast.makeText(context, "onGotInfoErr=密码错误", Toast.LENGTH_SHORT)
					.show();
			break;
		case MyConstants.HTTP_STATUS_FREQUENTLY_ERROR:
			Toast.makeText(context, "onGotInfoErr=登录频繁", Toast.LENGTH_SHORT)
					.show();
			break;
		case MyConstants.HTTP_STATUS_TIMEOUT:
			Toast.makeText(context, "onGotInfoErr=网络链接超时", Toast.LENGTH_SHORT)
					.show();
			break;
		default:
			Toast.makeText(context, "bInfo.getStatus()=" + status,
					Toast.LENGTH_SHORT).show();
			break;
		}
	}

}


// EditText mEdit = (EditText)findViewById(R.id.mEdit);
// InputFilter[] filters = {new AdnNameLengthFilter()};
// mEdit.setFilters(filters);
// public static class AdnNameLengthFilter implements InputFilter
// {
// private int nMax;
//
// public CharSequence filter (CharSequence source, int start, int end, Spanned
// dest, int dstart, int dend)
// {
// Log.w("Android_12",
// "source("+start+","+end+")="+source+",dest("+dstart+","+dend+")="+dest);
//
// if(isChinese(dest.toString())|| isChinese(source.toString()))
// {
// nMax = LENGTH_ZNAME;
// }else
// {
// nMax =LENGTH_ENAME;
// }
//
// int keep = nMax - (dest.length() - (dend - dstart));
//
// if (keep <= 0) {
// return "";
// } else if (keep >= end - start) {
// return null; // keep original
// } else {
// return source.subSequence(start, start + keep);
// }
//
// }
// }

