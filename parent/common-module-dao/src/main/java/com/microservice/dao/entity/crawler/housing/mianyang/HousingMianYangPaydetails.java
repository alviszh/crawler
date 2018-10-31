package com.microservice.dao.entity.crawler.housing.mianyang;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 绵阳公积金缴存信息
 */
@Entity
@Table(name="housing_mianyang_paydetails",indexes = {@Index(name = "index_housing_mianyang_paydetails_taskid", columnList = "taskid")})
public class HousingMianYangPaydetails extends IdEntity implements Serializable {
	private static final long serialVersionUID = -758773159050632619L;

	private String date;// 日期
	private String type;// 业务类型
	private String summary;// 摘要
	private String increaseAmount;// 收入
	private String reduceAmount;// 支出
	private String balance;// 余额
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
	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getIncreaseAmount() {
		return increaseAmount;
	}

	public void setIncreaseAmount(String increaseAmount) {
		this.increaseAmount = increaseAmount;
	}

	public String getReduceAmount() {
		return reduceAmount;
	}

	public void setReduceAmount(String reduceAmount) {
		this.reduceAmount = reduceAmount;
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
		return "HousingMianYangPaydetails [date=" + date + ", type=" + type + ", summary=" + summary
				+ ", increaseAmount=" + increaseAmount + ", reduceAmount=" + reduceAmount + ", balance=" + balance
				+ ", taskid=" + taskid + "]";
	}
	public HousingMianYangPaydetails() {
		super();
		// TODO Auto-generated constructor stub
	}
}
