package com.crawler.cmcc.domain.json;

public class CallRecordBean<T> {
	
	public T data;
	public Integer totalNum;				
	public String startDate;
	public String endDate;
	public Integer curCuror;
	public String retCode;
	public String retMsg;
	public String sOperTime;
	
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public Integer getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public Integer getCurCuror() {
		return curCuror;
	}
	public void setCurCuror(Integer curCuror) {
		this.curCuror = curCuror;
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
