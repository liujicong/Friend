package cn.yanshang.friend.common;

public interface MyConstants {

	public static final String APP_ID_QQ = "1103518746";
			
	public static final String APP_SERVER_NOTIFY_URI = "http://mobilem.cn/pay_callback.php";

	public static final String URL_GET_USER = "http://app.ylcdev.tk/test";

	public static final String URL_POST_USER_REGISTER = "http://app.ylcdev.tk/api/user/register";
	public static final String URL_POST_USER_LOGIN = "http://app.ylcdev.tk/api/user/login";
	public static final String URL_POST_SAVE_AUTH = "http://app.ylcdev.tk/api/user/save-auth";
	public static final String URL_POST_BIND_PHONE = "http://app.ylcdev.tk/api/user/bind-phone";

	// public static final String URL_POST_VALID_PHONE =
	// "http://app.ylcdev.tk/api/user/valid-phone";
	public static final String URL_POST_FIND_PASSWORD = "http://app.ylcdev.tk/api/user/reset-password";
	public static final String URL_POST_SEND_PHONE_CODE = "http://app.ylcdev.tk/api/user/send-phone-code";

	public static final String URL_POST_test = "http://app.ylcdev.tk/api/test/post-img";

	// "http://mobilem.cn/api.php?type=get_userinfo_by_token&debug=1&token=";
	public static final float BORROW_MAX_VALUE = 100000.0f;
	//
	public static final String DES_CODE_KEY_ROAD = "RoadFillKey";
	public static final String AES_CODE_KEY_ROAD = "AESRoadFillKey";

	public static final String SHARESDK_APPKEY = "3ec4f0c38a90";// "270698298a86";//270698298a86//"3ec4f0c38a90";
	public static final String SHARESDK_APPSECRET = "8f7a3aae03023b0936faeffb3d72017c";// "6e0294b313c1c62e3d94376b0b41c8fb";//"8f7a3aae03023b0936faeffb3d72017c";

	// http请求返回状态
	public static final int HTTP_STATUS_TIMEOUT = -1;
	public static final int HTTP_STATUS_JSON_ERROR = -2;
	public static final int HTTP_STATUS_OK = 0;
	public static final int HTTP_STATUS_FORMAT_ERROR = 10000;// 数据格式错误
	public static final int HTTP_STATUS_VALID_FALSE = 10002; // 该号码已注册过
	public static final int HTTP_STATUS_ACCOUNT_ERROR = 10004;// 账号不正确
	public static final int HTTP_STATUS_PASSWORD_ERROR = 10005;// 密码错误
	public static final int HTTP_STATUS_CHECK_ERROR = 10008;// 手机验证码错误
	
	public static final int HTTP_STATUS_REQUEST_ERROR = 10015;// 错误的请求
	public static final int HTTP_STATUS_VALID_TURE = 10017; // 该号码可以注册
	
	public static final int HTTP_STATUS_FREQUENTLY_2_ERROR = 10053;// 验证码频繁
	public static final int HTTP_STATUS_FREQUENTLY_ERROR = 10054;// 登录频繁


	

	// 链接异常状态码
	public static final int RESPONSE_STATUS_OK = 0;
	public static final int RESPONSE_STATUS_TIMEOUT = 1;
	public static final int RESPONSE_STATUS_IO_ERROR = 2;
	public static final int RESPONSE_STATUS_PRO_ERROR = 3;
	public static final String RESPONSE_STR_TIMEOUT = "timeout";

}
