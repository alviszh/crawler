package com.microservice.dao.entity.crawler.mobile.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity

@Table(name="mobile_report_call_statistics_three_month",indexes = {@Index(name = "index_mobile_report_call_statistics_three_month_taskid", columnList = "taskId")})


public class MobileReportCallStatisticsThreeMonth extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String interactMth;
	private String interactCnt;
	private String interactTime;
	private String dialTime;
	private String contractsCnt;
	private String mutualnumberCnt;
	private String dialCnt;
	private String dialedCnt;
	private String callFee;
	private String callsNumber;
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getInteractMth() {
		return interactMth;
	}
	public void setInteractMth(String interactMth) {
		this.interactMth = interactMth;
	}
	public String getInteractCnt() {
		return interactCnt;
	}
	public void setInteractCnt(String interactCnt) {
		this.interactCnt = interactCnt;
	}
	public String getInteractTime() {
		return interactTime;
	}
	public void setInteractTime(String interactTime) {
		this.interactTime = interactTime;
	}
	public String getDialTime() {
		return dialTime;
	}
	public void setDialTime(String dialTime) {
		this.dialTime = dialTime;
	}
	public String getContractsCnt() {
		return contractsCnt;
	}
	public void setContractsCnt(String contractsCnt) {
		this.contractsCnt = contractsCnt;
	}
	public String getMutualnumberCnt() {
		return mutualnumberCnt;
	}
	public void setMutualnumberCnt(String mutualnumberCnt) {
		this.mutualnumberCnt = mutualnumberCnt;
	}
	public String getDialCnt() {
		return dialCnt;
	}
	public void setDialCnt(String dialCnt) {
		this.dialCnt = dialCnt;
	}
	public String getDialedCnt() {
		return dialedCnt;
	}
	public void setDialedCnt(String dialedCnt) {
		this.dialedCnt = dialedCnt;
	}
	public String getCallFee() {
		return callFee;
	}
	public void setCallFee(String callFee) {
		this.callFee = callFee;
	}
	public String getCallsNumber() {
		return callsNumber;
	}
	public void setCallsNumber(String callsNumber) {
		this.callsNumber = callsNumber;
	}
	@Override
	public String toString() {
		return "MobileReportCallStatisticsThreeMonth [taskId=" + taskId + ", interactMth=" + interactMth
				+ ", interactCnt=" + interactCnt + ", interactTime=" + interactTime + ", dialTime=" + dialTime
				+ ", contractsCnt=" + contractsCnt + ", mutualnumberCnt=" + mutualnumberCnt + ", dialCnt=" + dialCnt
				+ ", dialedCnt=" + dialedCnt + ", callFee=" + callFee + ", callsNumber=" + callsNumber + "]";
	}
	
	
	
}
