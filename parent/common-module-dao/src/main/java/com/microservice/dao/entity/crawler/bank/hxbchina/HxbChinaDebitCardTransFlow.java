package com.microservice.dao.entity.crawler.bank.hxbchina;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="hxbchina_debitcard_transflow",indexes = {@Index(name = "index_hxbchina_debitcard_transflow_taskid", columnList = "taskid")})
public class HxbChinaDebitCardTransFlow extends IdEntity implements Serializable{
	private static final long serialVersionUID = 423627319144311197L;
	private String taskid;
//	序号
	private String disPlayNo;
//	交易日期
	private String transDate;
//	业务类型
	private String transChannel;
//	币种
	private String currency;
//	存入金额
	private String transAmtIncome;
//	支取金额
	private String transAmtPayOut;
//	账户余额
	private String acctBal;
//	对方账号
	private String otherAcctNo;
//	对方姓名
	private String otherName;
//	对方开户行
	private String otherOpenBankName;
//	汇款用途/附言
	private String summary;
//	摘要
	private String note;
//	打印回单
	private String printHDFlag;
//	查询范围
	private String qryDateRange;
//	所属卡号
	private String cardno;
	
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getDisPlayNo() {
		return disPlayNo;
	}
	public void setDisPlayNo(String disPlayNo) {
		this.disPlayNo = disPlayNo;
	}
	public String getTransDate() {
		return transDate;
	}
	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}
	public String getTransChannel() {
		return transChannel;
	}
	public void setTransChannel(String transChannel) {
		this.transChannel = transChannel;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getTransAmtIncome() {
		return transAmtIncome;
	}
	public void setTransAmtIncome(String transAmtIncome) {
		this.transAmtIncome = transAmtIncome;
	}
	public String getTransAmtPayOut() {
		return transAmtPayOut;
	}
	public void setTransAmtPayOut(String transAmtPayOut) {
		this.transAmtPayOut = transAmtPayOut;
	}
	public String getAcctBal() {
		return acctBal;
	}
	public void setAcctBal(String acctBal) {
		this.acctBal = acctBal;
	}
	public String getOtherAcctNo() {
		return otherAcctNo;
	}
	public void setOtherAcctNo(String otherAcctNo) {
		this.otherAcctNo = otherAcctNo;
	}
	public String getOtherName() {
		return otherName;
	}
	public void setOtherName(String otherName) {
		this.otherName = otherName;
	}
	public String getOtherOpenBankName() {
		return otherOpenBankName;
	}
	public void setOtherOpenBankName(String otherOpenBankName) {
		this.otherOpenBankName = otherOpenBankName;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getPrintHDFlag() {
		return printHDFlag;
	}
	public void setPrintHDFlag(String printHDFlag) {
		this.printHDFlag = printHDFlag;
	}
	public String getQryDateRange() {
		return qryDateRange;
	}
	public void setQryDateRange(String qryDateRange) {
		this.qryDateRange = qryDateRange;
	}
	
	public String getCardno() {
		return cardno;
	}
	public void setCardno(String cardno) {
		this.cardno = cardno;
	}
	public HxbChinaDebitCardTransFlow() {
		super();
		// TODO Auto-generated constructor stub
	}
}
