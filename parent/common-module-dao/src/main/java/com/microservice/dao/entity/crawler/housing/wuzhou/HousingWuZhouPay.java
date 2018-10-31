package com.microservice.dao.entity.crawler.housing.wuzhou;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name  ="housing_wuzhou_pay",indexes = {@Index(name = "index_housing_wuzhou_pay_taskid", columnList = "taskid")})
public class HousingWuZhouPay extends IdEntity implements Serializable{
	private String taskid;
	private String payDate;						//日期
	private String mark;						//摘要
	private String markDate;					//汇交年月
	private String money;					 	//金额
	private String balance;						//余额
	private String remarks;					    //备注
	
	@Override
	public String toString() {
		return "HousingWuZhouPay [taskid=" + taskid + ", payDate=" + payDate + ", mark=" + mark + ", markDate="
				+ markDate + ", money=" + money + ", balance=" + balance + ", remarks=" + remarks+ "]";
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getPayDate() {
		return payDate;
	}

	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getMarkDate() {
		return markDate;
	}

	public void setMarkDate(String markDate) {
		this.markDate = markDate;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	
}
