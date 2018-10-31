package com.microservice.dao.entity.crawler.housing.fuzhou;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_fuzhou_pay",indexes = {@Index(name = "index_housing_fuzhou_pay_taskid", columnList = "taskid")})
public class HousingFuZhouPay extends IdEntity implements Serializable{
	private String businessTime;            //业务时间
	private String paytime;                 //入账时间
	private String type;                    //业务类型
	private String amouOccurrence;          //发生金额
	private String balance;                 //实时余额
	private String describe;                //业务描述
	
	private String taskid;
	
	@Override
	public String toString() {
		return "HousingFuZhouPay [businessTime=" + businessTime + ", paytime=" + paytime + ", type=" + type
				+ ", amouOccurrence=" + amouOccurrence + ", balance=" + balance+ ", describe=" + describe
				+ ", taskid=" + taskid+ "]";
	}

	public String getBusinessTime() {
		return businessTime;
	}

	public void setBusinessTime(String businessTime) {
		this.businessTime = businessTime;
	}

	public String getPaytime() {
		return paytime;
	}

	public void setPaytime(String paytime) {
		this.paytime = paytime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAmouOccurrence() {
		return amouOccurrence;
	}

	public void setAmouOccurrence(String amouOccurrence) {
		this.amouOccurrence = amouOccurrence;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
}
