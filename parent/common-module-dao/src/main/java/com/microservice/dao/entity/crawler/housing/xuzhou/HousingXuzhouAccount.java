package com.microservice.dao.entity.crawler.housing.xuzhou;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_suzhou_account")
public class HousingXuzhouAccount extends IdEntity implements Serializable{

	private String name;//职工姓名
	
	private String idNumber;//身份证号
	
	private String workerNumber;//职工账号
	
	private String unitName;//所在单位
	
	private String homecat;//所属归集点
	
	private String taskid;
	
	private String openingDate;//开户日期
	
	private String currentState;//当前状态
	
	private String shouldPay;//单位应缴月份
	
	private String IndividualMonthPay;//个人月缴
	
	private String unitMonthPay;//单位月缴
	
	private String monthPayNumber;//月缴金额
	
	private String lastYearBalance;//上年余额
	
	private String nowYearPay;//本年补缴
	
	private String nowYearDue;//本年缴交
	
	private String nowYearDraw;//本金支取
	
	private String principalBalance;//本年余额

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getWorkerNumber() {
		return workerNumber;
	}

	public void setWorkerNumber(String workerNumber) {
		this.workerNumber = workerNumber;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getHomecat() {
		return homecat;
	}

	public void setHomecat(String homecat) {
		this.homecat = homecat;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getOpeningDate() {
		return openingDate;
	}

	public void setOpeningDate(String openingDate) {
		this.openingDate = openingDate;
	}

	public String getCurrentState() {
		return currentState;
	}

	public void setCurrentState(String currentState) {
		this.currentState = currentState;
	}

	public String getShouldPay() {
		return shouldPay;
	}

	public void setShouldPay(String shouldPay) {
		this.shouldPay = shouldPay;
	}

	public String getIndividualMonthPay() {
		return IndividualMonthPay;
	}

	public void setIndividualMonthPay(String individualMonthPay) {
		IndividualMonthPay = individualMonthPay;
	}

	public String getUnitMonthPay() {
		return unitMonthPay;
	}

	public void setUnitMonthPay(String unitMonthPay) {
		this.unitMonthPay = unitMonthPay;
	}

	public String getMonthPayNumber() {
		return monthPayNumber;
	}

	public void setMonthPayNumber(String monthPayNumber) {
		this.monthPayNumber = monthPayNumber;
	}

	public String getLastYearBalance() {
		return lastYearBalance;
	}

	public void setLastYearBalance(String lastYearBalance) {
		this.lastYearBalance = lastYearBalance;
	}

	public String getNowYearPay() {
		return nowYearPay;
	}

	public void setNowYearPay(String nowYearPay) {
		this.nowYearPay = nowYearPay;
	}

	public String getNowYearDue() {
		return nowYearDue;
	}

	public void setNowYearDue(String nowYearDue) {
		this.nowYearDue = nowYearDue;
	}

	public String getNowYearDraw() {
		return nowYearDraw;
	}

	public void setNowYearDraw(String nowYearDraw) {
		this.nowYearDraw = nowYearDraw;
	}

	public String getPrincipalBalance() {
		return principalBalance;
	}

	public void setPrincipalBalance(String principalBalance) {
		this.principalBalance = principalBalance;
	}
	
	

	
}
