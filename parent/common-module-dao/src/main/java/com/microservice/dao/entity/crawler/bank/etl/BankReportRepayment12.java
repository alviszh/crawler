package com.microservice.dao.entity.crawler.bank.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity

@Table(name="bank_report_repayment12",indexes = {@Index(name = "index_bank_report_repayment12_taskid", columnList = "taskId")})
public class BankReportRepayment12 extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String idNumber;
	private String basicUserId;
	private String bankName;
	private String avgPayAmount12;
	private String repayRatioAvg12;
	private String lastRepayNowNum12;
	private String minpayMons12;
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
	public String getAvgPayAmount12() {
		return avgPayAmount12;
	}
	public void setAvgPayAmount12(String avgPayAmount12) {
		this.avgPayAmount12 = avgPayAmount12;
	}
	public String getRepayRatioAvg12() {
		return repayRatioAvg12;
	}
	public void setRepayRatioAvg12(String repayRatioAvg12) {
		this.repayRatioAvg12 = repayRatioAvg12;
	}
	public String getLastRepayNowNum12() {
		return lastRepayNowNum12;
	}
	public void setLastRepayNowNum12(String lastRepayNowNum12) {
		this.lastRepayNowNum12 = lastRepayNowNum12;
	}
	public String getMinpayMons12() {
		return minpayMons12;
	}
	public void setMinpayMons12(String minpayMons12) {
		this.minpayMons12 = minpayMons12;
	}
	@Override
	public String toString() {
		return "BankReportRepayment12 [taskId=" + taskId + ", idNumber=" + idNumber + ", basicUserId=" + basicUserId
				+ ", bankName=" + bankName + ", avgPayAmount12=" + avgPayAmount12 + ", repayRatioAvg12="
				+ repayRatioAvg12 + ", lastRepayNowNum12=" + lastRepayNowNum12 + ", minpayMons12=" + minpayMons12 + "]";
	}
	
}
