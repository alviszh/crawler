package com.microservice.dao.entity.crawler.bank.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="bank_report_credit_card_summary",indexes = {@Index(name = "index_bank_report_credit_card_summary_taskid", columnList = "taskId")})
public class BankReportCreditCardSummary extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String idNumber;
	private String basicUserId;	//唯一标识
	private String bankName;	//唯一标识
	private String totalNewBalance1;
	private String totalLastBalance1;
	private String totalMinPayment1;
	private String totalConsumeAmount1;
	private String totalConsumeNum1;
	private String lastUnrepayAmount;
	private String curConsumeAvgAmount;
	private String curCashAmount;
	private String curCashNum;
	private String curCashAvgAmount;
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
	public String getTotalNewBalance1() {
		return totalNewBalance1;
	}
	public void setTotalNewBalance1(String totalNewBalance1) {
		this.totalNewBalance1 = totalNewBalance1;
	}
	public String getTotalLastBalance1() {
		return totalLastBalance1;
	}
	public void setTotalLastBalance1(String totalLastBalance1) {
		this.totalLastBalance1 = totalLastBalance1;
	}
	public String getTotalMinPayment1() {
		return totalMinPayment1;
	}
	public void setTotalMinPayment1(String totalMinPayment1) {
		this.totalMinPayment1 = totalMinPayment1;
	}
	public String getTotalConsumeAmount1() {
		return totalConsumeAmount1;
	}
	public void setTotalConsumeAmount1(String totalConsumeAmount1) {
		this.totalConsumeAmount1 = totalConsumeAmount1;
	}
	public String getTotalConsumeNum1() {
		return totalConsumeNum1;
	}
	public void setTotalConsumeNum1(String totalConsumeNum1) {
		this.totalConsumeNum1 = totalConsumeNum1;
	}
	public String getLastUnrepayAmount() {
		return lastUnrepayAmount;
	}
	public void setLastUnrepayAmount(String lastUnrepayAmount) {
		this.lastUnrepayAmount = lastUnrepayAmount;
	}
	public String getCurConsumeAvgAmount() {
		return curConsumeAvgAmount;
	}
	public void setCurConsumeAvgAmount(String curConsumeAvgAmount) {
		this.curConsumeAvgAmount = curConsumeAvgAmount;
	}
	public String getCurCashAmount() {
		return curCashAmount;
	}
	public void setCurCashAmount(String curCashAmount) {
		this.curCashAmount = curCashAmount;
	}
	public String getCurCashNum() {
		return curCashNum;
	}
	public void setCurCashNum(String curCashNum) {
		this.curCashNum = curCashNum;
	}
	public String getCurCashAvgAmount() {
		return curCashAvgAmount;
	}
	public void setCurCashAvgAmount(String curCashAvgAmount) {
		this.curCashAvgAmount = curCashAvgAmount;
	}
	@Override
	public String toString() {
		return "BankReportCreditCardSummary [taskId=" + taskId + ", idNumber=" + idNumber + ", basicUserId="
				+ basicUserId + ", bankName=" + bankName + ", totalNewBalance1=" + totalNewBalance1
				+ ", totalLastBalance1=" + totalLastBalance1 + ", totalMinPayment1=" + totalMinPayment1
				+ ", totalConsumeAmount1=" + totalConsumeAmount1 + ", totalConsumeNum1=" + totalConsumeNum1
				+ ", lastUnrepayAmount=" + lastUnrepayAmount + ", curConsumeAvgAmount=" + curConsumeAvgAmount
				+ ", curCashAmount=" + curCashAmount + ", curCashNum=" + curCashNum + ", curCashAvgAmount="
				+ curCashAvgAmount + "]";
	}
	
}
