package com.microservice.dao.entity.crawler.housing.yuxi;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "housing_yuxi_pay",indexes = {@Index(name = "index_housing_yuxi_pay_taskid", columnList = "taskid")})
public class HousingYuXiPay extends IdEntity implements Serializable {

	private String taskid;
	private String jndate;//日期
	private String abstracts;//摘要
	private String income;//收入
	private String expenditure;//支出
	private String balance;//余额
	private String num;//帐号
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
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
	public String getIncome() {
		return income;
	}
	public void setIncome(String income) {
		this.income = income;
	}
	public String getExpenditure() {
		return expenditure;
	}
	public void setExpenditure(String expenditure) {
		this.expenditure = expenditure;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public HousingYuXiPay(String taskid, String jndate, String abstracts, String income, String expenditure,
			String balance, String num) {
		super();
		this.taskid = taskid;
		this.jndate = jndate;
		this.abstracts = abstracts;
		this.income = income;
		this.expenditure = expenditure;
		this.balance = balance;
		this.num = num;
	}
	public HousingYuXiPay() {
		super();
	}
	@Override
	public String toString() {
		return "HousingYuXiPay [taskid=" + taskid + ", jndate=" + jndate + ", abstracts=" + abstracts + ", income="
				+ income + ", expenditure=" + expenditure + ", balance=" + balance + ", num=" + num + "]";
	}
	
	
	
	
}
