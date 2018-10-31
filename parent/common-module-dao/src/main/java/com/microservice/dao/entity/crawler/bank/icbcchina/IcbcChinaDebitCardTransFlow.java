package com.microservice.dao.entity.crawler.bank.icbcchina;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="icbcchina_debitcard_transflow" ,indexes = {@Index(name = "index_icbcchina_debitcard_transflow_taskid", columnList = "taskid")})
public class IcbcChinaDebitCardTransFlow extends IdEntity{
	private String taskid;
	private String cardNum;									//卡号
	private String cardType;								//卡类型（借记卡，信用卡等）
	private String transDate;								//交易日期
	private String stract;									//摘要
	private String transPlace;								//交易场所
	private String transCountry;							//交易国家
	private String currency;								//钞/汇
	private String transAmountIn;							//交易金额(收入)
	private String transAmountOut;							//交易金额(支出)
	private String transMoneyType;							//交易币种
	private String accountIn;								//记账金额(收入)
	private String accountOut;								//记账金额(支出)
	private String accountMoneyType;						//记账币种
	private String balance;									//余额
	private String oppositeName;							//对方户名
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
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getTransDate() {
		return transDate;
	}
	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}
	public String getStract() {
		return stract;
	}
	public void setStract(String stract) {
		this.stract = stract;
	}
	public String getTransPlace() {
		return transPlace;
	}
	public void setTransPlace(String transPlace) {
		this.transPlace = transPlace;
	}
	public String getTransCountry() {
		return transCountry;
	}
	public void setTransCountry(String transCountry) {
		this.transCountry = transCountry;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getTransAmountIn() {
		return transAmountIn;
	}
	public void setTransAmountIn(String transAmountIn) {
		this.transAmountIn = transAmountIn;
	}
	public String getTransAmountOut() {
		return transAmountOut;
	}
	public void setTransAmountOut(String transAmountOut) {
		this.transAmountOut = transAmountOut;
	}
	public String getTransMoneyType() {
		return transMoneyType;
	}
	public void setTransMoneyType(String transMoneyType) {
		this.transMoneyType = transMoneyType;
	}
	public String getAccountIn() {
		return accountIn;
	}
	public void setAccountIn(String accountIn) {
		this.accountIn = accountIn;
	}
	public String getAccountOut() {
		return accountOut;
	}
	public void setAccountOut(String accountOut) {
		this.accountOut = accountOut;
	}
	public String getAccountMoneyType() {
		return accountMoneyType;
	}
	public void setAccountMoneyType(String accountMoneyType) {
		this.accountMoneyType = accountMoneyType;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getOppositeName() {
		return oppositeName;
	}
	public void setOppositeName(String oppositeName) {
		this.oppositeName = oppositeName;
	}
	@Override
	public String toString() {
		return "IcbcChinaDebitCardTransFlow [taskid=" + taskid + ", cardNum=" + cardNum + ", cardType=" + cardType
				+ ", transDate=" + transDate + ", stract=" + stract + ", transPlace=" + transPlace + ", transCountry="
				+ transCountry + ", currency=" + currency + ", transAmountIn=" + transAmountIn + ", transAmountOut="
				+ transAmountOut + ", transMoneyType=" + transMoneyType + ", accountIn=" + accountIn + ", accountOut="
				+ accountOut + ", accountMoneyType=" + accountMoneyType + ", balance=" + balance + ", oppositeName="
				+ oppositeName + "]";
	}
	
	
	
	
}
