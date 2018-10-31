package com.microservice.dao.entity.crawler.bank.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="pro_bank_credit_bill_info",indexes = {@Index(name = "index_pro_bank_credit_bill_info_taskid", columnList = "taskId")})
public class ProBankCreditBillInfo extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String resource;
	private String billDate;
	private String payDate;
	private String billMonth;
	private String billAmount;
	private String billAmountShould;
	private String billAmountMin;
	private String availableLimit;
	private String lastNumber;
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	public String getBillMonth() {
		return billMonth;
	}
	public void setBillMonth(String billMonth) {
		this.billMonth = billMonth;
	}
	public String getBillAmount() {
		return billAmount;
	}
	public void setBillAmount(String billAmount) {
		this.billAmount = billAmount;
	}
	public String getBillAmountShould() {
		return billAmountShould;
	}
	public void setBillAmountShould(String billAmountShould) {
		this.billAmountShould = billAmountShould;
	}
	public String getBillAmountMin() {
		return billAmountMin;
	}
	public void setBillAmountMin(String billAmountMin) {
		this.billAmountMin = billAmountMin;
	}
	public String getAvailableLimit() {
		return availableLimit;
	}
	public void setAvailableLimit(String availableLimit) {
		this.availableLimit = availableLimit;
	}
	public String getLastNumber() {
		return lastNumber;
	}
	public void setLastNumber(String lastNumber) {
		this.lastNumber = lastNumber;
	}
	@Override
	public String toString() {
		return "ProBankCreditBillInfo [taskId=" + taskId + ", resource=" + resource + ", billDate=" + billDate
				+ ", payDate=" + payDate + ", billMonth=" + billMonth + ", billAmount=" + billAmount
				+ ", billAmountShould=" + billAmountShould + ", billAmountMin=" + billAmountMin + ", availableLimit="
				+ availableLimit + ", lastNumber=" + lastNumber + "]";
	}
	
	
		
}
