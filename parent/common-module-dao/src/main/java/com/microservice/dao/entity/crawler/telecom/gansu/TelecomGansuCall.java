package com.microservice.dao.entity.crawler.telecom.gansu;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name="telecomGansu_Call",indexes = {@Index(name = "index_telecomGansu_Call_taskid", columnList = "taskid")}) 
public class TelecomGansuCall extends IdEntity{

	private String callDate;//通话时间
	
	private String callStatus;//呼叫方式
	
	private String hisNum;//对方号码
	
	private String callTime;//通话时长
	
	private String callMoney;//通话金额
	
	private String callPlace;//通话地点
	
	private String callType;//通话类型
	
	private String taskid;

	

	public TelecomGansuCall() {
		super();
	}



	public TelecomGansuCall(String callDate, String callStatus, String hisNum, String callTime, String callMoney,
			String callPlace, String callType, String taskid) {
		super();
		this.callDate = callDate;
		this.callStatus = callStatus;
		this.hisNum = hisNum;
		this.callTime = callTime;
		this.callMoney = callMoney;
		this.callPlace = callPlace;
		this.callType = callType;
		this.taskid = taskid;
	}



	@Override
	public String toString() {
		return "TelecomGansuCall [callDate=" + callDate + ", callStatus=" + callStatus + ", hisNum=" + hisNum
				+ ", callTime=" + callTime + ", callMoney=" + callMoney + ", callPlace=" + callPlace + ", callType="
				+ callType + ", taskid=" + taskid + "]";
	}



	public String getCallDate() {
		return callDate;
	}



	public void setCallDate(String callDate) {
		this.callDate = callDate;
	}



	public String getCallStatus() {
		return callStatus;
	}



	public void setCallStatus(String callStatus) {
		this.callStatus = callStatus;
	}



	public String getHisNum() {
		return hisNum;
	}



	public void setHisNum(String hisNum) {
		this.hisNum = hisNum;
	}



	public String getCallTime() {
		return callTime;
	}



	public void setCallTime(String callTime) {
		this.callTime = callTime;
	}



	public String getCallMoney() {
		return callMoney;
	}



	public void setCallMoney(String callMoney) {
		this.callMoney = callMoney;
	}



	public String getCallPlace() {
		return callPlace;
	}



	public void setCallPlace(String callPlace) {
		this.callPlace = callPlace;
	}



	public String getCallType() {
		return callType;
	}



	public void setCallType(String callType) {
		this.callType = callType;
	}



	public String getTaskid() {
		return taskid;
	}



	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	
	
}
