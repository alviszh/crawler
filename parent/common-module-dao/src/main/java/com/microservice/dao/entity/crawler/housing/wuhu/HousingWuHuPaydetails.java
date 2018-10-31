package com.microservice.dao.entity.crawler.housing.wuhu;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 无锡公积金用户信息
 */
@Entity
@Table(name="housing_wuhu_paydetails")
public class HousingWuHuPaydetails extends IdEntity implements Serializable {
	private static final long serialVersionUID = -758773159050632619L;
	
	private String personaccount;//	个人公积金账号
	private String accountdate;//	收支日期
	private String type;//	收支类型
	private String creditoramount;//	金额
	private String accountbalance;//	账户余额
	private String taskid;
	
	public String getPersonaccount() {
		return personaccount;
	}
	public void setPersonaccount(String personaccount) {
		this.personaccount = personaccount;
	}
	public String getAccountdate() {
		return accountdate;
	}
	public void setAccountdate(String accountdate) {
		this.accountdate = accountdate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCreditoramount() {
		return creditoramount;
	}
	public void setCreditoramount(String creditoramount) {
		this.creditoramount = creditoramount;
	}
	public String getAccountbalance() {
		return accountbalance;
	}
	public void setAccountbalance(String accountbalance) {
		this.accountbalance = accountbalance;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Override
	public String toString() {
		return "HousingWuHuPaydetails [personaccount=" + personaccount + ", accountdate=" + accountdate + ", type="
				+ type + ", creditoramount=" + creditoramount + ", accountbalance=" + accountbalance + ", taskid="
				+ taskid + "]";
	}
	
	public HousingWuHuPaydetails(String personaccount, String accountdate, String type, String creditoramount,
			String accountbalance, String taskid) {
		super();
		this.personaccount = personaccount;
		this.accountdate = accountdate;
		this.type = type;
		this.creditoramount = creditoramount;
		this.accountbalance = accountbalance;
		this.taskid = taskid;
	}
	public HousingWuHuPaydetails() {
		super();
		// TODO Auto-generated constructor stub
	}	
}
