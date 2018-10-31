package com.microservice.dao.entity.crawler.bank.czbchina;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 浙商银行借记卡流水信息
 * 
 * @author tz
 *
 */
@Entity
@Table(name = "czbchina_debitcard_billdetails" ,indexes = {@Index(name = "index_czbchina_debitcard_billdetails_taskid", columnList = "taskid")})
public class CzbChinaDebitCardBillDetails extends IdEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5005502878689753746L;

	/**
	 * taskid uuid 前端通过uuid访问状态结果
	 */
	private String taskid;

	/**
	 * 序号
	 */
	private String num;
	/**
	 * 交易时间
	 */
	private String tran_date;

	/**
	 * 发生额（元）
	 */
	private String fee;
	/**
	 * 当前余额（元）
	 */
	private String balance;

	/**
	 * 摘要
	 */
	private String trans_decription;
	/**
	 * 对方名称
	 */
	private String opposite_name;
	/**
	 * 对方账号
	 */
	private String opposite_card_number;
	/**
	 * 对方开户行
	 */
	private String opposite_bank_name;
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
	public String getTran_date() {
		return tran_date;
	}
	public void setTran_date(String tran_date) {
		this.tran_date = tran_date;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getTrans_decription() {
		return trans_decription;
	}
	public void setTrans_decription(String trans_decription) {
		this.trans_decription = trans_decription;
	}
	public String getOpposite_name() {
		return opposite_name;
	}
	public void setOpposite_name(String opposite_name) {
		this.opposite_name = opposite_name;
	}
	public String getOpposite_card_number() {
		return opposite_card_number;
	}
	public void setOpposite_card_number(String opposite_card_number) {
		this.opposite_card_number = opposite_card_number;
	}
	public String getOpposite_bank_name() {
		return opposite_bank_name;
	}
	public void setOpposite_bank_name(String opposite_bank_name) {
		this.opposite_bank_name = opposite_bank_name;
	}
	public CzbChinaDebitCardBillDetails(String taskid, String num, String tran_date, String fee, String balance,
			String trans_decription, String opposite_name, String opposite_card_number, String opposite_bank_name) {
		super();
		this.taskid = taskid;
		this.num = num;
		this.tran_date = tran_date;
		this.fee = fee;
		this.balance = balance;
		this.trans_decription = trans_decription;
		this.opposite_name = opposite_name;
		this.opposite_card_number = opposite_card_number;
		this.opposite_bank_name = opposite_bank_name;
	}
	public CzbChinaDebitCardBillDetails() {
		super();
		// TODO Auto-generated constructor stub
	}

}
