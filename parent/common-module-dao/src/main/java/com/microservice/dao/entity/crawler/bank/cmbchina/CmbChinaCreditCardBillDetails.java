package com.microservice.dao.entity.crawler.bank.cmbchina;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 招商银行信用卡交易明细
 * 
 * @author tz
 *
 */
@Entity
@Table(name = "cmbchina_creditcard_billdetails" ,indexes = {@Index(name = "index_cmbchina_creditcard_billdetails_taskid", columnList = "taskid")})
public class CmbChinaCreditCardBillDetails extends IdEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 482626214601615555L;

	/**
	 * taskid uuid 前端通过uuid访问状态结果
	 */
	private String taskid;

	/**
	 * 交易日
	 */
	private String tradingDay;
	/**
	 * 记账日
	 */
	private String billingDay;
	/**
	 * 交易摘要
	 */
	private String summary;
	/**
	 * 人民币金额
	 */
	private String rmbAmount;
	/**
	 * 卡号末四位
	 */
	private String endfourNum;
	/**
	 * 交易地点
	 */
	private String tradingPlace;
	/**
	 * 交易地金额
	 */
	private String amountTransaction;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getTradingDay() {
		return tradingDay;
	}
	public void setTradingDay(String tradingDay) {
		this.tradingDay = tradingDay;
	}
	public String getBillingDay() {
		return billingDay;
	}
	public void setBillingDay(String billingDay) {
		this.billingDay = billingDay;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getRmbAmount() {
		return rmbAmount;
	}
	public void setRmbAmount(String rmbAmount) {
		this.rmbAmount = rmbAmount;
	}
	public String getEndfourNum() {
		return endfourNum;
	}
	public void setEndfourNum(String endfourNum) {
		this.endfourNum = endfourNum;
	}
	public String getTradingPlace() {
		return tradingPlace;
	}
	public void setTradingPlace(String tradingPlace) {
		this.tradingPlace = tradingPlace;
	}
	public String getAmountTransaction() {
		return amountTransaction;
	}
	public void setAmountTransaction(String amountTransaction) {
		this.amountTransaction = amountTransaction;
	}
	public CmbChinaCreditCardBillDetails(String taskid, String tradingDay, String billingDay, String summary,
			String rmbAmount, String endfourNum, String tradingPlace, String amountTransaction) {
		super();
		this.taskid = taskid;
		this.tradingDay = tradingDay;
		this.billingDay = billingDay;
		this.summary = summary;
		this.rmbAmount = rmbAmount;
		this.endfourNum = endfourNum;
		this.tradingPlace = tradingPlace;
		this.amountTransaction = amountTransaction;
	}
	public CmbChinaCreditCardBillDetails() {
		super();
		// TODO Auto-generated constructor stub
	}
	

}
