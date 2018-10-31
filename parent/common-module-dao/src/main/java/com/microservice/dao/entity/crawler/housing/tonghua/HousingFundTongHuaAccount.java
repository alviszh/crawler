package com.microservice.dao.entity.crawler.housing.tonghua;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_tonghua_account",indexes = {@Index(name = "index_housing_tonghua_account_taskid", columnList = "taskid")})
public class HousingFundTongHuaAccount extends IdEntity implements Serializable{

	private String datea;//业务日期
	
	private String descr;//业务摘要
	
	private String payDatea;//缴存年月
	
	private String getMoney;//收入
	
	private String costMoney;//支出
	
	private String fee;//余额
	
	private String taskid;

	@Override
	public String toString() {
		return "HousingFundTongHuaAccount [datea=" + datea + ", descr=" + descr + ", payDatea=" + payDatea
				+ ", getMoney=" + getMoney + ", costMoney=" + costMoney + ", fee=" + fee + ", taskid=" + taskid + "]";
	}

	public String getDatea() {
		return datea;
	}

	public void setDatea(String datea) {
		this.datea = datea;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getPayDatea() {
		return payDatea;
	}

	public void setPayDatea(String payDatea) {
		this.payDatea = payDatea;
	}

	public String getGetMoney() {
		return getMoney;
	}

	public void setGetMoney(String getMoney) {
		this.getMoney = getMoney;
	}

	public String getCostMoney() {
		return costMoney;
	}

	public void setCostMoney(String costMoney) {
		this.costMoney = costMoney;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
}
