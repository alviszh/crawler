package com.microservice.dao.entity.crawler.bank.cebchina;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="cebchina_creditcard_billing")
public class CebChinaCreditCardBilling extends IdEntity implements Serializable{

	private String taskid ;
	
	private String trade_date;//交易日期
	
	private String accounting_date;//记账日期
	
	private String num;//卡号
	
	private String state;//说明
	
	private String money;//金额
	
	private String account_num;//账户
	
	private String datetime;
	
	private String lei;
	
	private String shangdept;
	

	public String getShangdept() {
		return shangdept;
	}

	public void setShangdept(String shangdept) {
		this.shangdept = shangdept;
	}

	public String getLei() {
		return lei;
	}

	public void setLei(String lei) {
		this.lei = lei;
	}

	public String getAccount_num() {
		return account_num;
	}

	public void setAccount_num(String account_num) {
		this.account_num = account_num;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getTrade_date() {
		return trade_date;
	}

	public void setTrade_date(String trade_date) {
		this.trade_date = trade_date;
	}

	public String getAccounting_date() {
		return accounting_date;
	}

	public void setAccounting_date(String accounting_date) {
		this.accounting_date = accounting_date;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public CebChinaCreditCardBilling(String taskid, String trade_date, String accounting_date, String num, String state,
			String money) {
		super();
		this.taskid = taskid;
		this.trade_date = trade_date;
		this.accounting_date = accounting_date;
		this.num = num;
		this.state = state;
		this.money = money;
	}

	public CebChinaCreditCardBilling() {
		super();
	}
	
	
	
}
