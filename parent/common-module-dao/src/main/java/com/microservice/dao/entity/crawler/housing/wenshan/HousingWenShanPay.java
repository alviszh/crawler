package com.microservice.dao.entity.crawler.housing.wenshan;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name  ="housing_wenshan_pay",indexes = {@Index(name = "index_housing_wenshan_pay_taskid", columnList = "taskid")})
public class HousingWenShanPay extends IdEntity implements Serializable{
	private String taskid;
	private String name;						//姓名
	private String type;					    //业务类型
	private String money;					 	//发生额
	private String balance;						//账户余额
	private String payDate;						//记账日期
	
	@Override
	public String toString() {
		return "HousingWenShanPay [taskid=" + taskid + ", name=" + name + ", type=" + type + ", money="
				+ money + ", balance=" + balance + ", payDate=" + payDate +  "]";
	}

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getPayDate() {
		return payDate;
	}

	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	
	
}
