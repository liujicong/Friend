package cn.yanshang.friend.connect;

import android.content.Context;
import android.util.Log;
//import cn.yanshang.friend.Constants;
import cn.yanshang.friend.net.ConnectHttpListener;
import cn.yanshang.friend.net.ConnectHttpTask;

public class HttpConnect {

	private ConnectHttpTask sHttpTask;
	
    public static HttpConnect newInstance(){
        return new HttpConnect();
     }
    
    
    public void doPost(Context context, String jsonstring, final BaseInfo objInfo, String url,
            final BaseListener listener) {
    	
    	//String url = Constants.APP_SERVER_URL_POST_BIND_PHONE;
    	
    	if(sHttpTask!=null){
    		sHttpTask.cancel(true);
    	}
    	
    	sHttpTask = new ConnectHttpTask(context);
   	
    	sHttpTask.doPost(new ConnectHttpListener(){

			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				if(response==null)
				{
					Log.d("Test", "sHttpTask.doGet onResponse= null!");
					return ;
				}
				
                //TestUserInfo userInfo = objInfo.parseJson(response);
				if(objInfo.parseJson(response)){
					listener.onGotInfo(objInfo);
				}else {
					listener.onGotInfo(objInfo);
				}
  
                sHttpTask = null;
			}

			@Override 
			public void onCancelled() {
				// TODO Auto-generated method stub
                listener.onGotInfo(null);
                sHttpTask = null;
			}
    		
    	}, jsonstring, url);
    }
    
    public boolean doCancel() {
            return (sHttpTask != null) ? sHttpTask.cancel(true) : false;
   	}
    
}
