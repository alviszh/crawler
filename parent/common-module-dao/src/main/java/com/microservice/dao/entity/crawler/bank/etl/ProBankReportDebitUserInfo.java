package com.microservice.dao.entity.crawler.bank.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="pro_bank_report_debit_user_info",indexes = {@Index(name = "index_pro_bank_report_debit_user_info_taskid", columnList = "taskId")})

public class ProBankReportDebitUserInfo extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String userName;
	private String idNum;
	private String lastNum;
	private String cardNum;
	private String openDate;
	private String openBank;
	private String telNum;
	private String industry;
	private String email;
	private String balance;
	private String address;
	private String zipCode;
	private String basicIdNum;
	private String basicUserName;
	private String bankName;
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getIdNum() {
		return idNum;
	}
	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}
	public String getLastNum() {
		return lastNum;
	}
	public void setLastNum(String lastNum) {
		this.lastNum = lastNum;
	}
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public String getOpenDate() {
		return openDate;
	}
	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}
	public String getOpenBank() {
		return openBank;
	}
	public void setOpenBank(String openBank) {
		this.openBank = openBank;
	}
	public String getTelNum() {
		return telNum;
	}
	public void setTelNum(String telNum) {
		this.telNum = telNum;
	}
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getBasicIdNum() {
		return basicIdNum;
	}
	public void setBasicIdNum(String basicIdNum) {
		this.basicIdNum = basicIdNum;
	}
	public String getBasicUserName() {
		return basicUserName;
	}
	public void setBasicUserName(String basicUserName) {
		this.basicUserName = basicUserName;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	@Override
	public String toString() {
		return "ProBankReportDebitUserInfo [taskId=" + taskId + ", userName=" + userName + ", idNum=" + idNum
				+ ", lastNum=" + lastNum + ", cardNum=" + cardNum + ", openDate=" + openDate + ", openBank=" + openBank
				+ ", telNum=" + telNum + ", industry=" + industry + ", email=" + email + ", balance=" + balance
				+ ", address=" + address + ", zipCode=" + zipCode + ", basicIdNum=" + basicIdNum + ", basicUserName="
				+ basicUserName + ", bankName=" + bankName + "]";
	}
	
	
}
