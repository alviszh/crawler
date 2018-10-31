package com.microservice.dao.entity.crawler.telecom.hubei.wap;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_hubei_wap_recharges" ,indexes = {@Index(name = "index_telecom_hubei_wap_recharges_taskid", columnList = "taskid")})
public class TelecomHubeiWapRecharges extends IdEntity {

	private String cycle;// 账期
	private String paymentTime;// 缴费时间	
	private String paymentAmount;// 缴纳金额
	private String payChannelId;// 充值渠道及方式
	private String paymentRange;// 使用范围
	private String taskid;	
	public String getCycle() {
		return cycle;
	}
	public void setCycle(String cycle) {
		this.cycle = cycle;
	}
	

	public String getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(String paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	public String getPayChannelId() {
		return payChannelId;
	}
	public void setPayChannelId(String payChannelId) {
		this.payChannelId = payChannelId;
	}
	
	public String getPaymentTime() {
		return paymentTime;
	}
	public void setPaymentTime(String paymentTime) {
		this.paymentTime = paymentTime;
	}
	public String getPaymentRange() {
		return paymentRange;
	}
	public void setPaymentRange(String paymentRange) {
		this.paymentRange = paymentRange;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Override
	public String toString() {
		return "TelecomHubeiWapRecharges [cycle=" + cycle + ", paymentTime=" + paymentTime + ", paymentAmount="
				+ paymentAmount + ", payChannelId=" + payChannelId + ", paymentRange=" + paymentRange + ", taskid="
				+ taskid + "]";
	}
	public TelecomHubeiWapRecharges(String cycle, String paymentTime, String paymentAmount, String payChannelId,
			String paymentRange, String taskid) {
		super();
		this.cycle = cycle;
		this.paymentTime = paymentTime;
		this.paymentAmount = paymentAmount;
		this.payChannelId = payChannelId;
		this.paymentRange = paymentRange;
		this.taskid = taskid;
	}
	public TelecomHubeiWapRecharges() {
		super();
	}
}