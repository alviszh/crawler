package com.microservice.dao.entity.crawler.housing.xianyang;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 咸阳公积金用户信息
 */
@Entity
@Table(name="housing_xianyang_userinfo",indexes = {@Index(name = "index_housing_xianyang_userinfo_taskid", columnList = "taskid")})
public class HousingXianYangUserInfo extends IdEntity implements Serializable{
private static final long serialVersionUID = 1221206979457794498L;
	
	
	private String companyName;     //单位名称
	private String unitCode;        //单位账号
	private String username;        //姓名
	private String personalCode;    //个人账号
	private String idCard;          //身份证号
	private String monthlyPayment;  //月缴存额
	private String balance;         //余额(元)
	private String years;           //缴至年月
	private String state;           //账户状态
	
	private String taskid;
	
	@Override
	public String toString() {
		return "HousingXuChangUserInfo [unitCode=" + unitCode + ", companyName=" + companyName +",personalCode=" +personalCode
				+ ", username=" + username + ", idCard=" + idCard + ", monthlyPayment=" + monthlyPayment
				+ ", balance="+ balance + ", years=" +years + ",state=" + state
				+ ",taskid=" + taskid + "]";
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPersonalCode() {
		return personalCode;
	}

	public void setPersonalCode(String personalCode) {
		this.personalCode = personalCode;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getMonthlyPayment() {
		return monthlyPayment;
	}

	public void setMonthlyPayment(String monthlyPayment) {
		this.monthlyPayment = monthlyPayment;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getYears() {
		return years;
	}

	public void setYears(String years) {
		this.years = years;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
