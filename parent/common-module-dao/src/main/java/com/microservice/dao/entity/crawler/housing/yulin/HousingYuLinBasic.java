package com.microservice.dao.entity.crawler.housing.yulin;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_yulin_basic",indexes = {@Index(name = "index_housing_yulin_basic_taskid", columnList = "taskid")})
public class HousingYuLinBasic extends IdEntity implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String taskid;
	private String workNum;//职工账号
	private String name;// 职工姓名
	private String company;//单位名称
	private String num;//身份证号码
	private String date;//开户时间
	private String monthPay; //月存金额
	private String perMonthPay;//个人月存额
	private String companyMonthPay;//单位月存额
	private String yearDraw;//本年支取
	private String thisYearBalance;//本金余额
	private String workStatus;//职工状态
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getWorkNum() {
		return workNum;
	}
	public void setWorkNum(String workNum) {
		this.workNum = workNum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getMonthPay() {
		return monthPay;
	}
	public void setMonthPay(String monthPay) {
		this.monthPay = monthPay;
	}
	public String getPerMonthPay() {
		return perMonthPay;
	}
	public void setPerMonthPay(String perMonthPay) {
		this.perMonthPay = perMonthPay;
	}
	public String getCompanyMonthPay() {
		return companyMonthPay;
	}
	public void setCompanyMonthPay(String companyMonthPay) {
		this.companyMonthPay = companyMonthPay;
	}
	public String getYearDraw() {
		return yearDraw;
	}
	public void setYearDraw(String yearDraw) {
		this.yearDraw = yearDraw;
	}
	public String getThisYearBalance() {
		return thisYearBalance;
	}
	public void setThisYearBalance(String thisYearBalance) {
		this.thisYearBalance = thisYearBalance;
	}
	public String getWorkStatus() {
		return workStatus;
	}
	public void setWorkStatus(String workStatus) {
		this.workStatus = workStatus;
	}
	
}
