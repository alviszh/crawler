package com.microservice.dao.entity.crawler.housing.tieling;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name  ="housing_tieling_pay",indexes = {@Index(name = "index_housing_tieling_pay_taskid", columnList = "taskid")})
public class HousingTieLingPay extends IdEntity implements Serializable{
	private static final long serialVersionUID = 1221206979457794498L;

	private String taskid;	
	private String date;          //交易日期
	private String stract;        //摘要
	private String forehead;      //发生额
	private String balance;       //余额
	
	@Override
	public String toString() {
		return "HousingTieLingPay [date=" + date + ", stract=" + stract +",forehead=" +forehead
				+ ", balance=" + balance+ ",taskid=" + taskid + "]";
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getStract() {
		return stract;
	}

	public void setStract(String stract) {
		this.stract = stract;
	}

	public String getForehead() {
		return forehead;
	}

	public void setForehead(String forehead) {
		this.forehead = forehead;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
 
}
