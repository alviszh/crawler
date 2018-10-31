/**
  * Copyright 2017 bejson.com 
  */
package com.microservice.dao.entity.crawler.bank.bocchina;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * Auto-generated: 2017-11-29 16:41:24
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Entity
@Table(name = "bocchina_cebitcard_transList",indexes = {@Index(name = "index_bocchina_cebitcard_transList_taskid", columnList = "taskid")})
public class BocchinaCebitCardCrcdTransList extends IdEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String acntType;//币种
	private String dealDt;//交易日期
	private String checkDt;//记账日期
	private String dealCardId;//卡号
	private String dealDesc;//交易描述
	private String dealCcy;//"001" 未知含义
	private String transactionProfileCode;//“2010” 未知含义
	private String dealCnt;//支出金额	
	private String balCnt;//与支出金额	数值相同
	
	private String loanSign;// "DEBT" "NMON" 未知含义\
	
	private String cardNo;// 信用卡卡号

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	private String taskid;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public void setAcntType(String acntType) {
		this.acntType = acntType;
	}

	public String getAcntType() {
		return acntType;
	}

	public void setDealDt(String dealDt) {
		this.dealDt = dealDt;
	}

	public String getDealDt() {
		return dealDt;
	}

	public void setCheckDt(String checkDt) {
		this.checkDt = checkDt;
	}

	public String getCheckDt() {
		return checkDt;
	}

	public void setDealCardId(String dealCardId) {
		this.dealCardId = dealCardId;
	}

	public String getDealCardId() {
		return dealCardId;
	}

	public void setDealDesc(String dealDesc) {
		this.dealDesc = dealDesc;
	}

	public String getDealDesc() {
		return dealDesc;
	}

	public void setDealCcy(String dealCcy) {
		this.dealCcy = dealCcy;
	}

	public String getDealCcy() {
		return dealCcy;
	}

	public void setTransactionProfileCode(String transactionProfileCode) {
		this.transactionProfileCode = transactionProfileCode;
	}

	public String getTransactionProfileCode() {
		return transactionProfileCode;
	}

	public void setDealCnt(String dealCnt) {
		this.dealCnt = dealCnt;
	}

	public String getDealCnt() {
		return dealCnt;
	}

	public void setBalCnt(String balCnt) {
		this.balCnt = balCnt;
	}

	public String getBalCnt() {
		return balCnt;
	}

	public void setLoanSign(String loanSign) {
		this.loanSign = loanSign;
	}

	public String getLoanSign() {
		return loanSign;
	}

	@Override
	public String toString() {
		return "BocchinaCebitCardCrcdTransList [acntType=" + acntType + ", dealDt=" + dealDt + ", checkDt=" + checkDt
				+ ", dealCardId=" + dealCardId + ", dealDesc=" + dealDesc + ", dealCcy=" + dealCcy
				+ ", transactionProfileCode=" + transactionProfileCode + ", dealCnt=" + dealCnt + ", balCnt=" + balCnt
				+ ", loanSign=" + loanSign + ", cardNo=" + cardNo + ", taskid=" + taskid + "]";
	}
	
	

}