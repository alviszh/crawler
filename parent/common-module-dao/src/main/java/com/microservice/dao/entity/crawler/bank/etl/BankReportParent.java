package com.microservice.dao.entity.crawler.bank.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity

@Table(name="bank_report_parent",indexes = {@Index(name = "index_bank_report_parent_taskid", columnList = "taskId")})

public class BankReportParent extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String idNumber;
	private String basicUserId;
	private String bankName;
	private String bankCard;
	private String userName;
	private String balance;
	private String creditLimit;
	private String usdCreditLimit;
	private String cashBalance;
	private String cashLimit;
	private String usdCashLimit;
	private String openBank;
	private String openTime;
	private String cardType;
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankCard() {
		return bankCard;
	}
	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getCreditLimit() {
		return creditLimit;
	}
	public void setCreditLimit(String creditLimit) {
		this.creditLimit = creditLimit;
	}
	public String getUsdCreditLimit() {
		return usdCreditLimit;
	}
	public void setUsdCreditLimit(String usdCreditLimit) {
		this.usdCreditLimit = usdCreditLimit;
	}
	public String getCashBalance() {
		return cashBalance;
	}
	public void setCashBalance(String cashBalance) {
		this.cashBalance = cashBalance;
	}
	public String getCashLimit() {
		return cashLimit;
	}
	public void setCashLimit(String cashLimit) {
		this.cashLimit = cashLimit;
	}
	public String getUsdCashLimit() {
		return usdCashLimit;
	}
	public void setUsdCashLimit(String usdCashLimit) {
		this.usdCashLimit = usdCashLimit;
	}
	public String getOpenBank() {
		return openBank;
	}
	public void setOpenBank(String openBank) {
		this.openBank = openBank;
	}
	public String getOpenTime() {
		return openTime;
	}
	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getBasicUserId() {
		return basicUserId;
	}
	public void setBasicUserId(String basicUserId) {
		this.basicUserId = basicUserId;
	}
	@Override
	public String toString() {
		return "BankReportParent [taskId=" + taskId + ", idNumber=" + idNumber + ", basicUserId=" + basicUserId
				+ ", bankName=" + bankName + ", bankCard=" + bankCard + ", userName=" + userName + ", balance="
				+ balance + ", creditLimit=" + creditLimit + ", usdCreditLimit=" + usdCreditLimit + ", cashBalance="
				+ cashBalance + ", cashLimit=" + cashLimit + ", usdCashLimit=" + usdCashLimit + ", openBank=" + openBank
				+ ", openTime=" + openTime + ", cardType=" + cardType + "]";
	}	
}
