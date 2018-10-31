package com.microservice.dao.entity.crawler.telecom.guizhou;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_guizhou_recharges" ,indexes = {@Index(name = "index_telecom_guizhou_recharges_taskid", columnList = "taskid")})
public class TelecomGuizhouRecharges extends IdEntity {

	private String paymentTime;// 入账时间
	private String paymentAmount;// 入账金额
	private String paymentChannel;// 缴费渠道
	private String paymentType;// 交费方式
	private String userRange;// 使用范围
	private String taskid;
	public String getPaymentTime() {
		return paymentTime;
	}

	public void setPaymentTime(String paymentTime) {
		this.paymentTime = paymentTime;
	}

	public String getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(String paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public String getPaymentChannel() {
		return paymentChannel;
	}

	public void setPaymentChannel(String paymentChannel) {
		this.paymentChannel = paymentChannel;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getUserRange() {
		return userRange;
	}

	public void setUserRange(String userRange) {
		this.userRange = userRange;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public TelecomGuizhouRecharges(String paymentTime, String paymentAmount, String paymentChannel, String paymentType,
			String userRange, String taskid) {
		super();
		this.paymentTime = paymentTime;
		this.paymentAmount = paymentAmount;
		this.paymentChannel = paymentChannel;
		this.paymentType = paymentType;
		this.userRange = userRange;
		this.taskid = taskid;
	}
	public TelecomGuizhouRecharges() {
		super();
		// TODO Auto-generated constructor stub
	}
}