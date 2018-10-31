package com.microservice.dao.entity.crawler.bank.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity

@Table(name="bank_report_other_attribute12",indexes = {@Index(name = "index_bank_report_other_attribute12_taskid", columnList = "taskId")})
public class BankReportOtherAttribute12 extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String idNumber;
	private String basicUserId;
	private String bankName;
	private String haveBillMonthNumNearly12;
	private String longestMonthOfContinuousBillNearly12;
	private String noneBillMonthNumNearly12;
	private String noneBillMonthNumPerAll12;
	private String longestNumOfContinuousBillNearly12;
	private String singleCardDisconnectMonthNum12;
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
	public String getHaveBillMonthNumNearly12() {
		return haveBillMonthNumNearly12;
	}
	public void setHaveBillMonthNumNearly12(String haveBillMonthNumNearly12) {
		this.haveBillMonthNumNearly12 = haveBillMonthNumNearly12;
	}
	public String getLongestMonthOfContinuousBillNearly12() {
		return longestMonthOfContinuousBillNearly12;
	}
	public void setLongestMonthOfContinuousBillNearly12(String longestMonthOfContinuousBillNearly12) {
		this.longestMonthOfContinuousBillNearly12 = longestMonthOfContinuousBillNearly12;
	}
	public String getNoneBillMonthNumNearly12() {
		return noneBillMonthNumNearly12;
	}
	public void setNoneBillMonthNumNearly12(String noneBillMonthNumNearly12) {
		this.noneBillMonthNumNearly12 = noneBillMonthNumNearly12;
	}
	public String getNoneBillMonthNumPerAll12() {
		return noneBillMonthNumPerAll12;
	}
	public void setNoneBillMonthNumPerAll12(String noneBillMonthNumPerAll12) {
		this.noneBillMonthNumPerAll12 = noneBillMonthNumPerAll12;
	}
	public String getLongestNumOfContinuousBillNearly12() {
		return longestNumOfContinuousBillNearly12;
	}
	public void setLongestNumOfContinuousBillNearly12(String longestNumOfContinuousBillNearly12) {
		this.longestNumOfContinuousBillNearly12 = longestNumOfContinuousBillNearly12;
	}
	public String getSingleCardDisconnectMonthNum12() {
		return singleCardDisconnectMonthNum12;
	}
	public void setSingleCardDisconnectMonthNum12(String singleCardDisconnectMonthNum12) {
		this.singleCardDisconnectMonthNum12 = singleCardDisconnectMonthNum12;
	}
	@Override
	public String toString() {
		return "BankReportOtherAttribute12 [taskId=" + taskId + ", idNumber=" + idNumber + ", basicUserId="
				+ basicUserId + ", bankName=" + bankName + ", haveBillMonthNumNearly12=" + haveBillMonthNumNearly12
				+ ", longestMonthOfContinuousBillNearly12=" + longestMonthOfContinuousBillNearly12
				+ ", noneBillMonthNumNearly12=" + noneBillMonthNumNearly12 + ", noneBillMonthNumPerAll12="
				+ noneBillMonthNumPerAll12 + ", longestNumOfContinuousBillNearly12="
				+ longestNumOfContinuousBillNearly12 + ", singleCardDisconnectMonthNum12="
				+ singleCardDisconnectMonthNum12 + "]";
	}
	
}
