package com.crawler.domain.json;

import java.io.Serializable;

public class IdAuthBean implements Serializable{
	
	private static final long serialVersionUID = 2539404313487132516L;

	private String serialNum;
	
	private String retCode;
	
	private String retMsg;
	
	private String result;

	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
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

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "IdAuthBean [serialNum=" + serialNum + ", retCode=" + retCode + ", retMsg=" + retMsg + ", result="
				+ result + "]";
	}
 
	
	
	

}
