package com.microservice.dao.entity.crawler.bank.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity

@Table(name="bank_report_repayment3",indexes = {@Index(name = "index_bank_report_repayment3_taskid", columnList = "taskId")})
public class BankReportRepayment3 extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String idNumber;
	private String basicUserId;
	private String bankName;
	private String avgPayAmount3;
	private String repayRatioAvg3;
	private String lastRepayNowNum3;
	private String minpayMons3;
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
	public String getAvgPayAmount3() {
		return avgPayAmount3;
	}
	public void setAvgPayAmount3(String avgPayAmount3) {
		this.avgPayAmount3 = avgPayAmount3;
	}
	public String getRepayRatioAvg3() {
		return repayRatioAvg3;
	}
	public void setRepayRatioAvg3(String repayRatioAvg3) {
		this.repayRatioAvg3 = repayRatioAvg3;
	}
	public String getLastRepayNowNum3() {
		return lastRepayNowNum3;
	}
	public void setLastRepayNowNum3(String lastRepayNowNum3) {
		this.lastRepayNowNum3 = lastRepayNowNum3;
	}
	public String getMinpayMons3() {
		return minpayMons3;
	}
	public void setMinpayMons3(String minpayMons3) {
		this.minpayMons3 = minpayMons3;
	}
	@Override
	public String toString() {
		return "BankReportRepayment3 [taskId=" + taskId + ", idNumber=" + idNumber + ", basicUserId=" + basicUserId
				+ ", bankName=" + bankName + ", avgPayAmount3=" + avgPayAmount3 + ", repayRatioAvg3=" + repayRatioAvg3
				+ ", lastRepayNowNum3=" + lastRepayNowNum3 + ", minpayMons3=" + minpayMons3 + "]";
	}
	
	

}
