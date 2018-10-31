package com.microservice.dao.entity.crawler.bank.ccbchina;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 建行信用卡账户信息
 * @author zz
 *
 */
@Entity
@Table(name="ccbchina_creditcard_account_type")
public class CcbChinaCreditcardAccountType extends IdEntity{
	
	private String taskid;
	private String currency;				//币种
	private String tallyDate;				//记账日期
	private String dueDate;					//到期还款日
	private String creditLine;				//信用额度
	private String cashAmount;				//取现额度
	private String currentBalance;			//本期全部应还金额
	private String currentMinBalance;		//本期最低应还金额
	private String disputeCount;			//争议款/笔数
	private String previousBalance;			//上期账单余额
	private String cardNum;					//卡号
	@Override
	public String toString() {
		return "CcbChinaCreditcardAccountType [taskid=" + taskid + ", currency=" + currency + ", tallyDate=" + tallyDate
				+ ", dueDate=" + dueDate + ", creditLine=" + creditLine + ", cashAmount=" + cashAmount
				+ ", currentBalance=" + currentBalance + ", currentMinBalance=" + currentMinBalance + ", disputeCount="
				+ disputeCount + ", previousBalance=" + previousBalance + ", cardNum=" + cardNum + "]";
	}
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
	public String getTallyDate() {
		return tallyDate;
	}
	public void setTallyDate(String tallyDate) {
		this.tallyDate = tallyDate;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public String getCreditLine() {
		return creditLine;
	}
	public void setCreditLine(String creditLine) {
		this.creditLine = creditLine;
	}
	public String getCashAmount() {
		return cashAmount;
	}
	public void setCashAmount(String cashAmount) {
		this.cashAmount = cashAmount;
	}
	public String getCurrentBalance() {
		return currentBalance;
	}
	public void setCurrentBalance(String currentBalance) {
		this.currentBalance = currentBalance;
	}
	public String getCurrentMinBalance() {
		return currentMinBalance;
	}
	public void setCurrentMinBalance(String currentMinBalance) {
		this.currentMinBalance = currentMinBalance;
	}
	public String getDisputeCount() {
		return disputeCount;
	}
	public void setDisputeCount(String disputeCount) {
		this.disputeCount = disputeCount;
	}
	public String getPreviousBalance() {
		return previousBalance;
	}
	public void setPreviousBalance(String previousBalance) {
		this.previousBalance = previousBalance;
	}
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	
}
