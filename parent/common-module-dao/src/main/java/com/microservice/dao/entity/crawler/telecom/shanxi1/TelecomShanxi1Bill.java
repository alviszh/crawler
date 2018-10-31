package com.microservice.dao.entity.crawler.telecom.shanxi1;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;


/**
 * 山西电信缴费信息
 * @author tz
 *
 */
@Entity
@Table(name = "telecom_shanxi1_bill" ,indexes = {@Index(name = "index_telecom_shanxi1_bill_taskid", columnList = "taskid")})
public class TelecomShanxi1Bill extends IdEntity {
	
	/**
	 * taskid
	 */
	private String taskid;
	
	/**
	 * 支付类型
	 */
	private String paymentType;
	
	/**
	 * 支付金额
	 */
	private String fee;
	
	/**
	 * 时间
	 */
	private String date;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public TelecomShanxi1Bill(String taskid, String paymentType, String fee, String date) {
		super();
		this.taskid = taskid;
		this.paymentType = paymentType;
		this.fee = fee;
		this.date = date;
	}

	public TelecomShanxi1Bill() {
		super();
		// TODO Auto-generated constructor stub
	}

}