package com.microservice.dao.entity.crawler.housing.zhengzhou;

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
@Table(name  ="housing_zhengzhou_pay",indexes = {@Index(name = "index_housing_zhengzhou_pay_taskid", columnList = "taskid")})
public class HousingZhengZhouPay extends IdEntity implements Serializable{
	private Integer userid;

	private String taskid;
	private String account;       //公积金账户
	private String company;       //单位信息
	private String openAccount;   //开户日期
	private String name;          //缴存人姓名
	private String base;          //缴存基数
	private String amountPaid;    //月缴额
	private String indDeposit;    //个人缴存比例
	private String unitDeposit;   //单位缴存比例
	private String balance;       //缴存余额
	private String month;         //缴至月份
	private String state;         //缴存状态
	
	@Override
	public String toString() {
		return "HousingZhengZhouPay [company=" + company + ", openAccount=" + openAccount +",name=" +name
				+ ", base=" + base + ", amountPaid=" + amountPaid + ", indDeposit=" + indDeposit + ", unitDeposit="
				+ unitDeposit + ", month=" +month + ",state=" + state+ ", account=" +account + ",balance=" + balance
				+ ", userid=" + userid + ", taskid=" + taskid + "]";
	}	
	
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
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getOpenAccount() {
		return openAccount;
	}
	public void setOpenAccount(String openAccount) {
		this.openAccount = openAccount;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBase() {
		return base;
	}
	public void setBase(String base) {
		this.base = base;
	}
	public String getAmountPaid() {
		return amountPaid;
	}
	public void setAmountPaid(String amountPaid) {
		this.amountPaid = amountPaid;
	}
	public String getIndDeposit() {
		return indDeposit;
	}
	public void setIndDeposit(String indDeposit) {
		this.indDeposit = indDeposit;
	}
	public String getUnitDeposit() {
		return unitDeposit;
	}
	public void setUnitDeposit(String unitDeposit) {
		this.unitDeposit = unitDeposit;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	

}
