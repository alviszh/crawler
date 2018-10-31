package com.microservice.dao.entity.crawler.housing.qujing;

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
@Table(name = "housing_qujing_userinfo")
public class HousingQujingUserInfo extends IdEntity {
	private String staffAccount;//	个人公积金账号
	private String username;//	姓名
	
	private String idnum;//	证件号码
	private String phone;//	手机号码
	private String lastpaydate;//	最后汇缴月
	private String lastdrawdate;//	上次提取日
	private String payBase;//	缴存基数
	private String companyProportion;//	合计单位比例
	private String persionProportion;//	合计个人比例
	private String monthpayAmount;//	月缴额
	private String balance;//	当前余额
	
	private String state;//	账户状态
	private String companyName;//	单位名称
	private String companyAccount;//	单位账号
	private String institutionName;//	机构
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
	
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getPayBase() {
		return payBase;
	}
	public void setPayBase(String payBase) {
		this.payBase = payBase;
	}
	public String getCompanyProportion() {
		return companyProportion;
	}
	public void setCompanyProportion(String companyProportion) {
		this.companyProportion = companyProportion;
	}
	public String getPersionProportion() {
		return persionProportion;
	}
	public void setPersionProportion(String persionProportion) {
		this.persionProportion = persionProportion;
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
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyAccount() {
		return companyAccount;
	}
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getLastpaydate() {
		return lastpaydate;
	}
	public void setLastpaydate(String lastpaydate) {
		this.lastpaydate = lastpaydate;
	}
	public String getLastdrawdate() {
		return lastdrawdate;
	}
	public void setLastdrawdate(String lastdrawdate) {
		this.lastdrawdate = lastdrawdate;
	}
	public void setCompanyAccount(String companyAccount) {
		this.companyAccount = companyAccount;
	}
	public String getInstitutionName() {
		return institutionName;
	}
	public void setInstitutionName(String institutionName) {
		this.institutionName = institutionName;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

   
	@Override
	public String toString() {
		return "HousingQujingUserInfo [staffAccount=" + staffAccount + ", username=" + username + ", idnum=" + idnum
				+ ", phone=" + phone + ", lastpaydate=" + lastpaydate + ", lastdrawdate=" + lastdrawdate + ", payBase="
				+ payBase + ", companyProportion=" + companyProportion + ", persionProportion=" + persionProportion
				+ ", monthpayAmount=" + monthpayAmount + ", balance=" + balance + ", state=" + state + ", companyName="
				+ companyName + ", companyAccount=" + companyAccount + ", institutionName=" + institutionName
				+ ", taskid=" + taskid + "]";
	}
	public HousingQujingUserInfo(String staffAccount, String username, String idnum, String phone,
			String lastpaydate, String lastdrawdate, String payBase, String companyProportion, String persionProportion,
			String monthpayAmount, String balance, String state, String companyName, String companyAccount,
			String institutionName, String taskid) {
		super();
		this.staffAccount = staffAccount;
		this.username = username;
		this.idnum = idnum;
		this.phone = phone;
		this.lastpaydate = lastpaydate;
		this.lastdrawdate = lastdrawdate;
		this.payBase = payBase;
		this.companyProportion = companyProportion;
		this.persionProportion = persionProportion;
		this.monthpayAmount = monthpayAmount;
		this.balance = balance;
		this.state = state;
		this.companyName = companyName;
		this.companyAccount = companyAccount;
		this.institutionName = institutionName;
		this.taskid = taskid;
	}
	public HousingQujingUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
}
