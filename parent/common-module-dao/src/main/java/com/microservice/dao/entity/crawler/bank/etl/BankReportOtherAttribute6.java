package com.microservice.dao.entity.crawler.bank.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity

@Table(name="bank_report_other_attribute6",indexes = {@Index(name = "index_bank_report_other_attribute6_taskid", columnList = "taskId")})
public class BankReportOtherAttribute6 extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String idNumber;
	private String basicUserId;
	private String bankName;
	private String haveBillMonthNumNearly6;
	private String longestMonthOfContinuousBillNearly6;
	private String noneBillMonthNumNearly6;
	private String noneBillMonthNumPerAll6;
	private String longestNumOfContinuousBillNearly6;
	private String singleCardDisconnectMonthNum6;
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
	public String getHaveBillMonthNumNearly6() {
		return haveBillMonthNumNearly6;
	}
	public void setHaveBillMonthNumNearly6(String haveBillMonthNumNearly6) {
		this.haveBillMonthNumNearly6 = haveBillMonthNumNearly6;
	}
	public String getLongestMonthOfContinuousBillNearly6() {
		return longestMonthOfContinuousBillNearly6;
	}
	public void setLongestMonthOfContinuousBillNearly6(String longestMonthOfContinuousBillNearly6) {
		this.longestMonthOfContinuousBillNearly6 = longestMonthOfContinuousBillNearly6;
	}
	public String getNoneBillMonthNumNearly6() {
		return noneBillMonthNumNearly6;
	}
	public void setNoneBillMonthNumNearly6(String noneBillMonthNumNearly6) {
		this.noneBillMonthNumNearly6 = noneBillMonthNumNearly6;
	}
	public String getNoneBillMonthNumPerAll6() {
		return noneBillMonthNumPerAll6;
	}
	public void setNoneBillMonthNumPerAll6(String noneBillMonthNumPerAll6) {
		this.noneBillMonthNumPerAll6 = noneBillMonthNumPerAll6;
	}
	public String getLongestNumOfContinuousBillNearly6() {
		return longestNumOfContinuousBillNearly6;
	}
	public void setLongestNumOfContinuousBillNearly6(String longestNumOfContinuousBillNearly6) {
		this.longestNumOfContinuousBillNearly6 = longestNumOfContinuousBillNearly6;
	}
	public String getSingleCardDisconnectMonthNum6() {
		return singleCardDisconnectMonthNum6;
	}
	public void setSingleCardDisconnectMonthNum6(String singleCardDisconnectMonthNum6) {
		this.singleCardDisconnectMonthNum6 = singleCardDisconnectMonthNum6;
	}
	@Override
	public String toString() {
		return "BankReportOtherAttribute6 [taskId=" + taskId + ", idNumber=" + idNumber + ", basicUserId=" + basicUserId
				+ ", bankName=" + bankName + ", haveBillMonthNumNearly6=" + haveBillMonthNumNearly6
				+ ", longestMonthOfContinuousBillNearly6=" + longestMonthOfContinuousBillNearly6
				+ ", noneBillMonthNumNearly6=" + noneBillMonthNumNearly6 + ", noneBillMonthNumPerAll6="
				+ noneBillMonthNumPerAll6 + ", longestNumOfContinuousBillNearly6=" + longestNumOfContinuousBillNearly6
				+ ", singleCardDisconnectMonthNum6=" + singleCardDisconnectMonthNum6 + "]";
	}
	
}
