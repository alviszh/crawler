package com.microservice.dao.entity.crawler.housing.nanchong;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_nanchong_userinfo",indexes = {@Index(name = "index_housing_nanchong_userinfo_taskid", columnList = "taskid")})
public class HousingNanChongUserInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = -192951429507078904L;
	private String taskid;
//	职工姓名
	private String name;
//	个人账号
	private String peraccnum;
//	身份证号
	private String idnum;
//	单位名称
	private String unitname;
//	单位账号
	private String unitaccnum;
//	开户日期
	private String opendate;
//	缴至年月
	private String paytoyearmonth;
//	公积金卡号
	private String cardno;
//	月均工资
	private String monthavgwages;
//	缴存比例
	private String prop;
//	月缴存额
	private String monthcharge;
//	余额
	private String balance;
//	职工状态
	private String employeestatus;
//	是否冻结
	private String isfreeze;
//	是否贷款
	private String isloan;
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
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
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
	public String getPaytoyearmonth() {
		return paytoyearmonth;
	}
	public void setPaytoyearmonth(String paytoyearmonth) {
		this.paytoyearmonth = paytoyearmonth;
	}
	public String getCardno() {
		return cardno;
	}
	public void setCardno(String cardno) {
		this.cardno = cardno;
	}
	public String getMonthavgwages() {
		return monthavgwages;
	}
	public void setMonthavgwages(String monthavgwages) {
		this.monthavgwages = monthavgwages;
	}
	public String getProp() {
		return prop;
	}
	public void setProp(String prop) {
		this.prop = prop;
	}
	public String getMonthcharge() {
		return monthcharge;
	}
	public void setMonthcharge(String monthcharge) {
		this.monthcharge = monthcharge;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getEmployeestatus() {
		return employeestatus;
	}
	public void setEmployeestatus(String employeestatus) {
		this.employeestatus = employeestatus;
	}
	public String getIsfreeze() {
		return isfreeze;
	}
	public void setIsfreeze(String isfreeze) {
		this.isfreeze = isfreeze;
	}
	public String getIsloan() {
		return isloan;
	}
	public void setIsloan(String isloan) {
		this.isloan = isloan;
	}
	public String getIsmonthhedge() {
		return ismonthhedge;
	}
	public void setIsmonthhedge(String ismonthhedge) {
		this.ismonthhedge = ismonthhedge;
	}
	public HousingNanChongUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public HousingNanChongUserInfo(String taskid, String name, String peraccnum, String idnum, String unitname,
			String unitaccnum, String opendate, String paytoyearmonth, String cardno, String monthavgwages, String prop,
			String monthcharge, String balance, String employeestatus, String isfreeze, String isloan,
			String ismonthhedge) {
		super();
		this.taskid = taskid;
		this.name = name;
		this.peraccnum = peraccnum;
		this.idnum = idnum;
		this.unitname = unitname;
		this.unitaccnum = unitaccnum;
		this.opendate = opendate;
		this.paytoyearmonth = paytoyearmonth;
		this.cardno = cardno;
		this.monthavgwages = monthavgwages;
		this.prop = prop;
		this.monthcharge = monthcharge;
		this.balance = balance;
		this.employeestatus = employeestatus;
		this.isfreeze = isfreeze;
		this.isloan = isloan;
		this.ismonthhedge = ismonthhedge;
	}
	
}
