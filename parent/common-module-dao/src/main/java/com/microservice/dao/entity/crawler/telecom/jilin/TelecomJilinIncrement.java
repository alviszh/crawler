package com.microservice.dao.entity.crawler.telecom.jilin;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "telecom_jilin_increment", indexes = {@Index(name = "index_telecom_jilin_increment_taskid", columnList = "taskid")})
public class TelecomJilinIncrement extends IdEntity {

	private String taskid;
	private String serviceNum;								//业务号码
	private String startDate;								//起始时间
	private String endDate;									//结束时间
	private String serviceName;								//业务名称
	private String fee;										//费用
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getServiceNum() {
		return serviceNum;
	}
	public void setServiceNum(String serviceNum) {
		this.serviceNum = serviceNum;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	@Override
	public String toString() {
		return "TelecomJilinIncrement [taskid=" + taskid + ", serviceNum=" + serviceNum + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", serviceName=" + serviceName + ", fee=" + fee + "]";
	}
	
	

}