package com.microservice.dao.entity.crawler.mobile.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="mobile_report_sms",indexes = {@Index(name = "index_mobile_report_sms_taskid", columnList = "taskId")})

public class MobileReportSMS extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String peerNum;
	private String smsCntSixMonth;
	private String smsCntThreeMonth;
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getPeerNum() {
		return peerNum;
	}
	public void setPeerNum(String peerNum) {
		this.peerNum = peerNum;
	}
	public String getSmsCntSixMonth() {
		return smsCntSixMonth;
	}
	public void setSmsCntSixMonth(String smsCntSixMonth) {
		this.smsCntSixMonth = smsCntSixMonth;
	}
	public String getSmsCntThreeMonth() {
		return smsCntThreeMonth;
	}
	public void setSmsCntThreeMonth(String smsCntThreeMonth) {
		this.smsCntThreeMonth = smsCntThreeMonth;
	}
	@Override
	public String toString() {
		return "MobileReportSMS [taskId=" + taskId + ", peerNum=" + peerNum + ", smsCntSixMonth=" + smsCntSixMonth
				+ ", smsCntThreeMonth=" + smsCntThreeMonth + "]";
	}
	
	
}
