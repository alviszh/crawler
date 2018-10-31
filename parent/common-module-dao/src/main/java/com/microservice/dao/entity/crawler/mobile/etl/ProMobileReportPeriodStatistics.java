package com.microservice.dao.entity.crawler.mobile.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="pro_mobile_report_period_statistics",indexes = {@Index(name = "index_pro_mobile_report_period_statistics_taskid", columnList = "taskId")})
public class ProMobileReportPeriodStatistics extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String communicatePeriod;
	private String communicateCounts;
	private String contactNums;
	private String communicateDuration;
	private String dialCounts;
	private String calledCounts;
	private String lastCall;
	private String firstCall;
	private String dataType;
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getCommunicatePeriod() {
		return communicatePeriod;
	}
	public void setCommunicatePeriod(String communicatePeriod) {
		this.communicatePeriod = communicatePeriod;
	}
	public String getCommunicateCounts() {
		return communicateCounts;
	}
	public void setCommunicateCounts(String communicateCounts) {
		this.communicateCounts = communicateCounts;
	}
	public String getContactNums() {
		return contactNums;
	}
	public void setContactNums(String contactNums) {
		this.contactNums = contactNums;
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
	public String getLastCall() {
		return lastCall;
	}
	public void setLastCall(String lastCall) {
		this.lastCall = lastCall;
	}
	public String getFirstCall() {
		return firstCall;
	}
	public void setFirstCall(String firstCall) {
		this.firstCall = firstCall;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	@Override
	public String toString() {
		return "ProMobileReportPeriodStatistics [taskId=" + taskId + ", communicatePeriod=" + communicatePeriod
				+ ", communicateCounts=" + communicateCounts + ", contactNums=" + contactNums + ", communicateDuration="
				+ communicateDuration + ", dialCounts=" + dialCounts + ", calledCounts=" + calledCounts + ", lastCall="
				+ lastCall + ", firstCall=" + firstCall + ", dataType=" + dataType + "]";
	}	
}
