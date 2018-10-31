package com.microservice.dao.entity.crawler.housing.xinxiang;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 新乡公积金缴存信息
 */
@Entity
@Table(name="housing_xinxiang_paydetails")
public class HousingXinxiangPaydetails extends IdEntity implements Serializable {
	private static final long serialVersionUID = -758773159050632619L;
	
	private String companyNumber;;//单位编号
	private String accountNumber;//个人编号
	private String incomeAmount;//收入金额
	private String expendAmount;//支取金额
	private String balance;//余额
	private String type;//发生方式
	private String payForMonth;//缴交月份
	private String payDate;//发生日期
	private String taskid;
	public String getCompanyNumber() {
		return companyNumber;
	}
	public void setCompanyNumber(String companyNumber) {
		this.companyNumber = companyNumber;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getIncomeAmount() {
		return incomeAmount;
	}
	public void setIncomeAmount(String incomeAmount) {
		this.incomeAmount = incomeAmount;
	}
	public String getExpendAmount() {
		return expendAmount;
	}
	public void setExpendAmount(String expendAmount) {
		this.expendAmount = expendAmount;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPayForMonth() {
		return payForMonth;
	}
	public void setPayForMonth(String payForMonth) {
		this.payForMonth = payForMonth;
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

	@Override
	public String toString() {
		return "HousingXinxiangPaydetails [companyNumber=" + companyNumber + ", accountNumber=" + accountNumber
				+ ", incomeAmount=" + incomeAmount + ", expendAmount=" + expendAmount + ", balance=" + balance
				+ ", type=" + type + ", payForMonth=" + payForMonth + ", payDate=" + payDate + ", taskid=" + taskid
				+ "]";
	}
	
	public HousingXinxiangPaydetails(String companyNumber, String accountNumber, String incomeAmount,
			String expendAmount, String balance, String type, String payForMonth, String payDate, String taskid) {
		super();
		this.companyNumber = companyNumber;
		this.accountNumber = accountNumber;
		this.incomeAmount = incomeAmount;
		this.expendAmount = expendAmount;
		this.balance = balance;
		this.type = type;
		this.payForMonth = payForMonth;
		this.payDate = payDate;
		this.taskid = taskid;
	}
	public HousingXinxiangPaydetails() {
		super();
	}	
}
