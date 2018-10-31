package com.microservice.dao.entity.crawler.bank.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="debit_card_deposit_info") //定期存款信息
public class DebitCardDepositInfo extends IdEntity implements Serializable{

	private static final long serialVersionUID = -3207075026659390860L;
	
	@Column(name="task_id")
	private String taskId; //唯一标识
	private String cardNumber; //卡号
	private String depositType; //定期存款类型
	private String currency; //币种
	private String interestBegindate; //计息日期
	private String interestEnddate; //到期日期
	private String storgePeriod; //存期
	private String storgePeriodUnit; //存期单位
	private String interestRate; //利率
	private String balance; //余额
	
	@JsonBackReference
	private String resource; //溯源字段

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getDepositType() {
		return depositType;
	}

	public void setDepositType(String depositType) {
		this.depositType = depositType;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getInterestBegindate() {
		return interestBegindate;
	}

	public void setInterestBegindate(String interestBegindate) {
		this.interestBegindate = interestBegindate;
	}

	public String getInterestEnddate() {
		return interestEnddate;
	}

	public void setInterestEnddate(String interestEnddate) {
		this.interestEnddate = interestEnddate;
	}

	public String getStorgePeriod() {
		return storgePeriod;
	}

	public void setStorgePeriod(String storgePeriod) {
		this.storgePeriod = storgePeriod;
	}

	public String getStorgePeriodUnit() {
		return storgePeriodUnit;
	}

	public void setStorgePeriodUnit(String storgePeriodUnit) {
		this.storgePeriodUnit = storgePeriodUnit;
	}

	public String getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(String interestRate) {
		this.interestRate = interestRate;
	}

	@Column(columnDefinition="text")
	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}
	

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	@Override
	public String toString() {
		return "DebitCardDepositInfo [taskId=" + taskId + ", cardNumber=" + cardNumber + ", depositType=" + depositType
				+ ", currency=" + currency + ", interestBegindate=" + interestBegindate + ", interestEnddate="
				+ interestEnddate + ", storgePeriod=" + storgePeriod + ", storgePeriodUnit=" + storgePeriodUnit
				+ ", interestRate=" + interestRate + ", balance=" + balance + ", resource=" + resource + "]";
	}

	
	
	
}
