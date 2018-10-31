package com.microservice.dao.entity.crawler.telecom.shandong;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "telecom_shandong_increment", indexes = {@Index(name = "index_telecom_shandong_increment_taskid", columnList = "taskid")})
public class TelecomShandongIncrement extends IdEntity {

	private String taskid;
	private String serviceName;								//业务名称
	private String startDate;								//申请（起始）时间
	private String fee;										//费用/元
	private String spName;									//sp名称
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getSpName() {
		return spName;
	}
	public void setSpName(String spName) {
		this.spName = spName;
	}
	@Override
	public String toString() {
		return "TelecomShandongIncrement [taskid=" + taskid + ", serviceName=" + serviceName + ", startDate="
				+ startDate + ", fee=" + fee + ", spName=" + spName + "]";
	}
	
	
}