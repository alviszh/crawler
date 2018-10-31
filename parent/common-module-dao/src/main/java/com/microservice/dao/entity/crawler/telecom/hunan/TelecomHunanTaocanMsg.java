package com.microservice.dao.entity.crawler.telecom.hunan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 湖南电信套餐业务信息
 * @author qizhongbin
 *
 */
@Entity
@Table(name="telecom_hunan_taocanMsg",indexes = {@Index(name = "index_telecom_hunan_taocanMsg_taskid", columnList = "taskid")})
public class TelecomHunanTaocanMsg  extends IdEntity{
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	  
	
	/**产品号码*/   
	@Column(name="phone")
	private String phone;
	
	/**套餐名称*/   
	@Column(name="business_name")
	private String businessname;
	
	/**说明*/   
	@Column(name="explain")
	private String explain;
	
	
	/**生效时间*/   
	@Column(name="effect_time")
	private String effecttime;
	
	/**失效时间*/   
	@Column(name="lose_time")
	private String losetime;


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


	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

	public String getLosetime() {
		return losetime;
	}

	public void setLosetime(String losetime) {
		this.losetime = losetime;
	}

}
