package com.microservice.dao.entity.crawler.housing.guigang;

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
@Table(name="housing_guigang_userinfo",indexes = {@Index(name = "index_housing_guigang_userinfo_taskid", columnList = "taskid")})
public class HousingGuiGangUserInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = -5901921334712806791L;
	private String taskid;
//	个人账号
	private String accnum;
//	姓名
	private String name;
//	证件号码
	private String idnum;
//	账户状态
	private String accstatus;
//	余额
	private String balance;
//	开户日期
	private String opendate;
//	单位比例
	private String unitprop;
//	个人比例
	private String indiprop;
//	个人缴存基数
	private String indichargebasenum;
//	月缴存额
	private String monthcharge;
//	单位月缴存额
	private String unitmonthcharge;
//	个人月缴存额
	private String indimonthcharge;
//	缴至年月
	private String paytoyear;
//	单位账号
	private String unitaccnum;
//	单位名称
	private String unitname;
//	是否贷款
	private String isloan;
//	是否为共同借款人
	private String iscommonloan;
//	冲还贷标志
	private String repaymentsign;
//	联名卡号
	private String cardno;
//	借款合同号
	private String loancontractnum;
//	手机号码
	private String phonenum;
//	是否冻结
	private String isfreezed;
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
	public String getOpendate() {
		return opendate;
	}
	public void setOpendate(String opendate) {
		this.opendate = opendate;
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
	public String getIndichargebasenum() {
		return indichargebasenum;
	}
	public void setIndichargebasenum(String indichargebasenum) {
		this.indichargebasenum = indichargebasenum;
	}
	public String getMonthcharge() {
		return monthcharge;
	}
	public void setMonthcharge(String monthcharge) {
		this.monthcharge = monthcharge;
	}
	public String getUnitmonthcharge() {
		return unitmonthcharge;
	}
	public void setUnitmonthcharge(String unitmonthcharge) {
		this.unitmonthcharge = unitmonthcharge;
	}
	public String getIndimonthcharge() {
		return indimonthcharge;
	}
	public void setIndimonthcharge(String indimonthcharge) {
		this.indimonthcharge = indimonthcharge;
	}
	public String getPaytoyear() {
		return paytoyear;
	}
	public void setPaytoyear(String paytoyear) {
		this.paytoyear = paytoyear;
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
	public String getIsloan() {
		return isloan;
	}
	public void setIsloan(String isloan) {
		this.isloan = isloan;
	}
	public String getIscommonloan() {
		return iscommonloan;
	}
	public void setIscommonloan(String iscommonloan) {
		this.iscommonloan = iscommonloan;
	}
	public String getRepaymentsign() {
		return repaymentsign;
	}
	public void setRepaymentsign(String repaymentsign) {
		this.repaymentsign = repaymentsign;
	}
	public String getCardno() {
		return cardno;
	}
	public void setCardno(String cardno) {
		this.cardno = cardno;
	}
	public String getLoancontractnum() {
		return loancontractnum;
	}
	public void setLoancontractnum(String loancontractnum) {
		this.loancontractnum = loancontractnum;
	}
	public String getPhonenum() {
		return phonenum;
	}
	public void setPhonenum(String phonenum) {
		this.phonenum = phonenum;
	}
	public String getIsfreezed() {
		return isfreezed;
	}
	public void setIsfreezed(String isfreezed) {
		this.isfreezed = isfreezed;
	}
	public HousingGuiGangUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public HousingGuiGangUserInfo(String taskid, String accnum, String name, String idnum, String accstatus,
			String balance, String opendate, String unitprop, String indiprop, String indichargebasenum,
			String monthcharge, String unitmonthcharge, String indimonthcharge, String paytoyear, String unitaccnum,
			String unitname, String isloan, String iscommonloan, String repaymentsign, String cardno,
			String loancontractnum, String phonenum, String isfreezed) {
		super();
		this.taskid = taskid;
		this.accnum = accnum;
		this.name = name;
		this.idnum = idnum;
		this.accstatus = accstatus;
		this.balance = balance;
		this.opendate = opendate;
		this.unitprop = unitprop;
		this.indiprop = indiprop;
		this.indichargebasenum = indichargebasenum;
		this.monthcharge = monthcharge;
		this.unitmonthcharge = unitmonthcharge;
		this.indimonthcharge = indimonthcharge;
		this.paytoyear = paytoyear;
		this.unitaccnum = unitaccnum;
		this.unitname = unitname;
		this.isloan = isloan;
		this.iscommonloan = iscommonloan;
		this.repaymentsign = repaymentsign;
		this.cardno = cardno;
		this.loancontractnum = loancontractnum;
		this.phonenum = phonenum;
		this.isfreezed = isfreezed;
	}
}
