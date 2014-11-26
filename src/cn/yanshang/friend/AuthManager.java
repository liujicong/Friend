package cn.yanshang.friend;

//import cn.sharesdk.framework.Platform;
//import cn.sharesdk.framework.PlatformActionListener;

public class AuthManager {

	private static SignupListener signupListener;

	// private static PlatformActionListener platformActionListener;

	//
	// public static boolean doSignup(Platform platform){
	// if(signupListener != null && signupListener.isSignup()){
	// return signupListener.doSignup(platform);
	// }
	// return false;
	// }

	public static SignupListener getSignupListener() {
		return signupListener;
	}

	public static void setSignupListener(SignupListener signupListener) {
		AuthManager.signupListener = signupListener;
	}
}
