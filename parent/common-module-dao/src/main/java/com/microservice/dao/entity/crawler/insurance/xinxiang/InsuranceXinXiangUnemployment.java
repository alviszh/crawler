package com.microservice.dao.entity.crawler.insurance.xinxiang;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_xinxiang_unemployment")
public class InsuranceXinXiangUnemployment extends IdEntity{

	private String datea;//应缴年月           
	private String base;//缴费基数               
	private String personalPay;//个人缴纳        
	private String companyAccount;//单位缴划账户   
	private String payMoney;//应缴金额           
	private String payFlag;//缴费标志            
	private String taskid;
	@Override
	public String toString() {
		return "InsuranceXinXiangUnemployment [datea=" + datea + ", base=" + base + ", personalPay=" + personalPay
				+ ", companyAccount=" + companyAccount + ", payMoney=" + payMoney + ", payFlag=" + payFlag + ", taskid="
				+ taskid + "]";
	}
	public String getDatea() {
		return datea;
	}
	public void setDatea(String datea) {
		this.datea = datea;
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
	public String getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(String payMoney) {
		this.payMoney = payMoney;
	}
	public String getPayFlag() {
		return payFlag;
	}
	public void setPayFlag(String payFlag) {
		this.payFlag = payFlag;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
}
