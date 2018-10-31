package com.microservice.dao.entity.crawler.bank.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="debit_card_trans_detail") //交易明细
public class DebiCardTransDetail extends IdEntity implements Serializable{

	private static final long serialVersionUID = -3207075026659390860L;

	@Column(name="task_id")
	private String taskId; //唯一标识
	private String recordId; //记录号
	private String lastNumber;//卡号后四位
	private String tranDate; //交易日期
	private String consumeType; //消费类型
	private String chargeDate; //记账日期
	private String currency; //币种
	private String fee; //消费金额
	private String balance; // 余额
	private String transLocation; // 交易地点
	private String transWay; // 交易方式
	private String transMode; // 交易通道
	private String transDecription; // 交易描述
	private String comment; // 备注
	private String oppositeCardNumber; // 对方卡号
	private String oppositeCardHolder; //对方持卡人
	private String oppositeBankName; //对方银行
	private String currentaccountType; //往来账类型
	
	@JsonBackReference
	private String resource; //溯源字段

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getLastNumber() {
		return lastNumber;
	}

	public void setLastNumber(String lastNumber) {
		this.lastNumber = lastNumber;
	}

	public String getTranDate() {
		return tranDate;
	}

	public void setTranDate(String tranDate) {
		this.tranDate = tranDate;
	}

	public String getConsumeType() {
		return consumeType;
	}

	public void setConsumeType(String consumeType) {
		this.consumeType = consumeType;
	}

	public String getChargeDate() {
		return chargeDate;
	}

	public void setChargeDate(String chargeDate) {
		this.chargeDate = chargeDate;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
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

	public String getTransLocation() {
		return transLocation;
	}

	public void setTransLocation(String transLocation) {
		this.transLocation = transLocation;
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

	public String getOppositeBankName() {
		return oppositeBankName;
	}

	public void setOppositeBankName(String oppositeBankName) {
		this.oppositeBankName = oppositeBankName;
	}

	public String getCurrentaccountType() {
		return currentaccountType;
	}

	public void setCurrentaccountType(String currentaccountType) {
		this.currentaccountType = currentaccountType;
	}

	@Column(columnDefinition="text")
	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	@Override
	public String toString() {
		return "DebiCardTransDetail [taskId=" + taskId + ", recordId=" + recordId + ", lastNumber=" + lastNumber
				+ ", tranDate=" + tranDate + ", consumeType=" + consumeType + ", chargeDate=" + chargeDate
				+ ", currency=" + currency + ", fee=" + fee + ", balance=" + balance + ", transLocation="
				+ transLocation + ", transWay=" + transWay + ", transMode=" + transMode + ", transDecription="
				+ transDecription + ", comment=" + comment + ", oppositeCardNumber=" + oppositeCardNumber
				+ ", oppositeCardHolder=" + oppositeCardHolder + ", oppositeBankName=" + oppositeBankName
				+ ", currentaccountType=" + currentaccountType + ", resource=" + resource + "]";
	}
}
