package com.microservice.dao.entity.crawler.housing.yangzhou;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 公积金缴存信息
 */
@Entity
@Table(name="housing_yangzhou_paydetails")
public class HousingYangzhouPaydetails extends IdEntity implements Serializable {
	private static final long serialVersionUID = -758773159050632619L;
	
	private String confirmDate;//确认日期
	private String type;//处理类型
	private String paymonth;//汇缴年月
	private String incomeAmount;//公积金收入
	private String expendAmount;//公积金支出
	private String interests;//公积金利息
	private String balance;//公积金余额
	private String subsidyIncome;//补贴收入
	private String subsidyExpend;//补贴支出
	private String subsidyInterests;//补贴利息
	private String subsidyBalance;//补贴余额
	private String totalBalance;//余额合计
	private String taskid;
	
	public String getConfirmDate() {
		return confirmDate;
	}
	public void setConfirmDate(String confirmDate) {
		this.confirmDate = confirmDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPaymonth() {
		return paymonth;
	}

	public void setPaymonth(String paymonth) {
		this.paymonth = paymonth;
	}
	public String getIncomeAmount() {
		return incomeAmount;
	}

	public void setIncomeAmount(String incomeAmount) {
		this.incomeAmount = incomeAmount;
	}

	public String getExpendAmount() {
		return expendAmount;
	}

	public void setExpendAmount(String expendAmount) {
		this.expendAmount = expendAmount;
	}

	public String getInterests() {
		return interests;
	}

	public void setInterests(String interests) {
		this.interests = interests;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getSubsidyIncome() {
		return subsidyIncome;
	}

	public void setSubsidyIncome(String subsidyIncome) {
		this.subsidyIncome = subsidyIncome;
	}

	public String getSubsidyExpend() {
		return subsidyExpend;
	}

	public void setSubsidyExpend(String subsidyExpend) {
		this.subsidyExpend = subsidyExpend;
	}

	public String getSubsidyInterests() {
		return subsidyInterests;
	}

	public void setSubsidyInterests(String subsidyInterests) {
		this.subsidyInterests = subsidyInterests;
	}

	public String getSubsidyBalance() {
		return subsidyBalance;
	}
	public void setSubsidyBalance(String subsidyBalance) {
		this.subsidyBalance = subsidyBalance;
	}

	public String getTotalBalance() {
		return totalBalance;
	}

	public void setTotalBalance(String totalBalance) {
		this.totalBalance = totalBalance;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Override
	public String toString() {
		return "HousingYangzhouPaydetails [confirmDate=" + confirmDate + ", type=" + type + ", paymonth=" + paymonth
				+ ", incomeAmount=" + incomeAmount + ", expendAmount=" + expendAmount + ", interests=" + interests
				+ ", balance=" + balance + ", subsidyIncome=" + subsidyIncome + ", subsidyExpend=" + subsidyExpend
				+ ", subsidyInterests=" + subsidyInterests + ", subsidyBalance=" + subsidyBalance + ", totalBalance="
				+ totalBalance + ", taskid=" + taskid + "]";
	}
	
	public HousingYangzhouPaydetails(String confirmDate, String type, String paymonth, String incomeAmount,
			String expendAmount, String interests, String balance, String subsidyIncome, String subsidyExpend,
			String subsidyInterests, String subsidyBalance, String totalBalance, String taskid) {
		super();
		this.confirmDate = confirmDate;
		this.type = type;
		this.paymonth = paymonth;
		this.incomeAmount = incomeAmount;
		this.expendAmount = expendAmount;
		this.interests = interests;
		this.balance = balance;
		this.subsidyIncome = subsidyIncome;
		this.subsidyExpend = subsidyExpend;
		this.subsidyInterests = subsidyInterests;
		this.subsidyBalance = subsidyBalance;
		this.totalBalance = totalBalance;
		this.taskid = taskid;
	}
	public HousingYangzhouPaydetails() {
		super();
	}	
}
