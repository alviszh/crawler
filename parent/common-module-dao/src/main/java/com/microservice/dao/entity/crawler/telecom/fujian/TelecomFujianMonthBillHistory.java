package com.microservice.dao.entity.crawler.telecom.fujian;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 福建电信月账单记录
 * @author qizhongbin
 *
 */
@Entity
@Table(name="telecom_fujian_monthbillhistory",indexes = {@Index(name = "index_telecom_fujian_monthbillhistory_taskid", columnList = "taskid")})
public class TelecomFujianMonthBillHistory  extends IdEntity{
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	
	/**月份*/   
	@Column(name="month")
	private String month;
	
	/**当月合计*/   
	@Column(name="month_all")
	private String monthall;
	
	/**月基本费*/   
	@Column(name="month_fee")
	private String monthfee;
	
	/**上网费*/   
	@Column(name="inter_fee")
	private String interfee;
	

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}


	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getMonthall() {
		return monthall;
	}

	public void setMonthall(String monthall) {
		this.monthall = monthall;
	}



	public String getMonthfee() {
		return monthfee;
	}

	public void setMonthfee(String monthfee) {
		this.monthfee = monthfee;
	}

	public String getInterfee() {
		return interfee;
	}

	public void setInterfee(String interfee) {
		this.interfee = interfee;
	}

	
	

	


	
}
