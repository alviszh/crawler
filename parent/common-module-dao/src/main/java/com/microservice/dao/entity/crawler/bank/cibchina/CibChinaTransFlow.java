package com.microservice.dao.entity.crawler.bank.cibchina;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name="cibchina_debitcard_transflow",indexes = {@Index(name = "index_cibchina_debitcard_transflow_taskid", columnList = "taskid")})
public class CibChinaTransFlow extends IdEntity{
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	private static final long serialVersionUID = -7225639204374657354L;
	
	/**交易时间*/ 
	@Column(name="dealTime")
	private String dealTime ;
	
	/**记账日*/ 
	@Column(name="billingDay")
	private String billingDay;
	
	/**收入*/ 
	@Column(name="shiftTo")
	private String shiftTo ;
	
	/**支出*/ 
	@Column(name="rollOut")
	private String rollOut ;
	
	/**账户余额*/ 
	@Column(name="yue")
	private String yue ;
	
	/**摘要*/ 
	@Column(name="digest")
	private String digest ;
	
	/**对方户名*/ 
	@Column(name="oppName")
	private String oppName ;
	
	/**对方银行*/ 
	@Column(name="oppBank")
	private String oppBank ;
	
	/**对方账号*/ 
	@Column(name="oppNumber")
	private String oppNumber ;
	
	/**用途*/ 
	@Column(name="purpose")
	private String purpose ;
	
	/**交易渠道*/ 
	@Column(name="channel")
	private String channel ;
	
	
	public String getTaskid() {
		return taskid;
	}

	/**账户账号*/ 
	@Column(name="accNumber")
	private String accNumber ;
	
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getDealTime() {
		return dealTime;
	}

	public void setDealTime(String dealTime) {
		this.dealTime = dealTime;
	}

	public String getBillingDay() {
		return billingDay;
	}

	public void setBillingDay(String billingDay) {
		this.billingDay = billingDay;
	}

	public String getShiftTo() {
		return shiftTo;
	}

	public void setShiftTo(String shiftTo) {
		this.shiftTo = shiftTo;
	}

	public String getRollOut() {
		return rollOut;
	}

	public void setRollOut(String rollOut) {
		this.rollOut = rollOut;
	}

	public String getYue() {
		return yue;
	}

	public void setYue(String yue) {
		this.yue = yue;
	}

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}

	public String getOppName() {
		return oppName;
	}

	public void setOppName(String oppName) {
		this.oppName = oppName;
	}

	public String getOppBank() {
		return oppBank;
	}

	public void setOppBank(String oppBank) {
		this.oppBank = oppBank;
	}

	public String getOppNumber() {
		return oppNumber;
	}

	public void setOppNumber(String oppNumber) {
		this.oppNumber = oppNumber;
	}
	
	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getAccNumber() {
		return accNumber;
	}

	public void setAccNumber(String accNumber) {
		this.accNumber = accNumber;
	}
	
}
