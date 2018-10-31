package com.microservice.dao.entity.crawler.housing.datong;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 大同公积金缴存信息
 */
@Entity
@Table(name="housing_datong_paydetails")
public class HousingDaTongPaydetails extends IdEntity implements Serializable {
	private static final long serialVersionUID = -758773159050632619L;

	private String paydate;// 缴存年月
	private String accountdate;// 入账时间
	private String companyName;// 单位名称
	private String increaseAmount;// 收入
	private String reduceAmount;// 支出
	private String balance;// 当前余额
	private String type;// 业务类型
	private String taskid;
	public String getPaydate() {
		return paydate;
	}

	public void setPaydate(String paydate) {
		this.paydate = paydate;
	}

	public String getAccountdate() {
		return accountdate;
	}

	public void setAccountdate(String accountdate) {
		this.accountdate = accountdate;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getIncreaseAmount() {
		return increaseAmount;
	}

	public void setIncreaseAmount(String increaseAmount) {
		this.increaseAmount = increaseAmount;
	}

	public String getReduceAmount() {
		return reduceAmount;
	}

	public void setReduceAmount(String reduceAmount) {
		this.reduceAmount = reduceAmount;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Override
	public String toString() {
		return "HousingDaTongPaydetails [paydate=" + paydate + ", accountdate=" + accountdate + ", companyName="
				+ companyName + ", increaseAmount=" + increaseAmount + ", reduceAmount=" + reduceAmount + ", balance="
				+ balance + ", type=" + type + ", taskid=" + taskid + "]";
	}
  
	public HousingDaTongPaydetails(String paydate, String accountdate, String companyName, String increaseAmount,
			String reduceAmount, String balance, String type, String taskid) {
		super();
		this.paydate = paydate;
		this.accountdate = accountdate;
		this.companyName = companyName;
		this.increaseAmount = increaseAmount;
		this.reduceAmount = reduceAmount;
		this.balance = balance;
		this.type = type;
		this.taskid = taskid;
	}

	public HousingDaTongPaydetails() {
		super();
		// TODO Auto-generated constructor stub
	}
}
