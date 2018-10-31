package com.microservice.dao.entity.crawler.mobile.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="mobile_payment_info",indexes = {@Index(name = "index_mobile_payment_info_taskid", columnList = "taskId")})

/*
 * 手机缴费信息
 */
public class MobilePaymentInfo extends IdEntity implements Serializable {
	
	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String payDate;	//缴费日期
	private String payFee;	//缴费金额
	private String payMode;	//缴费渠道
	private String payWay;	//缴费方式
	private String telephoneNumber; //用户手机号
	private String months;
	@JsonBackReference
	private String resource; //溯源字段
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	public String getPayFee() {
		return payFee;
	}
	public void setPayFee(String payFee) {
		this.payFee = payFee;
	}
	public String getPayMode() {
		return payMode;
	}
	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}
	public String getPayWay() {
		return payWay;
	}
	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}
	public String getTelephoneNumber() {
		return telephoneNumber;
	}
	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}
	public String getMonths() {
		return months;
	}
	public void setMonths(String months) {
		this.months = months;
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
		return "MobilePaymentInfo [taskId=" + taskId + ", payDate=" + payDate + ", payFee=" + payFee + ", payMode="
				+ payMode + ", payWay=" + payWay + ", telephoneNumber=" + telephoneNumber + ", months=" + months
				+ ", resource=" + resource + "]";
	}
	
	
		
}
