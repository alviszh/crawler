/**
  * Copyright 2017 bejson.com 
  */
package com.microservice.dao.entity.crawler.bank.bocchina;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * Auto-generated: 2017-11-29 15:34:20
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Entity
@Table(name = "bocchina_cebitcard_userbill",indexes = {@Index(name = "index_bocchina_cebitcard_userbill_taskid", columnList = "taskid")})
public class BocchinaCebitCardUserBillActList extends IdEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String currentBalance;//取现可用额
	private String currency;// 001 代表人民币 014 代表美元
	private String realTimeBalance;//实时余额
	private String billAmout;//本期账单应还金额
	private String haveNotRepayAmout;//账单未还金额
	private String billLimitAmout;//本期账单最低还款额
	private String dividedPayLimit;//总可用额
	private String dividedPayAvaiBalance;//分期可用额
	private String cashLimit;//现金限额
	private String cashBalance;//现金余额
	private String totalLimt;//分期可用额限额
	private String toltalBalance;//分期可用余额
	private String rtBalanceFlag;//"0" 未知
	private String currentBalanceFlag;//"2" 未知
	
	private String taskid;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public void setCurrentBalance(String currentBalance) {
		this.currentBalance = currentBalance;
	}

	public String getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCurrency() {
		return currency;
	}

	public void setRealTimeBalance(String realTimeBalance) {
		this.realTimeBalance = realTimeBalance;
	}

	public String getRealTimeBalance() {
		return realTimeBalance;
	}

	public void setBillAmout(String billAmout) {
		this.billAmout = billAmout;
	}

	public String getBillAmout() {
		return billAmout;
	}

	public void setHaveNotRepayAmout(String haveNotRepayAmout) {
		this.haveNotRepayAmout = haveNotRepayAmout;
	}

	public String getHaveNotRepayAmout() {
		return haveNotRepayAmout;
	}

	public void setBillLimitAmout(String billLimitAmout) {
		this.billLimitAmout = billLimitAmout;
	}

	public String getBillLimitAmout() {
		return billLimitAmout;
	}

	public void setDividedPayLimit(String dividedPayLimit) {
		this.dividedPayLimit = dividedPayLimit;
	}

	public String getDividedPayLimit() {
		return dividedPayLimit;
	}

	public void setDividedPayAvaiBalance(String dividedPayAvaiBalance) {
		this.dividedPayAvaiBalance = dividedPayAvaiBalance;
	}

	public String getDividedPayAvaiBalance() {
		return dividedPayAvaiBalance;
	}

	public void setCashLimit(String cashLimit) {
		this.cashLimit = cashLimit;
	}

	public String getCashLimit() {
		return cashLimit;
	}

	public void setCashBalance(String cashBalance) {
		this.cashBalance = cashBalance;
	}

	public String getCashBalance() {
		return cashBalance;
	}

	public void setTotalLimt(String totalLimt) {
		this.totalLimt = totalLimt;
	}

	public String getTotalLimt() {
		return totalLimt;
	}

	public void setToltalBalance(String toltalBalance) {
		this.toltalBalance = toltalBalance;
	}

	public String getToltalBalance() {
		return toltalBalance;
	}

	public void setRtBalanceFlag(String rtBalanceFlag) {
		this.rtBalanceFlag = rtBalanceFlag;
	}

	public String getRtBalanceFlag() {
		return rtBalanceFlag;
	}

	public void setCurrentBalanceFlag(String currentBalanceFlag) {
		this.currentBalanceFlag = currentBalanceFlag;
	}

	public String getCurrentBalanceFlag() {
		return currentBalanceFlag;
	}

	@Override
	public String toString() {
		return "AccountBillActList [currentBalance=" + currentBalance + ", currency=" + currency + ", realTimeBalance="
				+ realTimeBalance + ", billAmout=" + billAmout + ", haveNotRepayAmout=" + haveNotRepayAmout
				+ ", billLimitAmout=" + billLimitAmout + ", dividedPayLimit=" + dividedPayLimit
				+ ", dividedPayAvaiBalance=" + dividedPayAvaiBalance + ", cashLimit=" + cashLimit + ", cashBalance="
				+ cashBalance + ", totalLimt=" + totalLimt + ", toltalBalance=" + toltalBalance + ", rtBalanceFlag="
				+ rtBalanceFlag + ", currentBalanceFlag=" + currentBalanceFlag + "]";
	}

}