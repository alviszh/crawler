package com.microservice.dao.entity.crawler.bank.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity

@Table(name="bank_report_credit_bills",indexes = {@Index(name = "index_bank_report_credit_bills_taskid", columnList = "taskId")})
public class BankReportCreditBills extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String idNumber;	//唯一标识
	private String basicUserId;	//唯一标识
	private String bankCard;
	private String billId;
	private String billType;
	private String bankName;
	private String billMonth;
	private String billDate;
	private String paymentDueDate;
	private String newBalance;
	private String usdNewBalance;
	private String minPayment;
	private String usdMinPayment;
	private String lastBalance;
	private String usdLastBalance;
	private String lastPayment;
	private String usdLastPayment;
	private String newCharges;
	private String usdNewCharges;
	private String interest;
	private String usdInterest;
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getBankCard() {
		return bankCard;
	}
	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	public String getBillType() {
		return billType;
	}
	public void setBillType(String billType) {
		this.billType = billType;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBillMonth() {
		return billMonth;
	}
	public void setBillMonth(String billMonth) {
		this.billMonth = billMonth;
	}
	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	public String getPaymentDueDate() {
		return paymentDueDate;
	}
	public void setPaymentDueDate(String paymentDueDate) {
		this.paymentDueDate = paymentDueDate;
	}
	public String getNewBalance() {
		return newBalance;
	}
	public void setNewBalance(String newBalance) {
		this.newBalance = newBalance;
	}
	public String getUsdNewBalance() {
		return usdNewBalance;
	}
	public void setUsdNewBalance(String usdNewBalance) {
		this.usdNewBalance = usdNewBalance;
	}
	public String getMinPayment() {
		return minPayment;
	}
	public void setMinPayment(String minPayment) {
		this.minPayment = minPayment;
	}
	public String getUsdMinPayment() {
		return usdMinPayment;
	}
	public void setUsdMinPayment(String usdMinPayment) {
		this.usdMinPayment = usdMinPayment;
	}
	public String getLastBalance() {
		return lastBalance;
	}
	public void setLastBalance(String lastBalance) {
		this.lastBalance = lastBalance;
	}
	public String getUsdLastBalance() {
		return usdLastBalance;
	}
	public void setUsdLastBalance(String usdLastBalance) {
		this.usdLastBalance = usdLastBalance;
	}
	public String getLastPayment() {
		return lastPayment;
	}
	public void setLastPayment(String lastPayment) {
		this.lastPayment = lastPayment;
	}
	public String getUsdLastPayment() {
		return usdLastPayment;
	}
	public void setUsdLastPayment(String usdLastPayment) {
		this.usdLastPayment = usdLastPayment;
	}
	public String getNewCharges() {
		return newCharges;
	}
	public void setNewCharges(String newCharges) {
		this.newCharges = newCharges;
	}
	public String getUsdNewCharges() {
		return usdNewCharges;
	}
	public void setUsdNewCharges(String usdNewCharges) {
		this.usdNewCharges = usdNewCharges;
	}
	public String getInterest() {
		return interest;
	}
	public void setInterest(String interest) {
		this.interest = interest;
	}
	public String getUsdInterest() {
		return usdInterest;
	}
	public void setUsdInterest(String usdInterest) {
		this.usdInterest = usdInterest;
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
	@Override
	public String toString() {
		return "BankReportCreditBills [taskId=" + taskId + ", idNumber=" + idNumber + ", basicUserId=" + basicUserId
				+ ", bankCard=" + bankCard + ", billId=" + billId + ", billType=" + billType + ", bankName=" + bankName
				+ ", billMonth=" + billMonth + ", billDate=" + billDate + ", paymentDueDate=" + paymentDueDate
				+ ", newBalance=" + newBalance + ", usdNewBalance=" + usdNewBalance + ", minPayment=" + minPayment
				+ ", usdMinPayment=" + usdMinPayment + ", lastBalance=" + lastBalance + ", usdLastBalance="
				+ usdLastBalance + ", lastPayment=" + lastPayment + ", usdLastPayment=" + usdLastPayment
				+ ", newCharges=" + newCharges + ", usdNewCharges=" + usdNewCharges + ", interest=" + interest
				+ ", usdInterest=" + usdInterest + "]";
	}	
}
