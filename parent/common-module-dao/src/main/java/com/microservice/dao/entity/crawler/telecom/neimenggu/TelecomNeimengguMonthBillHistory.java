package com.microservice.dao.entity.crawler.telecom.neimenggu;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 呼和浩特电信月账单记录
 * @author qizhongbin
 *
 */
@Entity
@Table(name="telecom_neimenggu_monthbillhistory",indexes = {@Index(name = "index_telecom_neimenggu_monthbillhistory_taskid", columnList = "taskid")})
public class TelecomNeimengguMonthBillHistory  extends IdEntity{
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	
	
	/**月份*/   
	@Column(name="month")
	private String month;
	
	/**当月合计*/   
	@Column(name="month_all")
	private String monthall;
	
	/**项目名称*/   
	@Column(name="xmmc")
	private String xmmc;
	
	/**项目费用*/   
	@Column(name="xmfy")
	private String xmfy;

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

	public String getXmmc() {
		return xmmc;
	}

	public void setXmmc(String xmmc) {
		this.xmmc = xmmc;
	}

	public String getXmfy() {
		return xmfy;
	}

	public void setXmfy(String xmfy) {
		this.xmfy = xmfy;
	}
	
	

	
	

	
}
