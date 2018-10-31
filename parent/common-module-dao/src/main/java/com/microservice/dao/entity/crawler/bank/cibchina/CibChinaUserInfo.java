package com.microservice.dao.entity.crawler.bank.cibchina;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="cibchina_debitcard_userinfo",indexes = {@Index(name = "index_cibchina_debitcard_userinfo_taskid", columnList = "taskid")})
public class CibChinaUserInfo extends IdEntity{
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	private static final long serialVersionUID = -7225639204374657354L;
	
	/**设置账户别名*/ 
	@Column(name="alias")
	private String alias ;
	
	/**账户户名*/ 
	@Column(name="name")
	private String name ;
	
	/**账户账号*/ 
	@Column(name="accNumber")
	private String accNumber ;
	
	/**账户类型*/ 
	@Column(name="type")
	private String type ;
	
	/**开户机构*/ 
	@Column(name="mechanism")
	private String mechanism ;

	/**开户日期*/ 
	@Column(name="opening")
	private String opening ;
	
	/**余额汇总*/ 
	@Column(name="balance")
	private String balance ;
	
	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccNumber() {
		return accNumber;
	}

	public void setAccNumber(String accNumber) {
		this.accNumber = accNumber;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMechanism() {
		return mechanism;
	}

	public void setMechanism(String mechanism) {
		this.mechanism = mechanism;
	}
	
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getOpening() {
		return opening;
	}

	public void setOpening(String opening) {
		this.opening = opening;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

}
