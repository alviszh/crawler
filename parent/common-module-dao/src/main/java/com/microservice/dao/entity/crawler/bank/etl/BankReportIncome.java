package com.microservice.dao.entity.crawler.bank.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="bank_report_income",indexes = {@Index(name = "index_bank_report_income_taskid", columnList = "taskId")})
public class BankReportIncome extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String idNumber;
	private String basicUserId;
	private String bankName;
	private String creditIncomAvg3;
	private String creditIncomAvg6;
	private String creditIncomAvg12;
	private String incomeAvg3Div6;
	private String incomeAvg6Div12;
	private String minIncomeNowMons3;
	private String minIncomeNowMons6;
	private String minIncomeNowMons12;
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
	public String getCreditIncomAvg3() {
		return creditIncomAvg3;
	}
	public void setCreditIncomAvg3(String creditIncomAvg3) {
		this.creditIncomAvg3 = creditIncomAvg3;
	}
	public String getCreditIncomAvg6() {
		return creditIncomAvg6;
	}
	public void setCreditIncomAvg6(String creditIncomAvg6) {
		this.creditIncomAvg6 = creditIncomAvg6;
	}
	public String getCreditIncomAvg12() {
		return creditIncomAvg12;
	}
	public void setCreditIncomAvg12(String creditIncomAvg12) {
		this.creditIncomAvg12 = creditIncomAvg12;
	}
	public String getIncomeAvg3Div6() {
		return incomeAvg3Div6;
	}
	public void setIncomeAvg3Div6(String incomeAvg3Div6) {
		this.incomeAvg3Div6 = incomeAvg3Div6;
	}
	public String getIncomeAvg6Div12() {
		return incomeAvg6Div12;
	}
	public void setIncomeAvg6Div12(String incomeAvg6Div12) {
		this.incomeAvg6Div12 = incomeAvg6Div12;
	}
	public String getMinIncomeNowMons3() {
		return minIncomeNowMons3;
	}
	public void setMinIncomeNowMons3(String minIncomeNowMons3) {
		this.minIncomeNowMons3 = minIncomeNowMons3;
	}
	public String getMinIncomeNowMons6() {
		return minIncomeNowMons6;
	}
	public void setMinIncomeNowMons6(String minIncomeNowMons6) {
		this.minIncomeNowMons6 = minIncomeNowMons6;
	}
	public String getMinIncomeNowMons12() {
		return minIncomeNowMons12;
	}
	public void setMinIncomeNowMons12(String minIncomeNowMons12) {
		this.minIncomeNowMons12 = minIncomeNowMons12;
	}
	@Override
	public String toString() {
		return "BankReportIncome [taskId=" + taskId + ", idNumber=" + idNumber + ", basicUserId=" + basicUserId
				+ ", bankName=" + bankName + ", creditIncomAvg3=" + creditIncomAvg3 + ", creditIncomAvg6="
				+ creditIncomAvg6 + ", creditIncomAvg12=" + creditIncomAvg12 + ", incomeAvg3Div6=" + incomeAvg3Div6
				+ ", incomeAvg6Div12=" + incomeAvg6Div12 + ", minIncomeNowMons3=" + minIncomeNowMons3
				+ ", minIncomeNowMons6=" + minIncomeNowMons6 + ", minIncomeNowMons12=" + minIncomeNowMons12 + "]";
	}
		
}
