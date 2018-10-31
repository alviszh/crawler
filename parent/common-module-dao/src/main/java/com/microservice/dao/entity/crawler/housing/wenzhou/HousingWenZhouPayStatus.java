package com.microservice.dao.entity.crawler.housing.wenzhou;

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
@Table(name = "housing_wenzhou_paystatus",indexes = {@Index(name = "index_housing_wenzhou_paystatus_taskid", columnList = "taskid")})
public class HousingWenZhouPayStatus extends IdEntity implements Serializable{

	private String jzdate;//记账日期
	
	private String summary;//摘要
	
	private String years;//所属年月
	
	private String money;//发生金额
	
	private String state;//状态
	
	private String yu;//余额
	
	private String taskid;
	
	private Integer userid;
	
	

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

	public String getJzdate() {
		return jzdate;
	}

	public void setJzdate(String jzdate) {
		this.jzdate = jzdate;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getYears() {
		return years;
	}

	public void setYears(String years) {
		this.years = years;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getYu() {
		return yu;
	}

	public void setYu(String yu) {
		this.yu = yu;
	}

	@Override
	public String toString() {
		return "HousingWenZhouPayStatus [jzdate=" + jzdate + ", summary=" + summary + ", years=" + years + ", money="
				+ money + ", state=" + state + ", yu=" + yu + "]";
	}
	
	
}
