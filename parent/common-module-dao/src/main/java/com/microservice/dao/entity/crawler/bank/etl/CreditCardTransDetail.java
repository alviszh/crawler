package com.microservice.dao.entity.crawler.bank.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="credit_card_trans_detail") //交易明细
public class CreditCardTransDetail extends IdEntity implements Serializable{

	private static final long serialVersionUID = -3207075026659390860L;
	
	@Column(name="task_id")
	private String taskId; //唯一标识
	private String cardNumber; //卡号
	private String lastNumber; //卡号后四位
	private String transLocation; // 交易地点
	private String chargeDate; //记账日期
	private String tranDate; //交易日期
	private String balance; //余额
	private String fee; //花费
	private String fee_dollar; //花费（美元）
	private String currency; //币种
	private String oppositeCardNumber; // 对方卡号
	private String oppositeCardHolder; //对方持卡人
	private String transDecription; // 交易描述
	private String comment; // 备注
	private String transWay; // 交易方式
	private String transMode; // 交易通道
	
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

	public String getLastNumber() {
		return lastNumber;
	}

	public void setLastNumber(String lastNumber) {
		this.lastNumber = lastNumber;
	}

	public String getTransLocation() {
		return transLocation;
	}

	public void setTransLocation(String transLocation) {
		this.transLocation = transLocation;
	}

	public String getChargeDate() {
		return chargeDate;
	}

	public void setChargeDate(String chargeDate) {
		this.chargeDate = chargeDate;
	}

	public String getTranDate() {
		return tranDate;
	}

	public void setTranDate(String tranDate) {
		this.tranDate = tranDate;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getFee_dollar() {
		return fee_dollar;
	}

	public void setFee_dollar(String fee_dollar) {
		this.fee_dollar = fee_dollar;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getOppositeCardNumber() {
		return oppositeCardNumber;
	}

	public void setOppositeCardNumber(String oppositeCardNumber) {
		this.oppositeCardNumber = oppositeCardNumber;
	}

	public String getOppositeCardHolder() {
		return oppositeCardHolder;
	}

	public void setOppositeCardHolder(String oppositeCardHolder) {
		this.oppositeCardHolder = oppositeCardHolder;
	}

	public String getTransDecription() {
		return transDecription;
	}

	public void setTransDecription(String transDecription) {
		this.transDecription = transDecription;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getTransWay() {
		return transWay;
	}

	public void setTransWay(String transWay) {
		this.transWay = transWay;
	}

	public String getTransMode() {
		return transMode;
	}

	public void setTransMode(String transMode) {
		this.transMode = transMode;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	@Override
	public String toString() {
		return "CreditCardTransDetail [taskId=" + taskId + ", cardNumber=" + cardNumber + ", lastNumber=" + lastNumber
				+ ", transLocation=" + transLocation + ", chargeDate=" + chargeDate + ", tranDate=" + tranDate
				+ ", balance=" + balance + ", fee=" + fee + ", fee_dollar=" + fee_dollar + ", currency=" + currency
				+ ", oppositeCardNumber=" + oppositeCardNumber + ", oppositeCardHolder=" + oppositeCardHolder
				+ ", transDecription=" + transDecription + ", comment=" + comment + ", transWay=" + transWay
				+ ", transMode=" + transMode + ", resource=" + resource + "]";
	}

	
}
