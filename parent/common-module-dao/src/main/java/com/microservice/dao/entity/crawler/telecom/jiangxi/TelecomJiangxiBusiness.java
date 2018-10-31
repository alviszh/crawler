package com.microservice.dao.entity.crawler.telecom.jiangxi;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @Description  业务信息实体
 * @author sln
 * @date 2017年9月16日
 */
@Entity
@Table(name = "telecom_jiangxi_business",indexes = {@Index(name = "index_telecom_jiangxi_business_taskid", columnList = "taskid")})
public class TelecomJiangxiBusiness extends IdEntity implements Serializable {
	private static final long serialVersionUID = -8834400350315276140L;
	private String taskid;
//  业务名称
	private String businessname;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getBusinessname() {
		return businessname;
	}
	public void setBusinessname(String businessname) {
		this.businessname = businessname;
	}
	public TelecomJiangxiBusiness() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TelecomJiangxiBusiness(String taskid, String businessname) {
		super();
		this.taskid = taskid;
		this.businessname = businessname;
	}

}

