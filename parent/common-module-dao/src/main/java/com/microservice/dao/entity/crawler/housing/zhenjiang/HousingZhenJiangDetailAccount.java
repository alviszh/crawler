package com.microservice.dao.entity.crawler.housing.zhenjiang;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 镇江市公积金明细账信息
 * @author: sln
 * @date:
 */
@Entity
@Table(name = "housing_zhenjiang_detailaccount")
public class HousingZhenJiangDetailAccount extends IdEntity implements Serializable {
	private static final long serialVersionUID = 6481831903414445397L;
	private String taskid;
//	入账日期
	private String reachdate;
//	汇缴年月
	private String chargeyearmonth;
//	摘要
	private String  summary;    
//	收入金额
	private String receivemoney;
//	支出金额
	private String  amount;
//	余额
	private String balance;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getReachdate() {
		return reachdate;
	}
	public void setReachdate(String reachdate) {
		this.reachdate = reachdate;
	}
	public String getChargeyearmonth() {
		return chargeyearmonth;
	}
	public void setChargeyearmonth(String chargeyearmonth) {
		this.chargeyearmonth = chargeyearmonth;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getReceivemoney() {
		return receivemoney;
	}
	public void setReceivemoney(String receivemoney) {
		this.receivemoney = receivemoney;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
}
