package com.microservice.dao.entity.crawler.housing.xiamen;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 厦门公积金用户信息
 */
@Entity
@Table(name="housing_xiamen_userinfo")
public class HousingXiamenUserInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = 1221206979457794498L;
	private String accountnum;//职工账号
	private String openbank;//开户银行
	private String username;//	职工姓名
	private String bankname;//	开户网点
	private String opendate;//	开户日期
	private String compname;//	单位名称
	private String acctstatus;// 账户状态
	private String balance;//	账户余额
	private String taskid;
	
	public String getAccountnum() {
		return accountnum;
	}

	public void setAccountnum(String accountnum) {
		this.accountnum = accountnum;
	}

	public String getOpenbank() {
		return openbank;
	}

	public void setOpenbank(String openbank) {
		this.openbank = openbank;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getBankname() {
		return bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

	public String getOpendate() {
		return opendate;
	}

	public void setOpendate(String opendate) {
		this.opendate = opendate;
	}

	public String getCompname() {
		return compname;
	}

	public void setCompname(String compname) {
		this.compname = compname;
	}

	public String getAcctstatus() {
		return acctstatus;
	}

	public void setAcctstatus(String acctstatus) {
		this.acctstatus = acctstatus;
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
		return "HousingXiamenUserInfo [accountnum=" + accountnum + ", openbank=" + openbank + ", username=" + username
				+ ", bankname=" + bankname + ", opendate=" + opendate + ", compname=" + compname + ", acctstatus="
				+ acctstatus + ", balance=" + balance + ", taskid=" + taskid + "]";
	}
	public HousingXiamenUserInfo(String accountnum, String openbank, String username, String bankname, String opendate,
			String compname, String acctstatus, String balance, String taskid) {
		super();
		this.accountnum = accountnum;
		this.openbank = openbank;
		this.username = username;
		this.bankname = bankname;
		this.opendate = opendate;
		this.compname = compname;
		this.acctstatus = acctstatus;
		this.balance = balance;
		this.taskid = taskid;
	}

	public HousingXiamenUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}	
}
