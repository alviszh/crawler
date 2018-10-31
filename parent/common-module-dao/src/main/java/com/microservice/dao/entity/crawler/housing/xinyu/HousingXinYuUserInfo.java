package com.microservice.dao.entity.crawler.housing.xinyu;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 新余公积金用户信息
 */
@Entity
@Table(name="housing_xinyu_userinfo",indexes = {@Index(name = "index_housing_xinyu_userinfo_taskid", columnList = "taskid")})
public class HousingXinYuUserInfo extends IdEntity implements Serializable{
	private String unitCode;        //公积金账号
	private String companyName;     //单位
	private String username;        //姓名
	private String balance;         //公积金余额
	private String monthlyPayment;  //月缴纳数
	
	private String taskid;
	
	@Override
	public String toString() {
		return "HousingXinYuUserInfo [unitCode=" + unitCode + ", companyName=" + companyName +",username=" +username
				+ ", balance=" + balance + ", monthlyPayment=" + monthlyPayment + ",taskid=" + taskid + "]";
	}

	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getMonthlyPayment() {
		return monthlyPayment;
	}

	public void setMonthlyPayment(String monthlyPayment) {
		this.monthlyPayment = monthlyPayment;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
}
