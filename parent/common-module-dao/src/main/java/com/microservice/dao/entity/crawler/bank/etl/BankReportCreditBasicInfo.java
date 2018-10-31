package com.microservice.dao.entity.crawler.bank.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="bank_report_credit_basicinfo",indexes = {@Index(name = "index_bank_report_credit_basicinfo_taskid", columnList = "taskId")})
public class BankReportCreditBasicInfo extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String idNumber;
	private String basicUserId;	//唯一标识
	private String bankName;	//唯一标识
	private String name;
	private String email;
	private String activeCardNum;
	private String bankNum;
	private String billStartDate;
	private String billStartDateMonth;
	private String pvcuCustomerGroupTag;
	private String pvcuCashoutsTag;
	private String latestCertificationTime;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getActiveCardNum() {
		return activeCardNum;
	}
	public void setActiveCardNum(String activeCardNum) {
		this.activeCardNum = activeCardNum;
	}
	public String getBankNum() {
		return bankNum;
	}
	public void setBankNum(String bankNum) {
		this.bankNum = bankNum;
	}
	public String getBillStartDate() {
		return billStartDate;
	}
	public void setBillStartDate(String billStartDate) {
		this.billStartDate = billStartDate;
	}
	public String getBillStartDateMonth() {
		return billStartDateMonth;
	}
	public void setBillStartDateMonth(String billStartDateMonth) {
		this.billStartDateMonth = billStartDateMonth;
	}
	public String getPvcuCustomerGroupTag() {
		return pvcuCustomerGroupTag;
	}
	public void setPvcuCustomerGroupTag(String pvcuCustomerGroupTag) {
		this.pvcuCustomerGroupTag = pvcuCustomerGroupTag;
	}
	public String getPvcuCashoutsTag() {
		return pvcuCashoutsTag;
	}
	public void setPvcuCashoutsTag(String pvcuCashoutsTag) {
		this.pvcuCashoutsTag = pvcuCashoutsTag;
	}
	public String getLatestCertificationTime() {
		return latestCertificationTime;
	}
	public void setLatestCertificationTime(String latestCertificationTime) {
		this.latestCertificationTime = latestCertificationTime;
	}
	@Override
	public String toString() {
		return "BankReportCreditBasicInfo [taskId=" + taskId + ", idNumber=" + idNumber + ", basicUserId=" + basicUserId
				+ ", bankName=" + bankName + ", name=" + name + ", email=" + email + ", activeCardNum=" + activeCardNum
				+ ", bankNum=" + bankNum + ", billStartDate=" + billStartDate + ", billStartDateMonth="
				+ billStartDateMonth + ", pvcuCustomerGroupTag=" + pvcuCustomerGroupTag + ", pvcuCashoutsTag="
				+ pvcuCashoutsTag + ", latestCertificationTime=" + latestCertificationTime + "]";
	}	
}
