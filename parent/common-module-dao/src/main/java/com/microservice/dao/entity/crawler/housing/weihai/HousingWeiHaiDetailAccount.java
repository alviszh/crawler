package com.microservice.dao.entity.crawler.housing.weihai;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 威海市公积金明细账信息
 * @author: sln
 * @date:
 */
@Entity
@Table(name = "housing_weihai_detailaccount",indexes = {@Index(name = "index_housing_weihai_detailaccount_taskid", columnList = "taskid")})
public class HousingWeiHaiDetailAccount extends IdEntity implements Serializable {
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
//	计息日期
	private String interestdate;
	//备注(更改了表结构之后新添加的字段，相对于之前，有的字段没有了，但是不用去除)
	private String note;
	public String getInterestdate() {
		return interestdate;
	}
	public void setInterestdate(String interestdate) {
		this.interestdate = interestdate;
	}
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

	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public HousingWeiHaiDetailAccount() {
		super();
		// TODO Auto-generated constructor stub
	}
}
