package cn.yanshang.friend.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.EditText;

public abstract class Shared {
	// key
	public static final String IS_BACK = "is_back";
	public static final String IS_EMPTY = "is_empty";
	public static final String SHARE_BORROW_INFO = "borrowinfo_all";
	public static final String SHARE_BORROW_INFO_MONEY = "borrowinfomoney";
	public static final String SHARE_BORROW_INFO_DATE = "borrowinfodate";
	public static final String SHARE_BORROW_INFO_INTEREST = "borrowinfoinerest";
	public static final String SHARE_BORROW_INFO_DEADLINE = "borrowinfodeadline";

	public static final String SHARE_BORROW_ALIPAY_ACCOUNT = "alipayAccount";
	public static final String SHARE_BORROW_ACCOUNT_NAME = "BankAccountName";
	public static final String SHARE_BORROW_BANK_NAME = "BankName";
	public static final String SHARE_BORROW_BANK_CARDNUM = "BankCardNum";

	// 登录信息保存
	public static final String SHARE_LOGIN_ALL_GET = "login_all_get";

	public static final String SHARE_LOGIN_UID = "login_uid";
	public static final String SHARE_LOGIN_SID = "login_sid";
	public static final String SHARE_LOGIN_SERVICE = "login_service";// 1-phone
																		// 2-qq
	public static final String SHARE_LOGIN_REALNAME = "login_realname";
	public static final String SHARE_LOGIN_NICKNAME = "login_nickname";
	public static final String SHARE_LOGIN_IDCARD = "login_idcard";
	public static final String SHARE_LOGIN_HEADIMGURL = "login_headimgurl";
	public static final String SHARE_LOGIN_SIGNATURE = "login_signature";

	// value
	public static final String _YES = "_yes";
	public static final String _NO = "_no";
	public static final String _NULL = "_null";
	public static final String _DEFAULT = "default_name";

	public static boolean isBackBorrow(Context cont) {
		SharedPreferences preferences = cont.getSharedPreferences(
				Shared.SHARE_BORROW_INFO, Context.MODE_PRIVATE);

		return Shared._YES.equals(preferences.getString(Shared.IS_BACK,
				Shared._NO));
	};

	public static void setBackBorrowYes(Context cont, boolean isYse) {

		SharedPreferences preferences = cont.getSharedPreferences(
				Shared.SHARE_BORROW_INFO, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		if (isYse) {
			editor.putString(Shared.IS_BACK, Shared._YES);
		} else {
			editor.putString(Shared.IS_BACK, Shared._NO);
		}

		editor.commit();
	}
}
