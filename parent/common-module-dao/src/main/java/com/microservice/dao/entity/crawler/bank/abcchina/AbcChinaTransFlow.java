package com.microservice.dao.entity.crawler.bank.abcchina;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name="abcchina_transflow",indexes = {@Index(name = "index_abcchina_transflow_taskid", columnList = "taskid")})
public class AbcChinaTransFlow  extends IdEntity{
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	private static final long serialVersionUID = -7225639204374657354L;

	/**账号*/ 
	@Column(name="account")
	private String account ;
	
	/**交易时间*/ 
	@Column(name="dealTime")
	private String dealTime ;
	
	/**交易金额*/ 
	@Column(name="dealMoney")
	private String dealMoney ;
	
	/**本次余额*/ 
	@Column(name="thisYue")
	private String thisYue ;
	
	/**交易类型*/ 
	@Column(name="dealType")
	private String dealType ;
	
	/**交易摘要*/ 
	@Column(name="dealDigest")
	private String dealDigest ;
	
	/**对方信息*/ 
	@Column(name="oppoMsg")
	private String oppoMsg ;
	
	/**交易渠道*/ 
	@Column(name="dealditch")
	private String dealditch ;

	public String getOppoMsg() {
		return oppoMsg;
	}

	public void setOppoMsg(String oppoMsg) {
		this.oppoMsg = oppoMsg;
	}

	public String getDealditch() {
		return dealditch;
	}

	public void setDealditch(String dealditch) {
		this.dealditch = dealditch;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getDealTime() {
		return dealTime;
	}

	public void setDealTime(String dealTime) {
		this.dealTime = dealTime;
	}

	public String getDealMoney() {
		return dealMoney;
	}

	public void setDealMoney(String dealMoney) {
		this.dealMoney = dealMoney;
	}

	public String getThisYue() {
		return thisYue;
	}

	public void setThisYue(String thisYue) {
		this.thisYue = thisYue;
	}


	public String getDealType() {
		return dealType;
	}

	public void setDealType(String dealType) {
		this.dealType = dealType;
	}

	public String getDealDigest() {
		return dealDigest;
	}

	public void setDealDigest(String dealDigest) {
		this.dealDigest = dealDigest;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
