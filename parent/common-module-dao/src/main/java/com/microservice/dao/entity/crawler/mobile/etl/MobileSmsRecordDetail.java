package com.microservice.dao.entity.crawler.mobile.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="mobile_sms_record_detail",indexes = {@Index(name = "index_mobile_sms_record_detail_taskid", columnList = "taskId")})

/*
 * 短信详细记录
 */

public class MobileSmsRecordDetail extends IdEntity implements Serializable {
	
	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String sendTime;	
	private String smsStatus;	
	private String oppositeNumber;	
	private String smsType;	
	private String smsArea;
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
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public String getSmsStatus() {
		return smsStatus;
	}
	public void setSmsStatus(String smsStatus) {
		this.smsStatus = smsStatus;
	}
	public String getOppositeNumber() {
		return oppositeNumber;
	}
	public void setOppositeNumber(String oppositeNumber) {
		this.oppositeNumber = oppositeNumber;
	}
	public String getSmsType() {
		return smsType;
	}
	public void setSmsType(String smsType) {
		this.smsType = smsType;
	}
	public String getSmsArea() {
		return smsArea;
	}
	public void setSmsArea(String smsArea) {
		this.smsArea = smsArea;
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
		return "MobileSmsRecordDetail [taskId=" + taskId + ", sendTime=" + sendTime + ", smsStatus=" + smsStatus
				+ ", oppositeNumber=" + oppositeNumber + ", smsType=" + smsType + ", smsArea=" + smsArea
				+ ", telephoneNumber=" + telephoneNumber + ", months=" + months + ", resource=" + resource + "]";
	}
	
	
}
