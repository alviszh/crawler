package com.microservice.dao.entity.crawler.housing.kunming;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description:
 * @author: sln 
 */
@Entity
@Table(name="housing_kunming_userinfo",indexes = {@Index(name = "index_housing_kunming_userinfo_taskid", columnList = "taskid")})
public class HousingKunMingUserInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = -5901921334712806791L;
	private String taskid;
//	公积金账号
	private String accnum;
//	姓名
	private String name;
//	证件号码
	private String idnum;
//	账户状态
	private String accstatus;
//	余额
	private String balance;
//	最后汇缴月
	private String lastpaymonth;
//	上次提取日
	private String lastextractday;
//	缴存基数
	private String chargebasenum;
//	月汇缴额合计
	private String monthcharge;
//	个人比例
	private String indiprop;
//	单位比例
	private String unitprop;
//	单位账号
	private String unitaccnum;
//	单位名称
	private String unitname;
//	所属机构
	private String organization; 
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getAccnum() {
		return accnum;
	}
	public void setAccnum(String accnum) {
		this.accnum = accnum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getAccstatus() {
		return accstatus;
	}
	public void setAccstatus(String accstatus) {
		this.accstatus = accstatus;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getLastpaymonth() {
		return lastpaymonth;
	}
	public void setLastpaymonth(String lastpaymonth) {
		this.lastpaymonth = lastpaymonth;
	}
	public String getLastextractday() {
		return lastextractday;
	}
	public void setLastextractday(String lastextractday) {
		this.lastextractday = lastextractday;
	}
	public String getChargebasenum() {
		return chargebasenum;
	}
	public void setChargebasenum(String chargebasenum) {
		this.chargebasenum = chargebasenum;
	}
	public String getMonthcharge() {
		return monthcharge;
	}
	public void setMonthcharge(String monthcharge) {
		this.monthcharge = monthcharge;
	}
	public String getIndiprop() {
		return indiprop;
	}
	public void setIndiprop(String indiprop) {
		this.indiprop = indiprop;
	}
	public String getUnitprop() {
		return unitprop;
	}
	public void setUnitprop(String unitprop) {
		this.unitprop = unitprop;
	}
	public String getUnitaccnum() {
		return unitaccnum;
	}
	public void setUnitaccnum(String unitaccnum) {
		this.unitaccnum = unitaccnum;
	}
	public String getUnitname() {
		return unitname;
	}
	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}
	
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public HousingKunMingUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public HousingKunMingUserInfo(String taskid, String accnum, String name, String idnum, String accstatus,
			String balance, String lastpaymonth, String lastextractday, String chargebasenum, String monthcharge,
			String indiprop, String unitprop, String unitaccnum, String unitname, String organization) {
		super();
		this.taskid = taskid;
		this.accnum = accnum;
		this.name = name;
		this.idnum = idnum;
		this.accstatus = accstatus;
		this.balance = balance;
		this.lastpaymonth = lastpaymonth;
		this.lastextractday = lastextractday;
		this.chargebasenum = chargebasenum;
		this.monthcharge = monthcharge;
		this.indiprop = indiprop;
		this.unitprop = unitprop;
		this.unitaccnum = unitaccnum;
		this.unitname = unitname;
		this.organization = organization;
	}
}
