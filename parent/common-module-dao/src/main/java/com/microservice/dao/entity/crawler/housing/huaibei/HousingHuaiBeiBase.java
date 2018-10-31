package com.microservice.dao.entity.crawler.housing.huaibei;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_huaibei_base")
public class HousingHuaiBeiBase extends IdEntity implements Serializable{

	
	private String taskid;
	private String companyBankAccount;//单位公积金银行账户
	private String companyName;//单位名称
	private String perHousingNum;//个人公积金账号
	private String name;//个人姓名
	private String companyPay;//单位缴交
	private String perPay;//个人缴交
	private String subsidyPay;//补贴缴交
	private String balance;//余额
	private String nextPayTime;//下次缴费日期
	private String perStatus;//个人状态
	private String wages;//工资额
	private String thisPayTime;//本次缴费日期
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getCompanyBankAccount() {
		return companyBankAccount;
	}
	public void setCompanyBankAccount(String companyBankAccount) {
		this.companyBankAccount = companyBankAccount;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getPerHousingNum() {
		return perHousingNum;
	}
	public void setPerHousingNum(String perHousingNum) {
		this.perHousingNum = perHousingNum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCompanyPay() {
		return companyPay;
	}
	public void setCompanyPay(String companyPay) {
		this.companyPay = companyPay;
	}
	public String getPerPay() {
		return perPay;
	}
	public void setPerPay(String perPay) {
		this.perPay = perPay;
	}
	public String getSubsidyPay() {
		return subsidyPay;
	}
	public void setSubsidyPay(String subsidyPay) {
		this.subsidyPay = subsidyPay;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getNextPayTime() {
		return nextPayTime;
	}
	public void setNextPayTime(String nextPayTime) {
		this.nextPayTime = nextPayTime;
	}
	public String getPerStatus() {
		return perStatus;
	}
	public void setPerStatus(String perStatus) {
		this.perStatus = perStatus;
	}
	public String getWages() {
		return wages;
	}
	public void setWages(String wages) {
		this.wages = wages;
	}
	public String getThisPayTime() {
		return thisPayTime;
	}
	public void setThisPayTime(String thisPayTime) {
		this.thisPayTime = thisPayTime;
	}

	

}
