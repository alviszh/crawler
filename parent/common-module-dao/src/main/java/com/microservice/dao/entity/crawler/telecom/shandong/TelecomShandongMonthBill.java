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
@Table(name = "telecom_shandong_monthbill", indexes = {@Index(name = "index_telecom_shandong_monthbill_taskid", columnList = "taskid")})
public class TelecomShandongMonthBill extends IdEntity {

	private String billDate;			//计费周期
	
	private String billType;			//费用项
	
	private String fee;					//金额（元）
	
	private String taskid;

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

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	@Override
	public String toString() {
		return "TelecomShandongMonthBill [billDate=" + billDate + ", billType=" + billType + ", fee=" + fee
				+ ", taskid=" + taskid + "]";
	}				
	
	
}