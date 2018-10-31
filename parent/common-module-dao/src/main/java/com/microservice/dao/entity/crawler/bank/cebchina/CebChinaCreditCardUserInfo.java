package com.microservice.dao.entity.crawler.bank.cebchina;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name="cebchina_creditcard_userinfo")
public class CebChinaCreditCardUserInfo  extends IdEntity implements Serializable {

	private String taskid ; 

	private String currency;//货币名称

	private String num;//卡号

	private String querytime;//查询时间

	private String line_of_credit;//信用额度

	private String temporarylines;//临时额度

	private String cashadvance;//预借现金额度

	private String availablecredit;//实际可用额度

	private String balance;//当前账户余额

	private String accountant_bill_date;//账单日

	private String repayment_date;//到期还款日

	private String payments;//本期账单应还款额

	private String minimumpayment;//本期最小还款额

	private String nopayments;//本期未还款金额

	private String overdraft_xf;//当前透支利率（消费类）

	private String overdraft_xj;//当前透支利率（现金类）

	private String integral;//当前可用积分

	private String endintegral;//即将到期积分

	private String name;//持卡人姓名
	
	private String numname;//卡号名称

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getNumname() {
		return numname;
	}

	public void setNumname(String numname) {
		this.numname = numname;
	}

	public String getQuerytime() {
		return querytime;
	}

	public void setQuerytime(String querytime) {
		this.querytime = querytime;
	}

	public String getLine_of_credit() {
		return line_of_credit;
	}

	public void setLine_of_credit(String line_of_credit) {
		this.line_of_credit = line_of_credit;
	}

	public String getTemporarylines() {
		return temporarylines;
	}

	public void setTemporarylines(String temporarylines) {
		this.temporarylines = temporarylines;
	}

	public String getCashadvance() {
		return cashadvance;
	}

	public void setCashadvance(String cashadvance) {
		this.cashadvance = cashadvance;
	}

	public String getAvailablecredit() {
		return availablecredit;
	}

	public void setAvailablecredit(String availablecredit) {
		this.availablecredit = availablecredit;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getAccountant_bill_date() {
		return accountant_bill_date;
	}

	public void setAccountant_bill_date(String accountant_bill_date) {
		this.accountant_bill_date = accountant_bill_date;
	}

	public String getRepayment_date() {
		return repayment_date;
	}

	public void setRepayment_date(String repayment_date) {
		this.repayment_date = repayment_date;
	}

	public String getPayments() {
		return payments;
	}

	public void setPayments(String payments) {
		this.payments = payments;
	}

	public String getMinimumpayment() {
		return minimumpayment;
	}

	public void setMinimumpayment(String minimumpayment) {
		this.minimumpayment = minimumpayment;
	}

	public String getNopayments() {
		return nopayments;
	}

	public void setNopayments(String nopayments) {
		this.nopayments = nopayments;
	}

	public String getOverdraft_xf() {
		return overdraft_xf;
	}

	public void setOverdraft_xf(String overdraft_xf) {
		this.overdraft_xf = overdraft_xf;
	}

	public String getOverdraft_xj() {
		return overdraft_xj;
	}

	public void setOverdraft_xj(String overdraft_xj) {
		this.overdraft_xj = overdraft_xj;
	}

	public String getIntegral() {
		return integral;
	}

	public void setIntegral(String integral) {
		this.integral = integral;
	}

	public String getEndintegral() {
		return endintegral;
	}

	public void setEndintegral(String endintegral) {
		this.endintegral = endintegral;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public CebChinaCreditCardUserInfo(String taskid, String currency, String num, String querytime,
			String line_of_credit, String temporarylines, String cashadvance, String availablecredit, String balance,
			String accountant_bill_date, String repayment_date, String payments, String minimumpayment,
			String nopayments, String overdraft_xf, String overdraft_xj, String integral, String endintegral,
			String name, String numname) {
		super();
		this.taskid = taskid;
		this.currency = currency;
		this.num = num;
		this.querytime = querytime;
		this.line_of_credit = line_of_credit;
		this.temporarylines = temporarylines;
		this.cashadvance = cashadvance;
		this.availablecredit = availablecredit;
		this.balance = balance;
		this.accountant_bill_date = accountant_bill_date;
		this.repayment_date = repayment_date;
		this.payments = payments;
		this.minimumpayment = minimumpayment;
		this.nopayments = nopayments;
		this.overdraft_xf = overdraft_xf;
		this.overdraft_xj = overdraft_xj;
		this.integral = integral;
		this.endintegral = endintegral;
		this.name = name;
		this.numname = numname;
	}

	public CebChinaCreditCardUserInfo() {
		super();
	}

	@Override
	public String toString() {
		return "CebChinaCreditCardUserInfo [taskid=" + taskid + ", currency=" + currency + ", num=" + num + ", numname="
				+ numname + ", querytime=" + querytime + ", line_of_credit=" + line_of_credit + ", temporarylines="
				+ temporarylines + ", cashadvance=" + cashadvance + ", availablecredit=" + availablecredit
				+ ", balance=" + balance + ", accountant_bill_date=" + accountant_bill_date + ", repayment_date="
				+ repayment_date + ", payments=" + payments + ", minimumpayment=" + minimumpayment + ", nopayments="
				+ nopayments + ", overdraft_xf=" + overdraft_xf + ", overdraft_xj=" + overdraft_xj + ", integral="
				+ integral + ", endintegral=" + endintegral + ", name=" + name + "]";
	}



}

