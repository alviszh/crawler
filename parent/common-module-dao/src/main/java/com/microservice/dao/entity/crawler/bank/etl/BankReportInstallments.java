package com.microservice.dao.entity.crawler.bank.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity

@Table(name="bank_report_installments",indexes = {@Index(name = "index_bank_report_installments_taskid", columnList = "taskId")})
public class BankReportInstallments extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String idNumber;
	private String basicUserId;
	private String bankName;
	private String installmentDesc;
	private String handingfeeDesc;
	private String transferfeeDesc;
	private String installmentType;
	private String totalMonth;
	private String currentMonth;
	private String currencyType;
	private String amountMoney;
	private String handingFee;
	private String transferFee;
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
	public String getInstallmentDesc() {
		return installmentDesc;
	}
	public void setInstallmentDesc(String installmentDesc) {
		this.installmentDesc = installmentDesc;
	}
	public String getHandingfeeDesc() {
		return handingfeeDesc;
	}
	public void setHandingfeeDesc(String handingfeeDesc) {
		this.handingfeeDesc = handingfeeDesc;
	}
	public String getTransferfeeDesc() {
		return transferfeeDesc;
	}
	public void setTransferfeeDesc(String transferfeeDesc) {
		this.transferfeeDesc = transferfeeDesc;
	}
	public String getInstallmentType() {
		return installmentType;
	}
	public void setInstallmentType(String installmentType) {
		this.installmentType = installmentType;
	}
	public String getCurrentMonth() {
		return currentMonth;
	}
	public void setCurrentMonth(String currentMonth) {
		this.currentMonth = currentMonth;
	}
	public String getCurrencyType() {
		return currencyType;
	}
	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}
	public String getAmountMoney() {
		return amountMoney;
	}
	public void setAmountMoney(String amountMoney) {
		this.amountMoney = amountMoney;
	}
	public String getHandingFee() {
		return handingFee;
	}
	public void setHandingFee(String handingFee) {
		this.handingFee = handingFee;
	}
	public String getTransferFee() {
		return transferFee;
	}
	public void setTransferFee(String transferFee) {
		this.transferFee = transferFee;
	}
	public String getTotalMonth() {
		return totalMonth;
	}
	public void setTotalMonth(String totalMonth) {
		this.totalMonth = totalMonth;
	}
	
	@Override
	public String toString() {
		return "BankReportInstallments [taskId=" + taskId + ", idNumber=" + idNumber + ", basicUserId=" + basicUserId
				+ ", bankName=" + bankName + ", installmentDesc=" + installmentDesc + ", handingfeeDesc="
				+ handingfeeDesc + ", transferfeeDesc=" + transferfeeDesc + ", installmentType=" + installmentType
				+ ", totalMonth=" + totalMonth + ", currentMonth=" + currentMonth + ", currencyType=" + currencyType
				+ ", amountMoney=" + amountMoney + ", handingFee=" + handingFee + ", transferFee=" + transferFee + "]";
	}
	
}
