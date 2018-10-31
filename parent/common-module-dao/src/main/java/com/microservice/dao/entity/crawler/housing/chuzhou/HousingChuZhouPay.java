package com.microservice.dao.entity.crawler.housing.chuzhou;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_chuzhou_pay",indexes = {@Index(name = "index_housing_chuzhou_pay_taskid", columnList = "taskid")})
public class HousingChuZhouPay extends IdEntity implements Serializable{
	private String remark;                  //摘要
	private String paytime;                 //记帐日期
	private String debit;                   //借方金额
	private String credit;                  //贷方金额
	private String balance;                 //余额
	
	private String taskid;

	@Override
	public String toString() {
		return "HousingDaLianPay [paytime=" + paytime + ", remark=" + remark + ", debit=" + debit
				+ ", credit=" + credit + ", balance=" + balance+ ", taskid=" + taskid + "]";
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPaytime() {
		return paytime;
	}

	public void setPaytime(String paytime) {
		this.paytime = paytime;
	}

	public String getDebit() {
		return debit;
	}

	public void setDebit(String debit) {
		this.debit = debit;
	}

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
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
	
	
}
