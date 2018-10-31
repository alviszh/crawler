package com.microservice.dao.entity.crawler.telecom.henan;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "telecom_henan_calldetail", indexes = {@Index(name = "index_telecom_henan_calldetail_taskid", columnList = "taskid")})
public class TelecomHenanCallDetail extends IdEntity {

	private String taskid;
	private String callNum;						//主叫号码
	private String calledNum;					//被叫号码
	private String startDate;					//开始时间
	private String endDate;						//结束时间
	private String times;						//时长
	private String callType;					//呼叫方式
	private String fee;							//费用
	private String extraFee;					//附加费
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getCallNum() {
		return callNum;
	}
	public void setCallNum(String callNum) {
		this.callNum = callNum;
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
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getTimes() {
		return times;
	}
	public void setTimes(String times) {
		this.times = times;
	}
	public String getCallType() {
		return callType;
	}
	public void setCallType(String callType) {
		this.callType = callType;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getExtraFee() {
		return extraFee;
	}
	public void setExtraFee(String extraFee) {
		this.extraFee = extraFee;
	}
	@Override
	public String toString() {
		return "TelecomHenanCallDetail [taskid=" + taskid + ", callNum=" + callNum + ", calledNum=" + calledNum
				+ ", startDate=" + startDate + ", endDate=" + endDate + ", times=" + times + ", callType=" + callType
				+ ", fee=" + fee + ", extraFee=" + extraFee + "]";
	}
	
	
	
}