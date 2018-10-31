package com.microservice.dao.entity.crawler.bank.hxbchina;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="hxbchina_creditcard_transflow",indexes = {@Index(name = "index_hxbchina_creditcard_transflow_taskid", columnList = "taskid")})
public class HxbChinaCreditCardTransFlow extends IdEntity implements Serializable{
	private static final long serialVersionUID = -4658147918284209784L;
	private String taskid;
//	序号
	private String sortNum;
//	交易日期
	private String transDate;
//	入账日期
	private String accountDate;
//	币种
	private String currency;
//	交易摘要
	private String remark;
//	交易金额
	private String transAmount;
//	卡号后四位
	private String cardEnd;
//	查询范围
	private String qryDateRange;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getSortNum() {
		return sortNum;
	}
	public void setSortNum(String sortNum) {
		this.sortNum = sortNum;
	}
	public String getTransDate() {
		return transDate;
	}
	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}
	public String getAccountDate() {
		return accountDate;
	}
	public void setAccountDate(String accountDate) {
		this.accountDate = accountDate;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getTransAmount() {
		return transAmount;
	}
	public void setTransAmount(String transAmount) {
		this.transAmount = transAmount;
	}
	public String getCardEnd() {
		return cardEnd;
	}
	public void setCardEnd(String cardEnd) {
		this.cardEnd = cardEnd;
	}
	public String getQryDateRange() {
		return qryDateRange;
	}
	public void setQryDateRange(String qryDateRange) {
		this.qryDateRange = qryDateRange;
	}
	public HxbChinaCreditCardTransFlow() {
		super();
		// TODO Auto-generated constructor stub
	}
	public HxbChinaCreditCardTransFlow(String taskid, String sortNum, String transDate, String accountDate,
			String currency, String remark, String transAmount, String cardEnd, String qryDateRange) {
		super();
		this.taskid = taskid;
		this.sortNum = sortNum;
		this.transDate = transDate;
		this.accountDate = accountDate;
		this.currency = currency;
		this.remark = remark;
		this.transAmount = transAmount;
		this.cardEnd = cardEnd;
		this.qryDateRange = qryDateRange;
	}
}
