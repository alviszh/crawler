package com.microservice.dao.entity.crawler.bank.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity

@Table(name="bank_report_repayment6",indexes = {@Index(name = "index_bank_report_repayment6_taskid", columnList = "taskId")})
public class BankReportRepayment6 extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String idNumber;
	private String basicUserId;
	private String bankName;
	private String avgPayAmount6;
	private String repayRatioAvg6;
	private String lastRepayNowNum6;
	private String minpayMons6;
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
	public String getAvgPayAmount6() {
		return avgPayAmount6;
	}
	public void setAvgPayAmount6(String avgPayAmount6) {
		this.avgPayAmount6 = avgPayAmount6;
	}
	public String getRepayRatioAvg6() {
		return repayRatioAvg6;
	}
	public void setRepayRatioAvg6(String repayRatioAvg6) {
		this.repayRatioAvg6 = repayRatioAvg6;
	}
	public String getLastRepayNowNum6() {
		return lastRepayNowNum6;
	}
	public void setLastRepayNowNum6(String lastRepayNowNum6) {
		this.lastRepayNowNum6 = lastRepayNowNum6;
	}
	public String getMinpayMons6() {
		return minpayMons6;
	}
	public void setMinpayMons6(String minpayMons6) {
		this.minpayMons6 = minpayMons6;
	}
	@Override
	public String toString() {
		return "BankReportRepayment6 [taskId=" + taskId + ", idNumber=" + idNumber + ", basicUserId=" + basicUserId
				+ ", bankName=" + bankName + ", avgPayAmount6=" + avgPayAmount6 + ", repayRatioAvg6=" + repayRatioAvg6
				+ ", lastRepayNowNum6=" + lastRepayNowNum6 + ", minpayMons6=" + minpayMons6 + "]";
	}
	
}
