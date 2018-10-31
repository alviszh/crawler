package com.microservice.dao.entity.crawler.bank.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="debit_card_baseinfo") //基本信息
public class DebitCardBaseInfo extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = -3207075026659390860L;

	@Column(name="task_id")
	private String taskId; //唯一标识
	private String cardId; //银行卡ID
	private String cardType; //卡片类型
	private String bankName; //银行名称
	private String cardNumber; //卡片号码
	private String lastNumber; //卡号后四位
	private String cardHolder; //卡片持有人
	private String depositBank; //开户行
	private String depositTime; //开户时间
	private String balance; //余额
	private String idNumber; //身份证号
//	private String basicIdnumber; //用户表身份证号
	
	@JsonBackReference
	private String resource; //溯源字段
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
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
	public String getCardHolder() {
		return cardHolder;
	}
	public void setCardHolder(String cardHolder) {
		this.cardHolder = cardHolder;
	}
	public String getDepositBank() {
		return depositBank;
	}
	public void setDepositBank(String depositBank) {
		this.depositBank = depositBank;
	}
	public String getDepositTime() {
		return depositTime;
	}
	public void setDepositTime(String depositTime) {
		this.depositTime = depositTime;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
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
		return "DebitCardBaseInfo [taskId=" + taskId + ", cardId=" + cardId + ", cardType=" + cardType + ", bankName="
				+ bankName + ", cardNumber=" + cardNumber + ", lastNumber=" + lastNumber + ", cardHolder=" + cardHolder
				+ ", depositBank=" + depositBank + ", depositTime=" + depositTime + ", balance=" + balance
				+ ", idNumber=" + idNumber + ", resource=" + resource + "]";
	}
		
}
