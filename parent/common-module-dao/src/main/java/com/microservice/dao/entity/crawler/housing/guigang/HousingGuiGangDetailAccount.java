package com.microservice.dao.entity.crawler.housing.guigang;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 个人明细
 * @author: sln 
 */
@Entity
@Table(name="housing_guigang_detailaccount",indexes = {@Index(name = "index_housing_guigang_detailaccount_taskid", columnList = "taskid")})
public class HousingGuiGangDetailAccount extends IdEntity implements Serializable {
	private static final long serialVersionUID = 2044745879268491789L;
	private String taskid;
//	交易日期
	private String transdate;
//	业务类型
	private String businesstype;
//  发生额
	private String amount;
//	余额
	private String balance;
//	开始年月
	private String startdate;
//	终止年月
	private String enddate;
//	备注
	private String remark;
//	入账状态
	private String entrystatus;
//	经办柜员
	private String 	operator;
	
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
	public String getBusinesstype() {
		return businesstype;
	}
	public void setBusinesstype(String businesstype) {
		this.businesstype = businesstype;
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
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getEntrystatus() {
		return entrystatus;
	}
	public void setEntrystatus(String entrystatus) {
		this.entrystatus = entrystatus;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public HousingGuiGangDetailAccount() {
		super();
		// TODO Auto-generated constructor stub
	}
	public HousingGuiGangDetailAccount(String taskid, String transdate, String businesstype, String amount,
			String balance, String startdate, String enddate, String remark, String entrystatus, String operator) {
		super();
		this.taskid = taskid;
		this.transdate = transdate;
		this.businesstype = businesstype;
		this.amount = amount;
		this.balance = balance;
		this.startdate = startdate;
		this.enddate = enddate;
		this.remark = remark;
		this.entrystatus = entrystatus;
		this.operator = operator;
	}
}
