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
@Table(name = "telecom_henan_monthbill", indexes = {@Index(name = "index_telecom_henan_monthbill_taskid", columnList = "taskid")})
public class TelecomHenanMonthBill extends IdEntity {

	private String taskid;
	private String billDate;							//账单日期
	private String billType;							//项目名称
	private String fee;									//金额/元
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
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
		return "TelecomHenanMonthBill [taskid=" + taskid + ", billDate=" + billDate + ", billType=" + billType
				+ ", fee=" + fee + "]";
	}
	
	
}