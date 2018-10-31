package com.microservice.dao.entity.crawler.standalone.bank.ccbchina;

import java.io.Serializable;

public class CcbChinaDebitCardBillDetailsId implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 5102348808225187501L;

	/**
	 * 交易日期
	 * 【存款日期】
	 */
	private String deal_date;
	
	/**
	 * 【入账银行】
	 */
	private String account;
	/**
	 *  收入
	 * 【金额】
	 */
	private String income;
	/**
	 * 对方户名
	 * 【存款人】
	 */
	private String name;
	/**
	 * 【备注】
	 */
	private String remark;
	
	/**
	 * 卡号后四位
	 */
	private String  card_no;
	

	public String getDeal_date() {
		return deal_date;
	}

	public void setDeal_date(String deal_date) {
		this.deal_date = deal_date;
	}
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCard_no() {
		return card_no;
	}

	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}
	
	
	
}
