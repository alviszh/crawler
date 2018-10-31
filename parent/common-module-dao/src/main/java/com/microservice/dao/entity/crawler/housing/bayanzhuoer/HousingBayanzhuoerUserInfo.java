package com.microservice.dao.entity.crawler.housing.bayanzhuoer;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

/**
 * 用户信息
 *
 */
@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "housing_bayanzhuoer_userinfo")
public class HousingBayanzhuoerUserInfo extends IdEntity {
	private String companyNum;//单位账号
	private String companyName;//	单位名称
	private String staffAccount;//	职工账号
	private String username;//职工姓名
	private String idnum;//	身份证号
	private String openingDate;//	开户日期
	private String lastPayMoney;//	上年汇缴累计
	private String lastInterest;//	上年利息
	private String yearPayMoney;//	本年汇补缴累计
	private String yearDrawMoney;//	本年支取累计
	private String balance;//	公积金余额
	private String monthlyPay;//	月缴额
	private String companyMonthlyPay;//	单位月缴额
	private String personalMonthlyPay;//	个人月缴额
	private String financeMonthPay;//	财政月缴额
	private String yearMonth;//	缴至年月
	private String state;//	账户状态
	private String idcard;//	联名卡号
	private String taskid;
	public String getCompanyNum() {
		return companyNum;
	}
	public void setCompanyNum(String companyNum) {
		this.companyNum = companyNum;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
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
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getOpeningDate() {
		return openingDate;
	}
	public void setOpeningDate(String openingDate) {
		this.openingDate = openingDate;
	}
	public String getLastPayMoney() {
		return lastPayMoney;
	}
	public void setLastPayMoney(String lastPayMoney) {
		this.lastPayMoney = lastPayMoney;
	}
	public String getLastInterest() {
		return lastInterest;
	}
	public void setLastInterest(String lastInterest) {
		this.lastInterest = lastInterest;
	}
	public String getYearPayMoney() {
		return yearPayMoney;
	}
	public void setYearPayMoney(String yearPayMoney) {
		this.yearPayMoney = yearPayMoney;
	}
	public String getYearDrawMoney() {
		return yearDrawMoney;
	}
	public void setYearDrawMoney(String yearDrawMoney) {
		this.yearDrawMoney = yearDrawMoney;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getMonthlyPay() {
		return monthlyPay;
	}
	public void setMonthlyPay(String monthlyPay) {
		this.monthlyPay = monthlyPay;
	}
	public String getCompanyMonthlyPay() {
		return companyMonthlyPay;
	}
	public void setCompanyMonthlyPay(String companyMonthlyPay) {
		this.companyMonthlyPay = companyMonthlyPay;
	}
	public String getPersonalMonthlyPay() {
		return personalMonthlyPay;
	}
	public void setPersonalMonthlyPay(String personalMonthlyPay) {
		this.personalMonthlyPay = personalMonthlyPay;
	}
	public String getFinanceMonthPay() {
		return financeMonthPay;
	}
	public void setFinanceMonthPay(String financeMonthPay) {
		this.financeMonthPay = financeMonthPay;
	}
	public String getYearMonth() {
		return yearMonth;
	}
	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	
	@Override
	public String toString() {
		return "HousingBayanzhuoerUserInfo [companyNum=" + companyNum + ", companyName=" + companyName
				+ ", staffAccount=" + staffAccount + ", username=" + username + ", idnum=" + idnum + ", openingDate="
				+ openingDate + ", lastPayMoney=" + lastPayMoney + ", lastInterest=" + lastInterest + ", yearPayMoney="
				+ yearPayMoney + ", yearDrawMoney=" + yearDrawMoney + ", balance=" + balance + ", monthlyPay="
				+ monthlyPay + ", companyMonthlyPay=" + companyMonthlyPay + ", personalMonthlyPay=" + personalMonthlyPay
				+ ", financeMonthPay=" + financeMonthPay + ", yearMonth=" + yearMonth + ", state=" + state + ", idcard="
				+ idcard + ", taskid=" + taskid + "]";
	}
	
	public HousingBayanzhuoerUserInfo(String companyNum, String companyName, String staffAccount, String username,
			String idnum, String openingDate, String lastPayMoney, String lastInterest, String yearPayMoney,
			String yearDrawMoney, String balance, String monthlyPay, String companyMonthlyPay,
			String personalMonthlyPay, String financeMonthPay, String yearMonth, String state, String idcard,
			String taskid) {
		super();
		this.companyNum = companyNum;
		this.companyName = companyName;
		this.staffAccount = staffAccount;
		this.username = username;
		this.idnum = idnum;
		this.openingDate = openingDate;
		this.lastPayMoney = lastPayMoney;
		this.lastInterest = lastInterest;
		this.yearPayMoney = yearPayMoney;
		this.yearDrawMoney = yearDrawMoney;
		this.balance = balance;
		this.monthlyPay = monthlyPay;
		this.companyMonthlyPay = companyMonthlyPay;
		this.personalMonthlyPay = personalMonthlyPay;
		this.financeMonthPay = financeMonthPay;
		this.yearMonth = yearMonth;
		this.state = state;
		this.idcard = idcard;
		this.taskid = taskid;
	}
	public HousingBayanzhuoerUserInfo() {
		super();
	}
}
