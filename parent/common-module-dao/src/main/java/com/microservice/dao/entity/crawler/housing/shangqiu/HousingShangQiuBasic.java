package com.microservice.dao.entity.crawler.housing.shangqiu;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name  ="housing_shangqiu_basic")
public class HousingShangQiuBasic extends IdEntity implements Serializable{
	private String taskid;
	
	private String companyNum;//单位账号
	
	private String companyName;//单位名称
	
	private String perNum;//个人账号
	
	private String num;//身份证号
	
	private String workName;//职工姓名
	
	private String wages;//工资额
	
	private String monthPay;//月缴金额
	
	private String monthToMonth;//缴至月份
	
	private String lastBalance;//上期余额
	
	private String thisBalance;//本期余额
	
	private String balanceTotal;//总余额
	
	private String workStatus;//职工状态

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getCompanyNum() {
		return companyNum;
	}

	public void setCompanyNum(String companyNum) {
		this.companyNum = companyNum;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getPerNum() {
		return perNum;
	}

	public void setPerNum(String perNum) {
		this.perNum = perNum;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getWorkName() {
		return workName;
	}

	public void setWorkName(String workName) {
		this.workName = workName;
	}

	public String getWages() {
		return wages;
	}

	public void setWages(String wages) {
		this.wages = wages;
	}

	public String getMonthPay() {
		return monthPay;
	}

	public void setMonthPay(String monthPay) {
		this.monthPay = monthPay;
	}

	public String getMonthToMonth() {
		return monthToMonth;
	}

	public void setMonthToMonth(String monthToMonth) {
		this.monthToMonth = monthToMonth;
	}

	public String getLastBalance() {
		return lastBalance;
	}

	public void setLastBalance(String lastBalance) {
		this.lastBalance = lastBalance;
	}

	public String getThisBalance() {
		return thisBalance;
	}

	public void setThisBalance(String thisBalance) {
		this.thisBalance = thisBalance;
	}

	public String getBalanceTotal() {
		return balanceTotal;
	}

	public void setBalanceTotal(String balanceTotal) {
		this.balanceTotal = balanceTotal;
	}

	public String getWorkStatus() {
		return workStatus;
	}

	public void setWorkStatus(String workStatus) {
		this.workStatus = workStatus;
	}
	
}
