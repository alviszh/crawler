package com.microservice.dao.entity.crawler.mobile.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="pro_mobile_report_contacts_top",indexes = {@Index(name = "index_pro_mobile_report_contacts_top_taskid", columnList = "taskId")})
public class ProMobileReportContactsTop extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String phoneNum;
	private String city;
	private String communicateCounts;
	private String communicateDuration;
	private String dialCounts;
	private String calledCounts;
	private String dataType;
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCommunicateCounts() {
		return communicateCounts;
	}
	public void setCommunicateCounts(String communicateCounts) {
		this.communicateCounts = communicateCounts;
	}
	public String getCommunicateDuration() {
		return communicateDuration;
	}
	public void setCommunicateDuration(String communicateDuration) {
		this.communicateDuration = communicateDuration;
	}
	public String getDialCounts() {
		return dialCounts;
	}
	public void setDialCounts(String dialCounts) {
		this.dialCounts = dialCounts;
	}
	public String getCalledCounts() {
		return calledCounts;
	}
	public void setCalledCounts(String calledCounts) {
		this.calledCounts = calledCounts;
	}
	
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	@Override
	public String toString() {
		return "ProMobileReportContactsTop [taskId=" + taskId + ", phoneNum=" + phoneNum + ", city=" + city
				+ ", communicateCounts=" + communicateCounts + ", communicateDuration=" + communicateDuration
				+ ", dialCounts=" + dialCounts + ", calledCounts=" + calledCounts + ", dataType=" + dataType + "]";
	}	
}
