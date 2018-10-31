package com.microservice.dao.entity.crawler.telecom.hubei;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_hubei_recharges" ,indexes = {@Index(name = "index_telecom_hubei_recharges_taskid", columnList = "taskid")})
public class TelecomHubeiRecharges extends IdEntity {

	private String cycle;// 账期
	private String reqSerial;// 流水号
	private String stateDate;// 入账时间	
	private String paymentAmount;// 入账金额
	private String payChannelId;// 缴费渠道
	private String paymentMethod;// 缴费方式	
	private String accNbrDetail;// 使用范围	
	private String taskid;
	

	
	public String getCycle() {
		return cycle;
	}
	public void setCycle(String cycle) {
		this.cycle = cycle;
	}
	
	public String getReqSerial() {
		return reqSerial;
	}
	public void setReqSerial(String reqSerial) {
		this.reqSerial = reqSerial;
	}
	public String getStateDate() {
		return stateDate;
	}
	public void setStateDate(String stateDate) {
		this.stateDate = stateDate;
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
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public String getAccNbrDetail() {
		return accNbrDetail;
	}
	public void setAccNbrDetail(String accNbrDetail) {
		this.accNbrDetail = accNbrDetail;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	@Override
	public String toString() {
		return "TelecomHubeiRecharges [cycle=" + cycle + ", reqSerial=" + reqSerial + ", stateDate=" + stateDate
				+ ", paymentAmount=" + paymentAmount + ", payChannelId=" + payChannelId + ", paymentMethod="
				+ paymentMethod + ", accNbrDetail=" + accNbrDetail + ", taskid=" + taskid + "]";
	}
	
	public TelecomHubeiRecharges(String cycle, String reqSerial, String stateDate, String paymentAmount,
			String payChannelId, String paymentMethod, String accNbrDetail, String taskid) {
		super();
		this.cycle = cycle;
		this.reqSerial = reqSerial;
		this.stateDate = stateDate;
		this.paymentAmount = paymentAmount;
		this.payChannelId = payChannelId;
		this.paymentMethod = paymentMethod;
		this.accNbrDetail = accNbrDetail;
		this.taskid = taskid;
	}
	public TelecomHubeiRecharges() {
		super();
		// TODO Auto-generated constructor stub
	}
}