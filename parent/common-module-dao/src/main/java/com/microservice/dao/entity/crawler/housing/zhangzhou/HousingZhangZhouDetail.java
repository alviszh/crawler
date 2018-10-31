package com.microservice.dao.entity.crawler.housing.zhangzhou;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_zhangzhou_detail")
public class HousingZhangZhouDetail extends IdEntity implements Serializable{

	
	private String taskid;
	private String accountNumber;//账号
	private String name;//姓名
	private String date;//日期
	private String abs;//摘要
	private String drawMoney;//支取金额
	private String payMoney;//缴存金额
	private String balance;//余额
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getAbs() {
		return abs;
	}
	public void setAbs(String abs) {
		this.abs = abs;
	}
	public String getDrawMoney() {
		return drawMoney;
	}
	public void setDrawMoney(String drawMoney) {
		this.drawMoney = drawMoney;
	}
	public String getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(String payMoney) {
		this.payMoney = payMoney;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	
	
	
}
