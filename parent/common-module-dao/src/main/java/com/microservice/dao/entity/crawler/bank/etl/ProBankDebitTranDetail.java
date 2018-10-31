package com.microservice.dao.entity.crawler.bank.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity

@Table(name="pro_bank_debit_tran_detail",indexes = {@Index(name = "index_pro_bank_debit_tran_detail_taskid", columnList = "taskId")})
public class ProBankDebitTranDetail extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String resource;
	private String accountNum;
	private String money;
	private String tranDate;
	private String oppositeAccount;
	private String tranDescription;
	private String balance;
	private String tranType;
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
	public String getAccountNum() {
		return accountNum;
	}
	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getTranDate() {
		return tranDate;
	}
	public void setTranDate(String tranDate) {
		this.tranDate = tranDate;
	}
	public String getOppositeAccount() {
		return oppositeAccount;
	}
	public void setOppositeAccount(String oppositeAccount) {
		this.oppositeAccount = oppositeAccount;
	}
	public String getTranDescription() {
		return tranDescription;
	}
	public void setTranDescription(String tranDescription) {
		this.tranDescription = tranDescription;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getTranType() {
		return tranType;
	}
	public void setTranType(String tranType) {
		this.tranType = tranType;
	}
	@Override
	public String toString() {
		return "ProBankDebitTranDetail [taskId=" + taskId + ", resource=" + resource + ", accountNum=" + accountNum
				+ ", money=" + money + ", tranDate=" + tranDate + ", oppositeAccount=" + oppositeAccount
				+ ", tranDescription=" + tranDescription + ", balance=" + balance + ", tranType=" + tranType + "]";
	}
	
}
