package com.microservice.dao.entity.crawler.housing.dezhou;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_dezhou_deposit",indexes = {@Index(name = "index_housing_dezhou_deposit_taskid", columnList = "taskid")})
public class HousingDeZhouDepositInformation extends IdEntity implements Serializable{
	
	private String companyNum;//单位账号
	
	private String companyName;//名称
	
	private String personNum;//个人账号
	
	private String name;//姓名

	private String idNum;//身份证
	
	private String sex;//性别
	
	private String time;//缴至日期
	
	private String wageBase;//工资基数
	
	private String payNum;//缴交额
	
	private String balance;//个人余额
	
	private String state;//状态
	
	private String companyPayRate;//单位缴率
	
	private String personPayRate;//个人缴率
	
	private String phoneNum;//联系方式（手机号码）
	
	private String taskid;

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

	public String getPersonNum() {
		return personNum;
	}

	public void setPersonNum(String personNum) {
		this.personNum = personNum;
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

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getWageBase() {
		return wageBase;
	}

	public void setWageBase(String wageBase) {
		this.wageBase = wageBase;
	}

	public String getPayNum() {
		return payNum;
	}

	public void setPayNum(String payNum) {
		this.payNum = payNum;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCompanyPayRate() {
		return companyPayRate;
	}

	public void setCompanyPayRate(String companyPayRate) {
		this.companyPayRate = companyPayRate;
	}

	public String getPersonPayRate() {
		return personPayRate;
	}

	public void setPersonPayRate(String personPayRate) {
		this.personPayRate = personPayRate;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
}
