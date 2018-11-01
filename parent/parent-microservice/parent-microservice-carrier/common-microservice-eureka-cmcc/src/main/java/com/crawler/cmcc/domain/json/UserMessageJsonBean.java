package com.crawler.cmcc.domain.json;

public class UserMessageJsonBean {
	
	public DateBean data;
	public String retCode;
	public String retMsg;
	public String sOperTime;
	
	public DateBean getData() {
		return data;
	}
	public void setData(DateBean data) {
		this.data = data;
	}
	public String getRetCode() {
		return retCode;
	}
	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}
	public String getRetMsg() {
		return retMsg;
	}
	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}
	public String getsOperTime() {
		return sOperTime;
	}
	public void setsOperTime(String sOperTime) {
		this.sOperTime = sOperTime;
	}
	
	

}
