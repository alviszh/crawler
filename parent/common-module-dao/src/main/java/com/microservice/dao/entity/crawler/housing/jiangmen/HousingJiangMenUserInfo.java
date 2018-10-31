package com.microservice.dao.entity.crawler.housing.jiangmen;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_jiangmen_userinfo",indexes = {@Index(name = "index_housing_jiangmen_userinfo_taskid", columnList = "taskid")})
public class HousingJiangMenUserInfo extends IdEntity implements Serializable{
	
	private String name;               //姓名
	private String accumulationFund;   //公积金账号
	private String number;             //单位登记号
	private String unitName;           //单位名称
	private String accountNum;         //个人编号
	private String balance;            //个人账号余额
	private String state;			   //个人账号状态
	private String base;               //个人缴存基数
	private String companyRatio;       //单位缴存比例
	private String companyMonthlyPay;  //月缴存额单位部分
	private String personRatio;        //个人缴存比例
	private String persionalMonthlyPay;//月缴存额个人部分

	private String taskid;
	
	@Override
	public String toString() {
		return "HousingJiangMenUserInfo [name=" + name + ", accumulationFund=" + accumulationFund
				+ ", number=" + number + ", unitName="+ unitName + ", accountNum=" + accountNum
				+ ", balance=" + balance + ", state="+ state + ", base=" + base
				+ ", companyRatio=" + companyRatio + ", companyMonthlyPay="+ companyMonthlyPay + ", personRatio=" + personRatio
				+ ", persionalMonthlyPay=" + persionalMonthlyPay + ",taskid=" +taskid + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccumulationFund() {
		return accumulationFund;
	}

	public void setAccumulationFund(String accumulationFund) {
		this.accumulationFund = accumulationFund;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getAccountNum() {
		return accountNum;
	}

	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getCompanyRatio() {
		return companyRatio;
	}

	public void setCompanyRatio(String companyRatio) {
		this.companyRatio = companyRatio;
	}

	public String getCompanyMonthlyPay() {
		return companyMonthlyPay;
	}

	public void setCompanyMonthlyPay(String companyMonthlyPay) {
		this.companyMonthlyPay = companyMonthlyPay;
	}

	public String getPersonRatio() {
		return personRatio;
	}

	public void setPersonRatio(String personRatio) {
		this.personRatio = personRatio;
	}

	public String getPersionalMonthlyPay() {
		return persionalMonthlyPay;
	}

	public void setPersionalMonthlyPay(String persionalMonthlyPay) {
		this.persionalMonthlyPay = persionalMonthlyPay;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
}
