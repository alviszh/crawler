package com.microservice.dao.entity.crawler.housing.jiaozuo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name  ="housing_jiaozuo_pay")
public class HousingJiaoZuoPay extends IdEntity implements Serializable{
	private String taskid;
	
	private String payProportion;//缴存比例
	
	private String monthSalary;//月均工资
	
	private String companyDeposit;//单位月存
	
	private String personDeposit;//个人月存
	
	private String payYears;//实缴年月
	
	private String lastYearCarryover;//上年结转
	
	private String yearRemitted;//本年汇缴
	
	private String yearBack;//本年补缴
	
	private String yearExtract;//本年提取
	
	private String balance;//余额
	
	private String yearInterest;//本年利息

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getPayProportion() {
		return payProportion;
	}

	public void setPayProportion(String payProportion) {
		this.payProportion = payProportion;
	}

	public String getMonthSalary() {
		return monthSalary;
	}

	public void setMonthSalary(String monthSalary) {
		this.monthSalary = monthSalary;
	}

	public String getCompanyDeposit() {
		return companyDeposit;
	}

	public void setCompanyDeposit(String companyDeposit) {
		this.companyDeposit = companyDeposit;
	}

	public String getPersonDeposit() {
		return personDeposit;
	}

	public void setPersonDeposit(String personDeposit) {
		this.personDeposit = personDeposit;
	}

	public String getPayYears() {
		return payYears;
	}

	public void setPayYears(String payYears) {
		this.payYears = payYears;
	}

	public String getLastYearCarryover() {
		return lastYearCarryover;
	}

	public void setLastYearCarryover(String lastYearCarryover) {
		this.lastYearCarryover = lastYearCarryover;
	}

	public String getYearRemitted() {
		return yearRemitted;
	}

	public void setYearRemitted(String yearRemitted) {
		this.yearRemitted = yearRemitted;
	}

	public String getYearBack() {
		return yearBack;
	}

	public void setYearBack(String yearBack) {
		this.yearBack = yearBack;
	}

	public String getYearExtract() {
		return yearExtract;
	}

	public void setYearExtract(String yearExtract) {
		this.yearExtract = yearExtract;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getYearInterest() {
		return yearInterest;
	}

	public void setYearInterest(String yearInterest) {
		this.yearInterest = yearInterest;
	}
	
	
}
