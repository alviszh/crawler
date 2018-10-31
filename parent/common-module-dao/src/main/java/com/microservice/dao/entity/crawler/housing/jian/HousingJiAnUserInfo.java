package com.microservice.dao.entity.crawler.housing.jian;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_jian_userinfo",indexes = {@Index(name = "index_housing_jian_userinfo_taskid", columnList = "taskid")})
public class HousingJiAnUserInfo extends IdEntity implements Serializable{

	private String name;               //姓名
	private String idNum;			   //身份证号
	private String accountCode;        //个人代码
	private String companyCode;        //单位代码
	private String unitName;           //单位名称
	private String state;			   //状态
	private String balance;            //余额
	private String wangdian;           //所属网点
	
	private String taskid;
	
	@Override
	public String toString() {
		return "HousingJiAnUserInfo [name=" + name + ", idNum=" + idNum
				+ ", accountCode=" + accountCode + ", companyCode="+ companyCode + ", unitName=" + unitName
				+ ", state=" + state + ", balance="+ balance + ", wangdian=" + wangdian
				+ ",taskid=" +taskid + "]";
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

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getWangdian() {
		return wangdian;
	}

	public void setWangdian(String wangdian) {
		this.wangdian = wangdian;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
}
