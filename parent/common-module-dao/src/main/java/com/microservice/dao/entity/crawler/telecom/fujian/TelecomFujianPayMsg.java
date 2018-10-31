package com.microservice.dao.entity.crawler.telecom.fujian;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 福建电信缴费信息
 * @author qizhongbin
 *
 */
@Entity
@Table(name="telecom_fujian_paymsg",indexes = {@Index(name = "index_telecom_fujian_paymsg_taskid", columnList = "taskid")})
public class TelecomFujianPayMsg  extends IdEntity{
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	
	/**交易日期*/     
	@Column(name="pay_date")
	private String paydate;
	
	/**交易金额*/     
	@Column(name="pay_money")
	private String paymoney;
	
	/**交费类型*/    
	@Column(name="pay_ditch")
	private String payditch;
	
	/**交费方式*/    
	@Column(name="pay_way")
	private String payway;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getPaydate() {
		return paydate;
	}

	public void setPaydate(String paydate) {
		this.paydate = paydate;
	}

	public String getPaymoney() {
		return paymoney;
	}

	public void setPaymoney(String paymoney) {
		this.paymoney = paymoney;
	}


	public String getPayditch() {
		return payditch;
	}

	public void setPayditch(String payditch) {
		this.payditch = payditch;
	}

	public String getPayway() {
		return payway;
	}

	public void setPayway(String payway) {
		this.payway = payway;
	}


	
}
