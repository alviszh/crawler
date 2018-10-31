package com.microservice.dao.entity.crawler.housing.baishan;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 白山公积金用户信息
 */
@Entity
@Table(name="housing_baishan_paydetails")
public class HousingBaishanPaydetails extends IdEntity implements Serializable {
	private static final long serialVersionUID = -758773159050632619L;

	private String accountDate;//	日期
	private String debtAmount;//	借方金额
	private String balance;//	余额
	private String creditAmount;//	贷方金额
	private String lendingdirection;//	借贷方向
	private String explanation;//	摘要
	private String taskid;	
	public String getAccountDate() {
		return accountDate;
	}
	public void setAccountDate(String accountDate) {
		this.accountDate = accountDate;
	}
	
	public String getDebtAmount() {
		return debtAmount;
	}
	public void setDebtAmount(String debtAmount) {
		this.debtAmount = debtAmount;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
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
	public String getExplanation() {
		return explanation;
	}
	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	@Override
	public String toString() {
		return "HousingBaishanPaydetails [accountDate=" + accountDate + ", debtAmount=" + debtAmount + ", balance="
				+ balance + ", creditAmount=" + creditAmount + ", lendingdirection=" + lendingdirection
				+ ", explanation=" + explanation +  ", taskid=" + taskid + "]";
	}
	public HousingBaishanPaydetails(String accountDate, String debtAmount, String balance, String creditAmount,
			String lendingdirection, String explanation, String taskid) {
		super();
		this.accountDate = accountDate;
		this.debtAmount = debtAmount;
		this.balance = balance;
		this.creditAmount = creditAmount;
		this.lendingdirection = lendingdirection;
		this.explanation = explanation;
		this.taskid = taskid;
	}
	public HousingBaishanPaydetails() {
		super();
		// TODO Auto-generated constructor stub
	}	
}
