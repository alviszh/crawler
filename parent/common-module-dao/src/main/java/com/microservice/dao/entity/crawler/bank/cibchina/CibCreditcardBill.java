package com.microservice.dao.entity.crawler.bank.cibchina;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="cibchina_creditcard_bill_info",indexes = {@Index(name = "index_cibchina_creditcard_bill_info_taskid", columnList = "taskid")})
public class CibCreditcardBill extends IdEntity{
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	private static final long serialVersionUID = -7225639204374657354L;
	
	/**卡号*/ 
	@Column(name="cardNumber")
	private String cardNumber ;
	
	/**账单周期起始日 */ 
	@Column(name="billPeriodStart")
	private String billPeriodStart ;
	
	/**账单周期结束日*/ 
	@Column(name="billPeriodEnd")
	private String billPeriodEnd ;
	
	/**账单日期*/ 
	@Column(name="billDate")
	private String billDate ;
	
	/**账单到期日期*/ 
	@Column(name="billEndDate")
	private String billEndDate ;
	
	/**本期应还金额*/ 
	@Column(name="billShouldPay")
	private String billShouldPay ;
	
	/**本期最低还款额*/ 
	@Column(name="billLeastPay")
	private String billLeastPay ;
	
	/**本期账单金额*/ 
	@Column(name="billPay")
	private String billPay ;
	
	/**调整金额*/ 
	@Column(name="trimAmount")
	private String trimAmount ;
	
	/**积分*/ 
	@Column(name="integral")
	private String integral ;
	
	/**新增积分*/ 
	@Column(name="integralAdd")
	private String integralAdd ;
	
	/**本期调整积分*/ 
	@Column(name="integralExchange")
	private String integralExchange ;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
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

	public String getBillShouldPay() {
		return billShouldPay;
	}

	public void setBillShouldPay(String billShouldPay) {
		this.billShouldPay = billShouldPay;
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

	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getIntegral() {
		return integral;
	}

	public void setIntegral(String integral) {
		this.integral = integral;
	}

	public String getIntegralAdd() {
		return integralAdd;
	}

	public void setIntegralAdd(String integralAdd) {
		this.integralAdd = integralAdd;
	}

	public String getIntegralExchange() {
		return integralExchange;
	}

	public void setIntegralExchange(String integralExchange) {
		this.integralExchange = integralExchange;
	}
	
	
}
