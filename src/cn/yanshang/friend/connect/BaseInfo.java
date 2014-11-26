package cn.yanshang.friend.connect;

public interface BaseInfo {

	public String getSid();

	public void setSid(String uid);

	public int getStatus();

	public void setStatus(int status);

	// public int getType();
	// public void setType(int iType);

	public boolean parseJson(String jsonString);
}
