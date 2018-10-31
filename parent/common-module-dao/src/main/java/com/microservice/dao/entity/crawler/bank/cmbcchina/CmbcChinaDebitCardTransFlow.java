package com.microservice.dao.entity.crawler.bank.cmbcchina;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="cmbcchina_debitcard_transflow",indexes = {@Index(name = "index_cmbcchina_debitcard_transflow_taskid", columnList = "taskid")})
public class CmbcChinaDebitCardTransFlow extends IdEntity implements Serializable{
	private static final long serialVersionUID = 8305069115516942648L;
	private String taskid;
//	本人账号
	private String acno;
//	币种
	private String currency;
//	交易信息
	private String remark;
//	交易金额
	private String amount;
//	账户余额
	private String balance;
//	交易渠道
	private String channel;
//	对方信息
	private String payeename;
//	交易日期
	private String transdate;
//	对方账户
	private String payeeaccount;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getPayeename() {
		return payeename;
	}
	public void setPayeename(String payeename) {
		this.payeename = payeename;
	}
	public String getTransdate() {
		return transdate;
	}
	public void setTransdate(String transdate) {
		this.transdate = transdate;
	}
	public String getPayeeaccount() {
		return payeeaccount;
	}
	public void setPayeeaccount(String payeeaccount) {
		this.payeeaccount = payeeaccount;
	}
	public String getAcno() {
		return acno;
	}
	public void setAcno(String acno) {
		this.acno = acno;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public CmbcChinaDebitCardTransFlow() {
		super();
		// TODO Auto-generated constructor stub
	}
}
