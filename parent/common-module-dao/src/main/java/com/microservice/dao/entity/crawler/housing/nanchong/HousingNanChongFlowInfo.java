package com.microservice.dao.entity.crawler.housing.nanchong;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_nanchong_flowinfo",indexes = {@Index(name = "index_housing_nanchong_flowinfo_taskid", columnList = "taskid")})
public class HousingNanChongFlowInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = 351229914049113222L;
	private String taskid;
//	日期
	private String date;
//	提取金额
	private String extract;
//	存入金额
	private String storage;
//	余额
	private String balance;
//	摘要
	private String summary;
//	查询所属期
//	private String qrydaterange;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getExtract() {
		return extract;
	}
	public void setExtract(String extract) {
		this.extract = extract;
	}
	public String getStorage() {
		return storage;
	}
	public void setStorage(String storage) {
		this.storage = storage;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public HousingNanChongFlowInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
}
