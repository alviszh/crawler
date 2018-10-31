package com.microservice.dao.entity.crawler.telecom.qinghai;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_qinghai_userinfo")
public class TelecomQingHaiUserInfoResult extends IdEntity {

	private String name;
	
	private String balance; //余额
	
	private String offerName; //套餐

	private int valueAddedSize; //已办理业务
	
	private String currentAddPoint;  //

	private String currentAvPoint;  

	private String currentCusPoint;

	private String currentYearCusPoint;
	
	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	private Integer userid;

	private String taskid;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getOfferName() {
		return offerName;
	}

	public void setOfferName(String offerName) {
		this.offerName = offerName;
	}

	public int getValueAddedSize() {
		return valueAddedSize;
	}

	public void setValueAddedSize(int valueAddedSize) {
		this.valueAddedSize = valueAddedSize;
	}

	public String getCurrentAddPoint() {
		return currentAddPoint;
	}

	public void setCurrentAddPoint(String currentAddPoint) {
		this.currentAddPoint = currentAddPoint;
	}

	public String getCurrentAvPoint() {
		return currentAvPoint;
	}

	public void setCurrentAvPoint(String currentAvPoint) {
		this.currentAvPoint = currentAvPoint;
	}

	public String getCurrentCusPoint() {
		return currentCusPoint;
	}

	public void setCurrentCusPoint(String currentCusPoint) {
		this.currentCusPoint = currentCusPoint;
	}

	public String getCurrentYearCusPoint() {
		return currentYearCusPoint;
	}

	public void setCurrentYearCusPoint(String currentYearCusPoint) {
		this.currentYearCusPoint = currentYearCusPoint;
	}

	@Override
	public String toString() {
		return "TelecomQingHaiUserInfoResult [name=" + name + ", balance=" + balance + ", offerName=" + offerName
				+ ", valueAddedSize=" + valueAddedSize + ", currentAddPoint=" + currentAddPoint + ", currentAvPoint="
				+ currentAvPoint + ", currentCusPoint=" + currentCusPoint + ", currentYearCusPoint="
				+ currentYearCusPoint + "]";
	}
	
}