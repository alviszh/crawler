package com.microservice.dao.entity.crawler.bank.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="pro_bank_credit_user_info",indexes = {@Index(name = "index_pro_bank_credit_user_info_taskid", columnList = "taskId")})
public class ProBankCreditUserInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String resource;
	private String userName;
	private String cardNumber;
	private String lastNumber;
	private String creditLimitUsd;
	private String cashLimitUsd;
	private String creditLimit;
	private String cashLimit;
	private String balance;
	private String points;
	private String address;
	private String billDate;
	private String payDate;
	private String openDate;
	private String dueDate;
	private String basicUserName;
	private String basicUserIdnum;
	private String email;
	private String phonenum;
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getLastNumber() {
		return lastNumber;
	}
	public void setLastNumber(String lastNumber) {
		this.lastNumber = lastNumber;
	}
	public String getCreditLimitUsd() {
		return creditLimitUsd;
	}
	public void setCreditLimitUsd(String creditLimitUsd) {
		this.creditLimitUsd = creditLimitUsd;
	}
	public String getCashLimitUsd() {
		return cashLimitUsd;
	}
	public void setCashLimitUsd(String cashLimitUsd) {
		this.cashLimitUsd = cashLimitUsd;
	}
	public String getCreditLimit() {
		return creditLimit;
	}
	public void setCreditLimit(String creditLimit) {
		this.creditLimit = creditLimit;
	}
	public String getCashLimit() {
		return cashLimit;
	}
	public void setCashLimit(String cashLimit) {
		this.cashLimit = cashLimit;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getPoints() {
		return points;
	}
	public void setPoints(String points) {
		this.points = points;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	public String getOpenDate() {
		return openDate;
	}
	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public String getBasicUserName() {
		return basicUserName;
	}
	public void setBasicUserName(String basicUserName) {
		this.basicUserName = basicUserName;
	}
	public String getBasicUserIdnum() {
		return basicUserIdnum;
	}
	public void setBasicUserIdnum(String basicUserIdnum) {
		this.basicUserIdnum = basicUserIdnum;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhonenum() {
		return phonenum;
	}
	public void setPhonenum(String phonenum) {
		this.phonenum = phonenum;
	}
	@Override
	public String toString() {
		return "ProBankCreditUserInfo [taskId=" + taskId + ", resource=" + resource + ", userName=" + userName
				+ ", cardNumber=" + cardNumber + ", lastNumber=" + lastNumber + ", creditLimitUsd=" + creditLimitUsd
				+ ", cashLimitUsd=" + cashLimitUsd + ", creditLimit=" + creditLimit + ", cashLimit=" + cashLimit
				+ ", balance=" + balance + ", points=" + points + ", address=" + address + ", billDate=" + billDate
				+ ", payDate=" + payDate + ", openDate=" + openDate + ", dueDate=" + dueDate + ", basicUserName="
				+ basicUserName + ", basicUserIdnum=" + basicUserIdnum + ", email=" + email + ", phonenum=" + phonenum
				+ "]";
	}
		
}
