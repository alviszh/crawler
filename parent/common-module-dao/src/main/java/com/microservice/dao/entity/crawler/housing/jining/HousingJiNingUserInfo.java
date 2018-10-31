package com.microservice.dao.entity.crawler.housing.jining;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 济宁市公积金个人基本信息
 * @author: sln 
 * @date: 
 */
@Entity
@Table(name="housing_jining_userinfo",indexes = {@Index(name = "index_housing_jining_userinfo_taskid", columnList = "taskid")})
public class HousingJiNingUserInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = -4523112843732781341L;
	private String taskid;
//	个人姓名
	private String accname;
//	个人账号
	private String accnum;
//	身份证号
	private String certinum;
//	单位账号
	private String unitaccnum;
//	单位名称
	private String unitaccname;
//	开户日期
	private String opendate;
//	公积金余额
	private String balance;
//	最后汇缴月
	private String lastpaydate;
//	账户状态
	private String accountstate;
//	账户机构
	private String accountorganization;
//	缴存基数
	private String paybasenum;
//	单位比例
	private String unitprop;
//	个人比例
	private String indiprop;
//	个人月缴存额
	private String permonthcharge;
//	是否有贷款
	private String isloan;
//	最后提取日期
	private String lastextractdate;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getAccname() {
		return accname;
	}
	public void setAccname(String accname) {
		this.accname = accname;
	}
	public String getAccnum() {
		return accnum;
	}
	public void setAccnum(String accnum) {
		this.accnum = accnum;
	}
	public String getCertinum() {
		return certinum;
	}
	public void setCertinum(String certinum) {
		this.certinum = certinum;
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
	public String getOpendate() {
		return opendate;
	}
	public void setOpendate(String opendate) {
		this.opendate = opendate;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getLastpaydate() {
		return lastpaydate;
	}
	public void setLastpaydate(String lastpaydate) {
		this.lastpaydate = lastpaydate;
	}
	public String getAccountstate() {
		return accountstate;
	}
	public void setAccountstate(String accountstate) {
		this.accountstate = accountstate;
	}
	public String getAccountorganization() {
		return accountorganization;
	}
	public void setAccountorganization(String accountorganization) {
		this.accountorganization = accountorganization;
	}
	public String getPaybasenum() {
		return paybasenum;
	}
	public void setPaybasenum(String paybasenum) {
		this.paybasenum = paybasenum;
	}
	public String getUnitprop() {
		return unitprop;
	}
	public void setUnitprop(String unitprop) {
		this.unitprop = unitprop;
	}
	public String getIndiprop() {
		return indiprop;
	}
	public void setIndiprop(String indiprop) {
		this.indiprop = indiprop;
	}
	public String getPermonthcharge() {
		return permonthcharge;
	}
	public void setPermonthcharge(String permonthcharge) {
		this.permonthcharge = permonthcharge;
	}
	public String getIsloan() {
		return isloan;
	}
	public void setIsloan(String isloan) {
		this.isloan = isloan;
	}
	public String getLastextractdate() {
		return lastextractdate;
	}
	public void setLastextractdate(String lastextractdate) {
		this.lastextractdate = lastextractdate;
	}
	public HousingJiNingUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public HousingJiNingUserInfo(String taskid, String accname, String accnum, String certinum, String unitaccnum,
			String unitaccname, String opendate, String balance, String lastpaydate, String accountstate,
			String accountorganization, String paybasenum, String unitprop, String indiprop, String permonthcharge,
			String isloan, String lastextractdate) {
		super();
		this.taskid = taskid;
		this.accname = accname;
		this.accnum = accnum;
		this.certinum = certinum;
		this.unitaccnum = unitaccnum;
		this.unitaccname = unitaccname;
		this.opendate = opendate;
		this.balance = balance;
		this.lastpaydate = lastpaydate;
		this.accountstate = accountstate;
		this.accountorganization = accountorganization;
		this.paybasenum = paybasenum;
		this.unitprop = unitprop;
		this.indiprop = indiprop;
		this.permonthcharge = permonthcharge;
		this.isloan = isloan;
		this.lastextractdate = lastextractdate;
	}
}
