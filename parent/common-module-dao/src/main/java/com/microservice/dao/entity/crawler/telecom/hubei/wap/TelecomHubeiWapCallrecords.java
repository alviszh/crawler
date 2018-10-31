package com.microservice.dao.entity.crawler.telecom.hubei.wap;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_hubei_wap_callrecords" ,indexes = {@Index(name = "index_telecom_hubei_wap_callrecords_taskid", columnList = "taskid")})
public class TelecomHubeiWapCallrecords extends IdEntity {

	private String cycle;// 账期
	private String startDate;// 起始时间
	private String calledArea;// 通话地点
	private String calledType;// 呼叫类型
	private String calledNum;//对方号码
	private String duration;// 通话时长
	private String type;// 漫游类型
	private String feeTotal;// 费用
	private String taskid;
	public String getCycle() {
		return cycle;
	}
	public void setCycle(String cycle) {
		this.cycle = cycle;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getCalledArea() {
		return calledArea;
	}
	public void setCalledArea(String calledArea) {
		this.calledArea = calledArea;
	}
	public String getCalledType() {
		return calledType;
	}
	public void setCalledType(String calledType) {
		this.calledType = calledType;
	}
	public String getCalledNum() {
		return calledNum;
	}
	public void setCalledNum(String calledNum) {
		this.calledNum = calledNum;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getFeeTotal() {
		return feeTotal;
	}
	public void setFeeTotal(String feeTotal) {
		this.feeTotal = feeTotal;
	}
	
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
	@Override
	public String toString() {
		return "TelecomHubeiWapCallrecords [cycle=" + cycle + ", startDate=" + startDate + ", calledArea=" + calledArea
				+ ", calledType=" + calledType + ", calledNum=" + calledNum + ", duration=" + duration + ", type="
				+ type + ", feeTotal=" + feeTotal + ", taskid=" + taskid + "]";
	}
	
	public TelecomHubeiWapCallrecords(String cycle, String startDate, String calledArea, String calledType,
			String calledNum, String duration, String type, String feeTotal, String taskid) {
		super();
		this.cycle = cycle;
		this.startDate = startDate;
		this.calledArea = calledArea;
		this.calledType = calledType;
		this.calledNum = calledNum;
		this.duration = duration;
		this.type = type;
		this.feeTotal = feeTotal;
		this.taskid = taskid;
	}
	public TelecomHubeiWapCallrecords() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}