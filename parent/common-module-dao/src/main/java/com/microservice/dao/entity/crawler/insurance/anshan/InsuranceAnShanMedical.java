package com.microservice.dao.entity.crawler.insurance.anshan;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_anshan_medical",indexes = {@Index(name = "index_insurance_anshan_medical_taskid", columnList = "taskid")})
public class InsuranceAnShanMedical extends IdEntity{
	private String payMonth;					//年度
	private String carryover;					//上年结转金额
	private String payPerson;					//本年帐户个人缴费部分本金
	private String payDepartment;				//本年帐户单位缴费划拨部分本金
	private String injection;                   //本年帐户注入金额
	private String cumulative;                  //帐户支付累计金额
	private String balance;                     //帐户结余金额
	private String taskid;
	
	@Override
	public String toString() {
		return "InsuranceAnShanMedical [payMonth=" + payMonth + ", carryover=" + carryover + ", injection="
				+ injection + ", payDepartment=" + payDepartment + ", payPerson=" + payPerson
				+ ", cumulative=" + cumulative + ", balance=" + balance + ", taskid=" + taskid + "]";
	}

	public String getPayMonth() {
		return payMonth;
	}

	public void setPayMonth(String payMonth) {
		this.payMonth = payMonth;
	}

	public String getCarryover() {
		return carryover;
	}

	public void setCarryover(String carryover) {
		this.carryover = carryover;
	}

	public String getPayPerson() {
		return payPerson;
	}

	public void setPayPerson(String payPerson) {
		this.payPerson = payPerson;
	}

	public String getPayDepartment() {
		return payDepartment;
	}

	public void setPayDepartment(String payDepartment) {
		this.payDepartment = payDepartment;
	}

	public String getInjection() {
		return injection;
	}

	public void setInjection(String injection) {
		this.injection = injection;
	}

	public String getCumulative() {
		return cumulative;
	}

	public void setCumulative(String cumulative) {
		this.cumulative = cumulative;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
}
