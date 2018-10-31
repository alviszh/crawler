package com.microservice.dao.entity.crawler.housing.yiwu;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_yiwu_detail")
public class HousingYiWuDetail extends IdEntity implements Serializable{

	
	private String taskid;
	private String date; //日期
	private String abs;//摘要
	private String drawMoney;//支取金额
	private String incomeMoney;//收入金额
	private String balance;//余额
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
	public String getAbs() {
		return abs;
	}
	public void setAbs(String abs) {
		this.abs = abs;
	}
	public String getDrawMoney() {
		return drawMoney;
	}
	public void setDrawMoney(String drawMoney) {
		this.drawMoney = drawMoney;
	}
	public String getIncomeMoney() {
		return incomeMoney;
	}
	public void setIncomeMoney(String incomeMoney) {
		this.incomeMoney = incomeMoney;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
}
