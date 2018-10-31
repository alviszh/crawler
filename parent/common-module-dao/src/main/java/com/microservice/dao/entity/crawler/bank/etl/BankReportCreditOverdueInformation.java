package com.microservice.dao.entity.crawler.bank.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="bank_report_credit_overdue_information",indexes = {@Index(name = "index_bank_report_credit_overdue_information_taskid", columnList = "taskId")})

public class BankReportCreditOverdueInformation extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = 5133365472766002696L;

	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String idNumber;
	private String basicUserId;	//唯一标识
	private String bankName;	//唯一标识
	private String delayTag1;
	private String delayStatus1;
	private String delayAmount1;
	private String delayAmountPer1;
	private String delayBillNum1;
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
	public String getDelayTag1() {
		return delayTag1;
	}
	public void setDelayTag1(String delayTag1) {
		this.delayTag1 = delayTag1;
	}
	public String getDelayStatus1() {
		return delayStatus1;
	}
	public void setDelayStatus1(String delayStatus1) {
		this.delayStatus1 = delayStatus1;
	}
	public String getDelayAmount1() {
		return delayAmount1;
	}
	public void setDelayAmount1(String delayAmount1) {
		this.delayAmount1 = delayAmount1;
	}
	public String getDelayAmountPer1() {
		return delayAmountPer1;
	}
	public void setDelayAmountPer1(String delayAmountPer1) {
		this.delayAmountPer1 = delayAmountPer1;
	}
	public String getDelayBillNum1() {
		return delayBillNum1;
	}
	public void setDelayBillNum1(String delayBillNum1) {
		this.delayBillNum1 = delayBillNum1;
	}
	@Override
	public String toString() {
		return "BankReportCreditOverdueInformation [taskId=" + taskId + ", idNumber=" + idNumber + ", basicUserId="
				+ basicUserId + ", bankName=" + bankName + ", delayTag1=" + delayTag1 + ", delayStatus1=" + delayStatus1
				+ ", delayAmount1=" + delayAmount1 + ", delayAmountPer1=" + delayAmountPer1 + ", delayBillNum1="
				+ delayBillNum1 + "]";
	}
	
	
}
