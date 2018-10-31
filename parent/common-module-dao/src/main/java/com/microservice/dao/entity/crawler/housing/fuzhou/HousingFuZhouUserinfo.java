package com.microservice.dao.entity.crawler.housing.fuzhou;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name  ="housing_fuzhou_userinfo",indexes = {@Index(name = "index_housing_fuzhou_userinfo_taskid", columnList = "taskid")})
public class HousingFuZhouUserinfo extends IdEntity implements Serializable{
	private String personalAccount;         //个人客户号
	private String unitAccount;             //单位客户号
	private String unitName;                //单位名称
	private String unitMonth;               //单位缴至月份
	private String personalMonth;           //个人缴至月份
	private String base;                    //缴存基数
	private String personPay;               //个人缴存额
	private String companyPay;              //单位缴存额
	private String interest;                //年度利息
	private String balance;                 //账户余额
	private String state;                   //缴存状态
    private String name;                    //姓名

	private String taskid;
	
	@Override
	public String toString() {
		return "HousingFuZhouUserinfo [personalAccount=" + personalAccount + ", unitAccount=" + unitAccount
				+ ", unitName=" + unitName+ ", unitMonth=" + unitMonth + ", personalMonth=" + personalMonth
				+ ", base=" + base + ", personPay=" + personPay+ ", companyPay=" + companyPay
				+ ", interest=" + interest + ", balance=" + balance+ ", state=" + state
				+ ", name=" + name+ ", taskid=" + taskid + "]";
	}

	public String getPersonalAccount() {
		return personalAccount;
	}

	public void setPersonalAccount(String personalAccount) {
		this.personalAccount = personalAccount;
	}

	public String getUnitAccount() {
		return unitAccount;
	}

	public void setUnitAccount(String unitAccount) {
		this.unitAccount = unitAccount;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getUnitMonth() {
		return unitMonth;
	}

	public void setUnitMonth(String unitMonth) {
		this.unitMonth = unitMonth;
	}

	public String getPersonalMonth() {
		return personalMonth;
	}

	public void setPersonalMonth(String personalMonth) {
		this.personalMonth = personalMonth;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getPersonPay() {
		return personPay;
	}

	public void setPersonPay(String personPay) {
		this.personPay = personPay;
	}

	public String getCompanyPay() {
		return companyPay;
	}

	public void setCompanyPay(String companyPay) {
		this.companyPay = companyPay;
	}

	public String getInterest() {
		return interest;
	}

	public void setInterest(String interest) {
		this.interest = interest;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
}
