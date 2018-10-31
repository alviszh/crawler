package com.microservice.dao.entity.crawler.telecom.hubei;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_hubei_callrecords" ,indexes = {@Index(name = "index_telecom_hubei_callrecords_taskid", columnList = "taskid")})
public class TelecomHubeiCallrecords extends IdEntity {

	private String cycle;// 账期
	private String startDate;// 起始时间
	private String calledNum;// 对方号码
	private String duration;// 通话时长
	private String calledType;// 呼叫类型
	private String type;// 漫游类型
	private String dataNum;// 折算流量
	private String calledArea;// 通话地点
	private String callChangeNum;// 呼转号码
	private String feeType;// 费用
	private String feeTotal;// 小计
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

	public String getDataNum() {
		return dataNum;
	}
	public void setDataNum(String dataNum) {
		this.dataNum = dataNum;
	}
	public String getCallChangeNum() {
		return callChangeNum;
	}
	public void setCallChangeNum(String callChangeNum) {
		this.callChangeNum = callChangeNum;
	}
	public String getFeeType() {
		return feeType;
	}
	public void setFeeType(String feeType) {
		this.feeType = feeType;
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
		return "TelecomHubeiCallrecords [cycle=" + cycle + ", startDate=" + startDate + ", calledNum=" + calledNum
				+ ", duration=" + duration + ", calledType=" + calledType + ", type=" + type + ", dataNum=" + dataNum
				+ ", calledArea=" + calledArea + ", callChangeNum=" + callChangeNum + ", feeType=" + feeType
				+ ", feeTotal=" + feeTotal + ", taskid=" + taskid + "]";
	}
	public TelecomHubeiCallrecords(String cycle, String startDate, String calledNum, String duration, String calledType,
			String type, String dataNum, String calledArea, String callChangeNum, String feeType, String feeTotal,
			String taskid) {
		super();
		this.cycle = cycle;
		this.startDate = startDate;
		this.calledNum = calledNum;
		this.duration = duration;
		this.calledType = calledType;
		this.type = type;
		this.dataNum = dataNum;
		this.calledArea = calledArea;
		this.callChangeNum = callChangeNum;
		this.feeType = feeType;
		this.feeTotal = feeTotal;
		this.taskid = taskid;
	}
	public TelecomHubeiCallrecords() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}