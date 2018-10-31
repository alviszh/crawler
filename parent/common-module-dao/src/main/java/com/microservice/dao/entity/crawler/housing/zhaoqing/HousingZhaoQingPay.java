package com.microservice.dao.entity.crawler.housing.zhaoqing;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name  ="housing_zhaoqing_pay",indexes = {@Index(name = "index_housing_zhaoqing_pay_taskid", columnList = "taskid")})
public class HousingZhaoQingPay extends IdEntity implements Serializable {
	private String jndate;                  //交易日期
	private String companyNum;             //单位账号
	private String companyName;			   //单位名称
	private String accountNum;             //个人账号
	private String name;				   //姓名
	private String loan;                   //借贷标志	
	private String amount;                 //发生额	
	private String balance;				   //个人账户余额
	private String abstracts;              //摘要
	private String taskid;
	
	@Override
	public String toString() {
		return "HousingZhaoQingPay [jndate=" + jndate + ", companyNum=" + companyNum + ", companyName=" + companyName 
				+ ", accountNum=" + accountNum + ", name=" + name + ", loan=" + loan
				+ ", amount=" + amount + ", balance=" + balance + ", abstracts=" + abstracts
				+ ", taskid=" + taskid + "]";
	}

	public String getJndate() {
		return jndate;
	}

	public void setJndate(String jndate) {
		this.jndate = jndate;
	}

	public String getCompanyNum() {
		return companyNum;
	}

	public void setCompanyNum(String companyNum) {
		this.companyNum = companyNum;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getAccountNum() {
		return accountNum;
	}

	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLoan() {
		return loan;
	}

	public void setLoan(String loan) {
		this.loan = loan;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getAbstracts() {
		return abstracts;
	}

	public void setAbstracts(String abstracts) {
		this.abstracts = abstracts;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
}
