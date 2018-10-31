package com.microservice.dao.entity.crawler.housing.jinan;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 济南市公积金明细账信息
 * @author: sln
 * @date:
 */
@Entity
@Table(name = "housing_jinan_detailaccount",indexes = {@Index(name = "index_housing_jinan_detailaccount_taskid", columnList = "taskid")})
public class HousingJiNanDetailAccount extends IdEntity implements Serializable {
	private static final long serialVersionUID = 6481831903414445397L;
	private String taskid;
	// 行号
	private String rownumber;
	// 交易日期
	private String transdate;
	// 摘要
	private String summary;
	// 借贷标志
	private String borrowflag;;
	// 发生额
	private String amount;
	// 账户余额
	private String balance;
	// 缴交起始年月
	private String begindate;
	// 缴交终止年月
	private String enddate;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getRownumber() {
		return rownumber;
	}
	public void setRownumber(String rownumber) {
		this.rownumber = rownumber;
	}
	public String getTransdate() {
		return transdate;
	}
	public void setTransdate(String transdate) {
		this.transdate = transdate;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getBorrowflag() {
		return borrowflag;
	}
	public void setBorrowflag(String borrowflag) {
		this.borrowflag = borrowflag;
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
	public String getBegindate() {
		return begindate;
	}
	public void setBegindate(String begindate) {
		this.begindate = begindate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public HousingJiNanDetailAccount() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
