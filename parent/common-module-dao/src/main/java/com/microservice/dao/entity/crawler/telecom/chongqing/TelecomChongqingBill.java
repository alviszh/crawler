package com.microservice.dao.entity.crawler.telecom.chongqing;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;


/**
 * 重庆用户账单
 * @author tz
 *
 */
@Entity
@Table(name = "telecom_chongqing_bill" ,indexes = {@Index(name = "index_telecom_chongqing_bill_taskid", columnList = "taskid")})
public class TelecomChongqingBill extends IdEntity {
	
	/**
	 * taskid
	 */
	private String taskid;
	
	/**
	 * 科目名称 
	 */
	private String billItem;
	
	/**
	 * 账目金额
	 */
	private String billAmount;
	
	/**
	 * 年月
	 */
	private String yearmonth;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getBillItem() {
		return billItem;
	}

	public void setBillItem(String billItem) {
		this.billItem = billItem;
	}

	public String getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(String billAmount) {
		this.billAmount = billAmount;
	}

	public String getYearmonth() {
		return yearmonth;
	}

	public void setYearmonth(String yearmonth) {
		this.yearmonth = yearmonth;
	}

	@Override
	public String toString() {
		return "TelecomChongqingBill [taskid=" + taskid + ", billItem=" + billItem + ", billAmount=" + billAmount
				+ ", yearmonth=" + yearmonth + "]";
	}

	public TelecomChongqingBill(String taskid, String billItem, String billAmount, String yearmonth) {
		super();
		this.taskid = taskid;
		this.billItem = billItem;
		this.billAmount = billAmount;
		this.yearmonth = yearmonth;
	}

	public TelecomChongqingBill() {
		super();
		// TODO Auto-generated constructor stub
	}


}