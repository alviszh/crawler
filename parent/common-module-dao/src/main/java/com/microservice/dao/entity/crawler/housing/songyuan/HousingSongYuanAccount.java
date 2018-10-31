package com.microservice.dao.entity.crawler.housing.songyuan;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_songyuan_account")
public class HousingSongYuanAccount extends IdEntity implements Serializable{
	
	private String name;//姓名

	private String companyCode;//单位代码
	
	private String personCode;//个人代码
	
	private String wageSettlement;//工资结算
	
	private String companyMonthPay;//单位月缴额
	
	private String personMonthPay;//个人月缴额
	
	private String monthPay;//月缴额合计
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getPersonCode() {
		return personCode;
	}

	public void setPersonCode(String personCode) {
		this.personCode = personCode;
	}

	public String getWageSettlement() {
		return wageSettlement;
	}

	public void setWageSettlement(String wageSettlement) {
		this.wageSettlement = wageSettlement;
	}

	public String getCompanyMonthPay() {
		return companyMonthPay;
	}

	public void setCompanyMonthPay(String companyMonthPay) {
		this.companyMonthPay = companyMonthPay;
	}

	public String getPersonMonthPay() {
		return personMonthPay;
	}

	public void setPersonMonthPay(String personMonthPay) {
		this.personMonthPay = personMonthPay;
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

	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	private String balance;//余额
	
	private String changeType;//变更类型
	
	private String idNum;//身份证号
	
	private String taskid;

}
