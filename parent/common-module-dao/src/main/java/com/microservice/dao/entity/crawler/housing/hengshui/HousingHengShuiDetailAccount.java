package com.microservice.dao.entity.crawler.housing.hengshui;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 衡水市公积金明细账信息
 * @author: sln
 * @date:
 */
@Entity
@Table(name = "housing_hengshui_detailaccount",indexes = {@Index(name = "index_housing_hengshui_detailaccount_taskid", columnList = "taskid")})
public class HousingHengShuiDetailAccount extends IdEntity implements Serializable {
	private static final long serialVersionUID = 6481831903414445397L;
	private String taskid;
//	行号
	private Integer rownum;
//	缴交年月
	private String chargeyearmonth;
//	摘要
//	private String  summary;    返回的代号过多，加之无太多实际意义，决定放弃此字段
//	收入金额
	private String receivemoney;
//	支出金额
	private String  amount;
//	余额
	private String balance;
	
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public Integer getRownum() {
		return rownum;
	}

	public void setRownum(Integer rownum) {
		this.rownum = rownum;
	}

	public String getChargeyearmonth() {
		return chargeyearmonth;
	}

	public void setChargeyearmonth(String chargeyearmonth) {
		this.chargeyearmonth = chargeyearmonth;
	}
	public String getReceivemoney() {
		return receivemoney;
	}

	public void setReceivemoney(String receivemoney) {
		this.receivemoney = receivemoney;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public HousingHengShuiDetailAccount() {
		super();
		// TODO Auto-generated constructor stub
	}
}
