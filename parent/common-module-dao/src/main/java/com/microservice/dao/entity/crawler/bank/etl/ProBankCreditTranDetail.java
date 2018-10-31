package com.microservice.dao.entity.crawler.bank.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="pro_bank_credit_tran_detail",indexes = {@Index(name = "index_pro_bank_credit_tran_detail_taskid", columnList = "taskId")})

public class ProBankCreditTranDetail extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String resource;
	private String amount;
	private String tranDescription;
	private String lastNumber;
	private String tranType;
	private String tranDate;
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
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getTranDescription() {
		return tranDescription;
	}
	public void setTranDescription(String tranDescription) {
		this.tranDescription = tranDescription;
	}
	public String getLastNumber() {
		return lastNumber;
	}
	public void setLastNumber(String lastNumber) {
		this.lastNumber = lastNumber;
	}
	public String getTranType() {
		return tranType;
	}
	public void setTranType(String tranType) {
		this.tranType = tranType;
	}
	public String getTranDate() {
		return tranDate;
	}
	public void setTranDate(String tranDate) {
		this.tranDate = tranDate;
	}
	@Override
	public String toString() {
		return "ProBankCreditTranDetail [taskId=" + taskId + ", resource=" + resource + ", amount=" + amount
				+ ", tranDescription=" + tranDescription + ", lastNumber=" + lastNumber + ", tranType=" + tranType
				+ ", tranDate=" + tranDate + "]";
	}
	
}
