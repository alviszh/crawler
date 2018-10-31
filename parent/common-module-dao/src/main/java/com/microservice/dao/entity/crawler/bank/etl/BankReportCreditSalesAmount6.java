package com.microservice.dao.entity.crawler.bank.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="bank_report_credit_sales_amount6",indexes = {@Index(name = "index_bank_report_credit_sales_amount6_taskid", columnList = "taskId")})
public class BankReportCreditSalesAmount6 extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String idNumber;
	private String basicUserId;	//唯一标识
	private String bankName;	//唯一标识
	private String ratioSellMonth6;
	private String avgConsumeAmount6;
	private String creditcardMaxBalance6;
	private String creditcardMinBalance6;
	private String withdrawAmount6;
	private String withdrawNumCc6;
	private String hasWithdrawalPercentage6m;
	private String avgCashAmount6;
	private String lastWithdrawalMonthNum6;
	private String earlyestSellAmount6;
	private String earlyestConsumeAmount6;
	private String maxSellNum6;
	private String hasSellMonthNum6;
	private String avgConsumeNum6;
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
	public String getRatioSellMonth6() {
		return ratioSellMonth6;
	}
	public void setRatioSellMonth6(String ratioSellMonth6) {
		this.ratioSellMonth6 = ratioSellMonth6;
	}
	public String getAvgConsumeAmount6() {
		return avgConsumeAmount6;
	}
	public void setAvgConsumeAmount6(String avgConsumeAmount6) {
		this.avgConsumeAmount6 = avgConsumeAmount6;
	}
	public String getCreditcardMaxBalance6() {
		return creditcardMaxBalance6;
	}
	public void setCreditcardMaxBalance6(String creditcardMaxBalance6) {
		this.creditcardMaxBalance6 = creditcardMaxBalance6;
	}
	public String getCreditcardMinBalance6() {
		return creditcardMinBalance6;
	}
	public void setCreditcardMinBalance6(String creditcardMinBalance6) {
		this.creditcardMinBalance6 = creditcardMinBalance6;
	}
	public String getWithdrawAmount6() {
		return withdrawAmount6;
	}
	public void setWithdrawAmount6(String withdrawAmount6) {
		this.withdrawAmount6 = withdrawAmount6;
	}
	public String getWithdrawNumCc6() {
		return withdrawNumCc6;
	}
	public void setWithdrawNumCc6(String withdrawNumCc6) {
		this.withdrawNumCc6 = withdrawNumCc6;
	}
	public String getHasWithdrawalPercentage6m() {
		return hasWithdrawalPercentage6m;
	}
	public void setHasWithdrawalPercentage6m(String hasWithdrawalPercentage6m) {
		this.hasWithdrawalPercentage6m = hasWithdrawalPercentage6m;
	}
	public String getAvgCashAmount6() {
		return avgCashAmount6;
	}
	public void setAvgCashAmount6(String avgCashAmount6) {
		this.avgCashAmount6 = avgCashAmount6;
	}
	public String getLastWithdrawalMonthNum6() {
		return lastWithdrawalMonthNum6;
	}
	public void setLastWithdrawalMonthNum6(String lastWithdrawalMonthNum6) {
		this.lastWithdrawalMonthNum6 = lastWithdrawalMonthNum6;
	}
	public String getEarlyestSellAmount6() {
		return earlyestSellAmount6;
	}
	public void setEarlyestSellAmount6(String earlyestSellAmount6) {
		this.earlyestSellAmount6 = earlyestSellAmount6;
	}
	public String getEarlyestConsumeAmount6() {
		return earlyestConsumeAmount6;
	}
	public void setEarlyestConsumeAmount6(String earlyestConsumeAmount6) {
		this.earlyestConsumeAmount6 = earlyestConsumeAmount6;
	}
	public String getMaxSellNum6() {
		return maxSellNum6;
	}
	public void setMaxSellNum6(String maxSellNum6) {
		this.maxSellNum6 = maxSellNum6;
	}
	public String getHasSellMonthNum6() {
		return hasSellMonthNum6;
	}
	public void setHasSellMonthNum6(String hasSellMonthNum6) {
		this.hasSellMonthNum6 = hasSellMonthNum6;
	}
	public String getAvgConsumeNum6() {
		return avgConsumeNum6;
	}
	public void setAvgConsumeNum6(String avgConsumeNum6) {
		this.avgConsumeNum6 = avgConsumeNum6;
	}
	@Override
	public String toString() {
		return "BankReportCreditSalesAmount6 [taskId=" + taskId + ", idNumber=" + idNumber + ", basicUserId="
				+ basicUserId + ", bankName=" + bankName + ", ratioSellMonth6=" + ratioSellMonth6
				+ ", avgConsumeAmount6=" + avgConsumeAmount6 + ", creditcardMaxBalance6=" + creditcardMaxBalance6
				+ ", creditcardMinBalance6=" + creditcardMinBalance6 + ", withdrawAmount6=" + withdrawAmount6
				+ ", withdrawNumCc6=" + withdrawNumCc6 + ", hasWithdrawalPercentage6m=" + hasWithdrawalPercentage6m
				+ ", avgCashAmount6=" + avgCashAmount6 + ", lastWithdrawalMonthNum6=" + lastWithdrawalMonthNum6
				+ ", earlyestSellAmount6=" + earlyestSellAmount6 + ", earlyestConsumeAmount6=" + earlyestConsumeAmount6
				+ ", maxSellNum6=" + maxSellNum6 + ", hasSellMonthNum6=" + hasSellMonthNum6 + ", avgConsumeNum6="
				+ avgConsumeNum6 + "]";
	}
	
}
