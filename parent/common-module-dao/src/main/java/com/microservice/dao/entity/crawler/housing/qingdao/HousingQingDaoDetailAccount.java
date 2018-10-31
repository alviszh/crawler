package com.microservice.dao.entity.crawler.housing.qingdao;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description:
 * @author: sln 
 * @date: 2017年10月25日 上午11:42:39 
 */
@Entity
@Table(name="housing_qingdao_detailaccount",indexes = {@Index(name = "index_housing_qingdao_detailaccount_taskid", columnList = "taskid")})
public class HousingQingDaoDetailAccount extends IdEntity implements Serializable {
	private static final long serialVersionUID = 6481831903414445397L;
	private String taskid;
//	明细笔次
	private String detailcount;
//	交易日期
	private String transdate;
//	业务类型
	private String businesstype;
//	所属年月
	private String belongtoyearmonth;
//	发生额
	private String amount;
//	余额
	private String balance;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getDetailcount() {
		return detailcount;
	}
	public void setDetailcount(String detailcount) {
		this.detailcount = detailcount;
	}
	public String getTransdate() {
		return transdate;
	}
	public void setTransdate(String transdate) {
		this.transdate = transdate;
	}
	public String getBusinesstype() {
		return businesstype;
	}
	public void setBusinesstype(String businesstype) {
		this.businesstype = businesstype;
	}
	public String getBelongtoyearmonth() {
		return belongtoyearmonth;
	}
	public void setBelongtoyearmonth(String belongtoyearmonth) {
		this.belongtoyearmonth = belongtoyearmonth;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public HousingQingDaoDetailAccount() {
		super();
		// TODO Auto-generated constructor stub
	}
	public HousingQingDaoDetailAccount(String taskid, String detailcount, String transdate, String businesstype,
			String belongtoyearmonth, String amount, String balance) {
		super();
		this.taskid = taskid;
		this.detailcount = detailcount;
		this.transdate = transdate;
		this.businesstype = businesstype;
		this.belongtoyearmonth = belongtoyearmonth;
		this.amount = amount;
		this.balance = balance;
	}
}
