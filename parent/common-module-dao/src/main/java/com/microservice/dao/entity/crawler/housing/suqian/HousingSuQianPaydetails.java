package com.microservice.dao.entity.crawler.housing.suqian;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 宿迁公积金用户信息
 */
@Entity
@Table(name="housing_suqian_paydetails")
public class HousingSuQianPaydetails extends IdEntity implements Serializable {
	private static final long serialVersionUID = -758773159050632619L;
	private String rnum;//行数
	private String confirmDate;//确认日期
	private String type;//业务类型
	private String accountDate;//对应年月
	private String increaseAmount;//收入
	private String reduceAmount;//支出
	private String balance;//余额
	private String taskid;	
	
	public String getRnum() {
		return rnum;
	}


	public void setRnum(String rnum) {
		this.rnum = rnum;
	}


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


	public String getAccountDate() {
		return accountDate;
	}


	public void setAccountDate(String accountDate) {
		this.accountDate = accountDate;
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
		return "HousingLiaoChengPaydetails [rnum=" + rnum + ", confirmDate=" + confirmDate + ", type=" + type
				+ ", accountDate=" + accountDate + ", increaseAmount=" + increaseAmount + ", reduceAmount="
				+ reduceAmount + ", balance=" + balance + ", taskid=" + taskid + "]";
	}
	public HousingSuQianPaydetails(String rnum, String confirmDate, String type, String accountDate,
			String increaseAmount, String reduceAmount, String balance, String taskid) {
		super();
		this.rnum = rnum;
		this.confirmDate = confirmDate;
		this.type = type;
		this.accountDate = accountDate;
		this.increaseAmount = increaseAmount;
		this.reduceAmount = reduceAmount;
		this.balance = balance;
		this.taskid = taskid;
	}


	public HousingSuQianPaydetails() {
		super();
		// TODO Auto-generated constructor stub
	}	
}
