package com.microservice.dao.entity.crawler.unicom;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "unicom_balance",indexes = {@Index(name = "index_unicom_balance_taskid", columnList = "taskid")})
public class UnicomBalance  extends IdEntity{

	private double  balance;//账户余额
	private double  acctBalance;//账户余额
	
	private Integer userid;

	private String taskid;

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public double getAcctBalance() {
		return acctBalance;
	}

	public void setAcctBalance(double acctBalance) {
		this.acctBalance = acctBalance;
	}




}