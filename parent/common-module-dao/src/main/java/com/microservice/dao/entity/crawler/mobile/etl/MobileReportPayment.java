package com.microservice.dao.entity.crawler.mobile.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;


@Entity
@Table(name="mobile_report_payment",indexes = {@Index(name = "index_mobile_report_payment_taskid", columnList = "taskId")})

public class MobileReportPayment extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String payDate;
	private String businessName;
	private String payMode;
	private String payWay;
	
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
	public String getBusinessName() {
		return businessName;
	}
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
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
	@Override
	public String toString() {
		return "MobileReportPayment [taskId=" + taskId + ", payDate=" + payDate + ", businessName=" + businessName
				+ ", payMode=" + payMode + ", payWay=" + payWay + "]";
	}
	
	

}
