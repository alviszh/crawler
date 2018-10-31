package com.microservice.dao.entity.crawler.housing.cangzhou;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;
@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "housing_cangzhou_userinfo",indexes = {@Index(name = "index_housing_cangzhou_userinfo_taskid", columnList = "taskid")})
public class HousingCangZhouUserInfo  extends IdEntity implements Serializable {

	private String taskid;
	
	private Integer userid;
	
	private String username;
	
	private String idcard;
	
	private String paymoney;
	
	private String balance;
	
	private String xdate;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getPaymoney() {
		return paymoney;
	}

	public void setPaymoney(String paymoney) {
		this.paymoney = paymoney;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getXdate() {
		return xdate;
	}

	public void setXdate(String xdate) {
		this.xdate = xdate;
	}

	public HousingCangZhouUserInfo(String taskid, Integer userid, String username, String idcard, String paymoney,
			String balance, String xdate) {
		super();
		this.taskid = taskid;
		this.userid = userid;
		this.username = username;
		this.idcard = idcard;
		this.paymoney = paymoney;
		this.balance = balance;
		this.xdate = xdate;
	}

	public HousingCangZhouUserInfo() {
		super();
	}

	@Override
	public String toString() {
		return "HousingCangZhouUserInfo [taskid=" + taskid + ", userid=" + userid + ", username=" + username
				+ ", idcard=" + idcard + ", paymoney=" + paymoney + ", balance=" + balance + ", xdate=" + xdate + "]";
	}
	
	
}
