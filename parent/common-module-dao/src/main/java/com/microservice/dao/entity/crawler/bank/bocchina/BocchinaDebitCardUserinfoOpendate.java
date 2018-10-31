/**
  * Copyright 2017 bejson.com 
  */
package com.microservice.dao.entity.crawler.bank.bocchina;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.microservice.dao.entity.IdEntity;

/**
 * Auto-generated: 2017-10-31 15:4:39
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Entity
@Table(name = "bocchina_debitcard_opendate",indexes = {@Index(name = "index_bocchina_debitcard_opendate_taskid", columnList = "taskid")})
public class BocchinaDebitCardUserinfoOpendate extends IdEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return "UserinfoOpendate [openDate=" + openDate + ", accountIbkNum=" + accountIbkNum + ", accountType="
				+ accountType + ", accountStatus=" + accountStatus + ", openBank=" + openBank + ", accountDetaiList="
				+ accountDetaiList + "]";
	}

	private String openDate;//开户日期
	private String accountIbkNum;//"40142" 
	private String accountType;//"119"
	private String accountStatus;//"00"
	private String openBank;//开户地点
	
	/*	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userinfobalance_id")
	@JsonIgnore
	public BocchinaDebitCardUserinfoOpendate getBocchinaDebitCardUserinfoOpendate() {
		return bocchinaDebitCardUserinfoOpendate;
	}

	public void setBocchinaDebitCardUserinfoOpendate(BocchinaDebitCardUserinfoOpendate bocchinaDebitCardUserinfoOpendate) {
		this.bocchinaDebitCardUserinfoOpendate = bocchinaDebitCardUserinfoOpendate;
	}*/


	@JsonBackReference
	private List<BocchinaDebitCardUserInfoBalance> accountDetaiList;
		
	private String taskid;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	

	public String getOpenDate() {
		return openDate;
	}

	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}

	public void setAccountIbkNum(String accountIbkNum) {
		this.accountIbkNum = accountIbkNum;
	}

	public String getAccountIbkNum() {
		return accountIbkNum;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setOpenBank(String openBank) {
		this.openBank = openBank;
	}

	public String getOpenBank() {
		return openBank;
	}

	public void setAccountDetaiList(List<BocchinaDebitCardUserInfoBalance> accountDetaiList) {
		this.accountDetaiList = accountDetaiList;
	}

	@Transient   
	public List<BocchinaDebitCardUserInfoBalance> getAccountDetaiList() {
		return accountDetaiList;
	}

}