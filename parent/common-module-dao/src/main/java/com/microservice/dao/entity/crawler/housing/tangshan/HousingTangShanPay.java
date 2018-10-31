package com.microservice.dao.entity.crawler.housing.tangshan;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;
@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "housing_tangshan_pay",indexes = {@Index(name = "index_housing_tangshan_pay_taskid", columnList = "taskid")})
public class HousingTangShanPay extends IdEntity implements Serializable{

	private static final long serialVersionUID = -1533831451520301916L;
	
	private String taskid;
	
	private String num;//个人帐号
	
	private String accounting_date;//记账日期
	
	private String abstracts;//摘要
	
	private String type;//业务明细类型
	
	private String accrual;//发送额
	
	private String balance;//账户余额
	
	private String deal_type;//交易类型

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getAccounting_date() {
		return accounting_date;
	}

	public void setAccounting_date(String accounting_date) {
		this.accounting_date = accounting_date;
	}

	public String getAbstracts() {
		return abstracts;
	}

	public void setAbstracts(String abstracts) {
		this.abstracts = abstracts;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAccrual() {
		return accrual;
	}

	public void setAccrual(String accrual) {
		this.accrual = accrual;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getDeal_type() {
		return deal_type;
	}

	public void setDeal_type(String deal_type) {
		this.deal_type = deal_type;
	}

	public HousingTangShanPay(String taskid, String num, String accounting_date, String abstracts, String type,
			String accrual, String balance, String deal_type) {
		super();
		this.taskid = taskid;
		this.num = num;
		this.accounting_date = accounting_date;
		this.abstracts = abstracts;
		this.type = type;
		this.accrual = accrual;
		this.balance = balance;
		this.deal_type = deal_type;
	}

	public HousingTangShanPay() {
		super();
	}

	@Override
	public String toString() {
		return "HousingTangShanUnit [taskid=" + taskid + ", num=" + num + ", accounting_date=" + accounting_date
				+ ", abstracts=" + abstracts + ", type=" + type + ", accrual=" + accrual + ", balance=" + balance
				+ ", deal_type=" + deal_type + "]";
	}
	
	
	
	
	
}
