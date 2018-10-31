package com.microservice.dao.entity.crawler.housing.guilin;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "housing_guilin_pay",indexes = {@Index(name = "index_housing_guilin_pay_taskid", columnList = "taskid")})
public class HousingGuiLinPay extends IdEntity implements Serializable {

	private String taskid;
	
	private String accountstime;//记账日期
	
	private String abstracts;//摘要
	
	private String reduce;//减少金额
	
	private String increase;//增加金额
	
	private String total;//累计金额

	private String name;
	
	private String idcard;
	
	
	
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

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getAccountstime() {
		return accountstime;
	}

	public void setAccountstime(String accountstime) {
		this.accountstime = accountstime;
	}

	public String getAbstracts() {
		return abstracts;
	}

	public void setAbstracts(String abstracts) {
		this.abstracts = abstracts;
	}

	public String getReduce() {
		return reduce;
	}

	public void setReduce(String reduce) {
		this.reduce = reduce;
	}

	public String getIncrease() {
		return increase;
	}

	public void setIncrease(String increase) {
		this.increase = increase;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public HousingGuiLinPay(String taskid, String accountstime, String abstracts, String reduce, String increase,
			String total, String name, String idcard) {
		super();
		this.taskid = taskid;
		this.accountstime = accountstime;
		this.abstracts = abstracts;
		this.reduce = reduce;
		this.increase = increase;
		this.total = total;
		this.name = name;
		this.idcard = idcard;
	}

	public HousingGuiLinPay() {
		super();
	}

	@Override
	public String toString() {
		return "HousingGuiLinPay [taskid=" + taskid + ", accountstime=" + accountstime + ", abstracts=" + abstracts
				+ ", reduce=" + reduce + ", increase=" + increase + ", total=" + total + ", name=" + name + ", idcard="
				+ idcard + "]";
	}
	
	
	
}
