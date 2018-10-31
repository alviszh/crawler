package com.microservice.dao.entity.crawler.housing.dandong;

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
@Table(name = "housing_dandong_pay",indexes = {@Index(name = "index_housing_dandong_pay_taskid", columnList = "taskid")})
public class HousingDanDongPay extends IdEntity implements Serializable{

	private String taskid;
	
	private String serialnum;//流水号
	
	private String jdate;//日期
	
	private String jiemoney;//借方金额
	
	private String daimoney;//贷方金额
	
	private String balance;//余额
	
	private String zai;//摘要

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getSerialnum() {
		return serialnum;
	}

	public void setSerialnum(String serialnum) {
		this.serialnum = serialnum;
	}

	public String getJdate() {
		return jdate;
	}

	public void setJdate(String jdate) {
		this.jdate = jdate;
	}

	public String getJiemoney() {
		return jiemoney;
	}

	public void setJiemoney(String jiemoney) {
		this.jiemoney = jiemoney;
	}

	public String getDaimoney() {
		return daimoney;
	}

	public void setDaimoney(String daimoney) {
		this.daimoney = daimoney;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getZai() {
		return zai;
	}

	public void setZai(String zai) {
		this.zai = zai;
	}

	public HousingDanDongPay(String taskid, String serialnum, String jdate, String jiemoney, String daimoney,
			String balance, String zai) {
		super();
		this.taskid = taskid;
		this.serialnum = serialnum;
		this.jdate = jdate;
		this.jiemoney = jiemoney;
		this.daimoney = daimoney;
		this.balance = balance;
		this.zai = zai;
	}

	public HousingDanDongPay() {
		super();
	}
	
	
}
