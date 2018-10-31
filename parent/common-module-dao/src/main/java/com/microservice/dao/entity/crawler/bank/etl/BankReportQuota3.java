package com.microservice.dao.entity.crawler.bank.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity

@Table(name="bank_report_quota3",indexes = {@Index(name = "index_bank_report_quota3_taskid", columnList = "taskId")})
public class BankReportQuota3 extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String idNumber;
	private String basicUserId;
	private String bankName;
	private String averageBalancePerQuotaNearly3m;
	private String propertionOfSalesAmountQuota3;
	private String propertionOfMaxSalesAmountInQuota3;
	private String propertionOfMinSalesAmountInQuota3;
	private String useageOfMaxQuota3;
	private String lastUseageMoreThan0Nearly3;
	private String avgPropertionOfConsumeWithdrawalInQuota3;
	private String propertionOfSalesAmntInQuotaMore05Num3;
	private String propertionOfSalesAmntInQuotaMore09Num3;
	private String maxPropertionOfLastUseage3;
	private String avgQuota3;
	private String maxQuota3;
	private String minQuota3;
	private String useageOfMinQuota3;
	private String averageUseageRateOfQuotaNearly3m;
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
	public String getAverageBalancePerQuotaNearly3m() {
		return averageBalancePerQuotaNearly3m;
	}
	public void setAverageBalancePerQuotaNearly3m(String averageBalancePerQuotaNearly3m) {
		this.averageBalancePerQuotaNearly3m = averageBalancePerQuotaNearly3m;
	}
	public String getPropertionOfSalesAmountQuota3() {
		return propertionOfSalesAmountQuota3;
	}
	public void setPropertionOfSalesAmountQuota3(String propertionOfSalesAmountQuota3) {
		this.propertionOfSalesAmountQuota3 = propertionOfSalesAmountQuota3;
	}
	public String getPropertionOfMaxSalesAmountInQuota3() {
		return propertionOfMaxSalesAmountInQuota3;
	}
	public void setPropertionOfMaxSalesAmountInQuota3(String propertionOfMaxSalesAmountInQuota3) {
		this.propertionOfMaxSalesAmountInQuota3 = propertionOfMaxSalesAmountInQuota3;
	}
	public String getPropertionOfMinSalesAmountInQuota3() {
		return propertionOfMinSalesAmountInQuota3;
	}
	public void setPropertionOfMinSalesAmountInQuota3(String propertionOfMinSalesAmountInQuota3) {
		this.propertionOfMinSalesAmountInQuota3 = propertionOfMinSalesAmountInQuota3;
	}
	public String getUseageOfMaxQuota3() {
		return useageOfMaxQuota3;
	}
	public void setUseageOfMaxQuota3(String useageOfMaxQuota3) {
		this.useageOfMaxQuota3 = useageOfMaxQuota3;
	}
	public String getLastUseageMoreThan0Nearly3() {
		return lastUseageMoreThan0Nearly3;
	}
	public void setLastUseageMoreThan0Nearly3(String lastUseageMoreThan0Nearly3) {
		this.lastUseageMoreThan0Nearly3 = lastUseageMoreThan0Nearly3;
	}
	public String getAvgPropertionOfConsumeWithdrawalInQuota3() {
		return avgPropertionOfConsumeWithdrawalInQuota3;
	}
	public void setAvgPropertionOfConsumeWithdrawalInQuota3(String avgPropertionOfConsumeWithdrawalInQuota3) {
		this.avgPropertionOfConsumeWithdrawalInQuota3 = avgPropertionOfConsumeWithdrawalInQuota3;
	}
	public String getPropertionOfSalesAmntInQuotaMore05Num3() {
		return propertionOfSalesAmntInQuotaMore05Num3;
	}
	public void setPropertionOfSalesAmntInQuotaMore05Num3(String propertionOfSalesAmntInQuotaMore05Num3) {
		this.propertionOfSalesAmntInQuotaMore05Num3 = propertionOfSalesAmntInQuotaMore05Num3;
	}
	public String getPropertionOfSalesAmntInQuotaMore09Num3() {
		return propertionOfSalesAmntInQuotaMore09Num3;
	}
	public void setPropertionOfSalesAmntInQuotaMore09Num3(String propertionOfSalesAmntInQuotaMore09Num3) {
		this.propertionOfSalesAmntInQuotaMore09Num3 = propertionOfSalesAmntInQuotaMore09Num3;
	}
	public String getMaxPropertionOfLastUseage3() {
		return maxPropertionOfLastUseage3;
	}
	public void setMaxPropertionOfLastUseage3(String maxPropertionOfLastUseage3) {
		this.maxPropertionOfLastUseage3 = maxPropertionOfLastUseage3;
	}
	public String getAvgQuota3() {
		return avgQuota3;
	}
	public void setAvgQuota3(String avgQuota3) {
		this.avgQuota3 = avgQuota3;
	}
	public String getMaxQuota3() {
		return maxQuota3;
	}
	public void setMaxQuota3(String maxQuota3) {
		this.maxQuota3 = maxQuota3;
	}
	public String getMinQuota3() {
		return minQuota3;
	}
	public void setMinQuota3(String minQuota3) {
		this.minQuota3 = minQuota3;
	}
	public String getUseageOfMinQuota3() {
		return useageOfMinQuota3;
	}
	public void setUseageOfMinQuota3(String useageOfMinQuota3) {
		this.useageOfMinQuota3 = useageOfMinQuota3;
	}
	public String getAverageUseageRateOfQuotaNearly3m() {
		return averageUseageRateOfQuotaNearly3m;
	}
	public void setAverageUseageRateOfQuotaNearly3m(String averageUseageRateOfQuotaNearly3m) {
		this.averageUseageRateOfQuotaNearly3m = averageUseageRateOfQuotaNearly3m;
	}
	@Override
	public String toString() {
		return "BankReportQuota3 [taskId=" + taskId + ", idNumber=" + idNumber + ", basicUserId=" + basicUserId
				+ ", bankName=" + bankName + ", averageBalancePerQuotaNearly3m=" + averageBalancePerQuotaNearly3m
				+ ", propertionOfSalesAmountQuota3=" + propertionOfSalesAmountQuota3
				+ ", propertionOfMaxSalesAmountInQuota3=" + propertionOfMaxSalesAmountInQuota3
				+ ", propertionOfMinSalesAmountInQuota3=" + propertionOfMinSalesAmountInQuota3 + ", useageOfMaxQuota3="
				+ useageOfMaxQuota3 + ", lastUseageMoreThan0Nearly3=" + lastUseageMoreThan0Nearly3
				+ ", avgPropertionOfConsumeWithdrawalInQuota3=" + avgPropertionOfConsumeWithdrawalInQuota3
				+ ", propertionOfSalesAmntInQuotaMore05Num3=" + propertionOfSalesAmntInQuotaMore05Num3
				+ ", propertionOfSalesAmntInQuotaMore09Num3=" + propertionOfSalesAmntInQuotaMore09Num3
				+ ", maxPropertionOfLastUseage3=" + maxPropertionOfLastUseage3 + ", avgQuota3=" + avgQuota3
				+ ", maxQuota3=" + maxQuota3 + ", minQuota3=" + minQuota3 + ", useageOfMinQuota3=" + useageOfMinQuota3
				+ ", averageUseageRateOfQuotaNearly3m=" + averageUseageRateOfQuotaNearly3m + "]";
	}
	
	
}
