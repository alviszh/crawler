package com.microservice.dao.entity.crawler.bank.psbcchina;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 邮政银行借记卡个人信息
 * 
 * @author tz
 *
 */
@Entity
@Table(name = "psbcchina_debitcard_userinfo" ,indexes = {@Index(name = "index_psbcchina_debitcard_userinfo_taskid", columnList = "taskid")})
public class PsbcChinaDebitCardUserInfo extends IdEntity implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -2421590620384640188L;

	/**
	 * taskid uuid 前端通过uuid访问状态结果
	 */
	private String taskid;

	/**
	 * 卡号/账号
	 */
	private String card_number;
	/**
	 * 别名
	 */
	private String aliases;

	/**
	 * 卡类型
	 */
	private String card_type;
	/**
	 * 币种
	 */
	private String currency;

	/**
	 * 账户余额
	 */
	private String balance;
	/**
	 * 可用余额
	 */
	private String available_balance;
	/**
	 * 账户状态
	 */
	private String account_state;
	/**
	 * 签约标志
	 */
	private String sign_sign;
	/**
	 * 开户机构
	 */
	private String opening_institution;
	/**
	 * 账户类型
	 */
	private String account_type;

	/**
	 * 开户日期
	 */
	private String account_opening;

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

	public String getAliases() {
		return aliases;
	}

	public void setAliases(String aliases) {
		this.aliases = aliases;
	}

	public String getCard_type() {
		return card_type;
	}

	public void setCard_type(String card_type) {
		this.card_type = card_type;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
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

	public String getAccount_state() {
		return account_state;
	}

	public void setAccount_state(String account_state) {
		this.account_state = account_state;
	}

	public String getSign_sign() {
		return sign_sign;
	}

	public void setSign_sign(String sign_sign) {
		this.sign_sign = sign_sign;
	}

	public String getOpening_institution() {
		return opening_institution;
	}

	public void setOpening_institution(String opening_institution) {
		this.opening_institution = opening_institution;
	}

	public String getAccount_type() {
		return account_type;
	}

	public void setAccount_type(String account_type) {
		this.account_type = account_type;
	}

	public String getAccount_opening() {
		return account_opening;
	}

	public void setAccount_opening(String account_opening) {
		this.account_opening = account_opening;
	}

	public PsbcChinaDebitCardUserInfo(String taskid, String card_number, String aliases, String card_type,
			String currency, String balance, String available_balance, String account_state, String sign_sign,
			String opening_institution, String account_type, String account_opening) {
		super();
		this.taskid = taskid;
		this.card_number = card_number;
		this.aliases = aliases;
		this.card_type = card_type;
		this.currency = currency;
		this.balance = balance;
		this.available_balance = available_balance;
		this.account_state = account_state;
		this.sign_sign = sign_sign;
		this.opening_institution = opening_institution;
		this.account_type = account_type;
		this.account_opening = account_opening;
	}

	public PsbcChinaDebitCardUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

}
