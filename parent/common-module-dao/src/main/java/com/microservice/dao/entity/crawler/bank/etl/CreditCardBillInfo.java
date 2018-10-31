package com.microservice.dao.entity.crawler.bank.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="credit_card_bill_info") //交易明细
public class CreditCardBillInfo extends IdEntity implements Serializable{

	private static final long serialVersionUID = -3207075026659390860L;
	
	@Column(name="task_id")
	private String taskId; //唯一标识
	private String cardNumber; //卡号
	private String lastNumber; //卡号后四位
	private String accountType;	//账户类型
	private String lastBill;	//上期账单
	private String lastBillPay;	//上期账单金额
	private String billPeriodStart;	//账单周期起始日 
	private String billPeriodEnd;	//账单周期结束日
	private String billDate;	//账单日期
	private String billEndDate;	//账单到期日期
	private String billShouldPayDollar;	//本期应还款金额（美元） 
	private String billShouldPay;	//本期应还金额
	private String billLeastPayDollar;	//本期最低还款额(美元)
	private String billLeastPay;	//本期最低还款额
	private String billPay;	//本期账单金额
	private String trimAmount;	//调整金额
	private String revolvingInterest;	//循环利息
	private String points;	//积分
	private String pointsAdd;	//新增积分 
	private String pointsExchange;	//本期调整积分 
	private String email;	//发件人的Email
	
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

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getLastBill() {
		return lastBill;
	}

	public void setLastBill(String lastBill) {
		this.lastBill = lastBill;
	}

	public String getLastBillPay() {
		return lastBillPay;
	}

	public void setLastBillPay(String lastBillPay) {
		this.lastBillPay = lastBillPay;
	}

	public String getBillPeriodStart() {
		return billPeriodStart;
	}

	public void setBillPeriodStart(String billPeriodStart) {
		this.billPeriodStart = billPeriodStart;
	}

	public String getBillPeriodEnd() {
		return billPeriodEnd;
	}

	public void setBillPeriodEnd(String billPeriodEnd) {
		this.billPeriodEnd = billPeriodEnd;
	}

	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	public String getBillEndDate() {
		return billEndDate;
	}

	public void setBillEndDate(String billEndDate) {
		this.billEndDate = billEndDate;
	}

	public String getBillShouldPayDollar() {
		return billShouldPayDollar;
	}

	public void setBillShouldPayDollar(String billShouldPayDollar) {
		this.billShouldPayDollar = billShouldPayDollar;
	}

	public String getBillShouldPay() {
		return billShouldPay;
	}

	public void setBillShouldPay(String billShouldPay) {
		this.billShouldPay = billShouldPay;
	}

	public String getBillLeastPayDollar() {
		return billLeastPayDollar;
	}

	public void setBillLeastPayDollar(String billLeastPayDollar) {
		this.billLeastPayDollar = billLeastPayDollar;
	}

	public String getBillLeastPay() {
		return billLeastPay;
	}

	public void setBillLeastPay(String billLeastPay) {
		this.billLeastPay = billLeastPay;
	}

	public String getBillPay() {
		return billPay;
	}

	public void setBillPay(String billPay) {
		this.billPay = billPay;
	}

	public String getTrimAmount() {
		return trimAmount;
	}

	public void setTrimAmount(String trimAmount) {
		this.trimAmount = trimAmount;
	}

	public String getRevolvingInterest() {
		return revolvingInterest;
	}

	public void setRevolvingInterest(String revolvingInterest) {
		this.revolvingInterest = revolvingInterest;
	}

	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
	}

	public String getPointsAdd() {
		return pointsAdd;
	}

	public void setPointsAdd(String pointsAdd) {
		this.pointsAdd = pointsAdd;
	}

	public String getPointsExchange() {
		return pointsExchange;
	}

	public void setPointsExchange(String pointsExchange) {
		this.pointsExchange = pointsExchange;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
		return "CreditCardBillInfo [taskId=" + taskId + ", cardNumber=" + cardNumber + ", lastNumber=" + lastNumber
				+ ", accountType=" + accountType + ", lastBill=" + lastBill + ", lastBillPay=" + lastBillPay
				+ ", billPeriodStart=" + billPeriodStart + ", billPeriodEnd=" + billPeriodEnd + ", billDate=" + billDate
				+ ", billEndDate=" + billEndDate + ", billShouldPayDollar=" + billShouldPayDollar + ", billShouldPay="
				+ billShouldPay + ", billLeastPayDollar=" + billLeastPayDollar + ", billLeastPay=" + billLeastPay
				+ ", billPay=" + billPay + ", trimAmount=" + trimAmount + ", revolvingInterest=" + revolvingInterest
				+ ", points=" + points + ", pointsAdd=" + pointsAdd + ", pointsExchange=" + pointsExchange + ", email="
				+ email + ", resource=" + resource + "]";
	}
	
	
}
