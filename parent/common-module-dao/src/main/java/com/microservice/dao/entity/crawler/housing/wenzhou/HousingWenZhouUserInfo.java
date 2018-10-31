package com.microservice.dao.entity.crawler.housing.wenzhou;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "housing_wenzhou_userinfo",indexes = {@Index(name = "index_housing_wenzhou_userinfo_taskid", columnList = "taskid")})
public class HousingWenZhouUserInfo extends IdEntity implements Serializable{

	private String account;//职工帐号
	
	private String name;//姓名
	
	private String state;//状态
	
	private String unitaccount;//单位帐号
	
	private String unitname;//单位名称
	
	private String depositbase;//缴费基数
	
	private String unitdeposit;//单位缴存比例
	
	private String userdeposit;//个人缴存比例
	
	private String unitmonthpayment;//单位月缴存额
	
	private String usermonthpayment;//个人月缴存额

	private String yu;//余额
	
	private String taskid;
	
	private Integer userid;
	
	
	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getYu() {
		return yu;
	}

	public void setYu(String yu) {
		this.yu = yu;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getUnitaccount() {
		return unitaccount;
	}

	public void setUnitaccount(String unitaccount) {
		this.unitaccount = unitaccount;
	}

	public String getUnitname() {
		return unitname;
	}

	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}

	public String getDepositbase() {
		return depositbase;
	}

	public void setDepositbase(String depositbase) {
		this.depositbase = depositbase;
	}

	public String getUnitdeposit() {
		return unitdeposit;
	}

	public void setUnitdeposit(String unitdeposit) {
		this.unitdeposit = unitdeposit;
	}

	public String getUserdeposit() {
		return userdeposit;
	}

	public void setUserdeposit(String userdeposit) {
		this.userdeposit = userdeposit;
	}

	public String getUnitmonthpayment() {
		return unitmonthpayment;
	}

	public void setUnitmonthpayment(String unitmonthpayment) {
		this.unitmonthpayment = unitmonthpayment;
	}

	public String getUsermonthpayment() {
		return usermonthpayment;
	}

	public void setUsermonthpayment(String usermonthpayment) {
		this.usermonthpayment = usermonthpayment;
	}

	@Override
	public String toString() {
		return "HousingWenZhouUserInfo [account=" + account + ", name=" + name + ", state=" + state + ", unitaccount="
				+ unitaccount + ", unitname=" + unitname + ", depositbase=" + depositbase + ", unitdeposit="
				+ unitdeposit + ", userdeposit=" + userdeposit + ", unitmonthpayment=" + unitmonthpayment
				+ ", usermonthpayment=" + usermonthpayment + "]";
	}

	public HousingWenZhouUserInfo(String account, String name, String state, String unitaccount, String unitname,
			String depositbase, String unitdeposit, String userdeposit, String unitmonthpayment,
			String usermonthpayment, String yu, String taskid, Integer userid) {
		super();
		this.account = account;
		this.name = name;
		this.state = state;
		this.unitaccount = unitaccount;
		this.unitname = unitname;
		this.depositbase = depositbase;
		this.unitdeposit = unitdeposit;
		this.userdeposit = userdeposit;
		this.unitmonthpayment = unitmonthpayment;
		this.usermonthpayment = usermonthpayment;
		this.yu = yu;
		this.taskid = taskid;
		this.userid = userid;
	}

	public HousingWenZhouUserInfo() {
		super();
	}
	
	
}
