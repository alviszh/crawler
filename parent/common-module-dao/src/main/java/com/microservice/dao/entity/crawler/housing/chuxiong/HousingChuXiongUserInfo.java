package com.microservice.dao.entity.crawler.housing.chuxiong;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 楚雄公积金用户信息
 */
@Entity
@Table(name="housing_chuxiong_userinfo")
public class HousingChuXiongUserInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = 1221206979457794498L;	

	private String username;//姓名
	private String companyName;//证件类型
	private String newAccount;//证件号码
	private String oldAccount;//性别
	private String balance;//个人账户余额
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
	public String getNewAccount() {
		return newAccount;
	}
	public void setNewAccount(String newAccount) {
		this.newAccount = newAccount;
	}
	public String getOldAccount() {
		return oldAccount;
	}
	public void setOldAccount(String oldAccount) {
		this.oldAccount = oldAccount;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	@Override
	public String toString() {
		return "HousingChuXiongUserInfo [username=" + username + ", companyName=" + companyName + ", newAccount="
				+ newAccount + ", oldAccount=" + oldAccount + ", balance=" + balance + ", taskid=" + taskid + "]";
	}
	public HousingChuXiongUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public HousingChuXiongUserInfo(String username, String companyName, String newAccount, String oldAccount,
			String balance, String taskid) {
		super();
		this.username = username;
		this.companyName = companyName;
		this.newAccount = newAccount;
		this.oldAccount = oldAccount;
		this.balance = balance;
		this.taskid = taskid;
	}
	
}
