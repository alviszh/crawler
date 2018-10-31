package com.microservice.dao.entity.crawler.housing.zhenjiang;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 镇江市公积金个人基本信息
 * @author: sln 
 * @date: 
 */
@Entity
@Table(name="housing_zhenjiang_userinfo")
public class HousingZhenJiangUserInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = -4523112843732781341L;
	private String taskid;
//	身份证号
	private String idnum;
//	缴存基数
	private String basenum;
//	月缴存额
	private String monthcharge;
//	公积金余额
	private String balance;
//	最新汇缴年月
	private String newchargemonth;
//	账号状态
	private String accountstatus;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getBasenum() {
		return basenum;
	}
	public void setBasenum(String basenum) {
		this.basenum = basenum;
	}
	public String getMonthcharge() {
		return monthcharge;
	}
	public void setMonthcharge(String monthcharge) {
		this.monthcharge = monthcharge;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getNewchargemonth() {
		return newchargemonth;
	}
	public void setNewchargemonth(String newchargemonth) {
		this.newchargemonth = newchargemonth;
	}
	public String getAccountstatus() {
		return accountstatus;
	}
	public void setAccountstatus(String accountstatus) {
		this.accountstatus = accountstatus;
	}

}
