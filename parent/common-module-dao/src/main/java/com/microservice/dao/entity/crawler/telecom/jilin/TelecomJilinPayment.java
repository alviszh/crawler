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
@Table(name = "telecom_jilin_payment", indexes = {@Index(name = "index_telecom_jilin_payment_taskid", columnList = "taskid")})
public class TelecomJilinPayment extends IdEntity {

	private String taskid;
	private String serialNum;			//流水号
	private String payDate;				//缴费时间
	private String payCount;			//金额
	private String payType;				//缴费类型
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getSerialNum() {
		return serialNum;
	}
	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	public String getPayCount() {
		return payCount;
	}
	public void setPayCount(String payCount) {
		this.payCount = payCount;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	@Override
	public String toString() {
		return "TelecomJilinPayment [taskid=" + taskid + ", serialNum=" + serialNum + ", payDate=" + payDate
				+ ", payCount=" + payCount + ", payType=" + payType + "]";
	}
	
	
	

}