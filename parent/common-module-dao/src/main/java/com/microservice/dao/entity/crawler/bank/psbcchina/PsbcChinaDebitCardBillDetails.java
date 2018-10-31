package com.microservice.dao.entity.crawler.bank.psbcchina;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 邮政银行借记卡交易明细
 * 
 * @author tz
 *
 */
@Entity
@Table(name = "psbcchina_debitcard_billdetails" ,indexes = {@Index(name = "index_psbcchina_debitcard_billdetails_taskid", columnList = "taskid")})
public class PsbcChinaDebitCardBillDetails extends IdEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 553182169134119194L;

	/**
	 * taskid uuid 前端通过uuid访问状态结果
	 */
	private String taskid;

	/**
	 * 流水号
	 */
	private String serial_number;
	/**
	 * 交易日期
	 */
	private String tran_date;

	/**
	 * 摘要
	 */
	private String trans_decription;
	
	/**
	 * 收入/支出
	 * in_out=='1'-------收入
	 * in_out=='2'-------支出
	 */
	private String in_out;
	
	/**
	 * 交易金额
	 */
	private String money;

	/**
	 * 账户余额
	 */
	private String balance;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getSerial_number() {
		return serial_number;
	}

	public void setSerial_number(String serial_number) {
		this.serial_number = serial_number;
	}

	public String getTran_date() {
		return tran_date;
	}

	public void setTran_date(String tran_date) {
		this.tran_date = tran_date;
	}

	public String getTrans_decription() {
		return trans_decription;
	}

	public void setTrans_decription(String trans_decription) {
		this.trans_decription = trans_decription;
	}

	public String getIn_out() {
		return in_out;
	}

	public void setIn_out(String in_out) {
		this.in_out = in_out;
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

	public PsbcChinaDebitCardBillDetails(String taskid, String serial_number, String tran_date, String trans_decription,
			String in_out, String money, String balance) {
		super();
		this.taskid = taskid;
		this.serial_number = serial_number;
		this.tran_date = tran_date;
		this.trans_decription = trans_decription;
		this.in_out = in_out;
		this.money = money;
		this.balance = balance;
	}

	public PsbcChinaDebitCardBillDetails() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
