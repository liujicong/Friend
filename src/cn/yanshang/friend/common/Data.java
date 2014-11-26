package cn.yanshang.friend.common;

import android.app.Application;
import android.graphics.Bitmap;

public class Data extends Application {

	private String bInfoMoney;
	private String bInfoDate;
	private String bInfoInerest;
	private String bInfoDeadline;
	private String bInfoInerestTotal;

	private String alipayAccount;
	private String bankAccountName;
	private String bankName;
	private String bankCardNum;

	private Bitmap borrowReasonImg;
	private String borrowReasonStr;

	//
	private String idName;
	private String identity;

	@Override
	public void onCreate() {
		super.onCreate();

		// bInfoMoney = "";
		// bInfoDate = "";
		// bInfoInerest = "";
		// bInfoDeadline = "";
		bInfoInerestTotal = "1";
		//
		// alipayAccount = "";
		// bankAccountName = "";
		// bankName = "";
		// bankCardNum = "";
	}

	public String getInfoMoney() {
		return this.bInfoMoney;
	}

	public void setInfoMoney(String money) {
		bInfoMoney = money;
	}

	public String getInfoDate() {
		return this.bInfoDate;
	}

	public void setInfoDate(String Date) {
		bInfoDate = Date;
	}

	public String getInfoInerest() {
		return this.bInfoInerest;
	}

	public void setInfoInerest(String Inerest) {
		bInfoInerest = Inerest;
	}

	public String getInfoDeadline() {
		return this.bInfoDeadline;
	}

	public void setInfoDeadline(String Deadline) {
		bInfoDeadline = Deadline;
	}

	public String getInfoInerestTotal() {
		return this.bInfoInerestTotal;
	}

	public void setInfoInerestTotal(String Total) {
		bInfoInerestTotal = Total;
	}

	// account
	public String getAlipayAccount() {
		return this.alipayAccount;
	}

	public void setAlipayAccount(String Account) {
		alipayAccount = Account;
	}

	public String getBankAccountName() {
		return this.bankAccountName;
	}

	public void setBankAccountName(String Name) {
		bankAccountName = Name;
	}

	public String getBankName() {
		return this.bankName;
	}

	public void setBankName(String Bank) {
		bankName = Bank;
	}

	public String getBankCardNum() {
		return this.bankCardNum;
	}

	public void setBankCardNum(String CardNum) {
		bankCardNum = CardNum;
	}

	// reason
	public Bitmap getBorrowReasonImg() {
		return this.borrowReasonImg;
	}

	public void setBorrowReasonImg(Bitmap img) {
		borrowReasonImg = img;
	}

	public String getBorrowReasonStr() {
		return this.borrowReasonStr;
	}

	public void setBorrowReasonStr(String ReasonStr) {
		borrowReasonStr = ReasonStr;
	}

	// 身份证
	public String getIdName() {
		return this.idName;
	}

	public void setIdName(String name) {
		idName = name;
	}

	public String getIdentity() {
		return this.identity;
	}

	public void setIdentity(String id) {
		identity = id;
	}

}
