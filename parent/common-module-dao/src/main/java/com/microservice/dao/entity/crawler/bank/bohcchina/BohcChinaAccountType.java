package com.microservice.dao.entity.crawler.bank.bohcchina;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name="bohcchina_accounttype",indexes = {@Index(name = "index_bohcchina_accounttype_taskid", columnList = "taskid")})
public class BohcChinaAccountType  extends IdEntity{
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	private static final long serialVersionUID = -7225639204374657354L;
	
	/**账号*/ 
	@Column(name="cardNo")
	private String cardNo ;
	
	/**账户类型*/ 
	@Column(name="prductCode_Name")
	private String prductCode_Name ;
	
	/**账户类型1*/ 
	@Column(name="prductCode_Name1")
	private String prductCode_Name1 ;
	
	/**凭证种类*/ 
	@Column(name="vouchertype")
	private String vouchertype ;
	
	/**币种*/ 
	@Column(name="currencytype")
	private String currencytype ;
	
	/**可用余额*/ 
	@Column(name="usableyue")
	private String usableyue ;
	
	/**账户状态*/ 
	@Column(name="callStat")
	private String callStat ;
	
	/**开户地*/ 
	@Column(name="cityName")
	private String cityName ;
	
	/**开户时间*/ 
	@Column(name="openDate")
	private String openDate ;

	public String getOpenDate() {
		return openDate;
	}

	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getPrductCode_Name() {
		return prductCode_Name;
	}

	public void setPrductCode_Name(String prductCode_Name) {
		this.prductCode_Name = prductCode_Name;
	}

	public String getPrductCode_Name1() {
		return prductCode_Name1;
	}

	public void setPrductCode_Name1(String prductCode_Name1) {
		this.prductCode_Name1 = prductCode_Name1;
	}

	public String getVouchertype() {
		return vouchertype;
	}

	public void setVouchertype(String vouchertype) {
		this.vouchertype = vouchertype;
	}

	public String getCurrencytype() {
		return currencytype;
	}

	public void setCurrencytype(String currencytype) {
		this.currencytype = currencytype;
	}

	public String getUsableyue() {
		return usableyue;
	}

	public void setUsableyue(String usableyue) {
		this.usableyue = usableyue;
	}

	public String getCallStat() {
		return callStat;
	}

	public void setCallStat(String callStat) {
		this.callStat = callStat;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
