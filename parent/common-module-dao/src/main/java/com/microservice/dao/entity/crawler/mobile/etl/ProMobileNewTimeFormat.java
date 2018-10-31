package com.microservice.dao.entity.crawler.mobile.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="pro_mobile_new_timeformat")
public class ProMobileNewTimeFormat extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String province;
	private String timeformat;
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getTimeformat() {
		return timeformat;
	}
	public void setTimeformat(String timeformat) {
		this.timeformat = timeformat;
	}
	@Override
	public String toString() {
		return "ProMobileNewTimeFormat [taskId=" + taskId + ", province=" + province + ", timeformat=" + timeformat
				+ "]";
	}
	
	
	
}
