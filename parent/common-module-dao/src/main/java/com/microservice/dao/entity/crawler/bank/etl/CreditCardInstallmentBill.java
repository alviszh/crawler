package com.microservice.dao.entity.crawler.bank.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="credit_card_installment_bill") //交易明细
public class CreditCardInstallmentBill extends IdEntity implements Serializable{

	private static final long serialVersionUID = -3207075026659390860L;
	
	@Column(name="task_id")
	private String taskId; //唯一标识
	private String cardNumber; //卡号
	private String lastNumber; //卡号后四位
	private String totalMoney; //分期总金额
	private String InstallmentBillDesc; //分期账单描述
	private String InstallmentBillPoundageDesc; //分期账单手续费描述
	private String InstallmentBillTransferPoundageDesc; //分期账单转账手续费描述
	private String InstallmentType; //分期类型
	private String InstallmentSum; //总分期数
	private String InstallmentCurrent; //当前期数
	private String currency; //币种
	private String currentFee; //本期金额
	private String TransferPoundage; //转账手续费
	
	@JsonBackReference
	private String resource; //溯源字段

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getLastNumber() {
		return lastNumber;
	}

	public void setLastNumber(String lastNumber) {
		this.lastNumber = lastNumber;
	}

	public String getInstallmentBillDesc() {
		return InstallmentBillDesc;
	}

	public void setInstallmentBillDesc(String installmentBillDesc) {
		InstallmentBillDesc = installmentBillDesc;
	}

	public String getInstallmentBillPoundageDesc() {
		return InstallmentBillPoundageDesc;
	}

	public void setInstallmentBillPoundageDesc(String installmentBillPoundageDesc) {
		InstallmentBillPoundageDesc = installmentBillPoundageDesc;
	}

	public String getInstallmentBillTransferPoundageDesc() {
		return InstallmentBillTransferPoundageDesc;
	}

	public void setInstallmentBillTransferPoundageDesc(String installmentBillTransferPoundageDesc) {
		InstallmentBillTransferPoundageDesc = installmentBillTransferPoundageDesc;
	}

	public String getInstallmentType() {
		return InstallmentType;
	}

	public void setInstallmentType(String installmentType) {
		InstallmentType = installmentType;
	}

	public String getInstallmentSum() {
		return InstallmentSum;
	}

	public void setInstallmentSum(String installmentSum) {
		InstallmentSum = installmentSum;
	}

	public String getInstallmentCurrent() {
		return InstallmentCurrent;
	}

	public void setInstallmentCurrent(String installmentCurrent) {
		InstallmentCurrent = installmentCurrent;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCurrentFee() {
		return currentFee;
	}

	public void setCurrentFee(String currentFee) {
		this.currentFee = currentFee;
	}

	public String getTransferPoundage() {
		return TransferPoundage;
	}

	public void setTransferPoundage(String transferPoundage) {
		TransferPoundage = transferPoundage;
	}
	
	@Column(columnDefinition="text")
	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

	@Override
	public String toString() {
		return "CreditCardInstallmentBill [taskId=" + taskId + ", cardNumber=" + cardNumber + ", lastNumber="
				+ lastNumber + ", totalMoney=" + totalMoney + ", InstallmentBillDesc=" + InstallmentBillDesc
				+ ", InstallmentBillPoundageDesc=" + InstallmentBillPoundageDesc
				+ ", InstallmentBillTransferPoundageDesc=" + InstallmentBillTransferPoundageDesc + ", InstallmentType="
				+ InstallmentType + ", InstallmentSum=" + InstallmentSum + ", InstallmentCurrent=" + InstallmentCurrent
				+ ", currency=" + currency + ", currentFee=" + currentFee + ", TransferPoundage=" + TransferPoundage
				+ ", resource=" + resource + "]";
	}

	

	
}
