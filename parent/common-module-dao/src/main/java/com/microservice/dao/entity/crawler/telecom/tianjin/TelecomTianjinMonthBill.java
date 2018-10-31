package com.microservice.dao.entity.crawler.telecom.tianjin;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @Description  月账单
 * @author sln
 * @date 2017年9月11日
 */
@Entity
@Table(name = "telecom_tianjin_monthbill",indexes = {@Index(name = "index_telecom_tianjin_monthbill_taskid", columnList = "taskid")})
public class TelecomTianjinMonthBill extends IdEntity implements Serializable {
	private static final long serialVersionUID = -4222892079058199216L;
//	账期
	private String querymonth;
//  接入号码
	private String accessnum;
//	费用项
	private String expensename;
//	费用（元）
	private String expense;
//	合计
	private String totalcost;
	private String taskid;
	public String getQuerymonth() {
		return querymonth;
	}
	public void setQuerymonth(String querymonth) {
		this.querymonth = querymonth;
	}
	public String getAccessnum() {
		return accessnum;
	}
	public void setAccessnum(String accessnum) {
		this.accessnum = accessnum;
	}
	public String getExpensename() {
		return expensename;
	}
	public void setExpensename(String expensename) {
		this.expensename = expensename;
	}
	public String getTotalcost() {
		return totalcost;
	}
	public void setTotalcost(String totalcost) {
		this.totalcost = totalcost;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public TelecomTianjinMonthBill() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getExpense() {
		return expense;
	}
	public void setExpense(String expense) {
		this.expense = expense;
	}
	public TelecomTianjinMonthBill(String querymonth, String accessnum, String expensename, String expense,
			String totalcost, String taskid) {
		super();
		this.querymonth = querymonth;
		this.accessnum = accessnum;
		this.expensename = expensename;
		this.expense = expense;
		this.totalcost = totalcost;
		this.taskid = taskid;
	}
	
}

