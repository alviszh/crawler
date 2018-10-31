package com.microservice.dao.entity.crawler.telecom.xinjiang;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_xinjiang_paymonths" ,indexes = {@Index(name = "index_telecom_xinjiang_paymonths_taskid", columnList = "taskid")})
public class TelecomXinjiangPayMonths extends IdEntity {

	private String month;//月份
	private String itemName;// 项目名称
	private String itemValue;// 项目金额
	private String taskid;

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemValue() {
		return itemValue;
	}

	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	
	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}
	public TelecomXinjiangPayMonths(String month, String itemName, String itemValue, String taskid) {
		super();
		this.month = month;
		this.itemName = itemName;
		this.itemValue = itemValue;
		this.taskid = taskid;
	}

	public TelecomXinjiangPayMonths() {
		super();
		// TODO Auto-generated constructor stub
	}
}