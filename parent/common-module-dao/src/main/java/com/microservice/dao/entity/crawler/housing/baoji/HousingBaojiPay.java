package com.microservice.dao.entity.crawler.housing.baoji;

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
@Table(name = "housing_baoji_pay")
public class HousingBaojiPay extends IdEntity {

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
	private String debtAmount;
	
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
	private String lendingdirection;
	/**
	 * 摘要
	 */
	private String summary;
	

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
		return "HousingBaojiPay [taskid=" + taskid + ", date=" + date + ", debtAmount=" + debtAmount + ", creditAmount="
				+ creditAmount + ", balance=" + balance + ", lendingdirection=" + lendingdirection + ", summary="
				+ summary + "]";
	}

	public HousingBaojiPay(String taskid, String date, String debtAmount, String creditAmount, String balance,
			String lendingdirection, String summary) {
		super();
		this.taskid = taskid;
		this.date = date;
		this.debtAmount = debtAmount;
		this.creditAmount = creditAmount;
		this.balance = balance;
		this.lendingdirection = lendingdirection;
		this.summary = summary;
	}
	public HousingBaojiPay() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
