package com.microservice.dao.entity.crawler.housing.quanzhou;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name  ="housing_quanzhou_pay",indexes = {@Index(name = "index_housing_quanzhou_pay_taskid", columnList = "taskid")})
public class HousingQuanZhouPay extends IdEntity implements Serializable {
	private String name;              //姓名
	private String idNumber;          //身份证号
	private String perAccount;        //个人账号
	private String state;             //状态
	private String monthSubscr;       //月缴额	
	private String balance;           //余额
	private String dueDay;            //最后缴交日期
	private String base;              //缴交基数
	private String proportion;        //缴交比例
	private String unitAccount;       //单位账号
	private String unitName;          //单位名称
	private String date;              //开户日期
	
	private Integer userid;

	private String taskid;
	@Override
	public String toString() {
		return "HousingQuanZhouPay [name=" + name + ",idNumber=" + idNumber + ",perAccount=" + perAccount
				+ ", state=" + state + ", monthSubscr=" + monthSubscr + ", balance=" + balance 
				+ ", dueDa="+ dueDay+ ", base=" + base + ", proportion=" + proportion
				+ ", unitAccount="+ unitAccount+ ", unitName=" + unitName + ", date=" + date
				+ ", userid=" + userid + ", taskid=" + taskid + "]";
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	public String getPerAccount() {
		return perAccount;
	}
	public void setPerAccount(String perAccount) {
		this.perAccount = perAccount;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getMonthSubscr() {
		return monthSubscr;
	}
	public void setMonthSubscr(String monthSubscr) {
		this.monthSubscr = monthSubscr;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getDueDay() {
		return dueDay;
	}
	public void setDueDay(String dueDay) {
		this.dueDay = dueDay;
	}
	public String getBase() {
		return base;
	}
	public void setBase(String base) {
		this.base = base;
	}
	public String getProportion() {
		return proportion;
	}
	public void setProportion(String proportion) {
		this.proportion = proportion;
	}
	public String getUnitAccount() {
		return unitAccount;
	}
	public void setUnitAccount(String unitAccount) {
		this.unitAccount = unitAccount;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
}
