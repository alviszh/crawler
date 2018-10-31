package com.microservice.dao.entity.crawler.housing.shangqiu;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name  ="housing_shangqiu_detail")
public class HousingShangQiuDetail extends IdEntity implements Serializable{
	private String taskid;
	
	private String accountingTime;//记账日期
	
	private String monthPay;//月缴额
	
	private String payMonths;//缴交月数
	
	private String happenForehead;//发生额
	
	private String balance;//余额
	
	private String beginMonth;//起始月份
	
	private String monthToMonth;//缴至月份
	
	private String happenMode;//发生方式

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getAccountingTime() {
		return accountingTime;
	}

	public void setAccountingTime(String accountingTime) {
		this.accountingTime = accountingTime;
	}

	public String getMonthPay() {
		return monthPay;
	}

	public void setMonthPay(String monthPay) {
		this.monthPay = monthPay;
	}

	public String getPayMonths() {
		return payMonths;
	}

	public void setPayMonths(String payMonths) {
		this.payMonths = payMonths;
	}

	public String getHappenForehead() {
		return happenForehead;
	}

	public void setHappenForehead(String happenForehead) {
		this.happenForehead = happenForehead;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getBeginMonth() {
		return beginMonth;
	}

	public void setBeginMonth(String beginMonth) {
		this.beginMonth = beginMonth;
	}

	public String getMonthToMonth() {
		return monthToMonth;
	}

	public void setMonthToMonth(String monthToMonth) {
		this.monthToMonth = monthToMonth;
	}

	public String getHappenMode() {
		return happenMode;
	}

	public void setHappenMode(String happenMode) {
		this.happenMode = happenMode;
	}
	
	
}
