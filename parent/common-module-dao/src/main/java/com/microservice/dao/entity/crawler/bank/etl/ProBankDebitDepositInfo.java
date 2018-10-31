package com.microservice.dao.entity.crawler.bank.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity

@Table(name="pro_bank_debit_deposit_info",indexes = {@Index(name = "index_pro_bank_debit_deposit_info_taskid", columnList = "taskId")})

public class ProBankDebitDepositInfo extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String resource;
	private String cardNum;
	private String depositType;
	private String currency;
	private String interestBegindate;
	private String interestEnddate;
	private String storgePeriod;
	private String interestRate;
	private String balance;
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public String getDepositType() {
		return depositType;
	}
	public void setDepositType(String depositType) {
		this.depositType = depositType;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getInterestBegindate() {
		return interestBegindate;
	}
	public void setInterestBegindate(String interestBegindate) {
		this.interestBegindate = interestBegindate;
	}
	public String getInterestEnddate() {
		return interestEnddate;
	}
	public void setInterestEnddate(String interestEnddate) {
		this.interestEnddate = interestEnddate;
	}
	public String getStorgePeriod() {
		return storgePeriod;
	}
	public void setStorgePeriod(String storgePeriod) {
		this.storgePeriod = storgePeriod;
	}
	public String getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(String interestRate) {
		this.interestRate = interestRate;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	@Override
	public String toString() {
		return "ProBankDebitDepositInfo [taskId=" + taskId + ", resource=" + resource + ", cardNum=" + cardNum
				+ ", depositType=" + depositType + ", currency=" + currency + ", interestBegindate=" + interestBegindate
				+ ", interestEnddate=" + interestEnddate + ", storgePeriod=" + storgePeriod + ", interestRate="
				+ interestRate + ", balance=" + balance + "]";
	}
		
}
