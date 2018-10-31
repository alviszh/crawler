package com.microservice.dao.entity.crawler.telecom.guizhou;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_guizhou_callrecord" ,indexes = {@Index(name = "index_telecom_guizhou_callrecord_taskid", columnList = "taskid")})
public class TelecomGuizhouCallrecord extends IdEntity {

	private String cycle;// 账期
	private String callType;// 通话类型
	private String calledArea;// 对方区号
	private String calledNum;// 对方号码
	private String startDate; // 通话起始时间
	private String duration; // 通话时长
	private String fee1;// 基本通话费
	private String fee2; // 长途费
	private String fee3; // 漫游费
	private String fee4; // 信息费
	private String fee5; // 总费用

	private String taskid;

	public String getCycle() {
		return cycle;
	}
	public void setCycle(String cycle) {
		this.cycle = cycle;
	}
	public String getCallType() {
		return callType;
	}

	public void setCallType(String callType) {
		this.callType = callType;
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
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getFee1() {
		return fee1;
	}
	public void setFee1(String fee1) {
		this.fee1 = fee1;
	}
	public String getFee2() {
		return fee2;
	}
	public void setFee2(String fee2) {
		this.fee2 = fee2;
	}
	public String getFee3() {
		return fee3;
	}
	public void setFee3(String fee3) {
		this.fee3 = fee3;
	}

	public String getFee4() {
		return fee4;
	}

	public void setFee4(String fee4) {
		this.fee4 = fee4;
	}

	public String getFee5() {
		return fee5;
	}

	public void setFee5(String fee5) {
		this.fee5 = fee5;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Override
	public String toString() {
		return "TelecomGuizhouCallrecord [cycle=" + cycle + ", callType=" + callType + ", calledArea=" + calledArea
				+ ", calledNum=" + calledNum + ", startDate=" + startDate + ", duration=" + duration + ", fee1=" + fee1
				+ ", fee2=" + fee2 + ", fee3=" + fee3 + ", fee4=" + fee4 + ", fee5=" + fee5 + ", taskid=" + taskid
				+ "]";
	}
	public TelecomGuizhouCallrecord(String cycle, String callType, String calledArea, String calledNum,
			String startDate, String duration, String fee1, String fee2, String fee3, String fee4, String fee5,
			String taskid) {
		super();
		this.cycle = cycle;
		this.callType = callType;
		this.calledArea = calledArea;
		this.calledNum = calledNum;
		this.startDate = startDate;
		this.duration = duration;
		this.fee1 = fee1;
		this.fee2 = fee2;
		this.fee3 = fee3;
		this.fee4 = fee4;
		this.fee5 = fee5;
		this.taskid = taskid;
	}
	public TelecomGuizhouCallrecord() {
		super();
		// TODO Auto-generated constructor stub
	}
}