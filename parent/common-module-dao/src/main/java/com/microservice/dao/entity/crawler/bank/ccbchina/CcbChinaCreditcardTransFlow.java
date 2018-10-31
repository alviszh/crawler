package com.microservice.dao.entity.crawler.bank.ccbchina;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 建行信用卡交易明细
 * @author zz
 *
 */
@Entity
@Table(name="ccbchina_creditcard_transflow")
public class CcbChinaCreditcardTransFlow extends IdEntity{
	
	@Override
	public String toString() {
		return "CcbChinaCreditcardTransFlow [taskid=" + taskid + ", dealDate=" + dealDate + ", tallyDate=" + tallyDate
				+ ", fourCardNum=" + fourCardNum + ", dealDescription=" + dealDescription + ", dealCurrency="
				+ dealCurrency + ", dealMoney=" + dealMoney + ", closeCurrency=" + closeCurrency + ", closeMoney="
				+ closeMoney + ", cardNum=" + cardNum + "]";
	}
	private String taskid;
	private String dealDate;				//交易日期
	private String tallyDate;				//记账日期
	private String fourCardNum;				//卡号后四位
	private String dealDescription;			//交易描述
	private String dealCurrency;			//交易币种
	private String dealMoney;				//交易金额
	private String closeCurrency;			//结算币种
	private String closeMoney;				//结算金额
	private String cardNum;					//卡号
	
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getDealDate() {
		return dealDate;
	}
	public void setDealDate(String dealDate) {
		this.dealDate = dealDate;
	}
	public String getTallyDate() {
		return tallyDate;
	}
	public void setTallyDate(String tallyDate) {
		this.tallyDate = tallyDate;
	}
	public String getFourCardNum() {
		return fourCardNum;
	}
	public void setFourCardNum(String fourCardNum) {
		this.fourCardNum = fourCardNum;
	}
	public String getDealDescription() {
		return dealDescription;
	}
	public void setDealDescription(String dealDescription) {
		this.dealDescription = dealDescription;
	}
	public String getDealCurrency() {
		return dealCurrency;
	}
	public void setDealCurrency(String dealCurrency) {
		this.dealCurrency = dealCurrency;
	}
	public String getDealMoney() {
		return dealMoney;
	}
	public void setDealMoney(String dealMoney) {
		this.dealMoney = dealMoney;
	}
	public String getCloseCurrency() {
		return closeCurrency;
	}
	public void setCloseCurrency(String closeCurrency) {
		this.closeCurrency = closeCurrency;
	}
	public String getCloseMoney() {
		return closeMoney;
	}
	public void setCloseMoney(String closeMoney) {
		this.closeMoney = closeMoney;
	}
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

}
