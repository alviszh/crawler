package com.microservice.dao.entity.crawler.housing.huizhou;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name  ="housing_huizhou_pay",indexes = {@Index(name = "index_housing_huizhou_pay_taskid", columnList = "taskid")})
public class HousingHuiZhouPay extends IdEntity implements Serializable{
	private String jndate;             //记账日期
	private String abstracts;          //摘要
	private String occurrence;         //发生额（元）
	private String balance;            //余额（元）
	private String payDate;			   //汇缴年月
	
    private String taskid;
    
    @Override
	public String toString() {
		return "HousingHuiZhouPay [jndate=" + jndate + ", abstracts=" + abstracts
				+ ", occurrence=" + occurrence + ", balance=" + balance
				+ ", payDate=" + payDate + ", taskid=" + taskid + "]";
	}

	public String getJndate() {
		return jndate;
	}

	public void setJndate(String jndate) {
		this.jndate = jndate;
	}

	public String getAbstracts() {
		return abstracts;
	}

	public void setAbstracts(String abstracts) {
		this.abstracts = abstracts;
	}

	public String getOccurrence() {
		return occurrence;
	}

	public void setOccurrence(String occurrence) {
		this.occurrence = occurrence;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getPayDate() {
		return payDate;
	}

	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
    
    
}
