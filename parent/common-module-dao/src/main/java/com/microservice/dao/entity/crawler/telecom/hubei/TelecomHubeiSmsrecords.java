package com.microservice.dao.entity.crawler.telecom.hubei;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_hubei_smsrecords" ,indexes = {@Index(name = "index_telecom_hubei_smsrecords_taskid", columnList = "taskid")})
public class TelecomHubeiSmsrecords extends IdEntity {

	private String cycle;// 账期
	
	private String calledNum;// 对方号码
	private String sendTime;// 发送时间
	private String feeType;// 费用类型
	private String fee;// 费用
	private String dataNum;// 折算流量
	private String taskid;
	public String getCycle() {
		return cycle;
	}
	public void setCycle(String cycle) {
		this.cycle = cycle;
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public String getCalledNum() {
		return calledNum;
	}
	public void setCalledNum(String calledNum) {
		this.calledNum = calledNum;
	}
	public String getFeeType() {
		return feeType;
	}
	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getDataNum() {
		return dataNum;
	}
	public void setDataNum(String dataNum) {
		this.dataNum = dataNum;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Override
	public String toString() {
		return "TelecomHubeiSmsrecords [cycle=" + cycle + ", sendTime=" + sendTime + ", calledNum=" + calledNum
				+ ", feeType=" + feeType + ", fee=" + fee + ", dataNum=" + dataNum + ", taskid="
				+ taskid + "]";
	}
	public TelecomHubeiSmsrecords(String cycle, String sendTime, String calledNum,  String feeType,
			String fee, String dataNum, String taskid) {
		super();
		this.cycle = cycle;
		this.sendTime = sendTime;
		this.calledNum = calledNum;
		this.feeType = feeType;
		this.fee = fee;
		this.dataNum = dataNum;
		this.taskid = taskid;
	}
	public TelecomHubeiSmsrecords() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}