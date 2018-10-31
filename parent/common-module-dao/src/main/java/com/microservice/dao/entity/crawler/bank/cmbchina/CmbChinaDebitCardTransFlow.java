package com.microservice.dao.entity.crawler.bank.cmbchina;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

//招商银行流水表
@Entity
@Table(name = "cmbchina_debitcard_transflow" ,indexes = {@Index(name = "index_cmbchina_debitcard_transflow_taskid", columnList = "taskid")})
public class CmbChinaDebitCardTransFlow extends IdEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7071150203211629386L;
	
	//交易日期
	private String date;
		
	//交易时间
	private String time;
	
	//支出
	private String pay;
	
	//存入
	private String deposit;
	
	//余额
	private String balance;
	
	//余额
	private String type;
	
	//备注
	private String remarks;
	
	//任务id
	private String taskid;
	
	//卡号
	private String cardNo;
	
	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getPay() {
		return pay;
	}

	public void setPay(String pay) {
		this.pay = pay;
	}

	public String getDeposit() {
		return deposit;
	}

	public void setDeposit(String deposit) {
		this.deposit = deposit;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

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

	@Override
	public String toString() {
		return "CmbChinaDebitCardTransFlow [date=" + date + ", time=" + time + ", pay=" + pay + ", deposit=" + deposit
				+ ", balance=" + balance + ", type=" + type + ", remarks=" + remarks + ", taskid=" + taskid
				+ ", cardNo=" + cardNo + "]";
	}

	

	

	
	
	
	
	
	

}
