package com.microservice.dao.entity.crawler.housing.zhaotong;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_zhaotong_base")
public class HousingZhaoTongBase extends IdEntity implements Serializable{

	
	private String taskid;
	private String companyName;//单位名称
	private String perNum;//个人账号
	private String name;//姓名
	private String num;//身份证
	private String notionalStatus;//归集状态
	private String perPayTime;//个人缴款时间
	private String wagesBase;//工资基数
	private String monthPay;//月缴金额
	private String notionalBalance;//归集余额
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getPerNum() {
		return perNum;
	}
	public void setPerNum(String perNum) {
		this.perNum = perNum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getNotionalStatus() {
		return notionalStatus;
	}
	public void setNotionalStatus(String notionalStatus) {
		this.notionalStatus = notionalStatus;
	}
	public String getPerPayTime() {
		return perPayTime;
	}
	public void setPerPayTime(String perPayTime) {
		this.perPayTime = perPayTime;
	}
	public String getWagesBase() {
		return wagesBase;
	}
	public void setWagesBase(String wagesBase) {
		this.wagesBase = wagesBase;
	}
	public String getMonthPay() {
		return monthPay;
	}
	public void setMonthPay(String monthPay) {
		this.monthPay = monthPay;
	}
	public String getNotionalBalance() {
		return notionalBalance;
	}
	public void setNotionalBalance(String notionalBalance) {
		this.notionalBalance = notionalBalance;
	}
}
