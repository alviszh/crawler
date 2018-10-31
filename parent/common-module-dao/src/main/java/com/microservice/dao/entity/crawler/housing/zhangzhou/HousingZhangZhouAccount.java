package com.microservice.dao.entity.crawler.housing.zhangzhou;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_zhangzhou_account")
public class HousingZhangZhouAccount extends IdEntity implements Serializable{

	
	private String taskid;
	private String accountStatus;//账户状态
	private String workNumber;//职工账号
	private String date;//开户日期
	private String bank;//开户银行
	private String companyName;//单位名称
	private String workName;//职工姓名
	private String num;//身份证号码
	private String lastYear;//上年结转额
	private String compPaymentRate;//单位缴交率
	private String perPaymentRate;//个人缴交率
	private String compPaymentNumber;//单位应缴额
	private String workPaymentNumber;//职工应缴额
	private String monthPayNumber;//月应缴额
	private String payYearMonth;//缴至年月
	private String currentBalance;//活期余额
	private String regularBalance;//定期余额
	private String accountBalance;//账户余额
	private String capitalCenter;//资金中心
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getAccountStatus() {
		return accountStatus;
	}
	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}
	public String getWorkNumber() {
		return workNumber;
	}
	public void setWorkNumber(String workNumber) {
		this.workNumber = workNumber;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getWorkName() {
		return workName;
	}
	public void setWorkName(String workName) {
		this.workName = workName;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getLastYear() {
		return lastYear;
	}
	public void setLastYear(String lastYear) {
		this.lastYear = lastYear;
	}
	public String getCompPaymentRate() {
		return compPaymentRate;
	}
	public void setCompPaymentRate(String compPaymentRate) {
		this.compPaymentRate = compPaymentRate;
	}
	public String getPerPaymentRate() {
		return perPaymentRate;
	}
	public void setPerPaymentRate(String perPaymentRate) {
		this.perPaymentRate = perPaymentRate;
	}
	public String getCompPaymentNumber() {
		return compPaymentNumber;
	}
	public void setCompPaymentNumber(String compPaymentNumber) {
		this.compPaymentNumber = compPaymentNumber;
	}
	public String getWorkPaymentNumber() {
		return workPaymentNumber;
	}
	public void setWorkPaymentNumber(String workPaymentNumber) {
		this.workPaymentNumber = workPaymentNumber;
	}
	public String getMonthPayNumber() {
		return monthPayNumber;
	}
	public void setMonthPayNumber(String monthPayNumber) {
		this.monthPayNumber = monthPayNumber;
	}
	public String getPayYearMonth() {
		return payYearMonth;
	}
	public void setPayYearMonth(String payYearMonth) {
		this.payYearMonth = payYearMonth;
	}
	public String getCurrentBalance() {
		return currentBalance;
	}
	public void setCurrentBalance(String currentBalance) {
		this.currentBalance = currentBalance;
	}
	public String getRegularBalance() {
		return regularBalance;
	}
	public void setRegularBalance(String regularBalance) {
		this.regularBalance = regularBalance;
	}
	public String getAccountBalance() {
		return accountBalance;
	}
	public void setAccountBalance(String accountBalance) {
		this.accountBalance = accountBalance;
	}
	public String getCapitalCenter() {
		return capitalCenter;
	}
	public void setCapitalCenter(String capitalCenter) {
		this.capitalCenter = capitalCenter;
	}
	
}
