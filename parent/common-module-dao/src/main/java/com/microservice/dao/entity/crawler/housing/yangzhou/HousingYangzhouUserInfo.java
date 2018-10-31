package com.microservice.dao.entity.crawler.housing.yangzhou;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 公积金用户信息
 */
@Entity
@Table(name="housing_yangzhou_userinfo")
public class HousingYangzhouUserInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = 1221206979457794498L;
	
	private String username;//	职工姓名
	private String companyName;//	单位名称
	private String useraccount;//	职工账号
	private String idnum;//	身份证号
	private String state;//	缴存状态
	private String payrate;//	缴存比例
	private String payamount;//	公积金月缴额
	private String subsidyamount;//	补贴月缴额
	private String balance;//	公积金余额
	private String subsidybalance;//	补贴余额
	private String totalamount;//	余额合计
	private String lastmonth;//	缴至年月
	private String taskid;
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
	public String getUseraccount() {
		return useraccount;
	}
	public void setUseraccount(String useraccount) {
		this.useraccount = useraccount;
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
	public String getPayrate() {
		return payrate;
	}
	public void setPayrate(String payrate) {
		this.payrate = payrate;
	}
	public String getPayamount() {
		return payamount;
	}
	public void setPayamount(String payamount) {
		this.payamount = payamount;
	}
	public String getSubsidyamount() {
		return subsidyamount;
	}
	public void setSubsidyamount(String subsidyamount) {
		this.subsidyamount = subsidyamount;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getSubsidybalance() {
		return subsidybalance;
	}
	public void setSubsidybalance(String subsidybalance) {
		this.subsidybalance = subsidybalance;
	}
	public String getTotalamount() {
		return totalamount;
	}
	public void setTotalamount(String totalamount) {
		this.totalamount = totalamount;
	}
	public String getLastmonth() {
		return lastmonth;
	}
	public void setLastmonth(String lastmonth) {
		this.lastmonth = lastmonth;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	@Override
	public String toString() {
		return "HousingYangzhouUserInfo [username=" + username + ", companyName=" + companyName + ", useraccount="
				+ useraccount + ", idnum=" + idnum + ", state=" + state + ", payrate=" + payrate + ", payamount="
				+ payamount + ", subsidyamount=" + subsidyamount + ", balance=" + balance + ", subsidybalance="
				+ subsidybalance + ", totalamount=" + totalamount + ", lastmonth=" + lastmonth + ", taskid=" + taskid
				+ "]";
	}
	public HousingYangzhouUserInfo(String username, String companyName, String useraccount, String idnum, String state,
			String payrate, String payamount, String subsidyamount, String balance, String subsidybalance,
			String totalamount, String lastmonth, String taskid) {
		super();
		this.username = username;
		this.companyName = companyName;
		this.useraccount = useraccount;
		this.idnum = idnum;
		this.state = state;
		this.payrate = payrate;
		this.payamount = payamount;
		this.subsidyamount = subsidyamount;
		this.balance = balance;
		this.subsidybalance = subsidybalance;
		this.totalamount = totalamount;
		this.lastmonth = lastmonth;
		this.taskid = taskid;
	}
	public HousingYangzhouUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}	
}
