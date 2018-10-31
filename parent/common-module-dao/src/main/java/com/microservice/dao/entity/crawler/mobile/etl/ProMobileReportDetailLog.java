package com.microservice.dao.entity.crawler.mobile.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="pro_mobile_report_detail_log")
public class ProMobileReportDetailLog extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String stepName;
	private String tableName;
	private String errorDetail;
	private String stepResult;
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getStepName() {
		return stepName;
	}
	public void setStepName(String stepName) {
		this.stepName = stepName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	@Column(columnDefinition="text")
	public String getErrorDetail() {
		return errorDetail;
	}
	public void setErrorDetail(String errorDetail) {
		this.errorDetail = errorDetail;
	}
	public String getStepResult() {
		return stepResult;
	}
	public void setStepResult(String stepResult) {
		this.stepResult = stepResult;
	}
	@Override
	public String toString() {
		return "ProMobileReportDetailLog [taskId=" + taskId + ", stepName=" + stepName + ", tableName=" + tableName
				+ ", errorDetail=" + errorDetail + ", stepResult=" + stepResult + "]";
	}
	
	
}
