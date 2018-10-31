package com.microservice.dao.entity.crawler.bank.citicchina;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="citicchina_creditcard_bill",indexes = {@Index(name = "index_citicchina_creditcard_bill_taskid", columnList = "taskid")}) 
public class CiticChinaCreditCardBill extends IdEntity implements Serializable{

	private String cardNum;//卡号
	
    private String repay;//  本期应还款额
	
	private String lowstRepay;//  最低还款金额
	
	private String alreadyMoney;//本期已还金额
	
	private String notRepay;//本期仍需还款
	
	private String thisDatea;//本期账单日
	
	private String repayDatea;//到期还款日
	
	private String credit_avail_1;//可用额度
	
	private String taskid;

	@Override
	public String toString() {
		return "CiticChinaCreditCardBill [cardNum=" + cardNum + ", repay=" + repay + ", lowstRepay=" + lowstRepay
				+ ", alreadyMoney=" + alreadyMoney + ", notRepay=" + notRepay + ", thisDatea=" + thisDatea
				+ ", repayDatea=" + repayDatea + ", credit_avail_1=" + credit_avail_1 + ", taskid=" + taskid + "]";
	}
	@Column(columnDefinition="text")
	public String getCardNum() {
		return cardNum;
	}
	
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	public String getRepay() {
		return repay;
	}

	public void setRepay(String repay) {
		this.repay = repay;
	}

	public String getLowstRepay() {
		return lowstRepay;
	}

	public void setLowstRepay(String lowstRepay) {
		this.lowstRepay = lowstRepay;
	}

	public String getAlreadyMoney() {
		return alreadyMoney;
	}

	public void setAlreadyMoney(String alreadyMoney) {
		this.alreadyMoney = alreadyMoney;
	}

	public String getNotRepay() {
		return notRepay;
	}

	public void setNotRepay(String notRepay) {
		this.notRepay = notRepay;
	}

	public String getThisDatea() {
		return thisDatea;
	}

	public void setThisDatea(String thisDatea) {
		this.thisDatea = thisDatea;
	}

	public String getRepayDatea() {
		return repayDatea;
	}

	public void setRepayDatea(String repayDatea) {
		this.repayDatea = repayDatea;
	}

	public String getCredit_avail_1() {
		return credit_avail_1;
	}

	public void setCredit_avail_1(String credit_avail_1) {
		this.credit_avail_1 = credit_avail_1;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	
	
}
