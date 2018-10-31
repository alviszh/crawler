package com.microservice.dao.entity.crawler.telecom.anhui;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="telecom_anhui_bill",indexes = {@Index(name = "index_telecom_anhui_bill_taskid", columnList = "taskid")}) 
public class TelecomAnhuiBill extends IdEntity{

	private String billName;//名字
	
	private String Money;//费用
	
	private String month;//月份
	
	private String desce;//描述
	private String taskid;
	@Override
	public String toString() {
		return "TelecomAnhuiBill [billName=" + billName + ", Money=" + Money + ", month=" + month + ", desce=" + desce
				+ ", taskid=" + taskid + "]";
	}
	@Column(columnDefinition="text")
	public String getBillName() {
		return billName;
	}
	public void setBillName(String billName) {
		this.billName = billName;
	}
	public String getMoney() {
		return Money;
	}
	public void setMoney(String money) {
		Money = money;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	
	@Column(columnDefinition="text")
	public String getDesce() {
		return desce;
	}
	public void setDesce(String desce) {
		this.desce = desce;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
}
