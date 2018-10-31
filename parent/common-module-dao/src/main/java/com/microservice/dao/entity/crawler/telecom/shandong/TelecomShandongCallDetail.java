package com.microservice.dao.entity.crawler.telecom.shandong;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "telecom_shandong_calldetail", indexes = {@Index(name = "index_telecom_shandong_calldetail_taskid", columnList = "taskid")})
public class TelecomShandongCallDetail extends IdEntity {

	private String taskid;
	private String type;						//类型
	private String callingNum;					//主叫号码
	private String calledNum;					//被叫号码
	private String startTime;					//开始时间
	private String timeCount;					//时长/秒
	private String fee;							//话费/元
	private String area;						//地点
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCallingNum() {
		return callingNum;
	}
	public void setCallingNum(String callingNum) {
		this.callingNum = callingNum;
	}
	public String getCalledNum() {
		return calledNum;
	}
	public void setCalledNum(String calledNum) {
		this.calledNum = calledNum;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getTimeCount() {
		return timeCount;
	}
	public void setTimeCount(String timeCount) {
		this.timeCount = timeCount;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	@Override
	public String toString() {
		return "TelecomShandongCallDetail [taskid=" + taskid + ", type=" + type + ", callingNum=" + callingNum
				+ ", calledNum=" + calledNum + ", startTime=" + startTime + ", timeCount=" + timeCount + ", fee=" + fee
				+ ", area=" + area + "]";
	}
	
	
}