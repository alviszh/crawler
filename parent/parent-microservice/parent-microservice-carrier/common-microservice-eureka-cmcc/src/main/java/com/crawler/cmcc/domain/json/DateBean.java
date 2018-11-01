package com.crawler.cmcc.domain.json;

import com.microservice.dao.entity.crawler.cmcc.CmccUserInfo;

public class DateBean{
	
	public String remark;
	public CmccUserInfo custInfoQryOut;
	public String sysTime;
	public String pointValue;
	public String curFee;
	public String curFeeTotal;
	public String realFee;
	public String brandName;
	public String curPlanId;
	public String curPlanName;
	public String nextPlanId;
	public String nextPlanName;
	
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getCurPlanId() {
		return curPlanId;
	}
	public void setCurPlanId(String curPlanId) {
		this.curPlanId = curPlanId;
	}
	public String getCurPlanName() {
		return curPlanName;
	}
	public void setCurPlanName(String curPlanName) {
		this.curPlanName = curPlanName;
	}
	public String getNextPlanId() {
		return nextPlanId;
	}
	public void setNextPlanId(String nextPlanId) {
		this.nextPlanId = nextPlanId;
	}
	public String getNextPlanName() {
		return nextPlanName;
	}
	public void setNextPlanName(String nextPlanName) {
		this.nextPlanName = nextPlanName;
	}
	public String getCurFee() {
		return curFee;
	}
	public void setCurFee(String curFee) {
		this.curFee = curFee;
	}
	public String getCurFeeTotal() {
		return curFeeTotal;
	}
	public void setCurFeeTotal(String curFeeTotal) {
		this.curFeeTotal = curFeeTotal;
	}
	public String getRealFee() {
		return realFee;
	}
	public void setRealFee(String realFee) {
		this.realFee = realFee;
	}
	
	public String getPointValue() {
		return pointValue;
	}
	public void setPointValue(String pointValue) {
		this.pointValue = pointValue;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public CmccUserInfo getCustInfoQryOut() {
		return custInfoQryOut;
	}
	public void setCustInfoQryOut(CmccUserInfo custInfoQryOut) {
		this.custInfoQryOut = custInfoQryOut;
	}
	public String getSysTime() {
		return sysTime;
	}
	public void setSysTime(String sysTime) {
		this.sysTime = sysTime;
	}

}
