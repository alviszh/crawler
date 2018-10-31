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
@Table(name = "telecom_jilin_smsdetails", indexes = {@Index(name = "index_telecom_jilin_smsdetails_taskid", columnList = "taskid")})
public class TelecomJilinSMSDetails extends IdEntity {

	private String taskid;
	private String sendNum;						//发送号码
	private String receiveNum;					//接收号码
	private String sendDate;					//发送时间
	private String fee;							//费用（元）
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
	public String getSendDate() {
		return sendDate;
	}
	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	@Override
	public String toString() {
		return "TelecomJilinSMSDetails [taskid=" + taskid + ", sendNum=" + sendNum + ", receiveNum=" + receiveNum
				+ ", sendDate=" + sendDate + ", fee=" + fee + "]";
	}
	
	

}