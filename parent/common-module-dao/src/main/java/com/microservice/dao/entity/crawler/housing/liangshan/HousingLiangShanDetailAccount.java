package com.microservice.dao.entity.crawler.housing.liangshan;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 凉山彝族自治州公积金明细账信息
 * @author: sln
 * @date:
 */
@Entity
@Table(name = "housing_liangshan_detailaccount",indexes = {@Index(name = "index_housing_liangshan_detailaccount_taskid", columnList = "taskid")})
public class HousingLiangShanDetailAccount extends IdEntity implements Serializable {
	private static final long serialVersionUID = 6481831903414445397L;
	private String taskid;
//	记账日期
	private String accdate;
//	业务类型
	private String businesstype;
//	收入
	private String income;
//	支出（本金）
	private String capitalexpense;
//	支出（利息）
	private String interestexpense;
//	余额
	private String balance;
//	摘要
	private String summary;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getAccdate() {
		return accdate;
	}
	public void setAccdate(String accdate) {
		this.accdate = accdate;
	}
	public String getBusinesstype() {
		return businesstype;
	}
	public void setBusinesstype(String businesstype) {
		this.businesstype = businesstype;
	}
	public String getIncome() {
		return income;
	}
	public void setIncome(String income) {
		this.income = income;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	public String getCapitalexpense() {
		return capitalexpense;
	}
	public void setCapitalexpense(String capitalexpense) {
		this.capitalexpense = capitalexpense;
	}
	public String getInterestexpense() {
		return interestexpense;
	}
	public void setInterestexpense(String interestexpense) {
		this.interestexpense = interestexpense;
	}
	public HousingLiangShanDetailAccount() {
		super();
		// TODO Auto-generated constructor stub
	}
}
