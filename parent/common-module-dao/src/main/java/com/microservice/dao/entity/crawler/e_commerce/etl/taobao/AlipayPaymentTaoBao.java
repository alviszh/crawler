package com.microservice.dao.entity.crawler.e_commerce.etl.taobao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="alipay_payment_taobao") //交易明细
public class AlipayPaymentTaoBao extends IdEntity implements Serializable {

	private static final long serialVersionUID = -3207075026659390860L;
	
	@Column(name="task_id")
	private String taskId; //唯一标识
	private String payTime;
	private String transType;
	private String transAccountType;
	private String transNumber;
	private String oppsitePerson;
	private String transAmount;
	private String transStatus;
	private String alipayAccount;
	@JsonBackReference
	private String resource; //溯源字段
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	public String getTransAccountType() {
		return transAccountType;
	}
	public void setTransAccountType(String transAccountType) {
		this.transAccountType = transAccountType;
	}
	public String getTransNumber() {
		return transNumber;
	}
	public void setTransNumber(String transNumber) {
		this.transNumber = transNumber;
	}
	public String getOppsitePerson() {
		return oppsitePerson;
	}
	public void setOppsitePerson(String oppsitePerson) {
		this.oppsitePerson = oppsitePerson;
	}
	public String getTransAmount() {
		return transAmount;
	}
	public void setTransAmount(String transAmount) {
		this.transAmount = transAmount;
	}
	public String getTransStatus() {
		return transStatus;
	}
	public void setTransStatus(String transStatus) {
		this.transStatus = transStatus;
	}
	public String getAlipayAccount() {
		return alipayAccount;
	}
	public void setAlipayAccount(String alipayAccount) {
		this.alipayAccount = alipayAccount;
	}
	
	@Column(columnDefinition="text")
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	
	@Override
	public String toString() {
		return "AlipayPaymentTaoBao [taskId=" + taskId + ", payTime=" + payTime + ", transType=" + transType
				+ ", transAccountType=" + transAccountType + ", transNumber=" + transNumber + ", oppsitePerson="
				+ oppsitePerson + ", transAmount=" + transAmount + ", transStatus=" + transStatus + ", alipayAccount="
				+ alipayAccount + ", resource=" + resource + "]";
	}
	
	
}
