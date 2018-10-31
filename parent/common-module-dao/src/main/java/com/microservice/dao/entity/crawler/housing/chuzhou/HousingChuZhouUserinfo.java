package com.microservice.dao.entity.crawler.housing.chuzhou;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name  ="housing_chuzhou_userinfo",indexes = {@Index(name = "index_housing_chuzhou_userinfo_taskid", columnList = "taskid")})
public class HousingChuZhouUserinfo extends IdEntity implements Serializable{
	
	private String unitAccount;             //单位账号
	private String unitName;                //单位名称
	private String personalAccount;         //个人账号
    private String name;                    //姓名
	private String idCard;                  //身份证号
	private String monthlyRemittance;       //月 缴 额
	private String balance;                 //余额
	private String taskid;
	
	@Override
	public String toString() {
		return "HousingChuZhouUserinfo [unitAccount=" + unitAccount + ", unitName=" + unitName
				+ ", personalAccount=" + personalAccount+ ", name=" + name + ", idCard=" + idCard
				+ ", monthlyRemittance=" + monthlyRemittance + ", balance=" + balance
				+ ", taskid=" + taskid + "]";
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

	public String getPersonalAccount() {
		return personalAccount;
	}

	public void setPersonalAccount(String personalAccount) {
		this.personalAccount = personalAccount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getMonthlyRemittance() {
		return monthlyRemittance;
	}

	public void setMonthlyRemittance(String monthlyRemittance) {
		this.monthlyRemittance = monthlyRemittance;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
}
