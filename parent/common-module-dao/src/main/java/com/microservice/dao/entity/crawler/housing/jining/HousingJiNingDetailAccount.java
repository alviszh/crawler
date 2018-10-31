package com.microservice.dao.entity.crawler.housing.jining;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 济宁市公积金明细账信息
 * @author: sln
 * @date:
 */
@Entity
@Table(name = "housing_jining_detailaccount",indexes = {@Index(name = "index_housing_jining_detailaccount_taskid", columnList = "taskid")})
public class HousingJiNingDetailAccount extends IdEntity implements Serializable {
	private static final long serialVersionUID = 6481831903414445397L;
	private String taskid;
//	交易日期
	private String transdate;
//	发生额
	private String  amount;
//	余额
	private String balance;
//	摘要
	private String summary;
//	经办机构
	private String treatganization;
//	备注
	private String note;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getTransdate() {
		return transdate;
	}
	public void setTransdate(String transdate) {
		this.transdate = transdate;
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
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getTreatganization() {
		return treatganization;
	}
	public void setTreatganization(String treatganization) {
		this.treatganization = treatganization;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public HousingJiNingDetailAccount() {
		super();
		// TODO Auto-generated constructor stub
	}
}
