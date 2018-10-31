package com.microservice.dao.entity.crawler.housing.yanan;

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
@Table(name = "housing_yanan_pay")
public class HousingYananPay extends IdEntity {

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
	 * 借方金额
	 */
	private String debtAmount;
	
	/**
	 * 贷方金额
	 */
	private String creditAmount;
	
	/**
	 * 借贷方向

	 */
	private String lendingdirection;

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
	public String getDebtAmount() {
		return debtAmount;
	}

	public void setDebtAmount(String debtAmount) {
		this.debtAmount = debtAmount;
	}

	public String getCreditAmount() {
		return creditAmount;
	}

	public void setCreditAmount(String creditAmount) {
		this.creditAmount = creditAmount;
	}

	public String getLendingdirection() {
		return lendingdirection;
	}

	public void setLendingdirection(String lendingdirection) {
		this.lendingdirection = lendingdirection;
	}
	@Override
	public String toString() {
		return "HousingYananPay [taskid=" + taskid + ", date=" + date + ", summary=" + summary + ", debtAmount="
				+ debtAmount + ", creditAmount=" + creditAmount + ", lendingdirection=" + lendingdirection
				+ ", balance=" + balance + "]";
	}   
	public HousingYananPay(String taskid, String date, String summary, String debtAmount, String creditAmount,
			String lendingdirection, String balance) {
		super();
		this.taskid = taskid;
		this.date = date;
		this.summary = summary;
		this.debtAmount = debtAmount;
		this.creditAmount = creditAmount;
		this.lendingdirection = lendingdirection;
		this.balance = balance;
	}

	public HousingYananPay() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
