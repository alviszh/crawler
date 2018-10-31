package com.microservice.dao.entity.crawler.mobile.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="pro_mobile_report_vitality_analysis_summary",indexes = {@Index(name = "index_pro_mobile_report_vitality_analysis_summary_taskid", columnList = "taskId")})
public class ProMobileReportVitalityAnalysisSummary extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String callDays;
	private String dialCounts;
	private String calledCounts;
	private String dialDuration;
	private String called_duration;
	private String dataType;
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getCallDays() {
		return callDays;
	}
	public void setCallDays(String callDays) {
		this.callDays = callDays;
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
	public String getDialDuration() {
		return dialDuration;
	}
	public void setDialDuration(String dialDuration) {
		this.dialDuration = dialDuration;
	}
	public String getCalled_duration() {
		return called_duration;
	}
	public void setCalled_duration(String called_duration) {
		this.called_duration = called_duration;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	@Override
	public String toString() {
		return "ProMobileReportVitalityAnalysisSummary [taskId=" + taskId + ", callDays=" + callDays + ", dialCounts="
				+ dialCounts + ", calledCounts=" + calledCounts + ", dialDuration=" + dialDuration
				+ ", called_duration=" + called_duration + ", dataType=" + dataType + "]";
	}
	
	
}
