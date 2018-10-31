package com.microservice.dao.entity.crawler.bank.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity

@Table(name="bank_report_other_attribute3",indexes = {@Index(name = "index_bank_report_other_attribute3_taskid", columnList = "taskId")})
public class BankReportOtherAttribute3 extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String idNumber;
	private String basicUserId;
	private String bankName;
	private String haveBillMonthNumNearly3;
	private String longestMonthOfContinuousBillNearly3;
	private String noneBillMonthNumNearly3;
	private String noneBillMonthNumPerAll3;
	private String longestNumOfContinuousBillNearly3;
	private String singleCardDisconnectMonthNum3;
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
	public String getHaveBillMonthNumNearly3() {
		return haveBillMonthNumNearly3;
	}
	public void setHaveBillMonthNumNearly3(String haveBillMonthNumNearly3) {
		this.haveBillMonthNumNearly3 = haveBillMonthNumNearly3;
	}
	public String getLongestMonthOfContinuousBillNearly3() {
		return longestMonthOfContinuousBillNearly3;
	}
	public void setLongestMonthOfContinuousBillNearly3(String longestMonthOfContinuousBillNearly3) {
		this.longestMonthOfContinuousBillNearly3 = longestMonthOfContinuousBillNearly3;
	}
	public String getNoneBillMonthNumNearly3() {
		return noneBillMonthNumNearly3;
	}
	public void setNoneBillMonthNumNearly3(String noneBillMonthNumNearly3) {
		this.noneBillMonthNumNearly3 = noneBillMonthNumNearly3;
	}
	public String getNoneBillMonthNumPerAll3() {
		return noneBillMonthNumPerAll3;
	}
	public void setNoneBillMonthNumPerAll3(String noneBillMonthNumPerAll3) {
		this.noneBillMonthNumPerAll3 = noneBillMonthNumPerAll3;
	}
	public String getLongestNumOfContinuousBillNearly3() {
		return longestNumOfContinuousBillNearly3;
	}
	public void setLongestNumOfContinuousBillNearly3(String longestNumOfContinuousBillNearly3) {
		this.longestNumOfContinuousBillNearly3 = longestNumOfContinuousBillNearly3;
	}
	public String getSingleCardDisconnectMonthNum3() {
		return singleCardDisconnectMonthNum3;
	}
	public void setSingleCardDisconnectMonthNum3(String singleCardDisconnectMonthNum3) {
		this.singleCardDisconnectMonthNum3 = singleCardDisconnectMonthNum3;
	}
	@Override
	public String toString() {
		return "BankReportOtherAttribute [taskId=" + taskId + ", idNumber=" + idNumber + ", basicUserId=" + basicUserId
				+ ", bankName=" + bankName + ", haveBillMonthNumNearly3=" + haveBillMonthNumNearly3
				+ ", longestMonthOfContinuousBillNearly3=" + longestMonthOfContinuousBillNearly3
				+ ", noneBillMonthNumNearly3=" + noneBillMonthNumNearly3 + ", noneBillMonthNumPerAll3="
				+ noneBillMonthNumPerAll3 + ", longestNumOfContinuousBillNearly3=" + longestNumOfContinuousBillNearly3
				+ ", singleCardDisconnectMonthNum3=" + singleCardDisconnectMonthNum3 + "]";
	}
	
}
