package cn.yanshang.friend.info;


import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import cn.yanshang.friend.common.Constants;
import cn.yanshang.friend.connect.BaseInfo;

public class BindPhoneInfotest implements BaseInfo{

	private String _Sid;
	private int _status;
	private int _iType;
	private String _phoneNum;
	
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
	
	public String getPhoneNum() {
		return _phoneNum;
	}
	
	public void setPhoneNum(String phoneNum) {
		 _phoneNum = phoneNum;
	}
	
	@Override
	public boolean parseJson(String jsonString) {
		// TODO Auto-generated method stub
		boolean isOk = false;
		
		if (!TextUtils.isEmpty(jsonString)) {
			try {
				JSONObject jsonObj = new JSONObject(jsonString);
				int status = jsonObj.getInt("status");
				this.setStatus(status);
				
				JSONObject dataJsonObj = jsonObj.getJSONObject("body");
				if (status == Constants.HTTP_STATUS_OK ) {
					//String uid = dataJsonObj.getString("uid");
					String phoneNum = dataJsonObj.getString("phone");

					//this.setUid(uid);
					this.setPhoneNum(phoneNum);
					
					isOk = true;
				}else {
					//this.setUid("error01");
					//this.setPhoneNum("error02");
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return isOk;
	}
	
	public String getJsonString(Map<String,String> keyMap) {
		
		//JSONArray arrayJson = new JSONArray();
		JSONObject obj = new JSONObject();
		
		try {
			JSONObject objdata = new JSONObject();
			objdata.put("imgName", keyMap.get("imgName"));
			objdata.put("img", keyMap.get("img"));
			
			obj.put("body", objdata);
			
		} catch (JSONException e){
			e.printStackTrace();
		}
		//arrayJson.put(obj);
		
		return obj.toString();
	}


}


