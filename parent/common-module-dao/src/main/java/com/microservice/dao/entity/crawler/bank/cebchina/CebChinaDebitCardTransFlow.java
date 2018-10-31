package com.microservice.dao.entity.crawler.bank.cebchina;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="cebchina_debitcard_transflow")
public class CebChinaDebitCardTransFlow  extends IdEntity implements Serializable{

	private String taskid;
	
	private String jydate;//交易日期
	
	private String debit;//支出金额
	
	private String amount;//存入金额
	
	private String balance;//余额
	
	private String reciprocalAccount;//对方帐号
	
	private String reciprocalname;//对方户名
	
	private String abstracts;//摘要

	private String num;//帐号
	
	private String currency;//币种

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getJydate() {
		return jydate;
	}

	public void setJydate(String jydate) {
		this.jydate = jydate;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getDebit() {
		return debit;
	}

	public void setDebit(String debit) {
		this.debit = debit;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getReciprocalAccount() {
		return reciprocalAccount;
	}

	public void setReciprocalAccount(String reciprocalAccount) {
		this.reciprocalAccount = reciprocalAccount;
	}

	public String getReciprocalname() {
		return reciprocalname;
	}

	public void setReciprocalname(String reciprocalname) {
		this.reciprocalname = reciprocalname;
	}

	public String getAbstracts() {
		return abstracts;
	}

	public void setAbstracts(String abstracts) {
		this.abstracts = abstracts;
	}

	public CebChinaDebitCardTransFlow(String taskid, String jydate, String debit, String amount, String balance,
			String reciprocalAccount, String reciprocalname, String abstracts, String num, String currency) {
		super();
		this.taskid = taskid;
		this.jydate = jydate;
		this.debit = debit;
		this.amount = amount;
		this.balance = balance;
		this.reciprocalAccount = reciprocalAccount;
		this.reciprocalname = reciprocalname;
		this.abstracts = abstracts;
		this.num = num;
		this.currency = currency;
	}

	public CebChinaDebitCardTransFlow() {
		super();
	}

	@Override
	public String toString() {
		return "CebChinaDebitCardTransFlow [taskid=" + taskid + ", jydate=" + jydate + ", debit=" + debit + ", amount="
				+ amount + ", balance=" + balance + ", reciprocalAccount=" + reciprocalAccount + ", reciprocalname="
				+ reciprocalname + ", abstracts=" + abstracts + ", num=" + num + ", currency=" + currency + "]";
	}


	
}
