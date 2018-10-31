package com.microservice.dao.entity.crawler.bank.czbchina;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 浙商银行借记卡定期存款信息
 * 
 * @author tz
 *
 */
@Entity
@Table(name = "czbchina_debitcard_deposit" ,indexes = {@Index(name = "index_czbchina_debitcard_deposit_taskid", columnList = "taskid")})
public class CzbChinaDebitCardDeposit extends IdEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -366356270995413234L;

	/**
	 * taskid uuid 前端通过uuid访问状态结果
	 */
	private String taskid;

	/**
	 * 子账户序号
	 */
	private String num;
	/**
	 * 账户类型
	 */
	private String card_type;

	/**
	 * 币种
	 */
	private String currency;
	/**
	 * 钞/汇
	 */
	private String banknote_remittance;

	/**
	 * 余额
	 */
	private String balance;
	/**
	 * 年利率（%）
	 */
	private String interest_rate;
	/**
	 * 存期
	 */
	private String storge_period;
	/**
	 * 开户日
	 */
	private String deposit_time;

	/**
	 * 到期日
	 */
	private String interest_dnddate;
	
	/**
	 * 状态
	 */
	private String state;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
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

	public String getInterest_rate() {
		return interest_rate;
	}

	public void setInterest_rate(String interest_rate) {
		this.interest_rate = interest_rate;
	}

	public String getStorge_period() {
		return storge_period;
	}

	public void setStorge_period(String storge_period) {
		this.storge_period = storge_period;
	}

	public String getDeposit_time() {
		return deposit_time;
	}

	public void setDeposit_time(String deposit_time) {
		this.deposit_time = deposit_time;
	}

	public String getInterest_dnddate() {
		return interest_dnddate;
	}

	public void setInterest_dnddate(String interest_dnddate) {
		this.interest_dnddate = interest_dnddate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public CzbChinaDebitCardDeposit(String taskid, String num, String card_type, String currency,
			String banknote_remittance, String balance, String interest_rate, String storge_period, String deposit_time,
			String interest_dnddate, String state) {
		super();
		this.taskid = taskid;
		this.num = num;
		this.card_type = card_type;
		this.currency = currency;
		this.banknote_remittance = banknote_remittance;
		this.balance = balance;
		this.interest_rate = interest_rate;
		this.storge_period = storge_period;
		this.deposit_time = deposit_time;
		this.interest_dnddate = interest_dnddate;
		this.state = state;
	}

	public CzbChinaDebitCardDeposit() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
