package com.microservice.dao.entity.crawler.bank.abcchina;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name="abcchina_accounttype",indexes = {@Index(name = "index_abcchina_accounttype_taskid", columnList = "taskid")})
public class AbcChinaAccountType  extends IdEntity{
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	private static final long serialVersionUID = -7225639204374657354L;
	
	/**账户种类*/ 
	@Column(name="accounttype")
	private String accounttype ;
	
	/**币种*/ 
	@Column(name="currency")
	private String currency ;
	
	/**钞汇标志*/ 
	@Column(name="speculate")
	private String speculate ;
	
	/**开户日期*/ 
	@Column(name="opendate")
	private String opendate ;
	
	/**存期*/ 
	@Column(name="deposit")
	private String deposit ;
	
	/**可用余额*/ 
	@Column(name="usableyue")
	private String usableyue ;
	
	/**余额*/ 
	@Column(name="yue")
	private String yue ;
	
	/**账户状态*/ 
	@Column(name="accountstatus")
	private String accountstatus ;
	
	/**账户状态含义*/ 
	@Column(name="accountstatusmeaning")
	private String accountstatusmeaning ;
	
	/**年利率*/ 
	@Column(name="annual")
	private String annual ;
	
	/**到期日期*/ 
	@Column(name="datedue")
	private String datedue ;
	
	/**存期处理*/ 
	@Column(name="depositdispose")
	private String depositdispose ;
	
	/**账户类型名*/ 
	@Column(name="accounttypename")
	private String accounttypename ;
	
	/**币种名*/ 
	@Column(name="currencyname")
	private String currencyname ;
	
	/**钞汇标志名*/ 
	@Column(name="speculatename")
	private String speculatename ;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getAccounttype() {
		return accounttype;
	}

	public void setAccounttype(String accounttype) {
		this.accounttype = accounttype;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getSpeculate() {
		return speculate;
	}

	public void setSpeculate(String speculate) {
		this.speculate = speculate;
	}

	public String getOpendate() {
		return opendate;
	}

	public void setOpendate(String opendate) {
		this.opendate = opendate;
	}

	public String getDeposit() {
		return deposit;
	}

	public void setDeposit(String deposit) {
		this.deposit = deposit;
	}

	public String getUsableyue() {
		return usableyue;
	}

	public void setUsableyue(String usableyue) {
		this.usableyue = usableyue;
	}

	public String getYue() {
		return yue;
	}

	public void setYue(String yue) {
		this.yue = yue;
	}

	public String getAccountstatus() {
		return accountstatus;
	}

	public void setAccountstatus(String accountstatus) {
		this.accountstatus = accountstatus;
	}

	public String getAccountstatusmeaning() {
		return accountstatusmeaning;
	}

	public void setAccountstatusmeaning(String accountstatusmeaning) {
		this.accountstatusmeaning = accountstatusmeaning;
	}

	public String getAnnual() {
		return annual;
	}

	public void setAnnual(String annual) {
		this.annual = annual;
	}

	public String getDatedue() {
		return datedue;
	}

	public void setDatedue(String datedue) {
		this.datedue = datedue;
	}

	public String getDepositdispose() {
		return depositdispose;
	}

	public void setDepositdispose(String depositdispose) {
		this.depositdispose = depositdispose;
	}

	public String getAccounttypename() {
		return accounttypename;
	}

	public void setAccounttypename(String accounttypename) {
		this.accounttypename = accounttypename;
	}

	public String getCurrencyname() {
		return currencyname;
	}

	public void setCurrencyname(String currencyname) {
		this.currencyname = currencyname;
	}

	public String getSpeculatename() {
		return speculatename;
	}

	public void setSpeculatename(String speculatename) {
		this.speculatename = speculatename;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
