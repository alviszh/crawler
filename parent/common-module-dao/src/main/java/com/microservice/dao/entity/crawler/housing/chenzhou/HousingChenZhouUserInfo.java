package com.microservice.dao.entity.crawler.housing.chenzhou;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 公积金用户信息
 */
@Entity
@Table(name="housing_chenzhou_userinfo")
public class HousingChenZhouUserInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = 1221206979457794498L;
	private String companyname;// 单位名称
	private String username;// 姓名
	private String idnum;// 身份证号
	private String useraccount;// 个人账号
	private String companyaccount;// 单位账号
	private String monthamount;// 月缴额
	private String paymonth;//汇缴年月
	private String balance;//公积金金额
	private String payRatio;// 汇缴比例
	private String state;//	账户状态
	private String taskid;
	public String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(String companyname) {
		this.companyname = companyname;
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
	public String getUseraccount() {
		return useraccount;
	}
	public void setUseraccount(String useraccount) {
		this.useraccount = useraccount;
	}
	public String getCompanyaccount() {
		return companyaccount;
	}
	public void setCompanyaccount(String companyaccount) {
		this.companyaccount = companyaccount;
	}
	public String getMonthamount() {
		return monthamount;
	}
	public void setMonthamount(String monthamount) {
		this.monthamount = monthamount;
	}
	public String getPaymonth() {
		return paymonth;
	}
	public void setPaymonth(String paymonth) {
		this.paymonth = paymonth;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getPayRatio() {
		return payRatio;
	}
	public void setPayRatio(String payRatio) {
		this.payRatio = payRatio;
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
	@Override
	public String toString() {
		return "HousingChenZhouUserInfo [companyname=" + companyname + ", username=" + username + ", idnum=" + idnum
				+ ", useraccount=" + useraccount + ", companyaccount=" + companyaccount + ", monthamount=" + monthamount
				+ ", paymonth=" + paymonth + ", balance=" + balance + ", payRatio=" + payRatio + ", state=" + state
				+ ", taskid=" + taskid + "]";
	}
	public HousingChenZhouUserInfo(String companyname, String username, String idnum, String useraccount,
			String companyaccount, String monthamount, String paymonth, String balance, String payRatio, String state,
			String taskid) {
		super();
		this.companyname = companyname;
		this.username = username;
		this.idnum = idnum;
		this.useraccount = useraccount;
		this.companyaccount = companyaccount;
		this.monthamount = monthamount;
		this.paymonth = paymonth;
		this.balance = balance;
		this.payRatio = payRatio;
		this.state = state;
		this.taskid = taskid;
	}
	public HousingChenZhouUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
}
