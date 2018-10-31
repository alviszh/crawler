package com.microservice.dao.entity.crawler.bank.pabchina;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name="pabchina_userinfo",indexes = {@Index(name = "index_pabchina_userinfo_taskid", columnList = "taskid")})
public class PabChinaUserInfo extends IdEntity{
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	private static final long serialVersionUID = -7225639204374657354L;
	
	/**姓名*/ 
	@Column(name="name")
	private String name ;
	/**城市*/ 
	@Column(name="position")
	private String position ;
	
	/**卡号*/ 
	@Column(name="accNum")
	private String accNum ;
	
	/**余额*/ 
	@Column(name="rbmBalance")
	private String rbmBalance ;
	
	/**卡类型*/ 
	@Column(name="cardType")
	private String cardType ;
	
	/**开户行*/ 
	@Column(name="openAccBank")
	private String openAccBank ;
	
	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getAccNum() {
		return accNum;
	}

	public void setAccNum(String accNum) {
		this.accNum = accNum;
	}

	public String getRbmBalance() {
		return rbmBalance;
	}

	public void setRbmBalance(String rbmBalance) {
		this.rbmBalance = rbmBalance;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getOpenAccBank() {
		return openAccBank;
	}

	public void setOpenAccBank(String openAccBank) {
		this.openAccBank = openAccBank;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
}
