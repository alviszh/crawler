package com.crawler.cmcc.domain.json;

import java.util.List;

import com.microservice.dao.entity.crawler.cmcc.CmccPayMsgResult;

public class PayMsgBean {
	
	public List<CmccPayMsgResult> data;
	public String retCode;
	public List<CmccPayMsgResult> getData() {
		return data;
	}
	public void setData(List<CmccPayMsgResult> data) {
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
	public String retMsg;
	public String sOperTime;

}
