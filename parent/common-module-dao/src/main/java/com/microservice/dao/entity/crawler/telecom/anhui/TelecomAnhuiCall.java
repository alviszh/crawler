package com.microservice.dao.entity.crawler.telecom.anhui;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="telecom_anhui_call",indexes = {@Index(name = "index_telecom_anhui_call_taskid", columnList = "taskid")}) 
public class TelecomAnhuiCall extends IdEntity{

	private String callType;//呼叫类型    qrySecondLine
	
	private String talkType;//通话类型    qryThirdLine
	
	private String callPlace;//呼出地    qryForthLine
	
	private String hisNum;//对方号码    qryFifthLine
	
	private String hisPlace;//对方归属地   qrySixthLine
	
	private String startTime;//通话开始时间   qryEighthLine
	
	private String callTime;//通话时长  qryTenthLine
	
	private String money;//费用   qryEleventhLine
	private String taskid;
	@Override
	public String toString() {
		return "TelecomAnhuiCall [callType=" + callType + ", talkType=" + talkType + ", callPlace=" + callPlace
				+ ", hisNum=" + hisNum + ", hisPlace=" + hisPlace + ", startTime=" + startTime + ", callTime="
				+ callTime + ", money=" + money + ", taskid=" + taskid + "]";
	}
	public String getCallType() {
		return callType;
	}
	public void setCallType(String callType) {
		this.callType = callType;
	}
	public String getTalkType() {
		return talkType;
	}
	public void setTalkType(String talkType) {
		this.talkType = talkType;
	}
	public String getCallPlace() {
		return callPlace;
	}
	public void setCallPlace(String callPlace) {
		this.callPlace = callPlace;
	}
	public String getHisNum() {
		return hisNum;
	}
	public void setHisNum(String hisNum) {
		this.hisNum = hisNum;
	}
	public String getHisPlace() {
		return hisPlace;
	}
	public void setHisPlace(String hisPlace) {
		this.hisPlace = hisPlace;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getCallTime() {
		return callTime;
	}
	public void setCallTime(String callTime) {
		this.callTime = callTime;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
}
