package com.microservice.dao.entity.crawler.housing.sz.fujian;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_sz_fujian_pay",indexes = {@Index(name = "index_housing_sz_fujian_pay_taskid", columnList = "taskid")})
public class HousingSZFuJianPay extends IdEntity implements Serializable{
	private String number;           //流水号
	private String type;             //账户类型
	private String account;          //个人账号
	private String years;            //发生日期
	private String abstra;           //摘要备注
	private String occurrence;       //发生金额
	private String balance;          //余额
	
	private String taskid;
	
	@Override
	public String toString() {
		return "HousingSZFuJianPay [number=" + number + ", type=" + type + ", account=" + account
				+ ", years=" + years + ", abstra=" + abstra + ", occurrence=" + occurrence
				+ ", balance=" + balance+ ", taskid=" + taskid + "]";
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getYears() {
		return years;
	}

	public void setYears(String years) {
		this.years = years;
	}

	public String getAbstra() {
		return abstra;
	}

	public void setAbstra(String abstra) {
		this.abstra = abstra;
	}

	public String getOccurrence() {
		return occurrence;
	}

	public void setOccurrence(String occurrence) {
		this.occurrence = occurrence;
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
