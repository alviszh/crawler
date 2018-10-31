package com.microservice.dao.entity.crawler.housing.chenzhou;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 白山公积金用户信息
 */
@Entity
@Table(name="housing_chenzhou_paydetails")
public class HousingChenZhouPaydetails extends IdEntity implements Serializable {
	private static final long serialVersionUID = -758773159050632619L;

	private String useraccount;//	个人账号
	private String paymentDate;//	汇缴日期
	private String type;//	汇缴类型
	private String paymentMonth;//	汇缴年月
	private String income;//	收入
	private String expend;//	支出
	private String balance;//	余额
	private String taskid;	
	public String getUseraccount() {
		return useraccount;
	}
	public void setUseraccount(String useraccount) {
		this.useraccount = useraccount;
	}
	public String getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPaymentMonth() {
		return paymentMonth;
	}
	public void setPaymentMonth(String paymentMonth) {
		this.paymentMonth = paymentMonth;
	}
	public String getIncome() {
		return income;
	}
	public void setIncome(String income) {
		this.income = income;
	}
	public String getExpend() {
		return expend;
	}
	public void setExpend(String expend) {
		this.expend = expend;
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
	
	@Override
	public String toString() {
		return "HousingChenZhouPaydetails [useraccount=" + useraccount + ", paymentDate=" + paymentDate + ", type="
				+ type + ", paymentMonth=" + paymentMonth + ", income=" + income + ", expend=" + expend + ", balance="
				+ balance + ", taskid=" + taskid + "]";
	}
	
	public HousingChenZhouPaydetails(String useraccount, String paymentDate, String type, String paymentMonth,
			String income, String expend, String balance, String taskid) {
		super();
		this.useraccount = useraccount;
		this.paymentDate = paymentDate;
		this.type = type;
		this.paymentMonth = paymentMonth;
		this.income = income;
		this.expend = expend;
		this.balance = balance;
		this.taskid = taskid;
	}
	public HousingChenZhouPaydetails() {
		super();
		// TODO Auto-generated constructor stub
	}	
}
