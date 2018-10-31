package com.microservice.dao.entity.crawler.bank.cibchina;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="cibchina_creditcard_installment_bill",indexes = {@Index(name = "index_cibchina_creditcard_installment_bill_info_taskid", columnList = "taskid")})
public class CibCreditcardInstallment extends IdEntity{
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	private static final long serialVersionUID = -7225639204374657354L;
	
	/**卡号*/ 
	@Column(name="cardNumber")
	private String cardNumber ;
	
	/**分期日期*/ 
	@Column(name="date")
	private String date ;
	
	/**币种*/ 
	@Column(name="currency")
	private String currency ;
	
	/**分期金额*/ 
	@Column(name="amount")
	private String amount ;
	
	/**分期期数*/ 
	@Column(name="number")
	private String number ;
	
	/**手续费收取方式*/ 
	@Column(name="feeCollection")
	private String feeCollection ;
	
	/**每月应还本金*/ 
	@Column(name="principal")
	private String principal ;
	
	/**剩余未还本金*/ 
	@Column(name="remaining ")
	private String remaining  ;
	
	/**分期状态*/ 
	@Column(name="state ")
	private String state  ;
	
	/**摘要*/ 
	@Column(name="abstracts")
	private String abstracts ;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getFeeCollection() {
		return feeCollection;
	}

	public void setFeeCollection(String feeCollection) {
		this.feeCollection = feeCollection;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getRemaining() {
		return remaining;
	}

	public void setRemaining(String remaining) {
		this.remaining = remaining;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getAbstracts() {
		return abstracts;
	}

	public void setAbstracts(String abstracts) {
		this.abstracts = abstracts;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
