package com.microservice.dao.entity.crawler.bank.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity

@Table(name="bank_report_quota12",indexes = {@Index(name = "index_bank_report_quota12_taskid", columnList = "taskId")})
public class BankReportQuota12 extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String idNumber;
	private String basicUserId;
	private String bankName;
	private String averageBalancePerQuotaNearly12m;
	private String propertionOfSalesAmountQuota12;
	private String propertionOfMaxSalesAmountInQuota12;
	private String propertionOfMinSalesAmountInQuota12;
	private String useageOfMaxQuota12;
	private String lastUseageMoreThan0Nearly12;
	private String avgPropertionOfConsumeWithdrawalInQuota12;
	private String propertionOfSalesAmntInQuotaMore05Num12;
	private String propertionOfSalesAmntInQuotaMore09Num12;
	private String maxPropertionOfLastUseage12;
	private String avgQuota12;
	private String maxQuota12;
	private String minQuota12;
	private String useageOfMinQuota12;
	private String averageUseageRateOfQuotaNearly12m;
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
	public String getAverageBalancePerQuotaNearly12m() {
		return averageBalancePerQuotaNearly12m;
	}
	public void setAverageBalancePerQuotaNearly12m(String averageBalancePerQuotaNearly12m) {
		this.averageBalancePerQuotaNearly12m = averageBalancePerQuotaNearly12m;
	}
	public String getPropertionOfSalesAmountQuota12() {
		return propertionOfSalesAmountQuota12;
	}
	public void setPropertionOfSalesAmountQuota12(String propertionOfSalesAmountQuota12) {
		this.propertionOfSalesAmountQuota12 = propertionOfSalesAmountQuota12;
	}
	public String getPropertionOfMaxSalesAmountInQuota12() {
		return propertionOfMaxSalesAmountInQuota12;
	}
	public void setPropertionOfMaxSalesAmountInQuota12(String propertionOfMaxSalesAmountInQuota12) {
		this.propertionOfMaxSalesAmountInQuota12 = propertionOfMaxSalesAmountInQuota12;
	}
	public String getPropertionOfMinSalesAmountInQuota12() {
		return propertionOfMinSalesAmountInQuota12;
	}
	public void setPropertionOfMinSalesAmountInQuota12(String propertionOfMinSalesAmountInQuota12) {
		this.propertionOfMinSalesAmountInQuota12 = propertionOfMinSalesAmountInQuota12;
	}
	public String getUseageOfMaxQuota12() {
		return useageOfMaxQuota12;
	}
	public void setUseageOfMaxQuota12(String useageOfMaxQuota12) {
		this.useageOfMaxQuota12 = useageOfMaxQuota12;
	}
	public String getLastUseageMoreThan0Nearly12() {
		return lastUseageMoreThan0Nearly12;
	}
	public void setLastUseageMoreThan0Nearly12(String lastUseageMoreThan0Nearly12) {
		this.lastUseageMoreThan0Nearly12 = lastUseageMoreThan0Nearly12;
	}
	public String getAvgPropertionOfConsumeWithdrawalInQuota12() {
		return avgPropertionOfConsumeWithdrawalInQuota12;
	}
	public void setAvgPropertionOfConsumeWithdrawalInQuota12(String avgPropertionOfConsumeWithdrawalInQuota12) {
		this.avgPropertionOfConsumeWithdrawalInQuota12 = avgPropertionOfConsumeWithdrawalInQuota12;
	}
	public String getPropertionOfSalesAmntInQuotaMore05Num12() {
		return propertionOfSalesAmntInQuotaMore05Num12;
	}
	public void setPropertionOfSalesAmntInQuotaMore05Num12(String propertionOfSalesAmntInQuotaMore05Num12) {
		this.propertionOfSalesAmntInQuotaMore05Num12 = propertionOfSalesAmntInQuotaMore05Num12;
	}
	public String getPropertionOfSalesAmntInQuotaMore09Num12() {
		return propertionOfSalesAmntInQuotaMore09Num12;
	}
	public void setPropertionOfSalesAmntInQuotaMore09Num12(String propertionOfSalesAmntInQuotaMore09Num12) {
		this.propertionOfSalesAmntInQuotaMore09Num12 = propertionOfSalesAmntInQuotaMore09Num12;
	}
	public String getMaxPropertionOfLastUseage12() {
		return maxPropertionOfLastUseage12;
	}
	public void setMaxPropertionOfLastUseage12(String maxPropertionOfLastUseage12) {
		this.maxPropertionOfLastUseage12 = maxPropertionOfLastUseage12;
	}
	public String getAvgQuota12() {
		return avgQuota12;
	}
	public void setAvgQuota12(String avgQuota12) {
		this.avgQuota12 = avgQuota12;
	}
	public String getMaxQuota12() {
		return maxQuota12;
	}
	public void setMaxQuota12(String maxQuota12) {
		this.maxQuota12 = maxQuota12;
	}
	public String getMinQuota12() {
		return minQuota12;
	}
	public void setMinQuota12(String minQuota12) {
		this.minQuota12 = minQuota12;
	}
	public String getUseageOfMinQuota12() {
		return useageOfMinQuota12;
	}
	public void setUseageOfMinQuota12(String useageOfMinQuota12) {
		this.useageOfMinQuota12 = useageOfMinQuota12;
	}
	public String getAverageUseageRateOfQuotaNearly12m() {
		return averageUseageRateOfQuotaNearly12m;
	}
	public void setAverageUseageRateOfQuotaNearly12m(String averageUseageRateOfQuotaNearly12m) {
		this.averageUseageRateOfQuotaNearly12m = averageUseageRateOfQuotaNearly12m;
	}
	@Override
	public String toString() {
		return "BankReportQuota12 [taskId=" + taskId + ", idNumber=" + idNumber + ", basicUserId=" + basicUserId
				+ ", bankName=" + bankName + ", averageBalancePerQuotaNearly12m=" + averageBalancePerQuotaNearly12m
				+ ", propertionOfSalesAmountQuota12=" + propertionOfSalesAmountQuota12
				+ ", propertionOfMaxSalesAmountInQuota12=" + propertionOfMaxSalesAmountInQuota12
				+ ", propertionOfMinSalesAmountInQuota12=" + propertionOfMinSalesAmountInQuota12
				+ ", useageOfMaxQuota12=" + useageOfMaxQuota12 + ", lastUseageMoreThan0Nearly12="
				+ lastUseageMoreThan0Nearly12 + ", avgPropertionOfConsumeWithdrawalInQuota12="
				+ avgPropertionOfConsumeWithdrawalInQuota12 + ", propertionOfSalesAmntInQuotaMore05Num12="
				+ propertionOfSalesAmntInQuotaMore05Num12 + ", propertionOfSalesAmntInQuotaMore09Num12="
				+ propertionOfSalesAmntInQuotaMore09Num12 + ", maxPropertionOfLastUseage12="
				+ maxPropertionOfLastUseage12 + ", avgQuota12=" + avgQuota12 + ", maxQuota12=" + maxQuota12
				+ ", minQuota12=" + minQuota12 + ", useageOfMinQuota12=" + useageOfMinQuota12
				+ ", averageUseageRateOfQuotaNearly12m=" + averageUseageRateOfQuotaNearly12m + "]";
	}
	
}
