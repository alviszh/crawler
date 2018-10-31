package com.microservice.dao.entity.crawler.bank.icbcchina;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="icbcchina_creditcard_userinfo" ,indexes = {@Index(name = "index_icbcchina_creditcard_userinfo_taskid", columnList = "taskid")})
public class IcbcChinaCreditCardUserinfo extends IdEntity{

	private String taskid;//uuid 前端通过uuid访问状态结果
	private String cardNum;					//卡号
	private String billDay;					//账单日
	private String repayDate;				//还款日
	private String balance;					//可用余额
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public String getBillDay() {
		return billDay;
	}
	public void setBillDay(String billDay) {
		this.billDay = billDay;
	}
	public String getRepayDate() {
		return repayDate;
	}
	public void setRepayDate(String repayDate) {
		this.repayDate = repayDate;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	@Override
	public String toString() {
		return "IcbcChinaCreditCardUserinfo [taskid=" + taskid + ", cardNum=" + cardNum + ", billDay=" + billDay
				+ ", repayDate=" + repayDate + ", balance=" + balance + "]";
	}
}
