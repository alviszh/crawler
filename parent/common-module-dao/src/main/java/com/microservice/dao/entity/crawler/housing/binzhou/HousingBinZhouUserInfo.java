package com.microservice.dao.entity.crawler.housing.binzhou;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 滨州公积金用户信息
 */
@Entity
@Table(name="housing_binzhou_userinfo",indexes = {@Index(name = "index_housing_binzhou_userinfo_taskid", columnList = "taskid")})
public class HousingBinZhouUserInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = 1221206979457794498L;
	
	private String userAccount;//职工账号
	private String username;//	职工姓名
	private String companyAccount;//单位账号
	private String companyName;//	单位名称
	private String idnum;//	身份证号
	private String state;//	缴存状态
	private String lastPaymonth;//	缴存年月
	private String basemny;//	工资基数
	private String payAmount;//	月缴额
	private String balance;//	职工余额
	private String isFreeze;//	是否冻结
	private String taskid;
	
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
	public String getCompanyAccount() {
		return companyAccount;
	}
	public void setCompanyAccount(String companyAccount) {
		this.companyAccount = companyAccount;
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
	public String getLastPaymonth() {
		return lastPaymonth;
	}
	public void setLastPaymonth(String lastPaymonth) {
		this.lastPaymonth = lastPaymonth;
	}
	public String getBasemny() {
		return basemny;
	}
	public void setBasemny(String basemny) {
		this.basemny = basemny;
	}
	public String getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(String payAmount) {
		this.payAmount = payAmount;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getIsFreeze() {
		return isFreeze;
	}
	public void setIsFreeze(String isFreeze) {
		this.isFreeze = isFreeze;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	@Override
	public String toString() {
		return "HousingBinZhouUserInfo [userAccount=" + userAccount + ", username=" + username + ", companyAccount="
				+ companyAccount + ", companyName=" + companyName + ", idnum=" + idnum + ", state=" + state
				+ ", lastPaymonth=" + lastPaymonth + ", basemny=" + basemny + ", payAmount=" + payAmount + ", balance="
				+ balance + ", isFreeze=" + isFreeze + ", taskid=" + taskid + "]";
	}
	
	public HousingBinZhouUserInfo(String userAccount, String username, String companyAccount, String companyName,
			String idnum, String state, String lastPaymonth, String basemny, String payAmount, String balance,
			String isFreeze, String taskid) {
		super();
		this.userAccount = userAccount;
		this.username = username;
		this.companyAccount = companyAccount;
		this.companyName = companyName;
		this.idnum = idnum;
		this.state = state;
		this.lastPaymonth = lastPaymonth;
		this.basemny = basemny;
		this.payAmount = payAmount;
		this.balance = balance;
		this.isFreeze = isFreeze;
		this.taskid = taskid;
	}
	public HousingBinZhouUserInfo() {
		super();
	}	
}
