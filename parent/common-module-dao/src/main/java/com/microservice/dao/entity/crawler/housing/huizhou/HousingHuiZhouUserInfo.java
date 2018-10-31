package com.microservice.dao.entity.crawler.housing.huizhou;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_huizhou_userinfo",indexes = {@Index(name = "index_housing_huizhou_userinfo_taskid", columnList = "taskid")})
public class HousingHuiZhouUserInfo extends IdEntity implements Serializable{
	private String name;               //姓名
	private String idNum;			   //证件号码
	private String accountNum;         //个人住房公积金账号
	private String accountOldNum;      //个人住房公积金旧账号
	private String companyNum;         //单位住房公积金账号
	private String companyOldNum;      //单位住房公积金旧账号
	private String unitName;           //缴存单位名称
	private String state;			   //个人现缴存状态
	private String base;               //个人当前缴存基数
	private String monthlyPay;         //个人当前月缴额
	private String balance;            //个人当前账户余额额

	private String taskid;
	
	@Override
	public String toString() {
		return "HousingHuiZhouUserInfo [name=" + name + ", idNum=" + idNum + ", accountNum=" + accountNum
				+ ", accountOldNum=" + accountOldNum + ", companyNum="+ companyNum + ", companyOldNum=" + companyOldNum
				+ ", unitName=" + unitName + ", state=" + state + ", base="+ base
				+ ", monthlyPay=" + monthlyPay + ", balance="+ balance+ ",taskid=" +taskid + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}

	public String getAccountNum() {
		return accountNum;
	}

	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}

	public String getAccountOldNum() {
		return accountOldNum;
	}

	public void setAccountOldNum(String accountOldNum) {
		this.accountOldNum = accountOldNum;
	}

	public String getCompanyNum() {
		return companyNum;
	}

	public void setCompanyNum(String companyNum) {
		this.companyNum = companyNum;
	}

	public String getCompanyOldNum() {
		return companyOldNum;
	}

	public void setCompanyOldNum(String companyOldNum) {
		this.companyOldNum = companyOldNum;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
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

	public String getMonthlyPay() {
		return monthlyPay;
	}

	public void setMonthlyPay(String monthlyPay) {
		this.monthlyPay = monthlyPay;
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
