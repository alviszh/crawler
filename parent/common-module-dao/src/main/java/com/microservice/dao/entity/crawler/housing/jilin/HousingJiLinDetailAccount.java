package com.microservice.dao.entity.crawler.housing.jilin;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 吉林市公积金明细账信息
 * @author: sln 
 * @date: 
 */
@Entity
@Table(name="housing_jilin_detailaccount",indexes = {@Index(name = "index_housing_jilin_detailaccount_taskid", columnList = "taskid")})
public class HousingJiLinDetailAccount extends IdEntity implements Serializable {
	private static final long serialVersionUID = 6481831903414445397L;
	private String taskid;
//	行号
	private String rownumber;
//	业务日期
	private String transdate;
//	缴交开始日期
	private String begindate;
//	缴交结束日期
	private String enddate;
//	业务明细
	private String usedatec;
//	借方发生额
	private String debitamount;
//	贷方发生额
	private String creditamount;
//	余额
	private String balance;
//	经办柜员
	private String operator;
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
	public String getUsedatec() {
		return usedatec;
	}
	public void setUsedatec(String usedatec) {
		this.usedatec = usedatec;
	}
	public String getDebitamount() {
		return debitamount;
	}
	public void setDebitamount(String debitamount) {
		this.debitamount = debitamount;
	}
	public String getCreditamount() {
		return creditamount;
	}
	public void setCreditamount(String creditamount) {
		this.creditamount = creditamount;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public HousingJiLinDetailAccount() {
		super();
		// TODO Auto-generated constructor stub
	}
	public HousingJiLinDetailAccount(String taskid, String rownumber, String transdate, String begindate,
			String enddate, String usedatec, String debitamount, String creditamount, String balance, String operator) {
		super();
		this.taskid = taskid;
		this.rownumber = rownumber;
		this.transdate = transdate;
		this.begindate = begindate;
		this.enddate = enddate;
		this.usedatec = usedatec;
		this.debitamount = debitamount;
		this.creditamount = creditamount;
		this.balance = balance;
		this.operator = operator;
	}
}
