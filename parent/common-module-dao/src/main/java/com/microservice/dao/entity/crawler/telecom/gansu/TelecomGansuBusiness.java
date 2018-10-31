package com.microservice.dao.entity.crawler.telecom.gansu;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="telecomGansu_business",indexes = {@Index(name = "index_telecomGansu_business_taskid", columnList = "taskid")}) 
public class TelecomGansuBusiness extends IdEntity{

	private String businessName;
	
	private String businessTime;
	
	
	private String businessInfo;
	
	private String taskid;

	@Override
	public String toString() {
		return "TelecomGansuBusiness [businessName=" + businessName + ", businessTime=" + businessTime
				+ ", businessInfo=" + businessInfo + ", taskid=" + taskid + "]";
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getBusinessTime() {
		return businessTime;
	}

	public void setBusinessTime(String businessTime) {
		this.businessTime = businessTime;
	}

	@Column(columnDefinition="text")
	public String getBusinessInfo() {
		return businessInfo;
	}

	public void setBusinessInfo(String businessInfo) {
		this.businessInfo = businessInfo;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public TelecomGansuBusiness(String businessName, String businessTime, String businessInfo, String taskid) {
		super();
		this.businessName = businessName;
		this.businessTime = businessTime;
		this.businessInfo = businessInfo;
		this.taskid = taskid;
	}

	public TelecomGansuBusiness() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
