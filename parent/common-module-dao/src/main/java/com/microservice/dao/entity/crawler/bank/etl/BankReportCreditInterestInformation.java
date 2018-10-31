package com.microservice.dao.entity.crawler.bank.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="bank_report_credit_interest_information",indexes = {@Index(name = "index_bank_report_credit_interest_information_taskid", columnList = "taskId")})
public class BankReportCreditInterestInformation extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String idNumber;
	private String basicUserId;	//唯一标识
	private String bankName;	//唯一标识
	private String delayInterest1;
	private String overdueAmount1;
	private String overduePay1;
	private String otherFee1;
	private String incomeAmt1;
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
	public String getDelayInterest1() {
		return delayInterest1;
	}
	public void setDelayInterest1(String delayInterest1) {
		this.delayInterest1 = delayInterest1;
	}
	public String getOverdueAmount1() {
		return overdueAmount1;
	}
	public void setOverdueAmount1(String overdueAmount1) {
		this.overdueAmount1 = overdueAmount1;
	}
	public String getOverduePay1() {
		return overduePay1;
	}
	public void setOverduePay1(String overduePay1) {
		this.overduePay1 = overduePay1;
	}
	public String getOtherFee1() {
		return otherFee1;
	}
	public void setOtherFee1(String otherFee1) {
		this.otherFee1 = otherFee1;
	}
	public String getIncomeAmt1() {
		return incomeAmt1;
	}
	public void setIncomeAmt1(String incomeAmt1) {
		this.incomeAmt1 = incomeAmt1;
	}
	@Override
	public String toString() {
		return "BankReportCreditInterestInformation [taskId=" + taskId + ", idNumber=" + idNumber + ", basicUserId="
				+ basicUserId + ", bankName=" + bankName + ", delayInterest1=" + delayInterest1 + ", overdueAmount1="
				+ overdueAmount1 + ", overduePay1=" + overduePay1 + ", otherFee1=" + otherFee1 + ", incomeAmt1="
				+ incomeAmt1 + "]";
	}
	
	

}
