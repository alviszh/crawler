package com.microservice.dao.entity.crawler.housing.taian;

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
@Table(name = "housing_taian_pay")
public class HousingTaianPay extends IdEntity {

	/**
	 * taskid uuid 前端通过uuid访问状态结果
	 */
	private String taskid;

	/**
	 * 日期
	 */
	private String date;
	
	/**
	 * 借方金额
	 */
	private String debitAmount;
	
	/**
	 * 贷方金额
	 */
	private String creditAmount;
	
	/**
	 * 余额
	 */
	private String balance;
	
	/**
	 * 借贷方向
	 */
	private String lendingDirection;
	
	/**
	 * 摘要
	 */
	private String summary;
	
	/**
	 * 查询年份
	 */
	private String year;

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

	public String getDebitAmount() {
		return debitAmount;
	}

	public void setDebitAmount(String debitAmount) {
		this.debitAmount = debitAmount;
	}

	public String getCreditAmount() {
		return creditAmount;
	}

	public void setCreditAmount(String creditAmount) {
		this.creditAmount = creditAmount;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getLendingDirection() {
		return lendingDirection;
	}

	public void setLendingDirection(String lendingDirection) {
		this.lendingDirection = lendingDirection;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public HousingTaianPay(String taskid, String date, String debitAmount, String creditAmount, String balance,
			String lendingDirection, String summary, String year) {
		super();
		this.taskid = taskid;
		this.date = date;
		this.debitAmount = debitAmount;
		this.creditAmount = creditAmount;
		this.balance = balance;
		this.lendingDirection = lendingDirection;
		this.summary = summary;
		this.year = year;
	}

	public HousingTaianPay() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
