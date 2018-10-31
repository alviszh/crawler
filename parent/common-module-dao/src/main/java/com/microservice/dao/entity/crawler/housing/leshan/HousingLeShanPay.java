package com.microservice.dao.entity.crawler.housing.leshan;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "housing_leshan_pay",indexes = {@Index(name = "index_housing_leshan_pay_taskid", columnList = "taskid")})
public class HousingLeShanPay extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String taskid;
	
	private String paydate;
	
	private String accountingtime;
	
	private String dwname;
	
	private String pay;
	
	private String extract;
	
	private String balance;
	
	private String businesstype;
	
	private String grnum;
	
	private String name;
	
	private String gsnum;

	public String getGrnum() {
		return grnum;
	}

	public void setGrnum(String grnum) {
		this.grnum = grnum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGsnum() {
		return gsnum;
	}

	public void setGsnum(String gsnum) {
		this.gsnum = gsnum;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getPaydate() {
		return paydate;
	}

	public void setPaydate(String paydate) {
		this.paydate = paydate;
	}

	public String getAccountingtime() {
		return accountingtime;
	}

	public void setAccountingtime(String accountingtime) {
		this.accountingtime = accountingtime;
	}

	public String getDwname() {
		return dwname;
	}

	public void setDwname(String dwname) {
		this.dwname = dwname;
	}

	public String getPay() {
		return pay;
	}

	public void setPay(String pay) {
		this.pay = pay;
	}

	public String getExtract() {
		return extract;
	}

	public void setExtract(String extract) {
		this.extract = extract;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getBusinesstype() {
		return businesstype;
	}

	public void setBusinesstype(String businesstype) {
		this.businesstype = businesstype;
	}

	public HousingLeShanPay(String taskid, String paydate, String accountingtime, String dwname, String pay,
			String extract, String balance, String businesstype, String grnum, String name, String gsnum) {
		super();
		this.taskid = taskid;
		this.paydate = paydate;
		this.accountingtime = accountingtime;
		this.dwname = dwname;
		this.pay = pay;
		this.extract = extract;
		this.balance = balance;
		this.businesstype = businesstype;
		this.grnum = grnum;
		this.name = name;
		this.gsnum = gsnum;
	}

	public HousingLeShanPay() {
		super();
	}

	@Override
	public String toString() {
		return "HousingLeShanPay [taskid=" + taskid + ", paydate=" + paydate + ", accountingtime=" + accountingtime
				+ ", dwname=" + dwname + ", pay=" + pay + ", extract=" + extract + ", balance=" + balance
				+ ", businesstype=" + businesstype + ", grnum=" + grnum + ", name=" + name + ", gsnum=" + gsnum + "]";
	}

	
	
}
