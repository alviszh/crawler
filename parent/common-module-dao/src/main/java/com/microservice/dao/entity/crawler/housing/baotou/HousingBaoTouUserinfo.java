package com.microservice.dao.entity.crawler.housing.baotou;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name  ="housing_baotou_userinfo",indexes = {@Index(name = "index_housing_baotou_userinfo_taskid", columnList = "taskid")})
public class HousingBaoTouUserinfo extends IdEntity implements Serializable{
	private String name;                    //姓名
	private String account;                 //账号
	private String unitName;                //单位名称
	private String unitAccount;             //单位账号
	private String monthlyRemittance;       //月 缴 额
	private String payDate;					//缴存至
	private String base;                    //缴存基数
	private String balance;                 //公积金余额
	private String ratio;                   //缴存比例
	private String state;					//帐号状态
	private String interest;				//利息

	private String taskid;
	
	@Override
	public String toString() {
		return "HousingBaoTouUserinfo [name=" + name + ", account=" + account
				+ ", unitName=" + unitName+ ", unitAccount=" + unitAccount + ", monthlyRemittance=" + monthlyRemittance
				+ ", payDate=" + payDate + ", base=" + base + ", balance=" + balance
				+ ", ratio=" + ratio + ", state=" + state + ", interest=" + interest
				+ ", taskid=" + taskid + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getUnitAccount() {
		return unitAccount;
	}

	public void setUnitAccount(String unitAccount) {
		this.unitAccount = unitAccount;
	}

	public String getMonthlyRemittance() {
		return monthlyRemittance;
	}

	public void setMonthlyRemittance(String monthlyRemittance) {
		this.monthlyRemittance = monthlyRemittance;
	}

	public String getPayDate() {
		return payDate;
	}

	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getRatio() {
		return ratio;
	}

	public void setRatio(String ratio) {
		this.ratio = ratio;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getInterest() {
		return interest;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	
}
