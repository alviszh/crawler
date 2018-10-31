package com.microservice.dao.entity.crawler.housing.maanshan;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 马鞍山市公积金明细账信息
 * @author: sln
 * @date:
 */
@Entity
@Table(name = "housing_maanshan_detailaccount")
public class HousingMaAnShanDetailAccount extends IdEntity implements Serializable {
	private static final long serialVersionUID = 6481831903414445397L;
	private String taskid;
//	交易日期
	private String transdate;
//	业务类型
	private String businesstype;
//	发生额
	private String amount;
//	开始年月
	private String startdate;
//	终止年月
	private String enddate;
//	备注
	private String note;
//	入账状态
	private String reachstatus;
//	余额
	private String balance;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
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
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getReachstatus() {
		return reachstatus;
	}
	public void setReachstatus(String reachstatus) {
		this.reachstatus = reachstatus;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public HousingMaAnShanDetailAccount() {
		super();
		// TODO Auto-generated constructor stub
	}
}
