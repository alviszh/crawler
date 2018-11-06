package com.microservice.dao.entity.crawler.mobile.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.microservice.dao.entity.IdEntity;

@Entity
//@Table(name="mobile_bill_info_month",indexes = {@Index(name = "index_mobile_bill_info_month_taskid", columnList = "taskId")})
/*
 * 手机月账单信息
 */
@Table(name="mobile_bill_info_month")
public class MobileBillInfoMonth extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一主键
	private String communicationPeriod;	//账单月份
	private String monthCost;	//当月花费
	private String voiceCost;	//语音通话花费
	private String smsCost;		//短信花费
	private String flowCost;	//流量花费
	private String penalty;		//违约金
	private String telephoneNumber; //用户手机号
	@JsonBackReference
	private String resource; //溯源字段
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getCommunicationPeriod() {
		return communicationPeriod;
	}
	public void setCommunicationPeriod(String communicationPeriod) {
		this.communicationPeriod = communicationPeriod;
	}
	public String getMonthCost() {
		return monthCost;
	}
	public void setMonthCost(String monthCost) {
		this.monthCost = monthCost;
	}
	public String getVoiceCost() {
		return voiceCost;
	}
	public void setVoiceCost(String voiceCost) {
		this.voiceCost = voiceCost;
	}
	public String getSmsCost() {
		return smsCost;
	}
	public void setSmsCost(String smsCost) {
		this.smsCost = smsCost;
	}
	public String getFlowCost() {
		return flowCost;
	}
	public void setFlowCost(String flowCost) {
		this.flowCost = flowCost;
	}
	public String getPenalty() {
		return penalty;
	}
	public void setPenalty(String penalty) {
		this.penalty = penalty;
	}
	public String getTelephoneNumber() {
		return telephoneNumber;
	}
	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}
	
	@Column(columnDefinition="text")
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	@Override
	public String toString() {
		return "MobileBillInfoMonth [taskId=" + taskId + ", communicationPeriod=" + communicationPeriod + ", monthCost="
				+ monthCost + ", voiceCost=" + voiceCost + ", smsCost=" + smsCost + ", flowCost=" + flowCost
				+ ", penalty=" + penalty + ", telephoneNumber=" + telephoneNumber + ", resource=" + resource + "]";
	}
	
	
}
