package com.microservice.dao.entity.crawler.bank.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="bank_report_credit_account_summary",indexes = {@Index(name = "index_bank_report_credit_account_summary_taskid", columnList = "taskId")})
public class BankReportCreditAccountSummary extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String idNumber;
	private String basicUserId;	//唯一标识
	private String bankName;	//唯一标识
	private String creditcardLimit;
	private String totalCanUseConsumeLimit1;
	private String creditcardBalance;
	private String creditcardCashLimit;
	private String creditcardCashBalance;
	private String consumeCreditLimit;
	private String singleBankMaxLimit;
	private String singleBankMinLimit;
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
	public String getCreditcardLimit() {
		return creditcardLimit;
	}
	public void setCreditcardLimit(String creditcardLimit) {
		this.creditcardLimit = creditcardLimit;
	}
	public String getTotalCanUseConsumeLimit1() {
		return totalCanUseConsumeLimit1;
	}
	public void setTotalCanUseConsumeLimit1(String totalCanUseConsumeLimit1) {
		this.totalCanUseConsumeLimit1 = totalCanUseConsumeLimit1;
	}
	public String getCreditcardBalance() {
		return creditcardBalance;
	}
	public void setCreditcardBalance(String creditcardBalance) {
		this.creditcardBalance = creditcardBalance;
	}
	public String getCreditcardCashLimit() {
		return creditcardCashLimit;
	}
	public void setCreditcardCashLimit(String creditcardCashLimit) {
		this.creditcardCashLimit = creditcardCashLimit;
	}
	public String getCreditcardCashBalance() {
		return creditcardCashBalance;
	}
	public void setCreditcardCashBalance(String creditcardCashBalance) {
		this.creditcardCashBalance = creditcardCashBalance;
	}
	public String getConsumeCreditLimit() {
		return consumeCreditLimit;
	}
	public void setConsumeCreditLimit(String consumeCreditLimit) {
		this.consumeCreditLimit = consumeCreditLimit;
	}
	public String getSingleBankMaxLimit() {
		return singleBankMaxLimit;
	}
	public void setSingleBankMaxLimit(String singleBankMaxLimit) {
		this.singleBankMaxLimit = singleBankMaxLimit;
	}
	public String getSingleBankMinLimit() {
		return singleBankMinLimit;
	}
	public void setSingleBankMinLimit(String singleBankMinLimit) {
		this.singleBankMinLimit = singleBankMinLimit;
	}
	@Override
	public String toString() {
		return "BankReportCreditAccountSummary [taskId=" + taskId + ", idNumber=" + idNumber + ", basicUserId="
				+ basicUserId + ", bankName=" + bankName + ", creditcardLimit=" + creditcardLimit
				+ ", totalCanUseConsumeLimit1=" + totalCanUseConsumeLimit1 + ", creditcardBalance=" + creditcardBalance
				+ ", creditcardCashLimit=" + creditcardCashLimit + ", creditcardCashBalance=" + creditcardCashBalance
				+ ", consumeCreditLimit=" + consumeCreditLimit + ", singleBankMaxLimit=" + singleBankMaxLimit
				+ ", singleBankMinLimit=" + singleBankMinLimit + "]";
	}
	
	
}
