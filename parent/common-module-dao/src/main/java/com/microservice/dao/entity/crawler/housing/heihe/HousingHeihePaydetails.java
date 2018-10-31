package com.microservice.dao.entity.crawler.housing.heihe;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 盘锦公积金用户信息
 */
@Entity
@Table(name="housing_heihe_paydetails")
public class HousingHeihePaydetails extends IdEntity implements Serializable {
	private static final long serialVersionUID = -758773159050632619L;
	private String accountDate;//	结算日期
	private String accountAmount;//	发生额
	private String summary;//	摘要
	private String balance;//	余额
	private String payTime;//	缴存期间
	private String taskid;	
	public String getAccountDate() {
		return accountDate;
	}
	public void setAccountDate(String accountDate) {
		this.accountDate = accountDate;
	}
	public String getAccountAmount() {
		return accountAmount;
	}
	public void setAccountAmount(String accountAmount) {
		this.accountAmount = accountAmount;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Override
	public String toString() {
		return "HousingPanjinPaydetails [accountDate=" + accountDate + ", accountAmount=" + accountAmount + ", summary="
				+ summary + ", balance=" + balance + ", payTime=" + payTime + ", taskid=" + taskid + "]";
	}
	public HousingHeihePaydetails(String accountDate, String accountAmount, String summary, String balance,
			String payTime, String taskid) {
		super();
		this.accountDate = accountDate;
		this.accountAmount = accountAmount;
		this.summary = summary;
		this.balance = balance;
		this.payTime = payTime;
		this.taskid = taskid;
	}
	
	public HousingHeihePaydetails() {
		super();
		// TODO Auto-generated constructor stub
	}	
}
