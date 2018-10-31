package com.microservice.dao.entity.crawler.bank.cebchina;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="cebchina_debitcard_deadline")
public class CebChinaDebitCardDeadline  extends IdEntity implements Serializable {

	private String taskid;
	
	private String type;//类型
	
	private String currency;//币种
	
	private String cashremit;//钞汇标志
	
	private String deadline;//期限
	
	private String kdate;//开户日期
	
	private String enddate;//到期日期
	
	private String balance;//余额
	
	private String marketvalue;//参考市值
	
	private String state;//状态

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCashremit() {
		return cashremit;
	}

	public void setCashremit(String cashremit) {
		this.cashremit = cashremit;
	}

	public String getDeadline() {
		return deadline;
	}

	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}

	public String getKdate() {
		return kdate;
	}

	public void setKdate(String kdate) {
		this.kdate = kdate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getMarketvalue() {
		return marketvalue;
	}

	public void setMarketvalue(String marketvalue) {
		this.marketvalue = marketvalue;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public CebChinaDebitCardDeadline(String taskid, String type, String currency, String cashremit, String deadline,
			String kdate, String enddate, String balance, String marketvalue, String state) {
		super();
		this.taskid = taskid;
		this.type = type;
		this.currency = currency;
		this.cashremit = cashremit;
		this.deadline = deadline;
		this.kdate = kdate;
		this.enddate = enddate;
		this.balance = balance;
		this.marketvalue = marketvalue;
		this.state = state;
	}

	public CebChinaDebitCardDeadline() {
		super();
	}

	@Override
	public String toString() {
		return "CebChinaDebitCardDeadline [taskid=" + taskid + ", type=" + type + ", currency=" + currency
				+ ", cashremit=" + cashremit + ", deadline=" + deadline + ", kdate=" + kdate + ", enddate=" + enddate
				+ ", balance=" + balance + ", marketvalue=" + marketvalue + ", state=" + state + "]";
	}
	
	
	
	
}
