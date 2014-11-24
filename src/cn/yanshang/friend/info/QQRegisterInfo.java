package cn.yanshang.friend.info;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import cn.yanshang.friend.common.Constants;
import cn.yanshang.friend.connect.BaseInfo;

public class QQRegisterInfo implements BaseInfo{

	private String _Sid;
	private String _uid;
	private int _status;
	private int _iType;
	private String _userid;
	
	@Override
	public String getSid() {
		// TODO Auto-generated method stub
		return _Sid;
	}

	@Override
	public void setSid(String Sid) {
		// TODO Auto-generated method stub
		_Sid = Sid;
	}

	@Override
	public int getStatus() {
		// TODO Auto-generated method stub
		return _status;
	}

	@Override
	public void setStatus(int status) {
		// TODO Auto-generated method stub
		_status = status;
	}

//	@Override
//	public int getType() {
//		// TODO Auto-generated method stub
//		return _iType;
//	}
//
//	@Override
//	public void setType(int iType) {
//		// TODO Auto-generated method stub
//		_iType = iType;
//	}
	
	public String getUserID() {
		return _userid;
	}
	
	public void setUserID(String userid) {
		 _userid = userid;
	}
	
	@Override
	public boolean parseJson(String jsonString) {
		// TODO Auto-generated method stub
		boolean isOk = false;
// "service": 2,
// "uid": 10008,
// "openid": "41452245"
		if (!TextUtils.isEmpty(jsonString)) {
			try {
				JSONObject jsonObj = new JSONObject(jsonString);
				int status = jsonObj.getInt("status");
				this.setStatus(status);
				
				JSONObject dataJsonObj = jsonObj.getJSONObject("body");
				if (status == Constants.HTTP_STATUS_OK ) {
					
					String uid = dataJsonObj.getString("uid");
//				
//					this.setType(dataJsonObj.getInt("service"));
//					this.setUid(uid);
					
					
					isOk = true;
				}else {
//					this.setUid("error01");
					//this.setPhoneNum(dataJsonObj.getString("errors"));
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return isOk;
	}
	
	
//	keyValueMap.put("service", iType.toString());
//	keyValueMap.put("nickname", platDb.getDb().getUserName());
//	keyValueMap.put("openid", platDb.getDb().getUserId());
//	keyValueMap.put("headimgurl", platDb.getDb().getUserIcon());
	
	public String getJsonString(Map<String,String> keyMap) {
		
		//JSONArray arrayJson = new JSONArray();
		JSONObject obj = new JSONObject();
		try {
			JSONObject objdata = new JSONObject();
			objdata.put("service", Integer.parseInt(keyMap.get("service")));
			objdata.put("nickname", keyMap.get("nickname"));
			objdata.put("openid", keyMap.get("openid"));
			objdata.put("headimgurl", keyMap.get("headimgurl"));
			
			obj.put("body", objdata);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//arrayJson.put(obj);
		
		return obj.toString();
	}


	

}


