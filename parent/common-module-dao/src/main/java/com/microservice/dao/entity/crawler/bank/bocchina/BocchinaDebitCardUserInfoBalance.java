/**
  * Copyright 2017 bejson.com 
  */
package com.microservice.dao.entity.crawler.bank.bocchina;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.microservice.dao.entity.IdEntity;

/**
 * Auto-generated: 2017-10-31 15:4:39
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Entity
@Table(name = "bocchina_debitcard_balance",indexes = {@Index(name = "index_bocchina_debitcard_balance_taskid", columnList = "taskid")})
public class BocchinaDebitCardUserInfoBalance extends IdEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return "BocchinaDebitCardUserInfoBalance [currencyCode=" + currencyCode + ", cashRemit=" + cashRemit
				+ ", bookBalance=" + bookBalance + ", availableBalance=" + availableBalance + ", volumeNumber="
				+ volumeNumber + ", type=" + type + ", interestRate=" + interestRate + ", status=" + status
				+ ", monthBalance=" + monthBalance + ", cdNumber=" + cdNumber + ", cdPeriod=" + cdPeriod + ", openDate="
				+ openDate + ", interestStartsDate=" + interestStartsDate + ", interestEndDate=" + interestEndDate
				+ ", settlementDate=" + settlementDate + ", convertType=" + convertType + ", pingNo=" + pingNo
				+ ", holdAmount=" + holdAmount + ", appointStatus=" + appointStatus + ", spplType=" + spplType
				+ ", taskid=" + taskid + "]";
	}

	private String currencyCode;//"001" 含义未知
	private String cashRemit;//"00"  含义未知
	private double bookBalance;//账户余额
	private double availableBalance;//可用余额
	private String volumeNumber;//null 含义未知
	private String type;//null 含义未知
	private String interestRate;//null 含义未知
	private String status;//"00"  含义未知
	private String monthBalance;//null 含义未知
	private String cdNumber;//null 含义未知
	private String cdPeriod;//null 含义未知
	private String openDate;//开户日期
	private String interestStartsDate;//null 含义未知
	private String interestEndDate;//null 含义未知
	private String settlementDate;//null 含义未知
	private String convertType;//null 含义未知
	private String pingNo;//null 含义未知
	private String holdAmount;//null 含义未知
	private String appointStatus;//null 含义未知
	private String spplType;//null 含义未知

	private String taskid;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCashRemit(String cashRemit) {
		this.cashRemit = cashRemit;
	}

	public String getCashRemit() {
		return cashRemit;
	}

	public void setBookBalance(double bookBalance) {
		this.bookBalance = bookBalance;
	}

	public double getBookBalance() {
		return bookBalance;
	}

	public void setAvailableBalance(double availableBalance) {
		this.availableBalance = availableBalance;
	}

	public double getAvailableBalance() {
		return availableBalance;
	}

	public void setVolumeNumber(String volumeNumber) {
		this.volumeNumber = volumeNumber;
	}

	public String getVolumeNumber() {
		return volumeNumber;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setInterestRate(String interestRate) {
		this.interestRate = interestRate;
	}

	public String getInterestRate() {
		return interestRate;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setMonthBalance(String monthBalance) {
		this.monthBalance = monthBalance;
	}

	public String getMonthBalance() {
		return monthBalance;
	}

	public void setCdNumber(String cdNumber) {
		this.cdNumber = cdNumber;
	}

	public String getCdNumber() {
		return cdNumber;
	}

	public void setCdPeriod(String cdPeriod) {
		this.cdPeriod = cdPeriod;
	}

	public String getCdPeriod() {
		return cdPeriod;
	}


	public String getOpenDate() {
		return openDate;
	}

	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}

	public void setInterestStartsDate(String interestStartsDate) {
		this.interestStartsDate = interestStartsDate;
	}

	public String getInterestStartsDate() {
		return interestStartsDate;
	}

	public void setInterestEndDate(String interestEndDate) {
		this.interestEndDate = interestEndDate;
	}

	public String getInterestEndDate() {
		return interestEndDate;
	}

	public void setSettlementDate(String settlementDate) {
		this.settlementDate = settlementDate;
	}

	public String getSettlementDate() {
		return settlementDate;
	}

	public void setConvertType(String convertType) {
		this.convertType = convertType;
	}

	public String getConvertType() {
		return convertType;
	}

	public void setPingNo(String pingNo) {
		this.pingNo = pingNo;
	}

	public String getPingNo() {
		return pingNo;
	}

	public void setHoldAmount(String holdAmount) {
		this.holdAmount = holdAmount;
	}

	public String getHoldAmount() {
		return holdAmount;
	}

	public void setAppointStatus(String appointStatus) {
		this.appointStatus = appointStatus;
	}

	public String getAppointStatus() {
		return appointStatus;
	}

	public void setSpplType(String spplType) {
		this.spplType = spplType;
	}

	public String getSpplType() {
		return spplType;
	}
	@JsonBackReference
	private BocchinaDebitCardUserinfSingleLimit bocchinaDebitCardUserinfSingleLimit;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userfnfobalance_id")
	@JsonIgnore
	public BocchinaDebitCardUserinfSingleLimit getBocchinaDebitCardUserinfSingleLimit() {
		return bocchinaDebitCardUserinfSingleLimit;
	}

	public void setBocchinaDebitCardUserinfSingleLimit(
			BocchinaDebitCardUserinfSingleLimit bocchinaDebitCardUserinfSingleLimit) {
		this.bocchinaDebitCardUserinfSingleLimit = bocchinaDebitCardUserinfSingleLimit;
	}

}