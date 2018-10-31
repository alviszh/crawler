package com.microservice.dao.entity.crawler.mobile.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="pro_mobile_report_call_sum_statistics",indexes = {@Index(name = "index_pro_mobile_report_call_sum_statistics_taskid", columnList = "taskId")})
public class ProMobileReportCallSumStatistics extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String communicate_counts;
	private String contactCounts;
	private String cityCounts;
	private String dialCounts;
	private String calledCounts;
	private String dialNums;
	private String calledNums;
	private String durationSum;
	private String dataType;
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getCommunicate_counts() {
		return communicate_counts;
	}
	public void setCommunicate_counts(String communicate_counts) {
		this.communicate_counts = communicate_counts;
	}
	public String getContactCounts() {
		return contactCounts;
	}
	public void setContactCounts(String contactCounts) {
		this.contactCounts = contactCounts;
	}
	public String getCityCounts() {
		return cityCounts;
	}
	public void setCityCounts(String cityCounts) {
		this.cityCounts = cityCounts;
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
	public String getDialNums() {
		return dialNums;
	}
	public void setDialNums(String dialNums) {
		this.dialNums = dialNums;
	}
	public String getCalledNums() {
		return calledNums;
	}
	public void setCalledNums(String calledNums) {
		this.calledNums = calledNums;
	}
	public String getDurationSum() {
		return durationSum;
	}
	public void setDurationSum(String durationSum) {
		this.durationSum = durationSum;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	@Override
	public String toString() {
		return "ProMobileReportCallSumStatistics [taskId=" + taskId + ", communicate_counts=" + communicate_counts
				+ ", contactCounts=" + contactCounts + ", cityCounts=" + cityCounts + ", dialCounts=" + dialCounts
				+ ", calledCounts=" + calledCounts + ", dialNums=" + dialNums + ", calledNums=" + calledNums
				+ ", durationSum=" + durationSum + ", dataType=" + dataType + "]";
	}
		
}
