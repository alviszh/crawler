package com.microservice.dao.entity.crawler.bank.bocom;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 交通银行-储蓄卡 卡信息
 * @author zz
 *
 */
@Entity
@Table(name="bocom_debitcard_message")
public class BocomDebitcardMsg extends IdEntity{
	
	private String taskid;
	private String cardNum;					//卡号
	private String username;				//户名
	private String alias;					//别名
	private String accountType;				//账户类型
	private String availableBalance;		//可用余额
	private String currency;				//币种
	private String balance;					//余额
	
	@Override
	public String toString() {
		return "BocomDebitcardMsg [taskid=" + taskid + ", cardNum=" + cardNum + ", username=" + username + ", alias="
				+ alias + ", accountType=" + accountType + ", availableBalance=" + availableBalance + ", currency="
				+ currency + ", balance=" + balance + "]";
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getAvailableBalance() {
		return availableBalance;
	}
	public void setAvailableBalance(String availableBalance) {
		this.availableBalance = availableBalance;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
}
