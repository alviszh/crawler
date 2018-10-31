/**
  * Copyright 2017 bejson.com 
  */
package com.microservice.dao.entity.crawler.bank.bocchina;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * Auto-generated: 2017-10-31 16:35:41
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Entity
@Table(name = "bocchina_debitcard_transflow",indexes = {@Index(name = "index_bocchina_debitcard_transflow_taskid", columnList = "taskid")})
public class BocchinaDebitCardTransFlow extends IdEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String transChnl;//transChnl 交易渠道
	private String chnlDetail;//中国银行北京怀柔府前街支行 交易银行
	private boolean chargeBack;//false 未知含义
	private String paymentDate;//交易日期
	private String currency;// 001 未知含义 推测为货币种类
	private String cashRemit;// 未知含义
	private double amount;//收入金额
	private double balance;//余额
	private String businessDigest;//业务摘要
	private String furInfo;// 未知含义
	private String payeeAccountName;//对方用户名
	private String payeeAccountNumber;//对方账号
	private String taskid;
	
	private String accountNumber;

	@Override
	public String toString() {
		return "BocchinaDebitCardTransFlow [transChnl=" + transChnl + ", chnlDetail=" + chnlDetail + ", chargeBack="
				+ chargeBack + ", paymentDate=" + paymentDate + ", currency=" + currency + ", cashRemit=" + cashRemit
				+ ", amount=" + amount + ", balance=" + balance + ", businessDigest=" + businessDigest + ", furInfo="
				+ furInfo + ", payeeAccountName=" + payeeAccountName + ", payeeAccountNumber=" + payeeAccountNumber
				+ ", taskid=" + taskid + ", accountNumber=" + accountNumber + "]";
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public void setTransChnl(String transChnl) {
		this.transChnl = transChnl;
	}

	public String getTransChnl() {
		return transChnl;
	}

	public void setChnlDetail(String chnlDetail) {
		this.chnlDetail = chnlDetail;
	}

	public String getChnlDetail() {
		return chnlDetail;
	}

	public void setChargeBack(boolean chargeBack) {
		this.chargeBack = chargeBack;
	}

	public boolean getChargeBack() {
		return chargeBack;
	}

	

	public String getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCashRemit(String cashRemit) {
		this.cashRemit = cashRemit;
	}

	public String getCashRemit() {
		return cashRemit;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getAmount() {
		return amount;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public double getBalance() {
		return balance;
	}

	public void setBusinessDigest(String businessDigest) {
		this.businessDigest = businessDigest;
	}

	public String getBusinessDigest() {
		return businessDigest;
	}

	public void setFurInfo(String furInfo) {
		this.furInfo = furInfo;
	}

	public String getFurInfo() {
		return furInfo;
	}

	public void setPayeeAccountName(String payeeAccountName) {
		this.payeeAccountName = payeeAccountName;
	}

	public String getPayeeAccountName() {
		return payeeAccountName;
	}

	public void setPayeeAccountNumber(String payeeAccountNumber) {
		this.payeeAccountNumber = payeeAccountNumber;
	}

	public String getPayeeAccountNumber() {
		return payeeAccountNumber;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	

}