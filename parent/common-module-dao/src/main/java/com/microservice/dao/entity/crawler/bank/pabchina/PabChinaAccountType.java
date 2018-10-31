package com.microservice.dao.entity.crawler.bank.pabchina;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name="pabchina_accounttype",indexes = {@Index(name = "index_pabchina_accounttype_taskid", columnList = "taskid")})
public class PabChinaAccountType extends IdEntity{
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	private static final long serialVersionUID = -7225639204374657354L;
	
	/**卡号*/ 
	@Column(name="accNum")
	private String accNum ;
	
	/**账户类型*/ 
	@Column(name="accType")
	private String accType ;
	
	/**余额*/ 
	@Column(name="balance")
	private String balance ;
	
	/**币种*/ 
	@Column(name="currType")
	private String currType ;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getAccNum() {
		return accNum;
	}

	public void setAccNum(String accNum) {
		this.accNum = accNum;
	}

	public String getAccType() {
		return accType;
	}

	public void setAccType(String accType) {
		this.accType = accType;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getCurrType() {
		return currType;
	}

	public void setCurrType(String currType) {
		this.currType = currType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
