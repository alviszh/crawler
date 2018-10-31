package com.microservice.dao.entity.crawler.insurance.xuzhou;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_xuzhou_medical_account")
public class InsuranceXuzhouMedicalAccount extends IdEntity{
	
	@Column(name="task_id")
	private String taskid;	//uuid唯一标识
	private String personalCode; //个人代码
	private String name; //姓名
	private String personalFirstTime;//社会保障号码
	private String unitCode; //单位代码
	private String unitName;//单位名称
	private String monthlyPaymentBase;	//当年月缴费基数
	private String debitAmount;			//当月应划账金额
	private String toAccountFlag;	//当月是否到帐
	private String accountBalance;//账户余额 
	private String 	payStandard;	//本年门诊慢性病起付标准累计;
	private String 	payCumulative;	//本年门诊慢性病支付累计

	public String getDebitAmount() {
		return debitAmount;
	}
	public void setDebitAmount(String debitAmount) {
		this.debitAmount = debitAmount;
	}
	public String getAccountBalance() {
		return accountBalance;
	}
	public void setAccountBalance(String accountBalance) {
		this.accountBalance = accountBalance;
	}
	public String getPayStandard() {
		return payStandard;
	}
	public void setPayStandard(String payStandard) {
		this.payStandard = payStandard;
	}
	public String getPayCumulative() {
		return payCumulative;
	}
	public void setPayCumulative(String payCumulative) {
		this.payCumulative = payCumulative;
	}
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
	public String getMonthlyPaymentBase() {
		return monthlyPaymentBase;
	}
	public void setMonthlyPaymentBase(String monthlyPaymentBase) {
		this.monthlyPaymentBase = monthlyPaymentBase;
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
