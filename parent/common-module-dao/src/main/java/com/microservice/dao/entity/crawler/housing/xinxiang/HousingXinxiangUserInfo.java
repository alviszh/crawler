package com.microservice.dao.entity.crawler.housing.xinxiang;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 新乡公积金用户信息
 */
@Entity
@Table(name="housing_xinxiang_userinfo")
public class HousingXinxiangUserInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = 1221206979457794498L;

	private String companyNumber;//	单位编号
	private String accountNumber;//	个人编号
	private String username;//	姓名
	private String companyName;//	单位名称
	private String idnum;//	身份证号码
	private String state;//	账户状态
	private String personPayAmount;//	个人月缴额
	private String companyPayAmount;//	单位月缴额
	private String payBase;//	工资基数
	private String balance;//	余额
	private String companyLastMonth;//	单位缴至月份
	private String personLastMonth;//	个人缴至月份
	private String taskid;
	
	public String getCompanyNumber() {
		return companyNumber;
	}
	public void setCompanyNumber(String companyNumber) {
		this.companyNumber = companyNumber;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPersonPayAmount() {
		return personPayAmount;
	}
	public void setPersonPayAmount(String personPayAmount) {
		this.personPayAmount = personPayAmount;
	}
	public String getCompanyPayAmount() {
		return companyPayAmount;
	}
	public void setCompanyPayAmount(String companyPayAmount) {
		this.companyPayAmount = companyPayAmount;
	}
	public String getPayBase() {
		return payBase;
	}
	public void setPayBase(String payBase) {
		this.payBase = payBase;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getCompanyLastMonth() {
		return companyLastMonth;
	}
	public void setCompanyLastMonth(String companyLastMonth) {
		this.companyLastMonth = companyLastMonth;
	}
	public String getPersonLastMonth() {
		return personLastMonth;
	}
	public void setPersonLastMonth(String personLastMonth) {
		this.personLastMonth = personLastMonth;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}	
	@Override
	public String toString() {
		return "HousingXinxiangUserInfo [companyNumber=" + companyNumber + ", accountNumber=" + accountNumber
				+ ", username=" + username + ", companyName=" + companyName + ", idnum=" + idnum + ", state=" + state
				+ ", personPayAmount=" + personPayAmount + ", companyPayAmount=" + companyPayAmount + ", payBase="
				+ payBase + ", balance=" + balance + ", companyLastMonth=" + companyLastMonth + ", personLastMonth="
				+ personLastMonth + ", taskid=" + taskid + "]";
	}
	public HousingXinxiangUserInfo(String companyNumber, String accountNumber, String username, String companyName,
			String idnum, String state, String personPayAmount, String companyPayAmount, String payBase, String balance,
			String companyLastMonth, String personLastMonth, String taskid) {
		super();
		this.companyNumber = companyNumber;
		this.accountNumber = accountNumber;
		this.username = username;
		this.companyName = companyName;
		this.idnum = idnum;
		this.state = state;
		this.personPayAmount = personPayAmount;
		this.companyPayAmount = companyPayAmount;
		this.payBase = payBase;
		this.balance = balance;
		this.companyLastMonth = companyLastMonth;
		this.personLastMonth = personLastMonth;
		this.taskid = taskid;
	}
	public HousingXinxiangUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}	
}
