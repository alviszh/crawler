package com.microservice.dao.entity.crawler.bank.bocom;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

/**
 * @Des 交通银行 - 储蓄卡 - 流水表
 * @author zz
 *
 */
@Entity
@Table(name="bocom_debitcard_transflow")
public class BocomDebitcardTransFlow extends IdEntity{
	
	private String taskid;
	private String transDate;				//交易时间 
	private String transType;				//交易类型
	private String currency;				//币种
	private String expend;					//支出金额
	private String income;					//收入金额
	private String balance;					//余额
	private String transSite;				//交易地点
	private String cardNum;					//卡号
	
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getTransDate() {
		return transDate;
	}
	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getExpend() {
		return expend;
	}
	public void setExpend(String expend) {
		this.expend = expend;
	}
	public String getIncome() {
		return income;
	}
	public void setIncome(String income) {
		this.income = income;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getTransSite() {
		return transSite;
	}
	public void setTransSite(String transSite) {
		this.transSite = transSite;
	}

}
