package com.microservice.dao.entity.crawler.bank.cibchina;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="cibchina_creditcard_trans_detail",indexes = {@Index(name = "index_cibchina_creditcard_trans_detail_taskid", columnList = "taskid")})
public class CibCreditcardTransFlow extends IdEntity{
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	private static final long serialVersionUID = -7225639204374657354L;
	
	/**卡号*/ 
	@Column(name="cardNumber")
	private String cardNumber ;
	
	/**卡号后四位*/ 
	@Column(name="lastNumber")
	private String lastNumber ;
	
	/**记账日期*/ 
	@Column(name="chargeDate")
	private String chargeDate ;
	
	/**交易日期*/ 
	@Column(name="tranDate")
	private String tranDate ;
	
	/**花费*/ 
	@Column(name="fee")
	private String fee ;
	
	/**币种*/ 
	@Column(name="currency")
	private String currency ;
	
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

	public String getLastNumber() {
		return lastNumber;
	}

	public void setLastNumber(String lastNumber) {
		this.lastNumber = lastNumber;
	}

	public String getChargeDate() {
		return chargeDate;
	}

	public void setChargeDate(String chargeDate) {
		this.chargeDate = chargeDate;
	}

	public String getTranDate() {
		return tranDate;
	}

	public void setTranDate(String tranDate) {
		this.tranDate = tranDate;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
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
