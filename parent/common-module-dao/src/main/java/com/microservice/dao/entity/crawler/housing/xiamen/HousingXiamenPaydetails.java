package com.microservice.dao.entity.crawler.housing.xiamen;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 厦门公积金用户信息
 */
@Entity
@Table(name="housing_xiamen_paydetails")
public class HousingXiamenPaydetails extends IdEntity implements Serializable {
	private static final long serialVersionUID = -758773159050632619L;
	private String bankdate;// 记账日期
	private String banksumy;// 摘要
	private String fixamount;// 支取金额
	private String saveamount;// 缴存金额
	private String balance;// 余额
	private String taskid;
	public String getBankdate() {
		return bankdate;
	}

	public void setBankdate(String bankdate) {
		this.bankdate = bankdate;
	}

	public String getBanksumy() {
		return banksumy;
	}

	public void setBanksumy(String banksumy) {
		this.banksumy = banksumy;
	}

	public String getFixamount() {
		return fixamount;
	}

	public void setFixamount(String fixamount) {
		this.fixamount = fixamount;
	}

	public String getSaveamount() {
		return saveamount;
	}

	public void setSaveamount(String saveamount) {
		this.saveamount = saveamount;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
   @Override
	public String toString() {
		return "HousingXiamenPaydetails [bankdate=" + bankdate + ", banksumy=" + banksumy + ", fixamount=" + fixamount
				+ ", saveamount=" + saveamount + ", balance=" + balance + ", taskid=" + taskid + "]";
	}
	public HousingXiamenPaydetails(String bankdate, String banksumy, String fixamount, String saveamount, String balance,
		String taskid) {
	super();
	this.bankdate = bankdate;
	this.banksumy = banksumy;
	this.fixamount = fixamount;
	this.saveamount = saveamount;
	this.balance = balance;
	this.taskid = taskid;
}

	public HousingXiamenPaydetails() {
		super();
		// TODO Auto-generated constructor stub
	}
}
