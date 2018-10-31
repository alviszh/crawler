package com.microservice.dao.entity.crawler.bank.hfbchina;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 恒丰银行借记卡个人信息
 * 
 * @author tz
 *
 */
@Entity
@Table(name = "hfbchina_debitcard_userinfo" ,indexes = {@Index(name = "index_hfbchina_debitcard_userinfo_taskid", columnList = "taskid")})
public class HfbChinaDebitCardUserInfo extends IdEntity implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -971731508793377285L;

	/**
	 * taskid uuid 前端通过uuid访问状态结果
	 */
	private String taskid;

	/**
	 * 卡片持有人
	 */
	private String card_holder;
	/**
	 * 手机号码
	 */
	private String mobile;

	/**
	 * 账户别名
	 */
	private String aliases;
	/**
	 * 签约方式
	 */
	private String signing_mode;

	/**
	 * 凭证状态
	 */
	private String credential_state;
	/**
	 * 开户行
	 */
	private String deposit_bank;
	/**
	 * 活期账户
	 */
	private String account;
	/**
	 * 活期余额
	 */
	private String balance;

	/**
	 * 币种
	 */
	private String currency_type;
	/**
	 * 账户状态
	 */
	private String account_state;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getCard_holder() {
		return card_holder;
	}
	public void setCard_holder(String card_holder) {
		this.card_holder = card_holder;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getAliases() {
		return aliases;
	}
	public void setAliases(String aliases) {
		this.aliases = aliases;
	}
	public String getSigning_mode() {
		return signing_mode;
	}
	public void setSigning_mode(String signing_mode) {
		this.signing_mode = signing_mode;
	}
	public String getCredential_state() {
		return credential_state;
	}
	public void setCredential_state(String credential_state) {
		this.credential_state = credential_state;
	}
	public String getDeposit_bank() {
		return deposit_bank;
	}
	public void setDeposit_bank(String deposit_bank) {
		this.deposit_bank = deposit_bank;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getCurrency_type() {
		return currency_type;
	}
	public void setCurrency_type(String currency_type) {
		this.currency_type = currency_type;
	}
	public String getAccount_state() {
		return account_state;
	}
	public void setAccount_state(String account_state) {
		this.account_state = account_state;
	}
	public HfbChinaDebitCardUserInfo(String taskid, String card_holder, String mobile, String aliases,
			String signing_mode, String credential_state, String deposit_bank, String account, String balance,
			String currency_type, String account_state) {
		super();
		this.taskid = taskid;
		this.card_holder = card_holder;
		this.mobile = mobile;
		this.aliases = aliases;
		this.signing_mode = signing_mode;
		this.credential_state = credential_state;
		this.deposit_bank = deposit_bank;
		this.account = account;
		this.balance = balance;
		this.currency_type = currency_type;
		this.account_state = account_state;
	}
	public HfbChinaDebitCardUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
