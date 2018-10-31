package com.microservice.dao.entity.crawler.mobile.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="pro_mobile_call_info",indexes = {@Index(name = "index_pro_mobile_call_info_taskid", columnList = "taskId")})
public class ProMobileCallInfo extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String resource;
	private String phoneNum;
	private String hisNum;
	private String callType;
	private String chargeType;
	private String callTime;
	private String callDuration;
	private String comment;
	private String fee;
	private String callLocation;
	private String hisLocation;
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getHisNum() {
		return hisNum;
	}
	public void setHisNum(String hisNum) {
		this.hisNum = hisNum;
	}
	public String getCallType() {
		return callType;
	}
	public void setCallType(String callType) {
		this.callType = callType;
	}
	public String getChargeType() {
		return chargeType;
	}
	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}
	public String getCallTime() {
		return callTime;
	}
	public void setCallTime(String callTime) {
		this.callTime = callTime;
	}
	public String getCallDuration() {
		return callDuration;
	}
	public void setCallDuration(String callDuration) {
		this.callDuration = callDuration;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getCallLocation() {
		return callLocation;
	}
	public void setCallLocation(String callLocation) {
		this.callLocation = callLocation;
	}
	public String getHisLocation() {
		return hisLocation;
	}
	public void setHisLocation(String hisLocation) {
		this.hisLocation = hisLocation;
	}
	@Override
	public String toString() {
		return "ProMobileCallInfo [taskId=" + taskId + ", resource=" + resource + ", phoneNum=" + phoneNum + ", hisNum="
				+ hisNum + ", callType=" + callType + ", chargeType=" + chargeType + ", callTime=" + callTime
				+ ", callDuration=" + callDuration + ", comment=" + comment + ", fee=" + fee + ", callLocation="
				+ callLocation + ", hisLocation=" + hisLocation + "]";
	}
		
}
