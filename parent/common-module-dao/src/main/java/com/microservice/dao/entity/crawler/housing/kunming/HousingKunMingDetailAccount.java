package com.microservice.dao.entity.crawler.housing.kunming;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 个人明细
 * @author: sln 
 */
@Entity
@Table(name="housing_kunming_detailaccount",indexes = {@Index(name = "index_housing_kunming_detailaccount_taskid", columnList = "taskid")})
public class HousingKunMingDetailAccount extends IdEntity implements Serializable {
	private static final long serialVersionUID = 2044745879268491789L;
	private String taskid;
//	交易日期
	private String transdate;
//	借方发生额
	private String debitamount;
//	贷方发生额
	private String creditamount;
//	余额
	private String balance;
//	起始日期
	private String startdate;
//	终止日期
	private String enddate;
//	经办机构
	private String agencies;
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
	public String getDebitamount() {
		return debitamount;
	}
	public void setDebitamount(String debitamount) {
		this.debitamount = debitamount;
	}
	public String getCreditamount() {
		return creditamount;
	}
	public void setCreditamount(String creditamount) {
		this.creditamount = creditamount;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
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
	public String getAgencies() {
		return agencies;
	}
	public void setAgencies(String agencies) {
		this.agencies = agencies;
	}
	public HousingKunMingDetailAccount() {
		super();
		// TODO Auto-generated constructor stub
	}
	public HousingKunMingDetailAccount(String taskid, String transdate, String debitamount, String creditamount,
			String balance, String startdate, String enddate, String agencies) {
		super();
		this.taskid = taskid;
		this.transdate = transdate;
		this.debitamount = debitamount;
		this.creditamount = creditamount;
		this.balance = balance;
		this.startdate = startdate;
		this.enddate = enddate;
		this.agencies = agencies;
	}
}
