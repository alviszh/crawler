package com.microservice.dao.entity.crawler.housing.zhumadian;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_zhumadian_account",indexes = {@Index(name = "index_housing_zhumadian_account_taskid", columnList = "taskid")})
public class HousingFundZhuMaDianAccount extends IdEntity{

	
	private String taskid;
	
	private String accountNum;
	
	private String datea;
	
	private String getMoney;
	
	private String setMoney;
	
	private String fee;
	
	private String descr;

	@Override
	public String toString() {
		return "HousingFundZhuMaDianAccount [taskid=" + taskid + ", accountNum=" + accountNum + ", datea=" + datea
				+ ", getMoney=" + getMoney + ", setMoney=" + setMoney + ", fee=" + fee + ", descr=" + descr + "]";
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getAccountNum() {
		return accountNum;
	}

	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}

	public String getDatea() {
		return datea;
	}

	public void setDatea(String datea) {
		this.datea = datea;
	}

	public String getGetMoney() {
		return getMoney;
	}

	public void setGetMoney(String getMoney) {
		this.getMoney = getMoney;
	}

	public String getSetMoney() {
		return setMoney;
	}

	public void setSetMoney(String setMoney) {
		this.setMoney = setMoney;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	
	
	
}
