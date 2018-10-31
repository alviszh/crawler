package com.microservice.dao.entity.crawler.mobile.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;


@Entity
@Table(name="mobile_report_location_statistics",indexes = {@Index(name = "index_mobile_report_location_statistics_taskid", columnList = "taskId")})


public class MobileReportLocationStatistics extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String tradeAddr;
	private String callCnt;
	private String callDuration;
	private String interactDuration;
	private String interactCnt;
	private String callCntThreeMonth;
	private String callCntSixMonth;
	private String continuityCntThreeMonth;
	private String continuityCntSixMonth;
	private String lastCallTime;
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getTradeAddr() {
		return tradeAddr;
	}
	public void setTradeAddr(String tradeAddr) {
		this.tradeAddr = tradeAddr;
	}
	public String getCallCnt() {
		return callCnt;
	}
	public void setCallCnt(String callCnt) {
		this.callCnt = callCnt;
	}
	public String getCallDuration() {
		return callDuration;
	}
	public void setCallDuration(String callDuration) {
		this.callDuration = callDuration;
	}
	public String getInteractDuration() {
		return interactDuration;
	}
	public void setInteractDuration(String interactDuration) {
		this.interactDuration = interactDuration;
	}
	public String getInteractCnt() {
		return interactCnt;
	}
	public void setInteractCnt(String interactCnt) {
		this.interactCnt = interactCnt;
	}
	public String getCallCntThreeMonth() {
		return callCntThreeMonth;
	}
	public void setCallCntThreeMonth(String callCntThreeMonth) {
		this.callCntThreeMonth = callCntThreeMonth;
	}
	public String getCallCntSixMonth() {
		return callCntSixMonth;
	}
	public void setCallCntSixMonth(String callCntSixMonth) {
		this.callCntSixMonth = callCntSixMonth;
	}
	public String getContinuityCntThreeMonth() {
		return continuityCntThreeMonth;
	}
	public void setContinuityCntThreeMonth(String continuityCntThreeMonth) {
		this.continuityCntThreeMonth = continuityCntThreeMonth;
	}
	public String getContinuityCntSixMonth() {
		return continuityCntSixMonth;
	}
	public void setContinuityCntSixMonth(String continuityCntSixMonth) {
		this.continuityCntSixMonth = continuityCntSixMonth;
	}
	public String getLastCallTime() {
		return lastCallTime;
	}
	public void setLastCallTime(String lastCallTime) {
		this.lastCallTime = lastCallTime;
	}
	@Override
	public String toString() {
		return "MobileReportLocationStatistics [taskId=" + taskId + ", tradeAddr=" + tradeAddr + ", callCnt=" + callCnt
				+ ", callDuration=" + callDuration + ", interactDuration=" + interactDuration + ", interactCnt="
				+ interactCnt + ", callCntThreeMonth=" + callCntThreeMonth + ", callCntSixMonth=" + callCntSixMonth
				+ ", continuityCntThreeMonth=" + continuityCntThreeMonth + ", continuityCntSixMonth="
				+ continuityCntSixMonth + ", lastCallTime=" + lastCallTime + "]";
	}
	
	
		
}
