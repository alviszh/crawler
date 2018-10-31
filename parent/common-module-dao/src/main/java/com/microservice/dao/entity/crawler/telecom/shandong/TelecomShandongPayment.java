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
@Table(name = "telecom_shandong_payment", indexes = {@Index(name = "index_telecom_shandong_payment_taskid", columnList = "taskid")})
public class TelecomShandongPayment extends IdEntity {

	private String taskid;
	private String payType;				//缴费方式
	private String payCount;			//缴费金额（元）
	private String orgName;				//缴费渠道
	private String cost;				//销账金额（元）
	private String breach;				//违约金（元）
	private String payDate;				//付费时间
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getPayCount() {
		return payCount;
	}
	public void setPayCount(String payCount) {
		this.payCount = payCount;
	}
	public String getCost() {
		return cost;
	}
	public void setCost(String cost) {
		this.cost = cost;
	}
	public String getBreach() {
		return breach;
	}
	public void setBreach(String breach) {
		this.breach = breach;
	}
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	@Override
	public String toString() {
		return "TelecomShandongPayment [taskid=" + taskid + ", payType=" + payType + ", payCount=" + payCount
				+ ", orgName=" + orgName + ", cost=" + cost + ", breach=" + breach + ", payDate=" + payDate + "]";
	}
	
	
	
	
}