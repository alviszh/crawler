package com.microservice.dao.entity.crawler.bank.bocom.creditcard;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**   
*    
* 项目名称：common-module-dao   
* 类名称：BocomCreditcardTransFlow   
* 类描述：   
* 创建人：hyx  
* 创建时间：2017年11月21日 上午10:54:13   
* @version        
*/
@Entity
@Table(name="bocom_creditcard_transflow",indexes = {@Index(name = "index_bocom_creditcard_transflow_taskid", columnList = "taskid")})
public class BocomCreditcardTransFlow extends IdEntity{

	private String tradeDate; //交易日期
	private String accountDate;//记账日期
	
	@Column(name="tranExplain")
	private String explain;//交易说明
	private String tradeCurrencyAmount;//交易币种/金额
	private String clearingCurrencyAmount;//清算币种/金额
	
	private String taskid;
	
	
	
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getTradeDate() {
		return tradeDate;
	}
	public void setTradeDate(String tradeDate) {
		this.tradeDate = tradeDate;
	}
	public String getAccountDate() {
		return accountDate;
	}
	public void setAccountDate(String accountDate) {
		this.accountDate = accountDate;
	}
	public String getExplain() {
		return explain;
	}
	public void setExplain(String explain) {
		this.explain = explain;
	}
	public String getTradeCurrencyAmount() {
		return tradeCurrencyAmount;
	}
	public void setTradeCurrencyAmount(String tradeCurrencyAmount) {
		this.tradeCurrencyAmount = tradeCurrencyAmount;
	}
	public String getClearingCurrencyAmount() {
		return clearingCurrencyAmount;
	}
	public void setClearingCurrencyAmount(String clearingCurrencyAmount) {
		this.clearingCurrencyAmount = clearingCurrencyAmount;
	}
	@Override
	public String toString() {
		return "BocomCreditcardTransFlow [tradeDate=" + tradeDate + ", accountDate=" + accountDate + ", explain="
				+ explain + ", tradeCurrencyAmount=" + tradeCurrencyAmount + ", clearingCurrencyAmount="
				+ clearingCurrencyAmount + ", taskid=" + taskid + "]";
	}
	
	
}
