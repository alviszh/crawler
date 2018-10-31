package com.microservice.dao.entity.crawler.housing.rizhao;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

/**
 * 缴存明细
 *
 */
@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "housing_rizhao_pay")
public class HousingRizhaoPay extends IdEntity {

	/**
	 * taskid uuid 前端通过uuid访问状态结果
	 */
	private String taskid;

	/**
	 * 日期
	 */
	private String date;
	/**
	 * 摘要
	 */
	private String summary;
	
	/**
	 * 减少金额
	 */
	private String reduceAmount;
	
	/**
	 * 贷方金额
	 */
	private String increaseAmount;
	
	/**
	 * 余额
	 */
	private String balance;

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

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}


	public String getReduceAmount() {
		return reduceAmount;
	}

	public void setReduceAmount(String reduceAmount) {
		this.reduceAmount = reduceAmount;
	}

	public String getIncreaseAmount() {
		return increaseAmount;
	}

	public void setIncreaseAmount(String increaseAmount) {
		this.increaseAmount = increaseAmount;
	}
	public HousingRizhaoPay(String taskid, String date, String summary, String reduceAmount, String increaseAmount,
			String balance) {
		super();
		this.taskid = taskid;
		this.date = date;
		this.summary = summary;
		this.reduceAmount = reduceAmount;
		this.increaseAmount = increaseAmount;
		this.balance = balance;
	}

	public HousingRizhaoPay() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
