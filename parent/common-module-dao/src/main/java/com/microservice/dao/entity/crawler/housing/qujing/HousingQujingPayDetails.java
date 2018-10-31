package com.microservice.dao.entity.crawler.housing.qujing;

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
@Table(name = "housing_qujing_paydetails")
public class HousingQujingPayDetails extends IdEntity {
	
	private String dealDate;//	交易日期
	private String summary;//	摘要
	
	private String increaseAmount;//收入
	private String reduceAmount;//支出
	private String balance;//余额
	private String taskid;
	public String getDealDate() {
		return dealDate;
	}
	public void setDealDate(String dealDate) {
		this.dealDate = dealDate;
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
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
	@Override
	public String toString() {
		return "HousingQujingPayDetails [dealDate=" + dealDate + ", summary=" + summary + ", increaseAmount="
				+ increaseAmount + ", reduceAmount=" + reduceAmount + ", balance=" + balance + ", taskid=" + taskid
				+ "]";
	}
	
	public HousingQujingPayDetails(String dealDate, String summary, String increaseAmount, String reduceAmount,
			String balance, String taskid) {
		super();
		this.dealDate = dealDate;
		this.summary = summary;
		this.increaseAmount = increaseAmount;
		this.reduceAmount = reduceAmount;
		this.balance = balance;
		this.taskid = taskid;
	}
	public HousingQujingPayDetails() {
		super();
		// TODO Auto-generated constructor stub
	}
    
}
