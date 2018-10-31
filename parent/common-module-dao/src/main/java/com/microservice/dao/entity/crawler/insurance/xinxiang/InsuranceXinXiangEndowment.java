package com.microservice.dao.entity.crawler.insurance.xinxiang;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_xinxiang_endowment")
public class InsuranceXinXiangEndowment extends IdEntity{

	private String endDate;//结算期
	private String base;//缴费基数
	private String personalPay;//个人缴纳
	private String companyAccount;//单位缴划账户
	private String companyPay;//单位缴划统筹
	private String average;//社平账户
	private String payMoney;//应缴金额
	private String status;//缴费类型
	private String payFlag;//缴费标志
	private String getDate;//到账日期
	private String accountFlag;//划账户标志
	private String taskid;
	@Override
	public String toString() {
		return "InsuranceXinXiangEndowment [endDate=" + endDate + ", base=" + base + ", personalPay=" + personalPay
				+ ", companyAccount=" + companyAccount + ", companyPay=" + companyPay + ", average=" + average
				+ ", payMoney=" + payMoney + ", status=" + status + ", payFlag=" + payFlag + ", getDate=" + getDate
				+ ", accountFlag=" + accountFlag + ", taskid=" + taskid + "]";
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getBase() {
		return base;
	}
	public void setBase(String base) {
		this.base = base;
	}
	public String getPersonalPay() {
		return personalPay;
	}
	public void setPersonalPay(String personalPay) {
		this.personalPay = personalPay;
	}
	public String getCompanyAccount() {
		return companyAccount;
	}
	public void setCompanyAccount(String companyAccount) {
		this.companyAccount = companyAccount;
	}
	public String getCompanyPay() {
		return companyPay;
	}
	public void setCompanyPay(String companyPay) {
		this.companyPay = companyPay;
	}
	public String getAverage() {
		return average;
	}
	public void setAverage(String average) {
		this.average = average;
	}
	public String getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(String payMoney) {
		this.payMoney = payMoney;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPayFlag() {
		return payFlag;
	}
	public void setPayFlag(String payFlag) {
		this.payFlag = payFlag;
	}
	public String getGetDate() {
		return getDate;
	}
	public void setGetDate(String getDate) {
		this.getDate = getDate;
	}
	public String getAccountFlag() {
		return accountFlag;
	}
	public void setAccountFlag(String accountFlag) {
		this.accountFlag = accountFlag;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
}
