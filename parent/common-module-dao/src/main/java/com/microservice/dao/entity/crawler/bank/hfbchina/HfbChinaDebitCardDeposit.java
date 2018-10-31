package com.microservice.dao.entity.crawler.bank.hfbchina;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 恒丰银行借记卡定期存款信息
 * 
 * @author tz
 *
 */
@Entity
@Table(name = "hfbchina_debitcard_deposit" ,indexes = {@Index(name = "index_hfbchina_debitcard_deposit_taskid", columnList = "taskid")})
public class HfbChinaDebitCardDeposit extends IdEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3110082505566492241L;

	/**
	 * taskid uuid 前端通过uuid访问状态结果
	 */
	private String taskid;

	/**
	 * 子账号
	 */
	private String subaccount;
	/**
	 * 存款本金
	 */
	private String deposit;

	/**
	 * 币种
	 */
	private String currency;
	/**
	 * 存期
	 */
	private String storge_period;

	/**
	 * 存入日
	 */
	private String deposit_date;
	/**
	 * 到期日
	 */
	private String due_date;
	/**
	 * 存款利率
	 */
	private String interest_rate;
	/**
	 * 账户状态
	 */
	private String account_state;

	/**
	 * 账户金额（零存整取）
	 */
	private String account_amount;
	
	/**
	 * 每次存取金额（零存整取）
	 */
	private String each_access_amount;
	
	/**
	 * 本期应存金额（零存整取）
	 */
	private String amount_deposit;
	
	/**
	 * 是否违约（零存整取）
	 */
	private String contract;
	
	/**
	 * 定期存款类型
	 */
	private String deposit_type;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getSubaccount() {
		return subaccount;
	}

	public void setSubaccount(String subaccount) {
		this.subaccount = subaccount;
	}

	public String getDeposit() {
		return deposit;
	}

	public void setDeposit(String deposit) {
		this.deposit = deposit;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getStorge_period() {
		return storge_period;
	}

	public void setStorge_period(String storge_period) {
		this.storge_period = storge_period;
	}

	public String getDeposit_date() {
		return deposit_date;
	}

	public void setDeposit_date(String deposit_date) {
		this.deposit_date = deposit_date;
	}

	public String getDue_date() {
		return due_date;
	}

	public void setDue_date(String due_date) {
		this.due_date = due_date;
	}

	public String getInterest_rate() {
		return interest_rate;
	}

	public void setInterest_rate(String interest_rate) {
		this.interest_rate = interest_rate;
	}

	public String getAccount_state() {
		return account_state;
	}

	public void setAccount_state(String account_state) {
		this.account_state = account_state;
	}

	public String getAccount_amount() {
		return account_amount;
	}

	public void setAccount_amount(String account_amount) {
		this.account_amount = account_amount;
	}

	public String getEach_access_amount() {
		return each_access_amount;
	}

	public void setEach_access_amount(String each_access_amount) {
		this.each_access_amount = each_access_amount;
	}

	public String getAmount_deposit() {
		return amount_deposit;
	}

	public void setAmount_deposit(String amount_deposit) {
		this.amount_deposit = amount_deposit;
	}

	public String getContract() {
		return contract;
	}

	public void setContract(String contract) {
		this.contract = contract;
	}

	public String getDeposit_type() {
		return deposit_type;
	}

	public void setDeposit_type(String deposit_type) {
		this.deposit_type = deposit_type;
	}

	public HfbChinaDebitCardDeposit(String taskid, String subaccount, String deposit, String currency,
			String storge_period, String deposit_date, String due_date, String interest_rate, String account_state,
			String account_amount, String each_access_amount, String amount_deposit, String contract,
			String deposit_type) {
		super();
		this.taskid = taskid;
		this.subaccount = subaccount;
		this.deposit = deposit;
		this.currency = currency;
		this.storge_period = storge_period;
		this.deposit_date = deposit_date;
		this.due_date = due_date;
		this.interest_rate = interest_rate;
		this.account_state = account_state;
		this.account_amount = account_amount;
		this.each_access_amount = each_access_amount;
		this.amount_deposit = amount_deposit;
		this.contract = contract;
		this.deposit_type = deposit_type;
	}

	public HfbChinaDebitCardDeposit() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
}
