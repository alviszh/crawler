package com.microservice.dao.entity.crawler.housing.puyang;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_puyang_userinfo",indexes = {@Index(name = "index_housing_puyang_userinfo_taskid", columnList = "taskid")})
public class HousingPuYangUserInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = -192951429507078904L;
	private String taskid;
//	职工姓名
	private String name;
//	个人账号
	private String peraccnum;
//	公积金余额
	private String balance;
//	身份证号
	private String idnum;
//	公积金卡号
	private String cardnum;
//	单位名称
	private String unitname;
//	单位账号
	private String unitaccnum;
//	开户日期
	private String opendate;
//	缴至年月
	private String chargetoyearmonth;
//	月均工资
	private String monthavgwages;
//	单位月缴额
	private String unitmonthcharge;
//	个人月缴额
	private String permonthcharge;
//	月缴存额
	private String monthcharge;
//	单位缴交率
	private String unitchargerate;
//	个人缴交率
	private String perchargerate;
//	上年结转额
	private String lastyearturnover;
//	本年补缴额
	private String thisyearpayment; 
//	本年汇缴额
	private String thisyearremittance; 
//	个人账户状态
	private  String accountstate;
//	本年结转利息
	private String thisyearturnoverinterest;
//	本年支取额
	private String thisyearextract;
//	是否冻结
	private String isfreeze;
//	是否有异地贷
	private String isforeignloan;
//	是否有中心贷
	private String iscenterloan; 
//	是否月对冲
	private String ismonthhedge;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPeraccnum() {
		return peraccnum;
	}
	public void setPeraccnum(String peraccnum) {
		this.peraccnum = peraccnum;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getCardnum() {
		return cardnum;
	}
	public void setCardnum(String cardnum) {
		this.cardnum = cardnum;
	}
	public String getUnitname() {
		return unitname;
	}
	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}
	public String getUnitaccnum() {
		return unitaccnum;
	}
	public void setUnitaccnum(String unitaccnum) {
		this.unitaccnum = unitaccnum;
	}
	public String getOpendate() {
		return opendate;
	}
	public void setOpendate(String opendate) {
		this.opendate = opendate;
	}
	public String getChargetoyearmonth() {
		return chargetoyearmonth;
	}
	public void setChargetoyearmonth(String chargetoyearmonth) {
		this.chargetoyearmonth = chargetoyearmonth;
	}
	public String getMonthavgwages() {
		return monthavgwages;
	}
	public void setMonthavgwages(String monthavgwages) {
		this.monthavgwages = monthavgwages;
	}
	public String getUnitmonthcharge() {
		return unitmonthcharge;
	}
	public void setUnitmonthcharge(String unitmonthcharge) {
		this.unitmonthcharge = unitmonthcharge;
	}
	public String getPermonthcharge() {
		return permonthcharge;
	}
	public void setPermonthcharge(String permonthcharge) {
		this.permonthcharge = permonthcharge;
	}
	public String getMonthcharge() {
		return monthcharge;
	}
	public void setMonthcharge(String monthcharge) {
		this.monthcharge = monthcharge;
	}
	public String getUnitchargerate() {
		return unitchargerate;
	}
	public void setUnitchargerate(String unitchargerate) {
		this.unitchargerate = unitchargerate;
	}
	public String getPerchargerate() {
		return perchargerate;
	}
	public void setPerchargerate(String perchargerate) {
		this.perchargerate = perchargerate;
	}
	public String getLastyearturnover() {
		return lastyearturnover;
	}
	public void setLastyearturnover(String lastyearturnover) {
		this.lastyearturnover = lastyearturnover;
	}
	public String getThisyearpayment() {
		return thisyearpayment;
	}
	public void setThisyearpayment(String thisyearpayment) {
		this.thisyearpayment = thisyearpayment;
	}
	public String getThisyearremittance() {
		return thisyearremittance;
	}
	public void setThisyearremittance(String thisyearremittance) {
		this.thisyearremittance = thisyearremittance;
	}
	public String getAccountstate() {
		return accountstate;
	}
	public void setAccountstate(String accountstate) {
		this.accountstate = accountstate;
	}
	public String getThisyearturnoverinterest() {
		return thisyearturnoverinterest;
	}
	public void setThisyearturnoverinterest(String thisyearturnoverinterest) {
		this.thisyearturnoverinterest = thisyearturnoverinterest;
	}
	public String getThisyearextract() {
		return thisyearextract;
	}
	public void setThisyearextract(String thisyearextract) {
		this.thisyearextract = thisyearextract;
	}
	public String getIsfreeze() {
		return isfreeze;
	}
	public void setIsfreeze(String isfreeze) {
		this.isfreeze = isfreeze;
	}
	public String getIsforeignloan() {
		return isforeignloan;
	}
	public void setIsforeignloan(String isforeignloan) {
		this.isforeignloan = isforeignloan;
	}
	public String getIscenterloan() {
		return iscenterloan;
	}
	public void setIscenterloan(String iscenterloan) {
		this.iscenterloan = iscenterloan;
	}
	public String getIsmonthhedge() {
		return ismonthhedge;
	}
	public void setIsmonthhedge(String ismonthhedge) {
		this.ismonthhedge = ismonthhedge;
	}
	public HousingPuYangUserInfo() {
		super();
	}
	public HousingPuYangUserInfo(String taskid, String name, String peraccnum, String balance, String idnum,
			String cardnum, String unitname, String unitaccnum, String opendate, String chargetoyearmonth,
			String monthavgwages, String unitmonthcharge, String permonthcharge, String monthcharge,
			String unitchargerate, String perchargerate, String lastyearturnover, String thisyearpayment,
			String thisyearremittance, String accountstate, String thisyearturnoverinterest, String thisyearextract,
			String isfreeze, String isforeignloan, String iscenterloan, String ismonthhedge) {
		super();
		this.taskid = taskid;
		this.name = name;
		this.peraccnum = peraccnum;
		this.balance = balance;
		this.idnum = idnum;
		this.cardnum = cardnum;
		this.unitname = unitname;
		this.unitaccnum = unitaccnum;
		this.opendate = opendate;
		this.chargetoyearmonth = chargetoyearmonth;
		this.monthavgwages = monthavgwages;
		this.unitmonthcharge = unitmonthcharge;
		this.permonthcharge = permonthcharge;
		this.monthcharge = monthcharge;
		this.unitchargerate = unitchargerate;
		this.perchargerate = perchargerate;
		this.lastyearturnover = lastyearturnover;
		this.thisyearpayment = thisyearpayment;
		this.thisyearremittance = thisyearremittance;
		this.accountstate = accountstate;
		this.thisyearturnoverinterest = thisyearturnoverinterest;
		this.thisyearextract = thisyearextract;
		this.isfreeze = isfreeze;
		this.isforeignloan = isforeignloan;
		this.iscenterloan = iscenterloan;
		this.ismonthhedge = ismonthhedge;
	}
	
}
