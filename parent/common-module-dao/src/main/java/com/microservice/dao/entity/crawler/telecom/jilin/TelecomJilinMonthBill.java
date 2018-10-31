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
@Table(name = "telecom_jilin_monthbill", indexes = {@Index(name = "index_telecom_jilin_monthbill_taskid", columnList = "taskid")})
public class TelecomJilinMonthBill extends IdEntity {

	private String phoneNum;			//接入号码
	
	private String billDate;			//账期
	
	private String billType;			//费用项
	
	private String fee;					//费用（元）
	
	private String taskid;				
	
	
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	public String getBillType() {
		return billType;
	}
	public void setBillType(String billType) {
		this.billType = billType;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	@Override
	public String toString() {
		return "TelecomJilinMonthBill [phoneNum=" + phoneNum + ", billDate=" + billDate + ", billType=" + billType
				+ ", fee=" + fee + ", taskid=" + taskid + "]";
	}
	
}