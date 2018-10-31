/**
  * Copyright 2017 bejson.com 
  */
package com.microservice.dao.entity.crawler.e_commerce.jingdong;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * Auto-generated: 2017-12-13 17:56:55
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */

@Entity
@Table(name = "e_commerce_jd_baitiao",indexes = {@Index(name = "index_e_commerce_jd_baitiao_taskid", columnList = "taskid")})
public class JDBtPrivilege extends IdEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String interestFreeDays;// 0.05 未知含义
	private String txnFeeRate;// 0.8 未知含义
	private String interestRate;// 0.05 未知含义
	private String creditAccountId;// 推测为 京东的id
	private String creditLimit;// 总额度
	private String availableLimit;// 可用额度
	private String outstandingAmount;// 全部待还款
	private String noIous;// 21 未知含义
	private String noIousUnpaid;// 1 未知含义
	private String noIousPaid;// 20 未知含义
	private String noIousInDlq;// 0 未知含义
	private String delinquencyInterest;// 0 未知含
	private String delinquencyBalance;// 0 未知含
	private String outstandingBalanceIn7Days;// 下月账单
	private String totalPaymentAmount;// 累计已交款金额
	private String totalReversalAmount;// 推测为累计退款总金额
	private String totalFeeAmount;// 0 未知含
	private String totalReversalFeeAmount;// 0 未知含
	private String responseTs;// "35 " 未知含义
	private String version;
	private String refundCount;// 3 未知含义
	private String acountStatus;// 1 未知含义
	private String creditScore;//信用分
	
	private String taskid; //唯一标识

	public String getInterestFreeDays() {
		return interestFreeDays;
	}

	public void setInterestFreeDays(String interestFreeDays) {
		this.interestFreeDays = interestFreeDays;
	}

	public String getTxnFeeRate() {
		return txnFeeRate;
	}

	public void setTxnFeeRate(String txnFeeRate) {
		this.txnFeeRate = txnFeeRate;
	}

	public String getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(String interestRate) {
		this.interestRate = interestRate;
	}

	public String getCreditAccountId() {
		return creditAccountId;
	}

	public void setCreditAccountId(String creditAccountId) {
		this.creditAccountId = creditAccountId;
	}

	public String getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(String creditLimit) {
		this.creditLimit = creditLimit;
	}

	public String getAvailableLimit() {
		return availableLimit;
	}

	public void setAvailableLimit(String availableLimit) {
		this.availableLimit = availableLimit;
	}

	public String getOutstandingAmount() {
		return outstandingAmount;
	}

	public void setOutstandingAmount(String outstandingAmount) {
		this.outstandingAmount = outstandingAmount;
	}

	public String getNoIous() {
		return noIous;
	}

	public void setNoIous(String noIous) {
		this.noIous = noIous;
	}

	public String getNoIousUnpaid() {
		return noIousUnpaid;
	}

	public void setNoIousUnpaid(String noIousUnpaid) {
		this.noIousUnpaid = noIousUnpaid;
	}

	public String getNoIousPaid() {
		return noIousPaid;
	}

	public void setNoIousPaid(String noIousPaid) {
		this.noIousPaid = noIousPaid;
	}

	public String getNoIousInDlq() {
		return noIousInDlq;
	}

	public void setNoIousInDlq(String noIousInDlq) {
		this.noIousInDlq = noIousInDlq;
	}

	public String getDelinquencyInterest() {
		return delinquencyInterest;
	}

	public void setDelinquencyInterest(String delinquencyInterest) {
		this.delinquencyInterest = delinquencyInterest;
	}

	public String getDelinquencyBalance() {
		return delinquencyBalance;
	}

	public void setDelinquencyBalance(String delinquencyBalance) {
		this.delinquencyBalance = delinquencyBalance;
	}

	public String getOutstandingBalanceIn7Days() {
		return outstandingBalanceIn7Days;
	}

	public void setOutstandingBalanceIn7Days(String outstandingBalanceIn7Days) {
		this.outstandingBalanceIn7Days = outstandingBalanceIn7Days;
	}

	public String getTotalPaymentAmount() {
		return totalPaymentAmount;
	}

	public void setTotalPaymentAmount(String totalPaymentAmount) {
		this.totalPaymentAmount = totalPaymentAmount;
	}

	public String getTotalReversalAmount() {
		return totalReversalAmount;
	}

	public void setTotalReversalAmount(String totalReversalAmount) {
		this.totalReversalAmount = totalReversalAmount;
	}

	public String getTotalFeeAmount() {
		return totalFeeAmount;
	}

	public void setTotalFeeAmount(String totalFeeAmount) {
		this.totalFeeAmount = totalFeeAmount;
	}

	public String getTotalReversalFeeAmount() {
		return totalReversalFeeAmount;
	}

	public void setTotalReversalFeeAmount(String totalReversalFeeAmount) {
		this.totalReversalFeeAmount = totalReversalFeeAmount;
	}

	public String getResponseTs() {
		return responseTs;
	}

	public void setResponseTs(String responseTs) {
		this.responseTs = responseTs;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getRefundCount() {
		return refundCount;
	}

	public void setRefundCount(String refundCount) {
		this.refundCount = refundCount;
	}

	public String getAcountStatus() {
		return acountStatus;
	}

	public void setAcountStatus(String acountStatus) {
		this.acountStatus = acountStatus;
	}

	public String getCreditScore() {
		return creditScore;
	}

	public void setCreditScore(String creditScore) {
		this.creditScore = creditScore;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	@Override
	public String toString() {
		return "JDBtPrivilege [interestFreeDays=" + interestFreeDays + ", txnFeeRate=" + txnFeeRate + ", interestRate="
				+ interestRate + ", creditAccountId=" + creditAccountId + ", creditLimit=" + creditLimit
				+ ", availableLimit=" + availableLimit + ", outstandingAmount=" + outstandingAmount + ", noIous="
				+ noIous + ", noIousUnpaid=" + noIousUnpaid + ", noIousPaid=" + noIousPaid + ", noIousInDlq="
				+ noIousInDlq + ", delinquencyInterest=" + delinquencyInterest + ", delinquencyBalance="
				+ delinquencyBalance + ", outstandingBalanceIn7Days=" + outstandingBalanceIn7Days
				+ ", totalPaymentAmount=" + totalPaymentAmount + ", totalReversalAmount=" + totalReversalAmount
				+ ", totalFeeAmount=" + totalFeeAmount + ", totalReversalFeeAmount=" + totalReversalFeeAmount
				+ ", responseTs=" + responseTs + ", version=" + version + ", refundCount=" + refundCount
				+ ", acountStatus=" + acountStatus + ", creditScore=" + creditScore + ", taskid=" + taskid + "]";
	}
	
}