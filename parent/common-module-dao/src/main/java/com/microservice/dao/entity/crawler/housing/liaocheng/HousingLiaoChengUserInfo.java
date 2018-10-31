package com.microservice.dao.entity.crawler.housing.liaocheng;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 聊城公积金用户信息
 */
@Entity
@Table(name="housing_liaocheng_userinfo")
public class HousingLiaoChengUserInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = 1221206979457794498L;

	private String companyName;//单位名称
	private String companyAccount;//单位账号
	private String staffName;//所属管理部
	private String userAccount;//职工账号
	private String username;//职工姓名
	private String monthpayAmount;//月缴金额
	private String balance;//当前余额
	private String openDate;//当开户日期
	private String lastPaymonth;//最后汇缴年月
	private String payBase;//汇缴基数
	private String state;//账户状态
	private String companyRatio;//单位缴存比例
	private String personRatio;//个人缴存比例
	private String taskid;
	
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyAccount() {
		return companyAccount;
	}
	public void setCompanyAccount(String companyAccount) {
		this.companyAccount = companyAccount;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMonthpayAmount() {
		return monthpayAmount;
	}

	public void setMonthpayAmount(String monthpayAmount) {
		this.monthpayAmount = monthpayAmount;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getOpenDate() {
		return openDate;
	}

	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}

	public String getLastPaymonth() {
		return lastPaymonth;
	}

	public void setLastPaymonth(String lastPaymonth) {
		this.lastPaymonth = lastPaymonth;
	}

	public String getPayBase() {
		return payBase;
	}

	public void setPayBase(String payBase) {
		this.payBase = payBase;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCompanyRatio() {
		return companyRatio;
	}

	public void setCompanyRatio(String companyRatio) {
		this.companyRatio = companyRatio;
	}

	public String getPersonRatio() {
		return personRatio;
	}

	public void setPersonRatio(String personRatio) {
		this.personRatio = personRatio;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Override
	public String toString() {
		return "HousingLiaoChengUserInfo [companyName=" + companyName + ", companyAccount=" + companyAccount
				+ ", staffName=" + staffName + ", userAccount=" + userAccount + ", username=" + username
				+ ", monthpayAmount=" + monthpayAmount + ", balance=" + balance + ", openDate=" + openDate
				+ ", lastPaymonth=" + lastPaymonth + ", payBase=" + payBase + ", state=" + state + ", companyRatio="
				+ companyRatio + ", personRatio=" + personRatio + ", taskid=" + taskid + "]";
	}

	public HousingLiaoChengUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
}
