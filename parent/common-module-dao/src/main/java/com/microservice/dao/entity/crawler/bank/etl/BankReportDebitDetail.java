package com.microservice.dao.entity.crawler.bank.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity

@Table(name="bank_report_debit_detail",indexes = {@Index(name = "index_bank_report_debit_detail_taskid", columnList = "taskId")})
public class BankReportDebitDetail extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String idNumber;
	private String basicUserId;	//唯一标识
	private String bankName;	//唯一标识
	private String billId;
	private String bankCard;
	private String tradeTime;
	private String payment;
	private String deposit;
	private String abstracts;
	private String balance;
	private String tradeType;
	private String tradePlace;
	private String otherAccount;
	private String otherAccountName;
	private String otherAccountBank;
	private String cardNum;
	private String category;
	private String orderIndex;
	private String currencyType;
	private String transMethod;
	private String transChannel;
	private String description;
	private String remark;
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	public String getBankCard() {
		return bankCard;
	}
	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}
	public String getTradeTime() {
		return tradeTime;
	}
	public void setTradeTime(String tradeTime) {
		this.tradeTime = tradeTime;
	}
	public String getPayment() {
		return payment;
	}
	public void setPayment(String payment) {
		this.payment = payment;
	}
	public String getDeposit() {
		return deposit;
	}
	public void setDeposit(String deposit) {
		this.deposit = deposit;
	}
	public String getAbstracts() {
		return abstracts;
	}
	public void setAbstracts(String abstracts) {
		this.abstracts = abstracts;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	public String getTradePlace() {
		return tradePlace;
	}
	public void setTradePlace(String tradePlace) {
		this.tradePlace = tradePlace;
	}
	public String getOtherAccount() {
		return otherAccount;
	}
	public void setOtherAccount(String otherAccount) {
		this.otherAccount = otherAccount;
	}
	public String getOtherAccountName() {
		return otherAccountName;
	}
	public void setOtherAccountName(String otherAccountName) {
		this.otherAccountName = otherAccountName;
	}
	public String getOtherAccountBank() {
		return otherAccountBank;
	}
	public void setOtherAccountBank(String otherAccountBank) {
		this.otherAccountBank = otherAccountBank;
	}
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getOrderIndex() {
		return orderIndex;
	}
	public void setOrderIndex(String orderIndex) {
		this.orderIndex = orderIndex;
	}
	public String getCurrencyType() {
		return currencyType;
	}
	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}
	public String getTransMethod() {
		return transMethod;
	}
	public void setTransMethod(String transMethod) {
		this.transMethod = transMethod;
	}
	public String getTransChannel() {
		return transChannel;
	}
	public void setTransChannel(String transChannel) {
		this.transChannel = transChannel;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	public String getBasicUserId() {
		return basicUserId;
	}
	public void setBasicUserId(String basicUserId) {
		this.basicUserId = basicUserId;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	@Override
	public String toString() {
		return "BankReportDebitDetail [taskId=" + taskId + ", idNumber=" + idNumber + ", basicUserId=" + basicUserId
				+ ", bankName=" + bankName + ", billId=" + billId + ", bankCard=" + bankCard + ", tradeTime="
				+ tradeTime + ", payment=" + payment + ", deposit=" + deposit + ", abstracts=" + abstracts
				+ ", balance=" + balance + ", tradeType=" + tradeType + ", tradePlace=" + tradePlace + ", otherAccount="
				+ otherAccount + ", otherAccountName=" + otherAccountName + ", otherAccountBank=" + otherAccountBank
				+ ", cardNum=" + cardNum + ", category=" + category + ", orderIndex=" + orderIndex + ", currencyType="
				+ currencyType + ", transMethod=" + transMethod + ", transChannel=" + transChannel + ", description="
				+ description + ", remark=" + remark + "]";
	}
	
}
