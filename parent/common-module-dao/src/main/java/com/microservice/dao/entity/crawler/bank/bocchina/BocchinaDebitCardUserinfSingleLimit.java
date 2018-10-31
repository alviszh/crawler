/**
  * Copyright 2017 bejson.com 
  */
package com.microservice.dao.entity.crawler.bank.bocchina;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * Auto-generated: 2017-10-31 15:7:23
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */

@Entity
@Table(name = "bocchina_debitcard_singlelimit",indexes = {@Index(name = "index_bocchina_debitcard_singlelimit_taskid", columnList = "taskid")})
public class BocchinaDebitCardUserinfSingleLimit extends IdEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Override
	public String toString() {
		return "BocchinaDebitCardUserinfSingleLimit [currency=" + currency + ", supplyBalance=" + supplyBalance
				+ ", totalBalance=" + totalBalance + ", maxLoadingAmount=" + maxLoadingAmount + ", accountStatus="
				+ accountStatus + ", singleLimit=" + singleLimit + ", cashRemit=" + cashRemit + ", eCashUpperLimit="
				+ eCashUpperLimit + ", taskid=" + taskid + "]";
	}

	private String currency;//"CNY"
	private int supplyBalance;//0
	private int totalBalance;//0
	private int maxLoadingAmount;
	private String accountStatus;//"A"
	private String singleLimit;//电子现金脱机消费单笔交易限额
	private String cashRemit;
	
	private int eCashUpperLimit;//电子现金卡片余额上限
	@Column(name = "ecashupper")
	public int geteCashUpperLimit() {
		return eCashUpperLimit;
	}
	public void seteCashUpperLimit(int eCashUpperLimit) {
		this.eCashUpperLimit = eCashUpperLimit;
	}
	
	private String taskid;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCurrency() {
		return currency;
	}

	public void setSupplyBalance(int supplyBalance) {
		this.supplyBalance = supplyBalance;
	}

	public int getSupplyBalance() {
		return supplyBalance;
	}

	public void setTotalBalance(int totalBalance) {
		this.totalBalance = totalBalance;
	}

	public int getTotalBalance() {
		return totalBalance;
	}

	public void setMaxLoadingAmount(int maxLoadingAmount) {
		this.maxLoadingAmount = maxLoadingAmount;
	}

	public int getMaxLoadingAmount() {
		return maxLoadingAmount;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setSingleLimit(String singleLimit) {
		this.singleLimit = singleLimit;
	}

	public String getSingleLimit() {
		return singleLimit;
	}

	public void setCashRemit(String cashRemit) {
		this.cashRemit = cashRemit;
	}

	public String getCashRemit() {
		return cashRemit;
	}

	public void setECashUpperLimit(int eCashUpperLimit) {
		this.eCashUpperLimit = eCashUpperLimit;
	}

	public int getECashUpperLimit() {
		return eCashUpperLimit;
	}

}