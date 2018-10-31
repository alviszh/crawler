package com.microservice.dao.entity.crawler.housing.yibin;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 宜宾公积金用户信息
 */
@Entity
@Table(name="housing_yibin_userinfo")
public class HousingYibinUserInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = 1221206979457794498L;
	private String staffAccount;//公积金账号
	private String username;//	姓名
	private String companyMonthlyPay;//单位月缴
	private String persionalMonthlyPay;//个人月缴
	private String monthlyPay;//月缴额
	private String balance;//	个人余额
	private String companyPayMonth;//单位缴至年月
	private String persionalPayMonth;//	个人缴至年月
	private String accountState;//	单位月缴存额
	private String taskid;
	
	public String getStaffAccount() {
		return staffAccount;
	}
	public void setStaffAccount(String staffAccount) {
		this.staffAccount = staffAccount;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getCompanyMonthlyPay() {
		return companyMonthlyPay;
	}
	public void setCompanyMonthlyPay(String companyMonthlyPay) {
		this.companyMonthlyPay = companyMonthlyPay;
	}
	public String getPersionalMonthlyPay() {
		return persionalMonthlyPay;
	}
	public void setPersionalMonthlyPay(String persionalMonthlyPay) {
		this.persionalMonthlyPay = persionalMonthlyPay;
	}
	public String getMonthlyPay() {
		return monthlyPay;
	}
	public void setMonthlyPay(String monthlyPay) {
		this.monthlyPay = monthlyPay;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getCompanyPayMonth() {
		return companyPayMonth;
	}
	public void setCompanyPayMonth(String companyPayMonth) {
		this.companyPayMonth = companyPayMonth;
	}
	public String getPersionalPayMonth() {
		return persionalPayMonth;
	}
	public void setPersionalPayMonth(String persionalPayMonth) {
		this.persionalPayMonth = persionalPayMonth;
	}
	public String getAccountState() {
		return accountState;
	}
	public void setAccountState(String accountState) {
		this.accountState = accountState;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	@Override
	public String toString() {
		return "HousingYibinUserInfo [staffAccount=" + staffAccount + ", username=" + username + ", companyMonthlyPay="
				+ companyMonthlyPay + ", persionalMonthlyPay=" + persionalMonthlyPay + ", monthlyPay=" + monthlyPay
				+ ", balance=" + balance + ", companyPayMonth=" + companyPayMonth + ", persionalPayMonth="
				+ persionalPayMonth + ", accountState=" + accountState + ", taskid=" + taskid + "]";
	}	
	public HousingYibinUserInfo(String staffAccount, String username, String companyMonthlyPay,
			String persionalMonthlyPay, String monthlyPay, String balance, String companyPayMonth,
			String persionalPayMonth, String accountState, String taskid) {
		super();
		this.staffAccount = staffAccount;
		this.username = username;
		this.companyMonthlyPay = companyMonthlyPay;
		this.persionalMonthlyPay = persionalMonthlyPay;
		this.monthlyPay = monthlyPay;
		this.balance = balance;
		this.companyPayMonth = companyPayMonth;
		this.persionalPayMonth = persionalPayMonth;
		this.accountState = accountState;
		this.taskid = taskid;
	}
	public HousingYibinUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}	
}
