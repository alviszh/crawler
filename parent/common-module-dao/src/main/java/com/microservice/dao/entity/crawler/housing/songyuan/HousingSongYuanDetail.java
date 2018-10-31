package com.microservice.dao.entity.crawler.housing.songyuan;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_songyuan_detail")
public class HousingSongYuanDetail extends IdEntity implements Serializable{
	
	private String serialNum;//业务流水号
	
	private String companyCode;//单位代码
	
	private String personCode;//个人代码
	
	private String payMonth;//缴至年月
	
	private String interestTime;//计息日期

	private String capitalType;//资金种类
	
	private String incomeAmount;//收入金额
	
	private String payMoney; // 支出金额
	
	private String taskid;

	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
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

	public String getPayMonth() {
		return payMonth;
	}

	public void setPayMonth(String payMonth) {
		this.payMonth = payMonth;
	}

	public String getInterestTime() {
		return interestTime;
	}

	public void setInterestTime(String interestTime) {
		this.interestTime = interestTime;
	}

	public String getCapitalType() {
		return capitalType;
	}

	public void setCapitalType(String capitalType) {
		this.capitalType = capitalType;
	}

	public String getIncomeAmount() {
		return incomeAmount;
	}

	public void setIncomeAmount(String incomeAmount) {
		this.incomeAmount = incomeAmount;
	}

	public String getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(String payMoney) {
		this.payMoney = payMoney;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	
}
