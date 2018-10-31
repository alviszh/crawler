package com.microservice.dao.entity.crawler.bank.hfbchina;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 恒丰银行借记卡流水信息
 * 
 * @author tz
 *
 */
@Entity
@Table(name = "hfbchina_debitcard_billdetails" ,indexes = {@Index(name = "index_hfbchina_debitcard_billdetails_taskid", columnList = "taskid")})
public class HfbChinaDebitCardBillDetails extends IdEntity implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 7906063240165485588L;

	/**
	 * taskid uuid 前端通过uuid访问状态结果
	 */
	private String taskid;

	/**
	 * 交易时间
	 */
	private String tran_date;
	/**
	 * 交易渠道
	 */
	private String tran_channel;

	/**
	 * 对方账号
	 */
	private String opposite_card_number;
	/**
	 * 对方户名
	 */
	private String opposite_name;

	/**
	 * 对方行名
	 */
	private String opposite_bank_name;
	/**
	 * 收付
	 */
	private String currentaccount_type;
	/**
	 * 金额
	 */
	private String money;
	/**
	 * 余额
	 */
	private String balance;

	/**
	 * 用途
	 */
	private String purpose;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getTran_date() {
		return tran_date;
	}

	public void setTran_date(String tran_date) {
		this.tran_date = tran_date;
	}

	public String getTran_channel() {
		return tran_channel;
	}

	public void setTran_channel(String tran_channel) {
		this.tran_channel = tran_channel;
	}

	public String getOpposite_card_number() {
		return opposite_card_number;
	}

	public void setOpposite_card_number(String opposite_card_number) {
		this.opposite_card_number = opposite_card_number;
	}

	public String getOpposite_name() {
		return opposite_name;
	}

	public void setOpposite_name(String opposite_name) {
		this.opposite_name = opposite_name;
	}

	public String getOpposite_bank_name() {
		return opposite_bank_name;
	}

	public void setOpposite_bank_name(String opposite_bank_name) {
		this.opposite_bank_name = opposite_bank_name;
	}

	public String getCurrentaccount_type() {
		return currentaccount_type;
	}

	public void setCurrentaccount_type(String currentaccount_type) {
		this.currentaccount_type = currentaccount_type;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public HfbChinaDebitCardBillDetails(String taskid, String tran_date, String tran_channel,
			String opposite_card_number, String opposite_name, String opposite_bank_name, String currentaccount_type,
			String money, String balance, String purpose) {
		super();
		this.taskid = taskid;
		this.tran_date = tran_date;
		this.tran_channel = tran_channel;
		this.opposite_card_number = opposite_card_number;
		this.opposite_name = opposite_name;
		this.opposite_bank_name = opposite_bank_name;
		this.currentaccount_type = currentaccount_type;
		this.money = money;
		this.balance = balance;
		this.purpose = purpose;
	}

	public HfbChinaDebitCardBillDetails() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
