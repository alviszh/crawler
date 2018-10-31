package com.microservice.dao.entity.crawler.housing.huhehaote;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_huhehaote_detailaccount",indexes = {@Index(name = "index_housing_huhehaote_detailaccount_taskid", columnList = "taskid")})
public class HousingHuHeHaoTeDetailAccount extends IdEntity implements Serializable {
	private static final long serialVersionUID = 5489158452811499470L;
	private String taskid;
//	交易日期
	private String transdate;
//	借方发生额
	private String debitamount;
//	贷方发生额
	private String creditamount;
//	余额
	private String balance;
//	单位账号
	private String unitaccnum;
//	单位名称
	private String unitaccname;
//	汇缴起始日期
	private String begindate;
//	汇缴终止日期
	private String enddate;
//	交易状态
	private String transtatus;
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
	public String getUnitaccnum() {
		return unitaccnum;
	}
	public void setUnitaccnum(String unitaccnum) {
		this.unitaccnum = unitaccnum;
	}
	public String getUnitaccname() {
		return unitaccname;
	}
	public void setUnitaccname(String unitaccname) {
		this.unitaccname = unitaccname;
	}
	public String getBegindate() {
		return begindate;
	}
	public void setBegindate(String begindate) {
		this.begindate = begindate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public String getTranstatus() {
		return transtatus;
	}
	public void setTranstatus(String transtatus) {
		this.transtatus = transtatus;
	}
	public HousingHuHeHaoTeDetailAccount() {
		super();
	}
}
