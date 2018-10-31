package com.microservice.dao.entity.crawler.housing.dalian;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_dalian_pay",indexes = {@Index(name = "index_housing_dalian_pay_taskid", columnList = "taskid")})
public class HousingDaLianPay extends IdEntity implements Serializable {
	private static final long serialVersionUID = -758773159050632619L;
	private String paytime;                 //交易日期
	private String remark;                  //摘要
	private String debit;                   //借方发生额
	private String credit;                  //贷方发生额
	private String balance;                 //余额
	private String unitName;                //单位名称
	private String startDate;               //起始日期
	private String terminationDate;         //终止日期
	private String tradingState;            //交易状态
	private String handle;                  //办理方式
	
	private String taskid;

	@Override
	public String toString() {
		return "HousingDaLianPay [paytime=" + paytime + ", remark=" + remark + ", debit=" + debit
				+ ", credit=" + credit + ", balance=" + balance + ", unitName=" + unitName
				+ ", startDate=" + startDate + ", terminationDate=" + terminationDate + ", tradingState=" + tradingState
				+ ", handle=" + handle+ ", taskid=" + taskid + "]";
	}
	
	
	public String getPaytime() {
		return paytime;
	}

	public void setPaytime(String paytime) {
		this.paytime = paytime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getTerminationDate() {
		return terminationDate;
	}

	public void setTerminationDate(String terminationDate) {
		this.terminationDate = terminationDate;
	}

	public String getTradingState() {
		return tradingState;
	}

	public void setTradingState(String tradingState) {
		this.tradingState = tradingState;
	}

	public String getHandle() {
		return handle;
	}

	public void setHandle(String handle) {
		this.handle = handle;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
