package com.microservice.dao.entity.crawler.telecom.shandong;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "telecom_shandong_smsdetail", indexes = {@Index(name = "index_telecom_shandong_smsdetail_taskid", columnList = "taskid")})
public class TelecomShandongSMSDetail extends IdEntity {

	private String taskid;
	
	private String type;							//发送类型
	private String callingNum;						//发送号码
	private String calledNum;						//接收号码
	private String sendTime;						//发送时间
	private String fee;								//费用/元
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	@Override
	public String toString() {
		return "TelecomShandongSMSDetail [taskid=" + taskid + ", type=" + type + ", callingNum=" + callingNum
				+ ", calledNum=" + calledNum + ", sendTime=" + sendTime + ", fee=" + fee + "]";
	}
	
	
	
	
}