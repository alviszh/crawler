package com.microservice.dao.entity.crawler.mobile.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="mobile_report_contracts_statistics_three_month",indexes = {@Index(name = "index_mobile_report_contracts_statistics_three_month_taskid", columnList = "taskId")})


public class MobileReportContactsStatisticsThreeMonth extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String rankNum;	//唯一标识
	private String peerNumber;
	private String callDuration;
	private String callCnt;
	private String callDurationNight;
	private String callCntNight;
	private String callDurationWorktime;
	private String callCntWorktime;
	private String callDurationResttime;
	private String callCntWorkResttime;
	private String callDurationWorkday;
	private String callDurationPlayday;
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getRankNum() {
		return rankNum;
	}
	public void setRankNum(String rankNum) {
		this.rankNum = rankNum;
	}
	public String getPeerNumber() {
		return peerNumber;
	}
	public void setPeerNumber(String peerNumber) {
		this.peerNumber = peerNumber;
	}
	public String getCallDuration() {
		return callDuration;
	}
	public void setCallDuration(String callDuration) {
		this.callDuration = callDuration;
	}
	public String getCallCnt() {
		return callCnt;
	}
	public void setCallCnt(String callCnt) {
		this.callCnt = callCnt;
	}
	public String getCallDurationNight() {
		return callDurationNight;
	}
	public void setCallDurationNight(String callDurationNight) {
		this.callDurationNight = callDurationNight;
	}
	public String getCallCntNight() {
		return callCntNight;
	}
	public void setCallCntNight(String callCntNight) {
		this.callCntNight = callCntNight;
	}
	public String getCallDurationWorktime() {
		return callDurationWorktime;
	}
	public void setCallDurationWorktime(String callDurationWorktime) {
		this.callDurationWorktime = callDurationWorktime;
	}
	public String getCallCntWorktime() {
		return callCntWorktime;
	}
	public void setCallCntWorktime(String callCntWorktime) {
		this.callCntWorktime = callCntWorktime;
	}
	public String getCallDurationResttime() {
		return callDurationResttime;
	}
	public void setCallDurationResttime(String callDurationResttime) {
		this.callDurationResttime = callDurationResttime;
	}
	public String getCallCntWorkResttime() {
		return callCntWorkResttime;
	}
	public void setCallCntWorkResttime(String callCntWorkResttime) {
		this.callCntWorkResttime = callCntWorkResttime;
	}
	public String getCallDurationWorkday() {
		return callDurationWorkday;
	}
	public void setCallDurationWorkday(String callDurationWorkday) {
		this.callDurationWorkday = callDurationWorkday;
	}
	public String getCallDurationPlayday() {
		return callDurationPlayday;
	}
	public void setCallDurationPlayday(String callDurationPlayday) {
		this.callDurationPlayday = callDurationPlayday;
	}
	@Override
	public String toString() {
		return "MobileReportContractsStatisticsThreeMonth [taskId=" + taskId + ", rankNum=" + rankNum + ", peerNumber="
				+ peerNumber + ", callDuration=" + callDuration + ", callCnt=" + callCnt + ", callDurationNight="
				+ callDurationNight + ", callCntNight=" + callCntNight + ", callDurationWorktime="
				+ callDurationWorktime + ", callCntWorktime=" + callCntWorktime + ", callDurationResttime="
				+ callDurationResttime + ", callCntWorkResttime=" + callCntWorkResttime + ", callDurationWorkday="
				+ callDurationWorkday + ", callDurationPlayday=" + callDurationPlayday + "]";
	}
			
}
