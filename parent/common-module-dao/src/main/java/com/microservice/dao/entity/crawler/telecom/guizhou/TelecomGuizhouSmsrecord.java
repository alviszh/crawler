package com.microservice.dao.entity.crawler.telecom.guizhou;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_guizhou_smsrecord" ,indexes = {@Index(name = "index_telecom_guizhou_smsrecord_taskid", columnList = "taskid")})
public class TelecomGuizhouSmsrecord extends IdEntity {

	private String cycle;// 账期
	private String name;// 传送方式
	private String calledArea;// 对方区号
	private String calledNum;// 对方号码
	private String startDate; // 通话起始时间
	private String fee; //费用
	private String taskid;
	public String getCycle() {
		return cycle;
	}
	public void setCycle(String cycle) {
		this.cycle = cycle;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCalledArea() {
		return calledArea;
	}
	public void setCalledArea(String calledArea) {
		this.calledArea = calledArea;
	}
	public String getCalledNum() {
		return calledNum;
	}
	public void setCalledNum(String calledNum) {
		this.calledNum = calledNum;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Override
	public String toString() {
		return "TelecomGuizhouSmsrecord [cycle=" + cycle + ", name=" + name + ", calledArea=" + calledArea
				+ ", calledNum=" + calledNum + ", startDate=" + startDate + ", fee=" + fee
				+ ", taskid=" + taskid + "]";
	}
	public TelecomGuizhouSmsrecord(String cycle, String name, String calledArea, String calledNum, String startDate,
			String fee, String taskid) {
		super();
		this.cycle = cycle;
		this.name = name;
		this.calledArea = calledArea;
		this.calledNum = calledNum;
		this.startDate = startDate;
		this.fee = fee;
		this.taskid = taskid;
	}
	public TelecomGuizhouSmsrecord() {
		super();
		// TODO Auto-generated constructor stub
	}
    
}