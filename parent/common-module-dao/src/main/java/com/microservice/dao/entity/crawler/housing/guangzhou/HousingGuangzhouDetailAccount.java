package com.microservice.dao.entity.crawler.housing.guangzhou;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 广州住房公积金明细账目信息
 * @author: sln 
 * @date: 2017年9月29日 上午10:06:07 
 */
@Entity
@Table(name="housing_guangzhou_detailaccount",indexes = {@Index(name = "index_housing_guangzhou_detailaccount_taskid", columnList = "taskid")})
public class HousingGuangzhouDetailAccount extends IdEntity implements Serializable {
	private static final long serialVersionUID = -7750906250664461048L;
	private String taskid;
//	查询开始日期
	private String startdate;
//	查询截止日期
	private String endate;
//	序号
	private String sortnum;
//	记账日期
	private String accountdate;
//	业务类型
	private String businesstype;
//	汇补缴年月
	private String payearmonth;
//	发生额（元）
	private String amount;
//	余额（元）
	private String balance;
//	到账日期
	private String reachaccountdate;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	public String getEndate() {
		return endate;
	}
	public void setEndate(String endate) {
		this.endate = endate;
	}
	public String getSortnum() {
		return sortnum;
	}
	public void setSortnum(String sortnum) {
		this.sortnum = sortnum;
	}
	public String getAccountdate() {
		return accountdate;
	}
	public void setAccountdate(String accountdate) {
		this.accountdate = accountdate;
	}
	public String getBusinesstype() {
		return businesstype;
	}
	public void setBusinesstype(String businesstype) {
		this.businesstype = businesstype;
	}
	public String getPayearmonth() {
		return payearmonth;
	}
	public void setPayearmonth(String payearmonth) {
		this.payearmonth = payearmonth;
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
	public String getReachaccountdate() {
		return reachaccountdate;
	}
	public void setReachaccountdate(String reachaccountdate) {
		this.reachaccountdate = reachaccountdate;
	}
	public HousingGuangzhouDetailAccount() {
		super();
		// TODO Auto-generated constructor stub
	}
	public HousingGuangzhouDetailAccount(String taskid, String startdate, String endate, String sortnum,
			String accountdate, String businesstype, String payearmonth, String amount, String balance,
			String reachaccountdate) {
		super();
		this.taskid = taskid;
		this.startdate = startdate;
		this.endate = endate;
		this.sortnum = sortnum;
		this.accountdate = accountdate;
		this.businesstype = businesstype;
		this.payearmonth = payearmonth;
		this.amount = amount;
		this.balance = balance;
		this.reachaccountdate = reachaccountdate;
	}
}
