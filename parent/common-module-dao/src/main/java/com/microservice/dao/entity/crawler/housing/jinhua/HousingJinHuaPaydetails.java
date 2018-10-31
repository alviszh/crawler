package com.microservice.dao.entity.crawler.housing.jinhua;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 金华公积金缴存信息
 */
@Entity
@Table(name="housing_jinhua_paydetails")
public class HousingJinHuaPaydetails extends IdEntity implements Serializable {
	private static final long serialVersionUID = -758773159050632619L;

	private String dealDate;//记账日期
	private String type;//业务类型
	private String dealAmount;//发生额

	private String balance;//账户余额
	private String summary;//摘要
	private String userAccount;// 公积金账号
	private String taskid;	
	
	
	public String getDealDate() {
		return dealDate;
	}
	public void setDealDate(String dealDate) {
		this.dealDate = dealDate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDealAmount() {
		return dealAmount;
	}
	public void setDealAmount(String dealAmount) {
		this.dealAmount = dealAmount;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	public String getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	
	@Override
	public String toString() {
		return "HousingJinHuaPaydetails [dealDate=" + dealDate + ", type=" + type + ", dealAmount=" + dealAmount
				+ ", balance=" + balance + ", summary=" + summary + ", userAccount=" + userAccount + ", taskid="
				+ taskid + "]";
	}

	public HousingJinHuaPaydetails(String dealDate, String type, String dealAmount, String balance, String summary,
			String userAccount, String taskid) {
		super();
		this.dealDate = dealDate;
		this.type = type;
		this.dealAmount = dealAmount;
		this.balance = balance;
		this.summary = summary;
		this.userAccount = userAccount;
		this.taskid = taskid;
	}
	public HousingJinHuaPaydetails() {
		super();
		// TODO Auto-generated constructor stub
	}	
}
