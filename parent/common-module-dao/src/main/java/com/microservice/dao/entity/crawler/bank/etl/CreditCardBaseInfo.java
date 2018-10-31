package com.microservice.dao.entity.crawler.bank.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="credit_card_base_info") //交易明细
public class CreditCardBaseInfo extends IdEntity implements Serializable{

	private static final long serialVersionUID = -3207075026659390860L;
	
	@Column(name="task_id")
	private String taskId; //唯一标识
	private String bankName; //银行名称
	private String cardNumber; //卡号
	private String lastNumber; //卡号后四位
	private String cardHolder; //持卡人姓名
	private String idNumber; //身份证号
	private String creditLimitDollar; //信用卡额度(美元)
	private String creditLimit; //信用卡额度
	private String availableLimitDollar; //可用余额(美元)
	private String availableLimit; //可用余额
	private String cashLimitDollar; //预借现金额度(美元)
	private String cashLimit; //预借现金额度
	private String creditLineDollar; //信用额度(美元)
	private String creditLine; //信用额度
	private String email; //邮箱
	private String billDate; //还款日
	
	@JsonBackReference
	private String resource; //溯源字段

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
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

	public String getCardHolder() {
		return cardHolder;
	}

	public void setCardHolder(String cardHolder) {
		this.cardHolder = cardHolder;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getCreditLimitDollar() {
		return creditLimitDollar;
	}

	public void setCreditLimitDollar(String creditLimitDollar) {
		this.creditLimitDollar = creditLimitDollar;
	}

	public String getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(String creditLimit) {
		this.creditLimit = creditLimit;
	}

	public String getAvailableLimitDollar() {
		return availableLimitDollar;
	}

	public void setAvailableLimitDollar(String availableLimitDollar) {
		this.availableLimitDollar = availableLimitDollar;
	}

	public String getAvailableLimit() {
		return availableLimit;
	}

	public void setAvailableLimit(String availableLimit) {
		this.availableLimit = availableLimit;
	}

	public String getCashLimitDollar() {
		return cashLimitDollar;
	}

	public void setCashLimitDollar(String cashLimitDollar) {
		this.cashLimitDollar = cashLimitDollar;
	}

	public String getCashLimit() {
		return cashLimit;
	}

	public void setCashLimit(String cashLimit) {
		this.cashLimit = cashLimit;
	}

	public String getCreditLineDollar() {
		return creditLineDollar;
	}

	public void setCreditLineDollar(String creditLineDollar) {
		this.creditLineDollar = creditLineDollar;
	}

	public String getCreditLine() {
		return creditLine;
	}

	public void setCreditLine(String creditLine) {
		this.creditLine = creditLine;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
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
		return "CreditCardBaseInfo [taskId=" + taskId + ", bankName=" + bankName + ", cardNumber=" + cardNumber
				+ ", lastNumber=" + lastNumber + ", cardHolder=" + cardHolder + ", idNumber=" + idNumber
				+ ", creditLimitDollar=" + creditLimitDollar + ", creditLimit=" + creditLimit
				+ ", availableLimitDollar=" + availableLimitDollar + ", availableLimit=" + availableLimit
				+ ", cashLimitDollar=" + cashLimitDollar + ", cashLimit=" + cashLimit + ", creditLineDollar="
				+ creditLineDollar + ", creditLine=" + creditLine + ", email=" + email + ", billDate=" + billDate
				+ ", resource=" + resource + "]";
	}

	
	
}
