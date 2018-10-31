package com.microservice.dao.entity.crawler.telecom.chongqing;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;


/**
 * 重庆用户积分详情
 * @author tz
 *
 */
@Entity
@Table(name = "telecom_chongqing_integral" ,indexes = {@Index(name = "index_telecom_chongqing_integral_taskid", columnList = "taskid")})
public class TelecomChongqingIntegral extends IdEntity {
	
	/**
	 * taskid
	 */
	private String taskid;
	
	/**
	 * 名称 
	 */
	private String type;
	
	/**
	 * 积分
	 */
	private String integral;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIntegral() {
		return integral;
	}

	public void setIntegral(String integral) {
		this.integral = integral;
	}

	@Override
	public String toString() {
		return "TelecomChongqingIntegral [taskid=" + taskid + ", type=" + type + ", integral=" + integral + "]";
	}

	public TelecomChongqingIntegral(String taskid, String type, String integral) {
		super();
		this.taskid = taskid;
		this.type = type;
		this.integral = integral;
	}

	public TelecomChongqingIntegral() {
		super();
		// TODO Auto-generated constructor stub
	}

	

}