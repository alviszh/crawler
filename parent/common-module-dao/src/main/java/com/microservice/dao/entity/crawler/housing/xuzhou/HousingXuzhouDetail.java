package com.microservice.dao.entity.crawler.housing.xuzhou;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_xuzhou_detail")
public class HousingXuzhouDetail extends IdEntity implements Serializable{

	private String date;//日期
	
	private String abstracts;//摘要
	
	private String withdrawalAmount;//支取金额
	
	private String IncomeAmount;//收入金额
	
	private String balance;//余额
	
	private String taskid;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAbstracts() {
		return abstracts;
	}

	public void setAbstracts(String abstracts) {
		this.abstracts = abstracts;
	}

	public String getWithdrawalAmount() {
		return withdrawalAmount;
	}

	public void setWithdrawalAmount(String withdrawalAmount) {
		this.withdrawalAmount = withdrawalAmount;
	}

	public String getIncomeAmount() {
		return IncomeAmount;
	}

	public void setIncomeAmount(String incomeAmount) {
		IncomeAmount = incomeAmount;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
	
}
