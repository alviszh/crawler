package com.microservice.dao.entity.crawler.bank.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="bank_report_credit_sales_amount3",indexes = {@Index(name = "index_bank_report_credit_sales_amount3_taskid", columnList = "taskId")})
public class BankReportCreditSalesAmount3 extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String idNumber;
	private String basicUserId;	//唯一标识
	private String bankName;	//唯一标识
	private String ratioSellMonth3;
	private String avgConsumeAmount3;
	private String creditcardMaxBalance3;
	private String creditcardMinBalance3;
	private String withdrawAmount3;
	private String withdrawNumCc3;
	private String hasWithdrawalPercentage3m;
	private String avgCashAmount3;
	private String lastWithdrawalMonthNum3;
	private String earlyestSellAmount3;
	private String earlyestConsumeAmount3;
	private String maxSellNum3;
	private String hasSellMonthNum3;
	private String avgConsumeNum3;
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
	public String getRatioSellMonth3() {
		return ratioSellMonth3;
	}
	public void setRatioSellMonth3(String ratioSellMonth3) {
		this.ratioSellMonth3 = ratioSellMonth3;
	}
	public String getAvgConsumeAmount3() {
		return avgConsumeAmount3;
	}
	public void setAvgConsumeAmount3(String avgConsumeAmount3) {
		this.avgConsumeAmount3 = avgConsumeAmount3;
	}
	public String getCreditcardMaxBalance3() {
		return creditcardMaxBalance3;
	}
	public void setCreditcardMaxBalance3(String creditcardMaxBalance3) {
		this.creditcardMaxBalance3 = creditcardMaxBalance3;
	}
	public String getCreditcardMinBalance3() {
		return creditcardMinBalance3;
	}
	public void setCreditcardMinBalance3(String creditcardMinBalance3) {
		this.creditcardMinBalance3 = creditcardMinBalance3;
	}
	public String getWithdrawAmount3() {
		return withdrawAmount3;
	}
	public void setWithdrawAmount3(String withdrawAmount3) {
		this.withdrawAmount3 = withdrawAmount3;
	}
	public String getWithdrawNumCc3() {
		return withdrawNumCc3;
	}
	public void setWithdrawNumCc3(String withdrawNumCc3) {
		this.withdrawNumCc3 = withdrawNumCc3;
	}
	public String getHasWithdrawalPercentage3m() {
		return hasWithdrawalPercentage3m;
	}
	public void setHasWithdrawalPercentage3m(String hasWithdrawalPercentage3m) {
		this.hasWithdrawalPercentage3m = hasWithdrawalPercentage3m;
	}
	public String getAvgCashAmount3() {
		return avgCashAmount3;
	}
	public void setAvgCashAmount3(String avgCashAmount3) {
		this.avgCashAmount3 = avgCashAmount3;
	}
	public String getLastWithdrawalMonthNum3() {
		return lastWithdrawalMonthNum3;
	}
	public void setLastWithdrawalMonthNum3(String lastWithdrawalMonthNum3) {
		this.lastWithdrawalMonthNum3 = lastWithdrawalMonthNum3;
	}
	public String getEarlyestSellAmount3() {
		return earlyestSellAmount3;
	}
	public void setEarlyestSellAmount3(String earlyestSellAmount3) {
		this.earlyestSellAmount3 = earlyestSellAmount3;
	}
	public String getEarlyestConsumeAmount3() {
		return earlyestConsumeAmount3;
	}
	public void setEarlyestConsumeAmount3(String earlyestConsumeAmount3) {
		this.earlyestConsumeAmount3 = earlyestConsumeAmount3;
	}
	public String getMaxSellNum3() {
		return maxSellNum3;
	}
	public void setMaxSellNum3(String maxSellNum3) {
		this.maxSellNum3 = maxSellNum3;
	}
	public String getHasSellMonthNum3() {
		return hasSellMonthNum3;
	}
	public void setHasSellMonthNum3(String hasSellMonthNum3) {
		this.hasSellMonthNum3 = hasSellMonthNum3;
	}
	public String getAvgConsumeNum3() {
		return avgConsumeNum3;
	}
	public void setAvgConsumeNum3(String avgConsumeNum3) {
		this.avgConsumeNum3 = avgConsumeNum3;
	}
	@Override
	public String toString() {
		return "BankReportCreditSalesAmount3 [taskId=" + taskId + ", idNumber=" + idNumber + ", basicUserId="
				+ basicUserId + ", bankName=" + bankName + ", ratioSellMonth3=" + ratioSellMonth3
				+ ", avgConsumeAmount3=" + avgConsumeAmount3 + ", creditcardMaxBalance3=" + creditcardMaxBalance3
				+ ", creditcardMinBalance3=" + creditcardMinBalance3 + ", withdrawAmount3=" + withdrawAmount3
				+ ", withdrawNumCc3=" + withdrawNumCc3 + ", hasWithdrawalPercentage3m=" + hasWithdrawalPercentage3m
				+ ", avgCashAmount3=" + avgCashAmount3 + ", lastWithdrawalMonthNum3=" + lastWithdrawalMonthNum3
				+ ", earlyestSellAmount3=" + earlyestSellAmount3 + ", earlyestConsumeAmount3=" + earlyestConsumeAmount3
				+ ", maxSellNum3=" + maxSellNum3 + ", hasSellMonthNum3=" + hasSellMonthNum3 + ", avgConsumeNum3="
				+ avgConsumeNum3 + "]";
	}
	
}
