package com.microservice.dao.entity.crawler.insurance.jian;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name = "insurance_jian_persion")
public class InsuranceJiAnPersion extends IdEntity implements Serializable{
	private static final long serialVersionUID = 5273473947864101643L;
	
	private String paymonth;//应缴年月
	private String base;//月缴费基数
	private String type;//缴费类型
	
	private String monthCount;//实缴月数
	private String personalPay;//个人缴费
	private String unitPay;//单位缴费
	
	private String intoPerson;//个人划入账户额
	private String intoCompany;//单位划入账户额
	private String accountSign;//到账标记
	private String accountMonth;//到账年月
	private String taskid;
	
	public String getPaymonth() {
		return paymonth;
	}
	public void setPaymonth(String paymonth) {
		this.paymonth = paymonth;
	}
	public String getBase() {
		return base;
	}
	public void setBase(String base) {
		this.base = base;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMonthCount() {
		return monthCount;
	}
	public void setMonthCount(String monthCount) {
		this.monthCount = monthCount;
	}
	public String getPersonalPay() {
		return personalPay;
	}
	public void setPersonalPay(String personalPay) {
		this.personalPay = personalPay;
	}
	public String getUnitPay() {
		return unitPay;
	}
	public void setUnitPay(String unitPay) {
		this.unitPay = unitPay;
	}
	public String getIntoPerson() {
		return intoPerson;
	}
	public void setIntoPerson(String intoPerson) {
		this.intoPerson = intoPerson;
	}
	public String getIntoCompany() {
		return intoCompany;
	}
	public void setIntoCompany(String intoCompany) {
		this.intoCompany = intoCompany;
	}
	public String getAccountSign() {
		return accountSign;
	}
	public void setAccountSign(String accountSign) {
		this.accountSign = accountSign;
	}

	public String getAccountMonth() {
		return accountMonth;
	}
	public void setAccountMonth(String accountMonth) {
		this.accountMonth = accountMonth;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	public InsuranceJiAnPersion(String paymonth, String base, String type, String monthCount, String personalPay,
			String unitPay, String intoPerson, String intoCompany, String accountSign,
			String accountMonth, String taskid) {
		super();
		this.paymonth = paymonth;
		this.base = base;
		this.type = type;
		this.monthCount = monthCount;
		this.personalPay = personalPay;
		this.unitPay = unitPay;
		this.intoPerson = intoPerson;
		this.intoCompany = intoCompany;
		this.accountSign = accountSign;
		this.accountMonth = accountMonth;
		this.taskid = taskid;
	}
	public InsuranceJiAnPersion() {
		super();
	}
	@Override
	public String toString() {
		return "InsuranceJiAnPersion [paymonth=" + paymonth + ", base=" + base + ", type=" + type + ", monthCount="
				+ monthCount + ", personalPay=" + personalPay + ", unitPay=" + unitPay + ", intoPerson=" + intoPerson
				+ ", intoCompany=" + intoCompany + ", accountSign=" + accountSign 
				+ ", accountMonth=" + accountMonth + ", taskid=" + taskid + "]";
	}
}
