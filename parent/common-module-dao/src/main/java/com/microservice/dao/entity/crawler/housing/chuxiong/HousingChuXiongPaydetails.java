package com.microservice.dao.entity.crawler.housing.chuxiong;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 楚雄公积金缴存信息
 */
@Entity
@Table(name="housing_chuxiong_paydetails")
public class HousingChuXiongPaydetails extends IdEntity implements Serializable {
	private static final long serialVersionUID = -758773159050632619L;

	private String date;// 日期
	private String summary;// 摘要
	private String drawAmount;// 提取
	private String payAmount;// 缴存
	private String balance;// 余额
	private String taskid;
	

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	
	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	

	public String getDrawAmount() {
		return drawAmount;
	}

	public void setDrawAmount(String drawAmount) {
		this.drawAmount = drawAmount;
	}

	public String getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(String payAmount) {
		this.payAmount = payAmount;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	@Override
	public String toString() {
		return "HousingChuXiongPaydetails [date=" + date + ", summary=" + summary + ", drawAmount=" + drawAmount
				+ ", payAmount=" + payAmount + ", balance=" + balance + ", taskid=" + taskid + "]";
	}

	public HousingChuXiongPaydetails() {
		super();
		// TODO Auto-generated constructor stub
	}
}
