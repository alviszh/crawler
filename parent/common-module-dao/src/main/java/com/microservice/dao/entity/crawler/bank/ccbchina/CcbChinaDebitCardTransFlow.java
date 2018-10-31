package com.microservice.dao.entity.crawler.bank.ccbchina;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="ccbchina_debitcard_transflow")
public class CcbChinaDebitCardTransFlow extends IdEntity{
	
	private String taskid;
	private String tallyDate;				//记账日期
	private String dealDate;				//交易日期
	private String expend;					//支出
	private String income;					//收入
	private String balance;					//账户余额
	private String reciprocalAccountNum;	//对方账号
	private String reciprocalAccountName;	//对方户名
	private String currency;				//币种
	private String digest;					//摘要
	private String dealPlace;				//交易地点
	private String bankCard;				//银行卡
	private String AccountOpeningCity;		//开户城市
	
	@Override
	public String toString() {
		return "CcbChinaDebitCardTransFlow [taskid=" + taskid + ", tallyDate=" + tallyDate + ", dealDate=" + dealDate
				+ ", expend=" + expend + ", income=" + income + ", balance=" + balance + ", reciprocalAccountNum="
				+ reciprocalAccountNum + ", reciprocalAccountName=" + reciprocalAccountName + ", currency=" + currency
				+ ", digest=" + digest + ", dealPlace=" + dealPlace + ", bankCard=" + bankCard + ", AccountOpeningCity="
				+ AccountOpeningCity + "]";
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getTallyDate() {
		return tallyDate;
	}
	public void setTallyDate(String tallyDate) {
		this.tallyDate = tallyDate;
	}
	public String getDealDate() {
		return dealDate;
	}
	public void setDealDate(String dealDate) {
		this.dealDate = dealDate;
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
	public String getReciprocalAccountNum() {
		return reciprocalAccountNum;
	}
	public void setReciprocalAccountNum(String reciprocalAccountNum) {
		this.reciprocalAccountNum = reciprocalAccountNum;
	}
	public String getReciprocalAccountName() {
		return reciprocalAccountName;
	}
	public void setReciprocalAccountName(String reciprocalAccountName) {
		this.reciprocalAccountName = reciprocalAccountName;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getDigest() {
		return digest;
	}
	public void setDigest(String digest) {
		this.digest = digest;
	}
	public String getDealPlace() {
		return dealPlace;
	}
	public void setDealPlace(String dealPlace) {
		this.dealPlace = dealPlace;
	}
	public String getBankCard() {
		return bankCard;
	}
	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}
	public String getAccountOpeningCity() {
		return AccountOpeningCity;
	}
	public void setAccountOpeningCity(String accountOpeningCity) {
		AccountOpeningCity = accountOpeningCity;
	}
	
}
