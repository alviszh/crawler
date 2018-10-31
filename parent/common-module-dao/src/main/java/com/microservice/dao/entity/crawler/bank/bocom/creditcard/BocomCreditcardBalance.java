package com.microservice.dao.entity.crawler.bank.bocom.creditcard;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**   
*    
* 项目名称：common-module-dao   
* 类名称：BocomCreditcardBalance   
* 类描述：   
* 创建人：hyx  
* 创建时间：2017年11月21日 上午11:25:20   
* @version        
*/
@Entity
@Table(name="bocom_creditcard_balance",indexes = {@Index(name = "index_bocom_creditcard_balance_taskid", columnList = "taskid")})
public class BocomCreditcardBalance extends IdEntity{

	private String payDate;//到期还款日
	private String currentRmb;//本期还款金额（RMB）
	private String currentDollar;//本期还款金额（DOLLAR）
	
	private String minimalPayRmb;//最低还款金额（RMB）
	private String minimalPayDollar;//最低还款金额（DOLLAR）
	
	private String creditRmb;//信用额度（RMB）
	private String creditDollar;//信用额度（DOLLAR）
	
	private String amountRmb;//取现额度（RMB）
	private String amountDollar;//取现额度（DOLLAR）
	
	private String taskid;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	public String getCurrentRmb() {
		return currentRmb;
	}
	public void setCurrentRmb(String currentRmb) {
		this.currentRmb = currentRmb;
	}
	public String getCurrentDollar() {
		return currentDollar;
	}
	public void setCurrentDollar(String currentDollar) {
		this.currentDollar = currentDollar;
	}
	public String getMinimalPayRmb() {
		return minimalPayRmb;
	}
	public void setMinimalPayRmb(String minimalPayRmb) {
		this.minimalPayRmb = minimalPayRmb;
	}
	public String getMinimalPayDollar() {
		return minimalPayDollar;
	}
	public void setMinimalPayDollar(String minimalPayDollar) {
		this.minimalPayDollar = minimalPayDollar;
	}
	public String getCreditRmb() {
		return creditRmb;
	}
	public void setCreditRmb(String creditRmb) {
		this.creditRmb = creditRmb;
	}
	public String getCreditDollar() {
		return creditDollar;
	}
	public void setCreditDollar(String creditDollar) {
		this.creditDollar = creditDollar;
	}
	public String getAmountRmb() {
		return amountRmb;
	}
	public void setAmountRmb(String amountRmb) {
		this.amountRmb = amountRmb;
	}
	public String getAmountDollar() {
		return amountDollar;
	}
	public void setAmountDollar(String amountDollar) {
		this.amountDollar = amountDollar;
	}
	@Override
	public String toString() {
		return "BocomCreditcardBalance [payDate=" + payDate + ", currentRmb=" + currentRmb + ", currentDollar="
				+ currentDollar + ", minimalPayRmb=" + minimalPayRmb + ", minimalPayDollar=" + minimalPayDollar
				+ ", creditRmb=" + creditRmb + ", creditDollar=" + creditDollar + ", amountRmb=" + amountRmb
				+ ", amountDollar=" + amountDollar + ", taskid=" + taskid + "]";
	}
	
	
}
