package com.microservice.dao.entity.crawler.bank.czbchina;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 浙商银行借记卡个人信息
 * 
 * @author tz
 *
 */
@Entity
@Table(name = "czbchina_debitcard_userinfo" ,indexes = {@Index(name = "index_czbchina_debitcard_userinfo_taskid", columnList = "taskid")})
public class CzbChinaDebitCardUserInfo extends IdEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5708479990775956898L;

	/**
	 * taskid uuid 前端通过uuid访问状态结果
	 */
	private String taskid;

	/**
	 * 卡号/账号
	 */
	private String card_number;
	/**
	 * 账户类型
	 */
	private String card_type;

	/**
	 * 账户别名
	 */
	private String aliases;
	/**
	 * 币种
	 */
	private String currency;

	/**
	 * 钞/汇
	 */
	private String banknote_remittance;
	/**
	 * 当前余额
	 */
	private String balance;
	/**
	 * 可用余额
	 */
	private String available_balance;
	/**
	 * 开户/认购日
	 */
	private String deposit_time;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getCard_number() {
		return card_number;
	}
	public void setCard_number(String card_number) {
		this.card_number = card_number;
	}
	public String getCard_type() {
		return card_type;
	}
	public void setCard_type(String card_type) {
		this.card_type = card_type;
	}
	public String getAliases() {
		return aliases;
	}
	public void setAliases(String aliases) {
		this.aliases = aliases;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getBanknote_remittance() {
		return banknote_remittance;
	}
	public void setBanknote_remittance(String banknote_remittance) {
		this.banknote_remittance = banknote_remittance;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getAvailable_balance() {
		return available_balance;
	}
	public void setAvailable_balance(String available_balance) {
		this.available_balance = available_balance;
	}
	public String getDeposit_time() {
		return deposit_time;
	}
	public void setDeposit_time(String deposit_time) {
		this.deposit_time = deposit_time;
	}
	public CzbChinaDebitCardUserInfo(String taskid, String card_number, String card_type, String aliases,
			String currency, String banknote_remittance, String balance, String available_balance,
			String deposit_time) {
		super();
		this.taskid = taskid;
		this.card_number = card_number;
		this.card_type = card_type;
		this.aliases = aliases;
		this.currency = currency;
		this.banknote_remittance = banknote_remittance;
		this.balance = balance;
		this.available_balance = available_balance;
		this.deposit_time = deposit_time;
	}
	public CzbChinaDebitCardUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
