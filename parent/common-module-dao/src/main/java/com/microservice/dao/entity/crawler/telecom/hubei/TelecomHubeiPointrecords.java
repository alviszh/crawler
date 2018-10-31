package com.microservice.dao.entity.crawler.telecom.hubei;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_hubei_pointrecords" ,indexes = {@Index(name = "index_telecom_hubei_pointrecords_taskid", columnList = "taskid")})
public class TelecomHubeiPointrecords extends IdEntity {

	private String availableAmount;// 账期
	private String pointValue;// 积分值
	private String startTime;// 时间
	private String remark;// 备注

	private String taskid;
	public String getAvailableAmount() {
		return availableAmount;
	}
	
	public String getPointValue() {
		return pointValue;
	}

	public void setPointValue(String pointValue) {
		this.pointValue = pointValue;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setAvailableAmount(String availableAmount) {
		this.availableAmount = availableAmount;
	}

	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	@Override
	public String toString() {
		return "TelecomHubeiPointrecords [availableAmount=" + availableAmount + ", pointValue=" + pointValue
				+ ", startTime=" + startTime + ", remark=" + remark + ", taskid=" + taskid + "]";
	}

	public TelecomHubeiPointrecords(String availableAmount, String pointValue, String startTime, String remark,
			String taskid) {
		super();
		this.availableAmount = availableAmount;
		this.pointValue = pointValue;
		this.startTime = startTime;
		this.remark = remark;
		this.taskid = taskid;
	}

	public TelecomHubeiPointrecords() {
		super();
		// TODO Auto-generated constructor stub
	}
}