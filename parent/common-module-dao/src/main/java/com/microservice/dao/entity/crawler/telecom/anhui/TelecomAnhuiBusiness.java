package com.microservice.dao.entity.crawler.telecom.anhui;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="telecom_anhui_business",indexes = {@Index(name = "index_telecom_anhui_business_taskid", columnList = "taskid")}) 
public class TelecomAnhuiBusiness extends IdEntity{

    private String name;//业务名字
	
	private String status;//状态
	
	private String doTime;//办理时间
	
	private String startTime;//生效时间
	
	private String endTime;//失效时间
	
	
	private String descr;//描述
	
	private String taskid;

	@Override
	public String toString() {
		return "TelecomAnhuiBusiness [name=" + name + ", status=" + status + ", doTime=" + doTime + ", startTime="
				+ startTime + ", endTime=" + endTime + ", descr=" + descr + ", taskid=" + taskid + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDoTime() {
		return doTime;
	}

	public void setDoTime(String doTime) {
		this.doTime = doTime;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	@Column(columnDefinition="text")
	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}


	
	
}
