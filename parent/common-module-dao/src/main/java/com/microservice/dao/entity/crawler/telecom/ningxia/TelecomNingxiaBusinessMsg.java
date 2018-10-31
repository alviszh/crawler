package com.microservice.dao.entity.crawler.telecom.ningxia;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 宁夏电信业务信息
 * @author qizhongbin
 *
 */
@Entity
@Table(name="telecom_ningxia_businessmsg",indexes = {@Index(name = "index_telecom_ningxia_businessmsg_taskid", columnList = "taskid")})
public class TelecomNingxiaBusinessMsg  extends IdEntity{
	@Column(name="userid")
	private Integer userid;
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	  
	///////////////////////////////////////增值业务组//////////////////////////////////
	/**品牌*/    //增值业务组
	@Column(name="brand")
	private String brand;
	
	/**业务名称*/   //增值业务组
	@Column(name="business_name")
	private String businessname;
	
	/**业务费用*/   //增值业务组
	@Column(name="business_cost")
	private String businesscost;
	
	/**费用周期*/    //增值业务组
	@Column(name="cost_period")
	private String costperiod;
	
	/**生效时间*/    //增值业务组
	@Column(name="effect_time")
	private String effecttime;

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getBusinessname() {
		return businessname;
	}

	public void setBusinessname(String businessname) {
		this.businessname = businessname;
	}

	public String getBusinesscost() {
		return businesscost;
	}

	public void setBusinesscost(String businesscost) {
		this.businesscost = businesscost;
	}

	public String getCostperiod() {
		return costperiod;
	}

	public void setCostperiod(String costperiod) {
		this.costperiod = costperiod;
	}

	public String getEffecttime() {
		return effecttime;
	}

	public void setEffecttime(String effecttime) {
		this.effecttime = effecttime;
	}
	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

}
