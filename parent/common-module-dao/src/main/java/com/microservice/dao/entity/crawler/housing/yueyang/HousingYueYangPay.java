package com.microservice.dao.entity.crawler.housing.yueyang;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name  ="housing_yueyang_pay",indexes = {@Index(name = "index_housing_yueyang_pay_taskid", columnList = "taskid")})
public class HousingYueYangPay extends IdEntity implements Serializable{
	private String jndate;                 //记账日期
	private String abstracts;              //归集和提取业务明细类型
	private String payDate;				   //汇补缴年月
	private String pay;                    //收入
	private String extract;			       //支出
	private String interest;               //利息
	private String balance;				   //个人账户余额
	private String companyName;             //单位名称
	
	private String taskid;
	
	@Override
	public String toString() {
		return "HousingYueYangPay [jndate=" + jndate + ", abstracts=" + abstracts + ", payDate=" + payDate 
				+ ", pay=" + pay + ", extract=" + extract + ", interest=" + interest
				+ ", balance=" + balance + ", companyName=" + companyName + ", taskid=" + taskid + "]";
	}

	public String getJndate() {
		return jndate;
	}

	public void setJndate(String jndate) {
		this.jndate = jndate;
	}

	public String getAbstracts() {
		return abstracts;
	}

	public void setAbstracts(String abstracts) {
		this.abstracts = abstracts;
	}

	public String getPayDate() {
		return payDate;
	}

	public void setPayDate(String payDate) {
		this.payDate = payDate;
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

	public String getInterest() {
		return interest;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}


	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
}
