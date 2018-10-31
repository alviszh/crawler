package com.microservice.dao.entity.crawler.mobile.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="mobile_call_record_detail",indexes = {@Index(name = "index_mobile_call_record_detail_taskid", columnList = "taskId")})


/*
 * 手机详细通话记录
 */
public class MobileCallRecordDetail extends IdEntity implements Serializable {
	
	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String communicationType;	//通信类型
	private String callDuration;	//通话时长
	private String callTime;	//通话时间
	private String callLocation;	//通话地点
	private String oppositeNumber;	//对方号码
	private String callType;	//通话类型
	private String callFee;		//通话费用
	private String telephoneNumber; //用户手机号
	private String months;
	@JsonBackReference
	private String resource; //溯源字段
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getCommunicationType() {
		return communicationType;
	}
	public void setCommunicationType(String communicationType) {
		this.communicationType = communicationType;
	}
	public String getCallDuration() {
		return callDuration;
	}
	public void setCallDuration(String callDuration) {
		this.callDuration = callDuration;
	}
	public String getCallTime() {
		return callTime;
	}
	public void setCallTime(String callTime) {
		this.callTime = callTime;
	}
	public String getCallLocation() {
		return callLocation;
	}
	public void setCallLocation(String callLocation) {
		this.callLocation = callLocation;
	}
	public String getOppositeNumber() {
		return oppositeNumber;
	}
	public void setOppositeNumber(String oppositeNumber) {
		this.oppositeNumber = oppositeNumber;
	}
	public String getCallType() {
		return callType;
	}
	public void setCallType(String callType) {
		this.callType = callType;
	}
	public String getCallFee() {
		return callFee;
	}
	public void setCallFee(String callFee) {
		this.callFee = callFee;
	}
	public String getTelephoneNumber() {
		return telephoneNumber;
	}
	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}
	public String getMonths() {
		return months;
	}
	public void setMonths(String months) {
		this.months = months;
	}
	@Column(columnDefinition="text")
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	@Override
	public String toString() {
		return "MobileCallRecordDetail [taskId=" + taskId + ", communicationType=" + communicationType
				+ ", callDuration=" + callDuration + ", callTime=" + callTime + ", callLocation=" + callLocation
				+ ", oppositeNumber=" + oppositeNumber + ", callType=" + callType + ", callFee=" + callFee
				+ ", telephoneNumber=" + telephoneNumber + ", months=" + months + ", resource=" + resource + "]";
	}
	
	
}
