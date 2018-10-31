package com.microservice.dao.entity.crawler.telecom.ningxia;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 宁夏电信月账单记录
 * @author qizhongbin
 *
 */
@Entity
@Table(name="telecom_ningxia_monthbillhistory",indexes = {@Index(name = "index_telecom_ningxia_monthbillhistory_taskid", columnList = "taskid")})
public class TelecomNingxiaMonthBillHistory  extends IdEntity{
	@Column(name="userid")
	private Integer userid;
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
    /////////////////////////////////账单查询组/////////////////////////////
	/**月份*/    //账单查询组
	@Column(name="month")
	private String month;
	
	/**本月已产生话费*/   
	@Column(name="byycshf")
	private String byycshf;
	
	/**本期已付费用*/   
	@Column(name="bqyffy")
	private String bqyffy;
	
	/**本期欠费*/   
	@Column(name="bqqf")
	private String bqqf;
	
	/**本月总话费*/   
	@Column(name="byzhf")
	private String byzhf;

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

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

	public String getByycshf() {
		return byycshf;
	}

	public void setByycshf(String byycshf) {
		this.byycshf = byycshf;
	}

	public String getBqyffy() {
		return bqyffy;
	}

	public void setBqyffy(String bqyffy) {
		this.bqyffy = bqyffy;
	}

	public String getBqqf() {
		return bqqf;
	}

	public void setBqqf(String bqqf) {
		this.bqqf = bqqf;
	}

	public String getByzhf() {
		return byzhf;
	}

	public void setByzhf(String byzhf) {
		this.byzhf = byzhf;
	}

	

	
}
