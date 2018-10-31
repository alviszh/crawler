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
@Table(name = "telecom_henan_payment", indexes = {@Index(name = "index_telecom_henan_payment_taskid", columnList = "taskid")})
public class TelecomHenanPayment extends IdEntity {

	private String taskid;
	private String serialNum;					//缴费流水号
	private String payType;						//缴费方式
	private String fee;							//缴费金额（元）
	private String payDate;						//缴费时间
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
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	@Override
	public String toString() {
		return "TelecomHenanPayment [taskid=" + taskid + ", serialNum=" + serialNum + ", payType=" + payType + ", fee="
				+ fee + ", payDate=" + payDate + "]";
	}
	

}