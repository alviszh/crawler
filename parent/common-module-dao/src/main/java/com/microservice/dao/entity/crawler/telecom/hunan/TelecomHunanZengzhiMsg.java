package com.microservice.dao.entity.crawler.telecom.hunan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 湖南电信增值业务信息
 * @author qizhongbin
 *
 */
@Entity
@Table(name="telecom_hunan_zengzhiMsg",indexes = {@Index(name = "index_telecom_hunan_zengzhiMsg_taskid", columnList = "taskid")})
public class TelecomHunanZengzhiMsg  extends IdEntity{
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	  
	

	/**产品号码*/   
	@Column(name="phone")
	private String phone;
	
	/**业务名称*/   
	@Column(name="business_name")
	private String businessname;
	
	/**SP名称*/   
	@Column(name="business_SPname")
	private String businessSPname;
	
	/**业务费用*/   
	@Column(name="business_fee")
	private String businessfee;
	
	
	/**失效时间*/   
	@Column(name="lose_time")
	private String losetime;
	
	
	/**生效时间*/   
	@Column(name="effect_time")
	private String effecttime;


	public String getBusinessfee() {
		return businessfee;
	}

	public void setBusinessfee(String businessfee) {
		this.businessfee = businessfee;
	}

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

	public String getBusinessSPname() {
		return businessSPname;
	}

	public void setBusinessSPname(String businessSPname) {
		this.businessSPname = businessSPname;
	}

	public String getLosetime() {
		return losetime;
	}

	public void setLosetime(String losetime) {
		this.losetime = losetime;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
