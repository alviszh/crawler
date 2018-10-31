package com.microservice.dao.entity.crawler.telecom.guangxi;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="telecomGuangxi_pay",indexes = {@Index(name = "index_telecomGuangxi_pay_taskid", columnList = "taskid")}) 
public class TelecomGuangxiPay extends IdEntity{

private String num;//缴费号码
	
	private String money;//缴费金额
	
	private String datea;//缴费日期
	
	private String type;//缴费来源
	
	private String taskid;//

	@Override
	public String toString() {
		return "TelecomNanningPay [num=" + num + ", money=" + money + ", datea=" + datea + ", type=" + type
				+ ", taskid=" + taskid + "]";
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
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

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
}
