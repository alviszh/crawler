package com.microservice.dao.entity.crawler.bank.cibchina;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="cibchina_creditcard_userinfo",indexes = {@Index(name = "index_cibchina_creditcard_userinfo_taskid", columnList = "taskid")})
public class CibCreditcardUserInfo extends IdEntity{
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	private static final long serialVersionUID = -7225639204374657354L;
	
	/**卡号*/ 
	@Column(name="cardNumber")
	private String cardNumber ;
	
	/**持卡人姓*/ 
	@Column(name="cardHolder")
	private String cardHolder ;
	
	/**信用卡额度*/ 
	@Column(name="creditLimit")
	private String creditLimit ;
	
	/**可用余额(美元)*/ 
	@Column(name="availableLimitDollar")
	private String availableLimitDollar ;
	
	/**可用余额*/ 
	@Column(name="availableLimit")
	private String availableLimit ;
	
	/**预借现金额度(美元)*/ 
	@Column(name="cashLimitDollar")
	private String cashLimitDollar ;
	
	/**预借现金额度*/ 
	@Column(name="cashLimit")
	private String cashLimit ;
	
	/**信用额度(美元)*/ 
	@Column(name="creditLineDollar")
	private String creditLineDollar ;
	
	/**信用额度*/ 
	@Column(name="creditLine")
	private String creditLine ;
	
	/**还款日*/ 
	@Column(name="bill_date")
	private String bill_date ;

	/**邮箱*/ 
	@Column(name="email")
	private String email ;
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

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

	public String getCardHolder() {
		return cardHolder;
	}

	public void setCardHolder(String cardHolder) {
		this.cardHolder = cardHolder;
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

	public String getBill_date() {
		return bill_date;
	}

	public void setBill_date(String bill_date) {
		this.bill_date = bill_date;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
