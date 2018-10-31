package com.microservice.dao.entity.crawler.housing.suzhou;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_suzhou_account_basic")
public class HousingSuzhouAccountBasic extends IdEntity implements Serializable{

	private String housingNum;//个人公积金账号
	
	private String name;//姓名
	
	private String idNum;//证件号码
	
	private String monthPay;//月应缴额
	
	private String balance;//账户余额
	
	private String yearMonth;//缴至年月
	
	private String accountState;//个人账户状态
	
	private String accountDate;//开户日期
	
	private String numState;//发卡状态
	
	private String companyName;//单位名称
	
	private String companyHousingNum;//单位公积金代码
	
	private String payBank;//缴存银行
	
	private String wageBase;//缴存工资基数
	
	private String companyProportion;//单位缴存比例
	
	private String personProportion;//个人缴存比例
	
	private String paySubsidy;//缴存补贴比例
	
	private String relationBank;//关联银行
	
	private String relationBankNum;//关联银行卡号
	
	private String taskid;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getHousingNum() {
		return housingNum;
	}

	public void setHousingNum(String housingNum) {
		this.housingNum = housingNum;
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

	public String getMonthPay() {
		return monthPay;
	}

	public void setMonthPay(String monthPay) {
		this.monthPay = monthPay;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}

	public String getAccountState() {
		return accountState;
	}

	public void setAccountState(String accountState) {
		this.accountState = accountState;
	}

	public String getAccountDate() {
		return accountDate;
	}

	public void setAccountDate(String accountDate) {
		this.accountDate = accountDate;
	}

	public String getNumState() {
		return numState;
	}

	public void setNumState(String numState) {
		this.numState = numState;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyHousingNum() {
		return companyHousingNum;
	}

	public void setCompanyHousingNum(String companyHousingNum) {
		this.companyHousingNum = companyHousingNum;
	}

	public String getPayBank() {
		return payBank;
	}

	public void setPayBank(String payBank) {
		this.payBank = payBank;
	}

	public String getWageBase() {
		return wageBase;
	}

	public void setWageBase(String wageBase) {
		this.wageBase = wageBase;
	}

	public String getCompanyProportion() {
		return companyProportion;
	}

	public void setCompanyProportion(String companyProportion) {
		this.companyProportion = companyProportion;
	}

	public String getPersonProportion() {
		return personProportion;
	}

	public void setPersonProportion(String personProportion) {
		this.personProportion = personProportion;
	}

	public String getPaySubsidy() {
		return paySubsidy;
	}

	public void setPaySubsidy(String paySubsidy) {
		this.paySubsidy = paySubsidy;
	}

	public String getRelationBank() {
		return relationBank;
	}

	public void setRelationBank(String relationBank) {
		this.relationBank = relationBank;
	}

	public String getRelationBankNum() {
		return relationBankNum;
	}

	public void setRelationBankNum(String relationBankNum) {
		this.relationBankNum = relationBankNum;
	}
	

	
}
