package cn.yanshang.friend.info;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import cn.yanshang.friend.common.MyConstants;
import cn.yanshang.friend.connect.BaseInfo;

public class QQRegisterInfo implements BaseInfo {

	private String _Sid;
	private int _status;
	private int _uid;
	private int _iService;// 1-phone 2-qq
	private String _phoneNum;
	private String _nickName;

	private String _idCard;
	private String _realname;
	private String _signature;
	private String _headimgurl;

	// 测试用
//	private String _code;

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

	public int getUid() {
		return _uid;
	}

	public void setUid(int uid) {
		_uid = uid;
	}

	public int getService() {
		// TODO Auto-generated method stub
		return _iService;
	}

	public void setService(int iService) {
		// TODO Auto-generated method stub
		_iService = iService;
	}

	public String getPhoneNum() {
		return _phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		_phoneNum = phoneNum;
	}

	public String getNickName() {
		return _nickName;
	}

	public void setNickName(String nickName) {
		this._nickName = nickName;
	}

	// private String _idCard;
	// private String _realname;
	// private String _signature;
	// private String _headimgurl;

	public String getIdCard() {
		return _idCard;
	}

	public void setIdCard(String idCard) {
		this._idCard = idCard;
	}

	public String getRealName() {
		return _realname;
	}

	public void setRealName(String realname) {
		this._realname = realname;
	}

	public String getSignature() {
		return _signature;
	}

	public void setSignature(String signature) {
		this._signature = signature;
	}

	public String getHeadimgurl() {
		return _headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this._headimgurl = headimgurl;
	}

//	// 测试用
//	public String getCode() {
//		return _code;
//	}
//
//	public void setCode(String code) {
//		_code = code;
//	}

	@Override
	public boolean parseJson(String jsonString) {
		// TODO Auto-generated method stub
		boolean isOk = false;

		if (!TextUtils.isEmpty(jsonString)) {

			// 链接超时
			if (jsonString.equals(MyConstants.RESPONSE_STR_TIMEOUT)) {
				this.setStatus(MyConstants.HTTP_STATUS_TIMEOUT);
				return false; 
			}

			try {
				JSONObject jsonObj = new JSONObject(jsonString);
				int status = jsonObj.getInt("status");
				this.setStatus(status);

				String sid = jsonObj.getString("sid");
				this.setSid(sid);

				JSONObject dataJsonObj = jsonObj.getJSONObject("body");
				if (status == MyConstants.HTTP_STATUS_OK) {
					String phoneNum = dataJsonObj.getString("phone");
					this.setPhoneNum(phoneNum);

					this.setUid(dataJsonObj.getInt("uid"));
					this.setService(dataJsonObj.getInt("service"));

					this.setRealName(dataJsonObj.getString("realName"));
					this.setIdCard(dataJsonObj.getString("idCard"));
					this.setHeadimgurl(dataJsonObj.getString("headimgurl"));
					this.setNickName(dataJsonObj.getString("nickname"));
					this.setSignature(dataJsonObj.getString("signature"));

					isOk = true;
				} 
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return isOk;
	}

	public String getJsonString(Map<String, String> keyMap) {

		// JSONArray arrayJson = new JSONArray();
		JSONObject obj = new JSONObject();
		
		try {
			JSONObject objdata = new JSONObject();

			objdata.put("service", Integer.parseInt(keyMap.get("service")));
			objdata.put("openid", keyMap.get("openid"));
			objdata.put("access_token", keyMap.get("access_token"));
				
			obj.put("body", objdata);
			obj.put("sid","");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// arrayJson.put(obj);

		return obj.toString();
	}

}

//objdata.put("service", Integer.parseInt(keyMap.get("service")));




