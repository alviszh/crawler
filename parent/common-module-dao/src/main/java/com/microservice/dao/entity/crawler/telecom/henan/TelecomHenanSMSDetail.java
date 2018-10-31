package com.microservice.dao.entity.crawler.telecom.henan;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "telecom_henan_smsdetail", indexes = {@Index(name = "index_telecom_henan_smsdetail_taskid", columnList = "taskid")})
public class TelecomHenanSMSDetail extends IdEntity {

	private String taskid;
	private String sendNum;								//发送号码
	private String receiveNum;							//接收号码
	private String receiveDate;							//接收时间
	private String type;								//类型
	private String fee;									//费用（元）
	private String extraFee;							//附加费（元）
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getSendNum() {
		return sendNum;
	}
	public void setSendNum(String sendNum) {
		this.sendNum = sendNum;
	}
	public String getReceiveNum() {
		return receiveNum;
	}
	public void setReceiveNum(String receiveNum) {
		this.receiveNum = receiveNum;
	}
	public String getReceiveDate() {
		return receiveDate;
	}
	public void setReceiveDate(String receiveDate) {
		this.receiveDate = receiveDate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getExtraFee() {
		return extraFee;
	}
	public void setExtraFee(String extraFee) {
		this.extraFee = extraFee;
	}
	@Override
	public String toString() {
		return "TelecomHenanSMSDetail [taskid=" + taskid + ", sendNum=" + sendNum + ", receiveNum=" + receiveNum
				+ ", receiveDate=" + receiveDate + ", type=" + type + ", fee=" + fee + ", extraFee=" + extraFee + "]";
	}
	
}