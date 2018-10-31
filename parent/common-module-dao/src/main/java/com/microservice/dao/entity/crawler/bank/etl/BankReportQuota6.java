package com.microservice.dao.entity.crawler.bank.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity

@Table(name="bank_report_quota6",indexes = {@Index(name = "index_bank_report_quota6_taskid", columnList = "taskId")})
public class BankReportQuota6 extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String idNumber;
	private String basicUserId;
	private String bankName;
	private String averageBalancePerQuotaNearly6m;
	private String propertionOfSalesAmountQuota6;
	private String propertionOfMaxSalesAmountInQuota6;
	private String propertionOfMinSalesAmountInQuota6;
	private String useageOfMaxQuota6;
	private String lastUseageMoreThan0Nearly6;
	private String avgPropertionOfConsumeWithdrawalInQuota6;
	private String propertionOfSalesAmntInQuotaMore05Num6;
	private String propertionOfSalesAmntInQuotaMore09Num6;
	private String maxPropertionOfLastUseage6;
	private String avgQuota6;
	private String maxQuota6;
	private String minQuota6;
	private String useageOfMinQuota6;
	private String averageUseageRateOfQuotaNearly6m;
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
	public String getAverageBalancePerQuotaNearly6m() {
		return averageBalancePerQuotaNearly6m;
	}
	public void setAverageBalancePerQuotaNearly6m(String averageBalancePerQuotaNearly6m) {
		this.averageBalancePerQuotaNearly6m = averageBalancePerQuotaNearly6m;
	}
	public String getPropertionOfSalesAmountQuota6() {
		return propertionOfSalesAmountQuota6;
	}
	public void setPropertionOfSalesAmountQuota6(String propertionOfSalesAmountQuota6) {
		this.propertionOfSalesAmountQuota6 = propertionOfSalesAmountQuota6;
	}
	public String getPropertionOfMaxSalesAmountInQuota6() {
		return propertionOfMaxSalesAmountInQuota6;
	}
	public void setPropertionOfMaxSalesAmountInQuota6(String propertionOfMaxSalesAmountInQuota6) {
		this.propertionOfMaxSalesAmountInQuota6 = propertionOfMaxSalesAmountInQuota6;
	}
	public String getPropertionOfMinSalesAmountInQuota6() {
		return propertionOfMinSalesAmountInQuota6;
	}
	public void setPropertionOfMinSalesAmountInQuota6(String propertionOfMinSalesAmountInQuota6) {
		this.propertionOfMinSalesAmountInQuota6 = propertionOfMinSalesAmountInQuota6;
	}
	public String getUseageOfMaxQuota6() {
		return useageOfMaxQuota6;
	}
	public void setUseageOfMaxQuota6(String useageOfMaxQuota6) {
		this.useageOfMaxQuota6 = useageOfMaxQuota6;
	}
	public String getLastUseageMoreThan0Nearly6() {
		return lastUseageMoreThan0Nearly6;
	}
	public void setLastUseageMoreThan0Nearly6(String lastUseageMoreThan0Nearly6) {
		this.lastUseageMoreThan0Nearly6 = lastUseageMoreThan0Nearly6;
	}
	public String getAvgPropertionOfConsumeWithdrawalInQuota6() {
		return avgPropertionOfConsumeWithdrawalInQuota6;
	}
	public void setAvgPropertionOfConsumeWithdrawalInQuota6(String avgPropertionOfConsumeWithdrawalInQuota6) {
		this.avgPropertionOfConsumeWithdrawalInQuota6 = avgPropertionOfConsumeWithdrawalInQuota6;
	}
	public String getPropertionOfSalesAmntInQuotaMore05Num6() {
		return propertionOfSalesAmntInQuotaMore05Num6;
	}
	public void setPropertionOfSalesAmntInQuotaMore05Num6(String propertionOfSalesAmntInQuotaMore05Num6) {
		this.propertionOfSalesAmntInQuotaMore05Num6 = propertionOfSalesAmntInQuotaMore05Num6;
	}
	public String getPropertionOfSalesAmntInQuotaMore09Num6() {
		return propertionOfSalesAmntInQuotaMore09Num6;
	}
	public void setPropertionOfSalesAmntInQuotaMore09Num6(String propertionOfSalesAmntInQuotaMore09Num6) {
		this.propertionOfSalesAmntInQuotaMore09Num6 = propertionOfSalesAmntInQuotaMore09Num6;
	}
	public String getMaxPropertionOfLastUseage6() {
		return maxPropertionOfLastUseage6;
	}
	public void setMaxPropertionOfLastUseage6(String maxPropertionOfLastUseage6) {
		this.maxPropertionOfLastUseage6 = maxPropertionOfLastUseage6;
	}
	public String getAvgQuota6() {
		return avgQuota6;
	}
	public void setAvgQuota6(String avgQuota6) {
		this.avgQuota6 = avgQuota6;
	}
	public String getMaxQuota6() {
		return maxQuota6;
	}
	public void setMaxQuota6(String maxQuota6) {
		this.maxQuota6 = maxQuota6;
	}
	public String getMinQuota6() {
		return minQuota6;
	}
	public void setMinQuota6(String minQuota6) {
		this.minQuota6 = minQuota6;
	}
	public String getUseageOfMinQuota6() {
		return useageOfMinQuota6;
	}
	public void setUseageOfMinQuota6(String useageOfMinQuota6) {
		this.useageOfMinQuota6 = useageOfMinQuota6;
	}
	public String getAverageUseageRateOfQuotaNearly6m() {
		return averageUseageRateOfQuotaNearly6m;
	}
	public void setAverageUseageRateOfQuotaNearly6m(String averageUseageRateOfQuotaNearly6m) {
		this.averageUseageRateOfQuotaNearly6m = averageUseageRateOfQuotaNearly6m;
	}
	@Override
	public String toString() {
		return "BankReportQuota6 [taskId=" + taskId + ", idNumber=" + idNumber + ", basicUserId=" + basicUserId
				+ ", bankName=" + bankName + ", averageBalancePerQuotaNearly6m=" + averageBalancePerQuotaNearly6m
				+ ", propertionOfSalesAmountQuota6=" + propertionOfSalesAmountQuota6
				+ ", propertionOfMaxSalesAmountInQuota6=" + propertionOfMaxSalesAmountInQuota6
				+ ", propertionOfMinSalesAmountInQuota6=" + propertionOfMinSalesAmountInQuota6 + ", useageOfMaxQuota6="
				+ useageOfMaxQuota6 + ", lastUseageMoreThan0Nearly6=" + lastUseageMoreThan0Nearly6
				+ ", avgPropertionOfConsumeWithdrawalInQuota6=" + avgPropertionOfConsumeWithdrawalInQuota6
				+ ", propertionOfSalesAmntInQuotaMore05Num6=" + propertionOfSalesAmntInQuotaMore05Num6
				+ ", propertionOfSalesAmntInQuotaMore09Num6=" + propertionOfSalesAmntInQuotaMore09Num6
				+ ", maxPropertionOfLastUseage6=" + maxPropertionOfLastUseage6 + ", avgQuota6=" + avgQuota6
				+ ", maxQuota6=" + maxQuota6 + ", minQuota6=" + minQuota6 + ", useageOfMinQuota6=" + useageOfMinQuota6
				+ ", averageUseageRateOfQuotaNearly6m=" + averageUseageRateOfQuotaNearly6m + "]";
	}

}
