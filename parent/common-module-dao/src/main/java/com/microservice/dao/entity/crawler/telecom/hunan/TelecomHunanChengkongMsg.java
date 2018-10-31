package com.microservice.dao.entity.crawler.telecom.hunan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 湖南电信程控业务信息
 * @author qizhongbin
 *
 */
@Entity
@Table(name="telecom_hunan_chengkongmsg",indexes = {@Index(name = "index_telecom_hunan_chengkongmsg_taskid", columnList = "taskid")})
public class TelecomHunanChengkongMsg  extends IdEntity{
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	  

	/**产品号码*/   
	@Column(name="phone")
	private String phone;
	
	/**业务名称*/   
	@Column(name="business_name")
	private String businessname;
	
	/**业务介绍*/   
	@Column(name="business_introduce")
	private String businessintroduce;
	
	
	/**生效时间*/   
	@Column(name="effect_time")
	private String effecttime;


	public String getBusinessname() {
		return businessname;
	}

	public void setBusinessname(String businessname) {
		this.businessname = businessname;
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

	public String getBusinessintroduce() {
		return businessintroduce;
	}

	public void setBusinessintroduce(String businessintroduce) {
		this.businessintroduce = businessintroduce;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
