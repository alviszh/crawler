package com.microservice.dao.entity.crawler.bank.cmbcchina;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 我的账户信息
 * @author: sln 
 * @date: 2017年11月14日 下午3:05:35 
 */
@Entity
@Table(name="cmbcchina_creditcard_myaccount",indexes = {@Index(name = "index_cmbcchina_creditcard_myaccount_taskid", columnList = "taskid")})
public class CmbcChinaCreditCardMyAccount extends IdEntity implements Serializable{
	private static final long serialVersionUID = -4887796426842238964L;
	private String taskid;
//	账户类型
	private String accountType;
//	信用额度
	private String creditLmit;
//	可用额度
	private String availableLmit;
//	预借现金信用额度
	private String cashAdvanceCreLmit;
//	预借现金可用额度
	private String cashAdvanceAvaiLmit;
//	人民币账面余额
	private String bookbalRmb;
//	外币账面余额
	private String bookbalUsd;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getCreditLmit() {
		return creditLmit;
	}
	public void setCreditLmit(String creditLmit) {
		this.creditLmit = creditLmit;
	}
	public String getAvailableLmit() {
		return availableLmit;
	}
	public void setAvailableLmit(String availableLmit) {
		this.availableLmit = availableLmit;
	}
	public String getCashAdvanceCreLmit() {
		return cashAdvanceCreLmit;
	}
	public void setCashAdvanceCreLmit(String cashAdvanceCreLmit) {
		this.cashAdvanceCreLmit = cashAdvanceCreLmit;
	}
	public String getCashAdvanceAvaiLmit() {
		return cashAdvanceAvaiLmit;
	}
	public void setCashAdvanceAvaiLmit(String cashAdvanceAvaiLmit) {
		this.cashAdvanceAvaiLmit = cashAdvanceAvaiLmit;
	}
	public String getBookbalRmb() {
		return bookbalRmb;
	}
	public void setBookbalRmb(String bookbalRmb) {
		this.bookbalRmb = bookbalRmb;
	}
	public String getBookbalUsd() {
		return bookbalUsd;
	}
	public void setBookbalUsd(String bookbalUsd) {
		this.bookbalUsd = bookbalUsd;
	}
	public CmbcChinaCreditCardMyAccount() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CmbcChinaCreditCardMyAccount(String taskid, String accountType, String creditLmit, String availableLmit,
			String cashAdvanceCreLmit, String cashAdvanceAvaiLmit, String bookbalRmb, String bookbalUsd) {
		super();
		this.taskid = taskid;
		this.accountType = accountType;
		this.creditLmit = creditLmit;
		this.availableLmit = availableLmit;
		this.cashAdvanceCreLmit = cashAdvanceCreLmit;
		this.cashAdvanceAvaiLmit = cashAdvanceAvaiLmit;
		this.bookbalRmb = bookbalRmb;
		this.bookbalUsd = bookbalUsd;
	}
}
