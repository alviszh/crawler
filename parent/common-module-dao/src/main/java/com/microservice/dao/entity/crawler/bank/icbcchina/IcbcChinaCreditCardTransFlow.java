package com.microservice.dao.entity.crawler.bank.icbcchina;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="icbcchina_creditcard_transflow" ,indexes = {@Index(name = "index_icbcchina_creditcard_transflow_taskid", columnList = "taskid")})
public class IcbcChinaCreditCardTransFlow extends IdEntity{
	private String taskid;
	private String cardNum;						//卡号
	private String cardLastNum;					//卡号后四位
	private String tradingDay;					//交易日
	private String bookingDay;					//记账日
	private String tradingType;					//交易类
	private String merchantName;				//商户名称/城市
	private String transAmount;					//交易金额/币种
	private String accountAmount;				//记账金额/币种
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
	public String getCardLastNum() {
		return cardLastNum;
	}
	public void setCardLastNum(String cardLastNum) {
		this.cardLastNum = cardLastNum;
	}
	public String getTradingDay() {
		return tradingDay;
	}
	public void setTradingDay(String tradingDay) {
		this.tradingDay = tradingDay;
	}
	public String getBookingDay() {
		return bookingDay;
	}
	public void setBookingDay(String bookingDay) {
		this.bookingDay = bookingDay;
	}
	public String getTradingType() {
		return tradingType;
	}
	public void setTradingType(String tradingType) {
		this.tradingType = tradingType;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public String getTransAmount() {
		return transAmount;
	}
	public void setTransAmount(String transAmount) {
		this.transAmount = transAmount;
	}
	public String getAccountAmount() {
		return accountAmount;
	}
	public void setAccountAmount(String accountAmount) {
		this.accountAmount = accountAmount;
	}
	@Override
	public String toString() {
		return "IcbcChinaCreditCardTransFlow [taskid=" + taskid + ", cardNum=" + cardNum + ", cardLastNum="
				+ cardLastNum + ", tradingDay=" + tradingDay + ", bookingDay=" + bookingDay + ", tradingType="
				+ tradingType + ", merchantName=" + merchantName + ", transAmount=" + transAmount + ", accountAmount="
				+ accountAmount + "]";
	}
	
}
