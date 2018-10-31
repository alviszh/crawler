package com.microservice.dao.entity.crawler.telecom.hubei;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_hubei_account" ,indexes = {@Index(name = "index_telecom_hubei_account_taskid", columnList = "taskid")})
public class TelecomHubeiAccount extends IdEntity {

	private String availableAmount;// 可用余额
	private String currentAvailableAmount;// 用户当期可用余额合计
	private String dueTotalAmount;// 账户应缴合计
	private String realtimeAmount;// 实时费用
	private String taskid;
	public String getAvailableAmount() {
		return availableAmount;
	}
	public void setAvailableAmount(String availableAmount) {
		this.availableAmount = availableAmount;
	}
	public String getCurrentAvailableAmount() {
		return currentAvailableAmount;
	}
	public void setCurrentAvailableAmount(String currentAvailableAmount) {
		this.currentAvailableAmount = currentAvailableAmount;
	}
	public String getDueTotalAmount() {
		return dueTotalAmount;
	}
	public void setDueTotalAmount(String dueTotalAmount) {
		this.dueTotalAmount = dueTotalAmount;
	}
	public String getRealtimeAmount() {
		return realtimeAmount;
	}
	public void setRealtimeAmount(String realtimeAmount) {
		this.realtimeAmount = realtimeAmount;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Override
	public String toString() {
		return "TelecomHubeiAccount [availableAmount=" + availableAmount + ", currentAvailableAmount="
				+ currentAvailableAmount + ", dueTotalAmount=" + dueTotalAmount + ", realtimeAmount=" + realtimeAmount
				+ ", taskid=" + taskid + "]";
	}
}