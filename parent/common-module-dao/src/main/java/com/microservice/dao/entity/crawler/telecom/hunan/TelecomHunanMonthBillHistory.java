package com.microservice.dao.entity.crawler.telecom.hunan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 湖南电信月账单记录
 * @author qizhongbin
 *
 */
@Entity
@Table(name="telecom_hunan_monthbillhistory",indexes = {@Index(name = "index_telecom_hunan_monthbillhistory_taskid", columnList = "taskid")})
public class TelecomHunanMonthBillHistory  extends IdEntity{
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	
	/**月份*/   
	@Column(name="month")
	private String month;
	
	/**当月合计*/   
	@Column(name="month_all")
	private String monthall;
	
	/**短信费*/   
	@Column(name="msg_fee")
	private String msgfee;
	
	/**月基本费*/   
	@Column(name="month_fee")
	private String monthfee;
	
	/**代收费费*/   
	@Column(name="daishou_fee")
	private String daishoufee;
	
	/**套外优惠*/   
	@Column(name="twyh_fee")
	private String twyhfee;
	
	/**红包返还*/   
	@Column(name="hbfh_fee")
	private String hbfhfee;

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

	public String getMsgfee() {
		return msgfee;
	}

	public void setMsgfee(String msgfee) {
		this.msgfee = msgfee;
	}

	public String getMonthfee() {
		return monthfee;
	}

	public void setMonthfee(String monthfee) {
		this.monthfee = monthfee;
	}

	public String getDaishoufee() {
		return daishoufee;
	}

	public void setDaishoufee(String daishoufee) {
		this.daishoufee = daishoufee;
	}

	public String getTwyhfee() {
		return twyhfee;
	}

	public void setTwyhfee(String twyhfee) {
		this.twyhfee = twyhfee;
	}

	public String getHbfhfee() {
		return hbfhfee;
	}

	public void setHbfhfee(String hbfhfee) {
		this.hbfhfee = hbfhfee;
	}
	

	


	
}
