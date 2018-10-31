package com.microservice.dao.entity.crawler.mobile.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="pro_mobile_sms_info",indexes = {@Index(name = "index_pro_mobile_sms_info_taskid", columnList = "taskId")})
public class ProMobileSmsInfo extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String resource;
	private String hostNum;
	private String otherNum;
	private String fee;
	private String sendTime;
	private String smsWay;
	private String smsType;
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
	public String getHostNum() {
		return hostNum;
	}
	public void setHostNum(String hostNum) {
		this.hostNum = hostNum;
	}
	public String getOtherNum() {
		return otherNum;
	}
	public void setOtherNum(String otherNum) {
		this.otherNum = otherNum;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public String getSmsWay() {
		return smsWay;
	}
	public void setSmsWay(String smsWay) {
		this.smsWay = smsWay;
	}
	public String getSmsType() {
		return smsType;
	}
	public void setSmsType(String smsType) {
		this.smsType = smsType;
	}
	@Override
	public String toString() {
		return "ProMobileSmsInfo [taskId=" + taskId + ", resource=" + resource + ", hostNum=" + hostNum + ", otherNum="
				+ otherNum + ", fee=" + fee + ", sendTime=" + sendTime + ", smsWay=" + smsWay + ", smsType=" + smsType
				+ "]";
	}
		
}
