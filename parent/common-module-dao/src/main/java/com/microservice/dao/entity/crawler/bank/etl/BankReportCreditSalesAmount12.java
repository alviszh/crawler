package com.microservice.dao.entity.crawler.bank.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="bank_report_credit_sales_amount12",indexes = {@Index(name = "index_bank_report_credit_sales_amount12_taskid", columnList = "taskId")})

public class BankReportCreditSalesAmount12 extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String idNumber;
	private String basicUserId;	//唯一标识
	private String bankName;	//唯一标识
	private String ratioSellMonth12;
	private String avgConsumeAmount12;
	private String creditcardMaxBalance12;
	private String creditcardMinBalance12;
	private String withdrawAmount12;
	private String withdrawNumCc12;
	private String hasWithdrawalPercentage12m;
	private String avgCashAmount12;
	private String lastWithdrawalMonthNum12;
	private String earlyestSellAmount12;
	private String earlyestConsumeAmount12;
	private String maxSellNum12;
	private String hasSellMonthNum12;
	private String avgConsumeNum12;
	
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
	public String getRatioSellMonth12() {
		return ratioSellMonth12;
	}
	public void setRatioSellMonth12(String ratioSellMonth12) {
		this.ratioSellMonth12 = ratioSellMonth12;
	}
	public String getAvgConsumeAmount12() {
		return avgConsumeAmount12;
	}
	public void setAvgConsumeAmount12(String avgConsumeAmount12) {
		this.avgConsumeAmount12 = avgConsumeAmount12;
	}
	public String getCreditcardMaxBalance12() {
		return creditcardMaxBalance12;
	}
	public void setCreditcardMaxBalance12(String creditcardMaxBalance12) {
		this.creditcardMaxBalance12 = creditcardMaxBalance12;
	}
	public String getCreditcardMinBalance12() {
		return creditcardMinBalance12;
	}
	public void setCreditcardMinBalance12(String creditcardMinBalance12) {
		this.creditcardMinBalance12 = creditcardMinBalance12;
	}
	public String getWithdrawAmount12() {
		return withdrawAmount12;
	}
	public void setWithdrawAmount12(String withdrawAmount12) {
		this.withdrawAmount12 = withdrawAmount12;
	}
	public String getWithdrawNumCc12() {
		return withdrawNumCc12;
	}
	public void setWithdrawNumCc12(String withdrawNumCc12) {
		this.withdrawNumCc12 = withdrawNumCc12;
	}
	public String getHasWithdrawalPercentage12m() {
		return hasWithdrawalPercentage12m;
	}
	public void setHasWithdrawalPercentage12m(String hasWithdrawalPercentage12m) {
		this.hasWithdrawalPercentage12m = hasWithdrawalPercentage12m;
	}
	public String getAvgCashAmount12() {
		return avgCashAmount12;
	}
	public void setAvgCashAmount12(String avgCashAmount12) {
		this.avgCashAmount12 = avgCashAmount12;
	}
	public String getLastWithdrawalMonthNum12() {
		return lastWithdrawalMonthNum12;
	}
	public void setLastWithdrawalMonthNum12(String lastWithdrawalMonthNum12) {
		this.lastWithdrawalMonthNum12 = lastWithdrawalMonthNum12;
	}
	public String getEarlyestSellAmount12() {
		return earlyestSellAmount12;
	}
	public void setEarlyestSellAmount12(String earlyestSellAmount12) {
		this.earlyestSellAmount12 = earlyestSellAmount12;
	}
	public String getEarlyestConsumeAmount12() {
		return earlyestConsumeAmount12;
	}
	public void setEarlyestConsumeAmount12(String earlyestConsumeAmount12) {
		this.earlyestConsumeAmount12 = earlyestConsumeAmount12;
	}
	public String getMaxSellNum12() {
		return maxSellNum12;
	}
	public void setMaxSellNum12(String maxSellNum12) {
		this.maxSellNum12 = maxSellNum12;
	}
	public String getHasSellMonthNum12() {
		return hasSellMonthNum12;
	}
	public void setHasSellMonthNum12(String hasSellMonthNum12) {
		this.hasSellMonthNum12 = hasSellMonthNum12;
	}
	public String getAvgConsumeNum12() {
		return avgConsumeNum12;
	}
	public void setAvgConsumeNum12(String avgConsumeNum12) {
		this.avgConsumeNum12 = avgConsumeNum12;
	}
	@Override
	public String toString() {
		return "BankReportCreditSalesAmount12 [taskId=" + taskId + ", idNumber=" + idNumber + ", basicUserId="
				+ basicUserId + ", bankName=" + bankName + ", ratioSellMonth12=" + ratioSellMonth12
				+ ", avgConsumeAmount12=" + avgConsumeAmount12 + ", creditcardMaxBalance12=" + creditcardMaxBalance12
				+ ", creditcardMinBalance12=" + creditcardMinBalance12 + ", withdrawAmount12=" + withdrawAmount12
				+ ", withdrawNumCc12=" + withdrawNumCc12 + ", hasWithdrawalPercentage12m=" + hasWithdrawalPercentage12m
				+ ", avgCashAmount12=" + avgCashAmount12 + ", lastWithdrawalMonthNum12=" + lastWithdrawalMonthNum12
				+ ", earlyestSellAmount12=" + earlyestSellAmount12 + ", earlyestConsumeAmount12="
				+ earlyestConsumeAmount12 + ", maxSellNum12=" + maxSellNum12 + ", hasSellMonthNum12="
				+ hasSellMonthNum12 + ", avgConsumeNum12=" + avgConsumeNum12 + "]";
	}
	
	
}
