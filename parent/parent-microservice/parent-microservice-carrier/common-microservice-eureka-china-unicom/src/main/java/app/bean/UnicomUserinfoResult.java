/**
  * Copyright 2017 bejson.com 
  */
package app.bean;

import java.util.List;

import com.microservice.dao.entity.crawler.unicom.UnicomUserActivityInfo;

public class UnicomUserinfoResult {

	private List<UnicomUserActivityInfo> activityInfo;
	private boolean success;
	private String busiOrder;
	private String respCode;
	private String respDesc;

	public void setActivityInfo(List<UnicomUserActivityInfo> activityInfo) {
		this.activityInfo = activityInfo;
	}

	public List<UnicomUserActivityInfo> getActivityInfo() {
		return activityInfo;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public boolean getSuccess() {
		return success;
	}

	public void setBusiOrder(String busiOrder) {
		this.busiOrder = busiOrder;
	}

	public String getBusiOrder() {
		return busiOrder;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	public String getRespCode() {
		return respCode;
	}

	public void setRespDesc(String respDesc) {
		this.respDesc = respDesc;
	}

	public String getRespDesc() {
		return respDesc;
	}

}