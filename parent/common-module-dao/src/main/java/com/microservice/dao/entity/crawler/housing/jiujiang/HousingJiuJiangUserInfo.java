package com.microservice.dao.entity.crawler.housing.jiujiang;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "housing_jiujiang_userinfo",indexes = {@Index(name = "index_housing_jiujiang_userinfo_taskid", columnList = "taskid")})
public class HousingJiuJiangUserInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = 8345021305379569011L;

	private String taskid;
	
	private String name;
	
	private String idcard;
	
	private String dw_name;
	
	private String num;
	
	private String paymentbase;
	
	private String paymentmonth;
	
	private String balance;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getDw_name() {
		return dw_name;
	}

	public void setDw_name(String dw_name) {
		this.dw_name = dw_name;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getPaymentbase() {
		return paymentbase;
	}

	public void setPaymentbase(String paymentbase) {
		this.paymentbase = paymentbase;
	}

	public String getPaymentmonth() {
		return paymentmonth;
	}

	public void setPaymentmonth(String paymentmonth) {
		this.paymentmonth = paymentmonth;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public HousingJiuJiangUserInfo(String taskid, String name, String idcard, String dw_name, String num,
			String paymentbase, String paymentmonth, String balance) {
		super();
		this.taskid = taskid;
		this.name = name;
		this.idcard = idcard;
		this.dw_name = dw_name;
		this.num = num;
		this.paymentbase = paymentbase;
		this.paymentmonth = paymentmonth;
		this.balance = balance;
	}

	public HousingJiuJiangUserInfo() {
		super();
	}

	@Override
	public String toString() {
		return "HousingJiuJiangUserInfo [taskid=" + taskid + ", name=" + name + ", idcard=" + idcard + ", dw_name="
				+ dw_name + ", num=" + num + ", paymentbase=" + paymentbase + ", paymentmonth=" + paymentmonth
				+ ", balance=" + balance + "]";
	}
	
	
}
