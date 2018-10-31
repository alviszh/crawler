package com.microservice.dao.entity.crawler.bank.icbcchina;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="icbcchina_creditcard_monthbill" ,indexes = {@Index(name = "index_icbcchina_creditcard_monthbill_taskid", columnList = "taskid")})
public class IcbcChinaCreditCardMonthbill extends IdEntity{

	private String taskid;//uuid 前端通过uuid访问状态结果
	private String cardNum;							//卡号
	private String repayDate;						//还款日
	private String billingCycle;					//账单周期
	private String billDay;							//账单生成日
	private String cardLastNum;						//卡号后四位
	private String currency;						//币种
	private String repay;							//应还款额
	private String repayMin;						//最低还款额
	private String creditLine;						//信用额度
	private String integral;						//积分
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getRepayDate() {
		return repayDate;
	}
	public void setRepayDate(String repayDate) {
		this.repayDate = repayDate;
	}
	public String getBillingCycle() {
		return billingCycle;
	}
	public void setBillingCycle(String billingCycle) {
		this.billingCycle = billingCycle;
	}
	public String getCardLastNum() {
		return cardLastNum;
	}
	public void setCardLastNum(String cardLastNum) {
		this.cardLastNum = cardLastNum;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getRepay() {
		return repay;
	}
	public void setRepay(String repay) {
		this.repay = repay;
	}
	public String getRepayMin() {
		return repayMin;
	}
	public void setRepayMin(String repayMin) {
		this.repayMin = repayMin;
	}
	public String getCreditLine() {
		return creditLine;
	}
	public void setCreditLine(String creditLine) {
		this.creditLine = creditLine;
	}
	public String getIntegral() {
		return integral;
	}
	public void setIntegral(String integral) {
		this.integral = integral;
	}
	public String getBillDay() {
		return billDay;
	}
	public void setBillDay(String billDay) {
		this.billDay = billDay;
	}
	@Override
	public String toString() {
		return "IcbcChinaCreditCardMonthbill [taskid=" + taskid + ", cardNum=" + cardNum + ", repayDate=" + repayDate
				+ ", billingCycle=" + billingCycle + ", billDay=" + billDay + ", cardLastNum=" + cardLastNum
				+ ", currency=" + currency + ", repay=" + repay + ", repayMin=" + repayMin + ", creditLine="
				+ creditLine + ", integral=" + integral + "]";
	}
	
	
	
}
