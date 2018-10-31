package com.microservice.dao.entity.crawler.bank.cmbcchina;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description:信用卡账单信息概要 是每期间账单的总览
 * @author: sln 
 * @date: 2017年11月14日 下午3:10:41 
 */
@Entity
@Table(name="cmbcchina_creditcard_billgeneral",indexes = {@Index(name = "index_cmbcchina_creditcard_billgeneral_taskid", columnList = "taskid")})
public class CmbcChinaCreditCardBillGeneral extends IdEntity implements Serializable{
	private static final long serialVersionUID = 5972133644736286743L;
	private String taskid;
//	币种
	private String currency;
//	账单日期
	private String billDate;
//	到期还款日
	private String repayLimitDate;	
//	本期应还款金额
	private String currentNeedPay;
//	最低还款金额
	private String minRepayLimit;
	
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
	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	public String getRepayLimitDate() {
		return repayLimitDate;
	}
	public void setRepayLimitDate(String repayLimitDate) {
		this.repayLimitDate = repayLimitDate;
	}
	public String getCurrentNeedPay() {
		return currentNeedPay;
	}
	public void setCurrentNeedPay(String currentNeedPay) {
		this.currentNeedPay = currentNeedPay;
	}
	public String getMinRepayLimit() {
		return minRepayLimit;
	}
	public void setMinRepayLimit(String minRepayLimit) {
		this.minRepayLimit = minRepayLimit;
	}
	public CmbcChinaCreditCardBillGeneral() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CmbcChinaCreditCardBillGeneral(String taskid, String currency, String billDate, String repayLimitDate,
			String currentNeedPay, String minRepayLimit) {
		super();
		this.taskid = taskid;
		this.currency = currency;
		this.billDate = billDate;
		this.repayLimitDate = repayLimitDate;
		this.currentNeedPay = currentNeedPay;
		this.minRepayLimit = minRepayLimit;
	}
}
