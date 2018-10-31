package com.microservice.dao.entity.crawler.telecom.jilin;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "telecom_jilin_calldetails", indexes = {@Index(name = "index_telecom_jilin_calldetails_taskid", columnList = "taskid")})
public class TelecomJilinCallDetails extends IdEntity {

	private String taskid;
	private String callingNum;						//主叫号码
	private String calledNum;						//被叫号码
	private String callType;						//呼叫类型
	private String startDate;						//起始时间
	private String callTime;						//时长
	private String callFee;							//费用
	private String type;							//通话类型（长途/市话）
	private String callPlace;						//通话地
	private String attribution;						//归属地
	private String otherAttribution;				//对端归属地
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getCallingNum() {
		return callingNum;
	}
	public void setCallingNum(String callingNum) {
		this.callingNum = callingNum;
	}
	public String getCalledNum() {
		return calledNum;
	}
	public void setCalledNum(String calledNum) {
		this.calledNum = calledNum;
	}
	public String getCallType() {
		return callType;
	}
	public void setCallType(String callType) {
		this.callType = callType;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getCallTime() {
		return callTime;
	}
	public void setCallTime(String callTime) {
		this.callTime = callTime;
	}
	public String getCallFee() {
		return callFee;
	}
	public void setCallFee(String callFee) {
		this.callFee = callFee;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCallPlace() {
		return callPlace;
	}
	public void setCallPlace(String callPlace) {
		this.callPlace = callPlace;
	}
	public String getAttribution() {
		return attribution;
	}
	public void setAttribution(String attribution) {
		this.attribution = attribution;
	}
	public String getOtherAttribution() {
		return otherAttribution;
	}
	public void setOtherAttribution(String otherAttribution) {
		this.otherAttribution = otherAttribution;
	}
	@Override
	public String toString() {
		return "TelecomJilinCallDetails [taskid=" + taskid + ", callingNum=" + callingNum + ", calledNum=" + calledNum
				+ ", callType=" + callType + ", startDate=" + startDate + ", callTime=" + callTime + ", callFee="
				+ callFee + ", type=" + type + ", callPlace=" + callPlace + ", attribution=" + attribution
				+ ", otherAttribution=" + otherAttribution + "]";
	}
	
	
	
}