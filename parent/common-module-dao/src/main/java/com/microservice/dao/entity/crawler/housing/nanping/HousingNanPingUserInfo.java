package com.microservice.dao.entity.crawler.housing.nanping;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "housing_nanping_userinfo",indexes = {@Index(name = "index_housing_nanping_userinfo_taskid", columnList = "taskid")})
public class HousingNanPingUserInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = 1712738081323671973L;

	private String taskid;
	
	private String num;
	
	private String idcard;
	
	private String name;
	
	private String dw_name;
	
	private String balance;
	
	private String bankname;
	
	private String jdate;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDw_name() {
		return dw_name;
	}

	public void setDw_name(String dw_name) {
		this.dw_name = dw_name;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getBankname() {
		return bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

	public String getJdate() {
		return jdate;
	}

	public void setJdate(String jdate) {
		this.jdate = jdate;
	}

	public HousingNanPingUserInfo(String taskid, String num, String idcard, String name, String dw_name, String balance,
			String bankname, String jdate) {
		super();
		this.taskid = taskid;
		this.num = num;
		this.idcard = idcard;
		this.name = name;
		this.dw_name = dw_name;
		this.balance = balance;
		this.bankname = bankname;
		this.jdate = jdate;
	}

	public HousingNanPingUserInfo() {
		super();
	}

	@Override
	public String toString() {
		return "HousingNanPingUserInfo [taskid=" + taskid + ", num=" + num + ", idcard=" + idcard + ", name=" + name
				+ ", dw_name=" + dw_name + ", balance=" + balance + ", bankname=" + bankname + ", jdate=" + jdate + "]";
	}
	
	
}
