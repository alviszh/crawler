package com.microservice.dao.entity.crawler.bank.citicchina;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="citicchina_creditcard_account",indexes = {@Index(name = "index_citicchina_creditcard_account_taskid", columnList = "taskid")}) 
public class CiticChinaCreditCardAccount extends IdEntity{

	private String description;//交易描述
	
	private String moneyStatus;//交易币种/金额
	
	private String datea;//交易日期
	
	private String sum;//结算货币/金额
	
	private String getDatea;//入账日期
	
	private String idCard;//卡号
	
	private String taskid;

	private String accountType;//账单类型
	
	
	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	@Override
	public String toString() {
		return "CiticChinaCreditCardAccount [description=" + description + ", moneyStatus=" + moneyStatus + ", datea="
				+ datea + ", sum=" + sum + ", getDatea=" + getDatea + ", idCard=" + idCard + ", taskid=" + taskid
				+ ", accountType=" + accountType + "]";
	}

	@Column(columnDefinition="text")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMoneyStatus() {
		return moneyStatus;
	}

	public void setMoneyStatus(String moneyStatus) {
		this.moneyStatus = moneyStatus;
	}

	public String getDatea() {
		return datea;
	}

	public void setDatea(String datea) {
		this.datea = datea;
	}

	public String getSum() {
		return sum;
	}

	public void setSum(String sum) {
		this.sum = sum;
	}

	public String getGetDatea() {
		return getDatea;
	}

	public void setGetDatea(String getDatea) {
		this.getDatea = getDatea;
	}
	@Column(columnDefinition="text")
	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	
}
