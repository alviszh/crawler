package com.microservice.dao.entity.crawler.mobile.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="mobile_report_familyinfo",indexes = {@Index(name = "index_mobile_report_familyinfo_taskid", columnList = "taskId")})


public class MobileReportFamilyInfo extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String familyNetworkNum;
	private String code;
	private String message;
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getFamilyNetworkNum() {
		return familyNetworkNum;
	}
	public void setFamilyNetworkNum(String familyNetworkNum) {
		this.familyNetworkNum = familyNetworkNum;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "MobileReportFamilyInfo [taskId=" + taskId + ", familyNetworkNum=" + familyNetworkNum + ", code=" + code
				+ ", message=" + message + "]";
	}
	
	

}
