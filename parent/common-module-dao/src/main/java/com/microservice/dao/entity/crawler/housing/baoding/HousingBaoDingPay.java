package com.microservice.dao.entity.crawler.housing.baoding;

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
@Table(name = "housing_baoding_pay",indexes = {@Index(name = "index_housing_baoding_pay_taskid", columnList = "taskid")})
public class HousingBaoDingPay extends IdEntity implements Serializable{

	private String taskid;

	private String paynum;//流水号
	
	private String jdate;//日期
	
	private String timoney;//提取金额
	
	private String crmoney;//存入金额
	
	private String yu;//余额
	
	private String zai;//摘要

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getPaynum() {
		return paynum;
	}

	public void setPaynum(String paynum) {
		this.paynum = paynum;
	}

	public String getJdate() {
		return jdate;
	}

	public void setJdate(String jdate) {
		this.jdate = jdate;
	}

	public String getTimoney() {
		return timoney;
	}

	public void setTimoney(String timoney) {
		this.timoney = timoney;
	}

	public String getCrmoney() {
		return crmoney;
	}

	public void setCrmoney(String crmoney) {
		this.crmoney = crmoney;
	}

	public String getYu() {
		return yu;
	}

	public void setYu(String yu) {
		this.yu = yu;
	}

	public String getZai() {
		return zai;
	}

	public void setZai(String zai) {
		this.zai = zai;
	}

	public HousingBaoDingPay(String taskid, String paynum, String jdate, String timoney, String crmoney, String yu,
			String zai) {
		super();
		this.taskid = taskid;
		this.paynum = paynum;
		this.jdate = jdate;
		this.timoney = timoney;
		this.crmoney = crmoney;
		this.yu = yu;
		this.zai = zai;
	}

	public HousingBaoDingPay() {
		super();
	}
	
	
	
	
}
