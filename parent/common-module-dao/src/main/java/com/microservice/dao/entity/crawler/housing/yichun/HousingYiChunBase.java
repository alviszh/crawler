package com.microservice.dao.entity.crawler.housing.yichun;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_yichun_base")
public class HousingYiChunBase extends IdEntity implements Serializable{

	
	private String taskid;
	private String name;//姓名
	private String num;//身份证
	private String compCode;//单位代码
	private String perCode;//个人代码
	private String perMonthPay;//个人月交
	private String compMonthPay;//单位月交
	private String balance;//余额(账户余额)
	private String payMonth;//缴交年月
	private String companyName;//单位名称
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
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
	public String getCompCode() {
		return compCode;
	}
	public void setCompCode(String compCode) {
		this.compCode = compCode;
	}
	public String getPerCode() {
		return perCode;
	}
	public void setPerCode(String perCode) {
		this.perCode = perCode;
	}
	public String getPerMonthPay() {
		return perMonthPay;
	}
	public void setPerMonthPay(String perMonthPay) {
		this.perMonthPay = perMonthPay;
	}
	public String getCompMonthPay() {
		return compMonthPay;
	}
	public void setCompMonthPay(String compMonthPay) {
		this.compMonthPay = compMonthPay;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getPayMonth() {
		return payMonth;
	}
	public void setPayMonth(String payMonth) {
		this.payMonth = payMonth;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	
}
