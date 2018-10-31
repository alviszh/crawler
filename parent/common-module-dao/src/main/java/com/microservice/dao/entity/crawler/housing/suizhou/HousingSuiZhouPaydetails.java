package com.microservice.dao.entity.crawler.housing.suizhou;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 公积金缴存信息
 */
@Entity
@Table(name="housing_suizhou_paydetails")
public class HousingSuiZhouPaydetails extends IdEntity implements Serializable {
	private static final long serialVersionUID = -758773159050632619L;
	
	private String date;//	日期
	private String type;//	业务类型
	private String amount;//	金额
	private String summary;//	摘要	
	private String taskid;	
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Override
	public String toString() {
		return "HousingSuiZhouPaydetails [date=" + date + ", type=" + type + ", amount=" + amount + ", summary="
				+ summary + ", taskid=" + taskid + "]";
	}
	public HousingSuiZhouPaydetails(String date, String type, String amount, String summary, String taskid) {
		super();
		this.date = date;
		this.type = type;
		this.amount = amount;
		this.summary = summary;
		this.taskid = taskid;
	}
	public HousingSuiZhouPaydetails() {
		super();
		// TODO Auto-generated constructor stub
	}	
}
