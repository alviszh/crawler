package com.microservice.dao.entity.crawler.mobile.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="pro_mobile_report_calltime_detail_statistics",indexes = {@Index(name = "index_pro_mobile_report_calltime_detail_statistics_taskid", columnList = "taskId")})
public class ProMobileReportCallTimeDetailStatistics extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String longgestCall;
	private String shortestCall;
	private String callWithinOneminute;
	private String callBetweenOneminuteFiveminutes;
	private String callBetweenFiveminutesTenminutes;
	private String callOverTenminutes;
	private String daytimeCounts;
	private String nightCounts;
	private String daytimeDuration;
	private String nightDuration;
	private String inLocalCounts;
	private String outLocalCounts;
	private String inLocalDuration;
	private String outLocalDuration;
	private String dataType;
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getLonggestCall() {
		return longgestCall;
	}
	public void setLonggestCall(String longgestCall) {
		this.longgestCall = longgestCall;
	}
	public String getShortestCall() {
		return shortestCall;
	}
	public void setShortestCall(String shortestCall) {
		this.shortestCall = shortestCall;
	}
	public String getCallWithinOneminute() {
		return callWithinOneminute;
	}
	public void setCallWithinOneminute(String callWithinOneminute) {
		this.callWithinOneminute = callWithinOneminute;
	}
	public String getCallBetweenOneminuteFiveminutes() {
		return callBetweenOneminuteFiveminutes;
	}
	public void setCallBetweenOneminuteFiveminutes(String callBetweenOneminuteFiveminutes) {
		this.callBetweenOneminuteFiveminutes = callBetweenOneminuteFiveminutes;
	}
	public String getCallBetweenFiveminutesTenminutes() {
		return callBetweenFiveminutesTenminutes;
	}
	public void setCallBetweenFiveminutesTenminutes(String callBetweenFiveminutesTenminutes) {
		this.callBetweenFiveminutesTenminutes = callBetweenFiveminutesTenminutes;
	}
	public String getCallOverTenminutes() {
		return callOverTenminutes;
	}
	public void setCallOverTenminutes(String callOverTenminutes) {
		this.callOverTenminutes = callOverTenminutes;
	}
	public String getDaytimeCounts() {
		return daytimeCounts;
	}
	public void setDaytimeCounts(String daytimeCounts) {
		this.daytimeCounts = daytimeCounts;
	}
	public String getNightCounts() {
		return nightCounts;
	}
	public void setNightCounts(String nightCounts) {
		this.nightCounts = nightCounts;
	}
	public String getDaytimeDuration() {
		return daytimeDuration;
	}
	public void setDaytimeDuration(String daytimeDuration) {
		this.daytimeDuration = daytimeDuration;
	}
	public String getNightDuration() {
		return nightDuration;
	}
	public void setNightDuration(String nightDuration) {
		this.nightDuration = nightDuration;
	}
	public String getInLocalCounts() {
		return inLocalCounts;
	}
	public void setInLocalCounts(String inLocalCounts) {
		this.inLocalCounts = inLocalCounts;
	}
	public String getOutLocalCounts() {
		return outLocalCounts;
	}
	public void setOutLocalCounts(String outLocalCounts) {
		this.outLocalCounts = outLocalCounts;
	}
	public String getInLocalDuration() {
		return inLocalDuration;
	}
	public void setInLocalDuration(String inLocalDuration) {
		this.inLocalDuration = inLocalDuration;
	}
	public String getOutLocalDuration() {
		return outLocalDuration;
	}
	public void setOutLocalDuration(String outLocalDuration) {
		this.outLocalDuration = outLocalDuration;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	@Override
	public String toString() {
		return "ProMobileReportCallTimeDetailStatistics [taskId=" + taskId + ", longgestCall=" + longgestCall
				+ ", shortestCall=" + shortestCall + ", callWithinOneminute=" + callWithinOneminute
				+ ", callBetweenOneminuteFiveminutes=" + callBetweenOneminuteFiveminutes
				+ ", callBetweenFiveminutesTenminutes=" + callBetweenFiveminutesTenminutes + ", callOverTenminutes="
				+ callOverTenminutes + ", daytimeCounts=" + daytimeCounts + ", nightCounts=" + nightCounts
				+ ", daytimeDuration=" + daytimeDuration + ", nightDuration=" + nightDuration + ", inLocalCounts="
				+ inLocalCounts + ", outLocalCounts=" + outLocalCounts + ", inLocalDuration=" + inLocalDuration
				+ ", outLocalDuration=" + outLocalDuration + ", dataType=" + dataType + "]";
	}
	
	
}
