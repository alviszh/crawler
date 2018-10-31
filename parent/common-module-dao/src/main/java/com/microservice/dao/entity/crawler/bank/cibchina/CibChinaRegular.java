package com.microservice.dao.entity.crawler.bank.cibchina;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="cibchina_debitcard_regular",indexes = {@Index(name = "index_cibchina_debitcard_regular_taskid", columnList = "taskid")})
public class CibChinaRegular extends IdEntity{
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	private static final long serialVersionUID = -7225639204374657354L;
	
	/**储种*/ 
	@Column(name="kindof")
	private String kindof ;
	
	/**币种*/ 
	@Column(name="currency")
	private String currency ;
	
	/**钞汇*/ 
	@Column(name="exchange")
	private String exchange ;
	
	/**余额*/ 
	@Column(name="balance")
	private String balance ;
	
	/**可用余额*/ 
	@Column(name="avaBalance")
	private String avaBalance ;
	
	/**开户日期*/ 
	@Column(name="opening")
	private String opening ;
	
	/**存期*/ 
	@Column(name="period")
	private String period ;
	
	/**到期日期*/ 
	@Column(name="expiraDate")
	private String expiraDate ;
	
	/**续存存期*/ 
	@Column(name="continued")
	private String continued ;
	
	/**状态*/ 
	@Column(name="state")
	private String state ;

	/**卡号*/ 
	@Column(name="accNumber")
	private String accNumber;
	
	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getKindof() {
		return kindof;
	}

	public void setKindof(String kindof) {
		this.kindof = kindof;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getAvaBalance() {
		return avaBalance;
	}

	public void setAvaBalance(String avaBalance) {
		this.avaBalance = avaBalance;
	}

	public String getOpening() {
		return opening;
	}

	public void setOpening(String opening) {
		this.opening = opening;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getExpiraDate() {
		return expiraDate;
	}

	public void setExpiraDate(String expiraDate) {
		this.expiraDate = expiraDate;
	}

	public String getContinued() {
		return continued;
	}

	public void setContinued(String continued) {
		this.continued = continued;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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
