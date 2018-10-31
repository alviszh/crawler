package com.microservice.dao.entity.crawler.bank.cgbchina;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name="cgbchina_accounttype",indexes = {@Index(name = "index_cgbchina_accounttype_taskid", columnList = "taskid")})
public class CgbChinaAccountType  extends IdEntity{
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	private static final long serialVersionUID = -7225639204374657354L;
	
	/**卡号*/ 
	@Column(name="number")
	private String number ;
	
	/**账户总余额*/ 
	@Column(name="balance")
	private String balance ;
	
	/**余额*/ 
	@Column(name="balance2")
	private String balance2 ;
	
	/**活期余额*/ 
	@Column(name="canUseAmt")
	private String canUseAmt ;
	
	/**冻结余额*/ 
	@Column(name="frozenAmount")
	private String frozenAmount ;
	
	/**定期余额*/ 
	@Column(name="useVol")
	private String useVol ;
	
	/**账号*/ 
	@Column(name="sequeceNo")
	private String sequeceNo ;
	
	/**类型*/ 
	@Column(name="subAccountType")
	private String subAccountType ;
	
	/**币种*/ 
	@Column(name="currencyType")
	private String currencyType ;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getBalance2() {
		return balance2;
	}

	public void setBalance2(String balance2) {
		this.balance2 = balance2;
	}

	public String getCanUseAmt() {
		return canUseAmt;
	}

	public void setCanUseAmt(String canUseAmt) {
		this.canUseAmt = canUseAmt;
	}

	public String getFrozenAmount() {
		return frozenAmount;
	}

	public void setFrozenAmount(String frozenAmount) {
		this.frozenAmount = frozenAmount;
	}

	public String getUseVol() {
		return useVol;
	}

	public void setUseVol(String useVol) {
		this.useVol = useVol;
	}

	public String getSequeceNo() {
		return sequeceNo;
	}

	public void setSequeceNo(String sequeceNo) {
		this.sequeceNo = sequeceNo;
	}

	public String getSubAccountType() {
		return subAccountType;
	}

	public void setSubAccountType(String subAccountType) {
		this.subAccountType = subAccountType;
	}

	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
