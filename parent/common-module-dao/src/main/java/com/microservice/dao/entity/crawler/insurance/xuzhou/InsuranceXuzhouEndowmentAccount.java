package com.microservice.dao.entity.crawler.insurance.xuzhou;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_xuzhou_endowment_account")
public class InsuranceXuzhouEndowmentAccount extends IdEntity{
	
	@Column(name="task_id")
	private String taskid;	//uuid唯一标识
	private String personalCode; //个人代码
	private String name; //姓名
	private String personalFirstTime;//社会保障号码
	private String unitCode; //单位代码
	private String unitName;//单位名称
	private String accountStorageAmount;//截止至x年x月个人账户累计储存额
	private String monthlyPaymentBase;	//当年月缴费基数
	private String paymentMonths;	//当年缴费月数
	private String toAccountFlag;	//当月是否到帐
	public String getPersonalCode() {
		return personalCode;
	}
	public void setPersonalCode(String personalCode) {
		this.personalCode = personalCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPersonalFirstTime() {
		return personalFirstTime;
	}
	public void setPersonalFirstTime(String personalFirstTime) {
		this.personalFirstTime = personalFirstTime;
	}
	public String getUnitCode() {
		return unitCode;
	}
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getAccountStorageAmount() {
		return accountStorageAmount;
	}
	public void setAccountStorageAmount(String accountStorageAmount) {
		this.accountStorageAmount = accountStorageAmount;
	}
	public String getMonthlyPaymentBase() {
		return monthlyPaymentBase;
	}
	public void setMonthlyPaymentBase(String monthlyPaymentBase) {
		this.monthlyPaymentBase = monthlyPaymentBase;
	}
	public String getPaymentMonths() {
		return paymentMonths;
	}
	public void setPaymentMonths(String paymentMonths) {
		this.paymentMonths = paymentMonths;
	}
	public String getToAccountFlag() {
		return toAccountFlag;
	}
	public void setToAccountFlag(String toAccountFlag) {
		this.toAccountFlag = toAccountFlag;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
	
}
