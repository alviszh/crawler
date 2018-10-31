package com.microservice.dao.entity.crawler.bank.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="bank_report_credit_repayment_summary",indexes = {@Index(name = "index_bank_report_credit_repayment_summary_taskid", columnList = "taskId")})
public class BankReportCreditRepaymentSummary extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String idNumber;
	private String basicUserId;	//唯一标识
	private String bankName;	//唯一标识
	private String repayAmount1;
	private String repayNum1;
	private String repayRatio1;
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
	public String getRepayAmount1() {
		return repayAmount1;
	}
	public void setRepayAmount1(String repayAmount1) {
		this.repayAmount1 = repayAmount1;
	}
	public String getRepayNum1() {
		return repayNum1;
	}
	public void setRepayNum1(String repayNum1) {
		this.repayNum1 = repayNum1;
	}
	public String getRepayRatio1() {
		return repayRatio1;
	}
	public void setRepayRatio1(String repayRatio1) {
		this.repayRatio1 = repayRatio1;
	}
	@Override
	public String toString() {
		return "BankReportCreditRepaymentSummary [taskId=" + taskId + ", idNumber=" + idNumber + ", basicUserId="
				+ basicUserId + ", bankName=" + bankName + ", repayAmount1=" + repayAmount1 + ", repayNum1=" + repayNum1
				+ ", repayRatio1=" + repayRatio1 + "]";
	}
	
	
}
