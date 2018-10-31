package com.microservice.dao.entity.crawler.housing.ningbo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_ningbo_pay",indexes = {@Index(name = "index_housing_ningbo_pay_taskid", columnList = "taskid")})
public class HousingNingboPay extends IdEntity implements Serializable{

	private String company;//单位名称
	
	private String datea;//交易日期
	
	private String type;//业务类型
	
	private String money;//发生额
	
	private String fee;//余额
	
	private String taskid;

	@Override
	public String toString() {
		return "HousingNingboPay [company=" + company + ", datea=" + datea + ", type=" + type + ", money=" + money
				+ ", fee=" + fee + ", taskid=" + taskid + "]";
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getDatea() {
		return datea;
	}

	public void setDatea(String datea) {
		this.datea = datea;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
}
