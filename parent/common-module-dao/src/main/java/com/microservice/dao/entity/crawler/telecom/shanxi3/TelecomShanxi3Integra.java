package com.microservice.dao.entity.crawler.telecom.shanxi3;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @Description 积分信息实体
 * @author sln
 * @date 2017年8月24日 上午9:39:16
 */
@Entity
@Table(name = "telecom_shanxi3_integra",indexes = {@Index(name = "index_telecom_shanxi3_integra_taskid", columnList = "taskid")})
public class TelecomShanxi3Integra extends IdEntity implements Serializable {
	private static final long serialVersionUID = -8485366714529920660L;
	private String taskid;
//	月份
	private String yearmonth;
//	积分类型
	private String type;
//	积分值
	private String integra;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getYearmonth() {
		return yearmonth;
	}
	public void setYearmonth(String yearmonth) {
		this.yearmonth = yearmonth;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIntegra() {
		return integra;
	}
	public void setIntegra(String integra) {
		this.integra = integra;
	}
	public TelecomShanxi3Integra() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TelecomShanxi3Integra(String taskid, String yearmonth, String type, String integra) {
		super();
		this.taskid = taskid;
		this.yearmonth = yearmonth;
		this.type = type;
		this.integra = integra;
	}
	
}

