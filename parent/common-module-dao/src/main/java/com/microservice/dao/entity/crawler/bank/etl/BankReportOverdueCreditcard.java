package com.microservice.dao.entity.crawler.bank.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="bank_report_overdue_creditcard",indexes = {@Index(name = "index_bank_report_overdue_creditcard_taskid", columnList = "taskId")})
public class BankReportOverdueCreditcard extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String idNumber;
	private String basicUserId;
	private String bankName;
	private String nonDelayedPeriodsNum3;
	private String nonDelayedPeriodsNum6;
	private String nonDelayedPeriodsNum12;
	private String delayedPeriodsNum3;
	private String delayedPeriodsNum6;
	private String delayedPeriodsNum12;
	private String delayedBankNum3;
	private String delayedBankNum6;
	private String delayedBankNum12;
	private String delayedCardNum3;
	private String delayedCardNum6;
	private String delayedCardNum12;
	private String delayedAmntFirst3;
	private String delayedAmntFirst6;
	private String delayedAmntFirst12;
	private String maxAmntOfBeyondDelayed3;
	private String maxAmntOfBeyondDelayed6;
	private String maxAmntOfBeyondDelayed12;
	private String maxBeyondAmnt3;
	private String maxBeyondAmnt6;
	private String maxBeyondAmnt12;
	private String highestDelayed6;
	private String highestDelayed12;
	private String lastDelayedMonNum3;
	private String lastDelayedMonNum6;
	private String lastDelayedMonNum12;
	private String caseDelayedPeriodEqualsOneMonNum3;
	private String caseDelayedPeriodEqualsOneMonNum6;
	private String caseDelayedPeriodEqualsOneMonNum12;
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
	public String getNonDelayedPeriodsNum3() {
		return nonDelayedPeriodsNum3;
	}
	public void setNonDelayedPeriodsNum3(String nonDelayedPeriodsNum3) {
		this.nonDelayedPeriodsNum3 = nonDelayedPeriodsNum3;
	}
	public String getNonDelayedPeriodsNum6() {
		return nonDelayedPeriodsNum6;
	}
	public void setNonDelayedPeriodsNum6(String nonDelayedPeriodsNum6) {
		this.nonDelayedPeriodsNum6 = nonDelayedPeriodsNum6;
	}
	public String getNonDelayedPeriodsNum12() {
		return nonDelayedPeriodsNum12;
	}
	public void setNonDelayedPeriodsNum12(String nonDelayedPeriodsNum12) {
		this.nonDelayedPeriodsNum12 = nonDelayedPeriodsNum12;
	}
	public String getDelayedPeriodsNum3() {
		return delayedPeriodsNum3;
	}
	public void setDelayedPeriodsNum3(String delayedPeriodsNum3) {
		this.delayedPeriodsNum3 = delayedPeriodsNum3;
	}
	public String getDelayedPeriodsNum6() {
		return delayedPeriodsNum6;
	}
	public void setDelayedPeriodsNum6(String delayedPeriodsNum6) {
		this.delayedPeriodsNum6 = delayedPeriodsNum6;
	}
	public String getDelayedPeriodsNum12() {
		return delayedPeriodsNum12;
	}
	public void setDelayedPeriodsNum12(String delayedPeriodsNum12) {
		this.delayedPeriodsNum12 = delayedPeriodsNum12;
	}
	public String getDelayedBankNum3() {
		return delayedBankNum3;
	}
	public void setDelayedBankNum3(String delayedBankNum3) {
		this.delayedBankNum3 = delayedBankNum3;
	}
	public String getDelayedBankNum6() {
		return delayedBankNum6;
	}
	public void setDelayedBankNum6(String delayedBankNum6) {
		this.delayedBankNum6 = delayedBankNum6;
	}
	public String getDelayedBankNum12() {
		return delayedBankNum12;
	}
	public void setDelayedBankNum12(String delayedBankNum12) {
		this.delayedBankNum12 = delayedBankNum12;
	}
	public String getDelayedCardNum3() {
		return delayedCardNum3;
	}
	public void setDelayedCardNum3(String delayedCardNum3) {
		this.delayedCardNum3 = delayedCardNum3;
	}
	public String getDelayedCardNum6() {
		return delayedCardNum6;
	}
	public void setDelayedCardNum6(String delayedCardNum6) {
		this.delayedCardNum6 = delayedCardNum6;
	}
	public String getDelayedCardNum12() {
		return delayedCardNum12;
	}
	public void setDelayedCardNum12(String delayedCardNum12) {
		this.delayedCardNum12 = delayedCardNum12;
	}
	public String getDelayedAmntFirst3() {
		return delayedAmntFirst3;
	}
	public void setDelayedAmntFirst3(String delayedAmntFirst3) {
		this.delayedAmntFirst3 = delayedAmntFirst3;
	}
	public String getDelayedAmntFirst6() {
		return delayedAmntFirst6;
	}
	public void setDelayedAmntFirst6(String delayedAmntFirst6) {
		this.delayedAmntFirst6 = delayedAmntFirst6;
	}
	public String getDelayedAmntFirst12() {
		return delayedAmntFirst12;
	}
	public void setDelayedAmntFirst12(String delayedAmntFirst12) {
		this.delayedAmntFirst12 = delayedAmntFirst12;
	}
	public String getMaxAmntOfBeyondDelayed3() {
		return maxAmntOfBeyondDelayed3;
	}
	public void setMaxAmntOfBeyondDelayed3(String maxAmntOfBeyondDelayed3) {
		this.maxAmntOfBeyondDelayed3 = maxAmntOfBeyondDelayed3;
	}
	public String getMaxAmntOfBeyondDelayed6() {
		return maxAmntOfBeyondDelayed6;
	}
	public void setMaxAmntOfBeyondDelayed6(String maxAmntOfBeyondDelayed6) {
		this.maxAmntOfBeyondDelayed6 = maxAmntOfBeyondDelayed6;
	}
	public String getMaxAmntOfBeyondDelayed12() {
		return maxAmntOfBeyondDelayed12;
	}
	public void setMaxAmntOfBeyondDelayed12(String maxAmntOfBeyondDelayed12) {
		this.maxAmntOfBeyondDelayed12 = maxAmntOfBeyondDelayed12;
	}
	public String getMaxBeyondAmnt3() {
		return maxBeyondAmnt3;
	}
	public void setMaxBeyondAmnt3(String maxBeyondAmnt3) {
		this.maxBeyondAmnt3 = maxBeyondAmnt3;
	}
	public String getMaxBeyondAmnt6() {
		return maxBeyondAmnt6;
	}
	public void setMaxBeyondAmnt6(String maxBeyondAmnt6) {
		this.maxBeyondAmnt6 = maxBeyondAmnt6;
	}
	public String getMaxBeyondAmnt12() {
		return maxBeyondAmnt12;
	}
	public void setMaxBeyondAmnt12(String maxBeyondAmnt12) {
		this.maxBeyondAmnt12 = maxBeyondAmnt12;
	}
	public String getHighestDelayed6() {
		return highestDelayed6;
	}
	public void setHighestDelayed6(String highestDelayed6) {
		this.highestDelayed6 = highestDelayed6;
	}
	public String getHighestDelayed12() {
		return highestDelayed12;
	}
	public void setHighestDelayed12(String highestDelayed12) {
		this.highestDelayed12 = highestDelayed12;
	}
	public String getLastDelayedMonNum3() {
		return lastDelayedMonNum3;
	}
	public void setLastDelayedMonNum3(String lastDelayedMonNum3) {
		this.lastDelayedMonNum3 = lastDelayedMonNum3;
	}
	public String getLastDelayedMonNum6() {
		return lastDelayedMonNum6;
	}
	public void setLastDelayedMonNum6(String lastDelayedMonNum6) {
		this.lastDelayedMonNum6 = lastDelayedMonNum6;
	}
	public String getLastDelayedMonNum12() {
		return lastDelayedMonNum12;
	}
	public void setLastDelayedMonNum12(String lastDelayedMonNum12) {
		this.lastDelayedMonNum12 = lastDelayedMonNum12;
	}
	public String getCaseDelayedPeriodEqualsOneMonNum3() {
		return caseDelayedPeriodEqualsOneMonNum3;
	}
	public void setCaseDelayedPeriodEqualsOneMonNum3(String caseDelayedPeriodEqualsOneMonNum3) {
		this.caseDelayedPeriodEqualsOneMonNum3 = caseDelayedPeriodEqualsOneMonNum3;
	}
	public String getCaseDelayedPeriodEqualsOneMonNum6() {
		return caseDelayedPeriodEqualsOneMonNum6;
	}
	public void setCaseDelayedPeriodEqualsOneMonNum6(String caseDelayedPeriodEqualsOneMonNum6) {
		this.caseDelayedPeriodEqualsOneMonNum6 = caseDelayedPeriodEqualsOneMonNum6;
	}
	public String getCaseDelayedPeriodEqualsOneMonNum12() {
		return caseDelayedPeriodEqualsOneMonNum12;
	}
	public void setCaseDelayedPeriodEqualsOneMonNum12(String caseDelayedPeriodEqualsOneMonNum12) {
		this.caseDelayedPeriodEqualsOneMonNum12 = caseDelayedPeriodEqualsOneMonNum12;
	}
	@Override
	public String toString() {
		return "BankReportOverdueCreditcard [taskId=" + taskId + ", idNumber=" + idNumber + ", basicUserId="
				+ basicUserId + ", bankName=" + bankName + ", nonDelayedPeriodsNum3=" + nonDelayedPeriodsNum3
				+ ", nonDelayedPeriodsNum6=" + nonDelayedPeriodsNum6 + ", nonDelayedPeriodsNum12="
				+ nonDelayedPeriodsNum12 + ", delayedPeriodsNum3=" + delayedPeriodsNum3 + ", delayedPeriodsNum6="
				+ delayedPeriodsNum6 + ", delayedPeriodsNum12=" + delayedPeriodsNum12 + ", delayedBankNum3="
				+ delayedBankNum3 + ", delayedBankNum6=" + delayedBankNum6 + ", delayedBankNum12=" + delayedBankNum12
				+ ", delayedCardNum3=" + delayedCardNum3 + ", delayedCardNum6=" + delayedCardNum6
				+ ", delayedCardNum12=" + delayedCardNum12 + ", delayedAmntFirst3=" + delayedAmntFirst3
				+ ", delayedAmntFirst6=" + delayedAmntFirst6 + ", delayedAmntFirst12=" + delayedAmntFirst12
				+ ", maxAmntOfBeyondDelayed3=" + maxAmntOfBeyondDelayed3 + ", maxAmntOfBeyondDelayed6="
				+ maxAmntOfBeyondDelayed6 + ", maxAmntOfBeyondDelayed12=" + maxAmntOfBeyondDelayed12
				+ ", maxBeyondAmnt3=" + maxBeyondAmnt3 + ", maxBeyondAmnt6=" + maxBeyondAmnt6 + ", maxBeyondAmnt12="
				+ maxBeyondAmnt12 + ", highestDelayed6=" + highestDelayed6 + ", highestDelayed12=" + highestDelayed12
				+ ", lastDelayedMonNum3=" + lastDelayedMonNum3 + ", lastDelayedMonNum6=" + lastDelayedMonNum6
				+ ", lastDelayedMonNum12=" + lastDelayedMonNum12 + ", caseDelayedPeriodEqualsOneMonNum3="
				+ caseDelayedPeriodEqualsOneMonNum3 + ", caseDelayedPeriodEqualsOneMonNum6="
				+ caseDelayedPeriodEqualsOneMonNum6 + ", caseDelayedPeriodEqualsOneMonNum12="
				+ caseDelayedPeriodEqualsOneMonNum12 + "]";
	}
	
}
