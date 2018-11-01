package com.crawler.cmcc.domain.json;

public class VerifyBean {
	
	public Integer data;
	
	public String retCode;
	
	public String retMsg;
	
	public Integer sOperTime;

	@Override
	public String toString() {
		return "VerifyBean [data=" + data + ", retCode=" + retCode + ", retMsg=" + retMsg + ", sOperTime=" + sOperTime
				+ "]";
	}

	public Integer getData() {
		return data;
	}

	public void setData(Integer data) {
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

	public Integer getsOperTime() {
		return sOperTime;
	}

	public void setsOperTime(Integer sOperTime) {
		this.sOperTime = sOperTime;
	}

}
