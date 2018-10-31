package com.microservice.dao.entity.crawler.mobile.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="pro_mobile_report_consumeinfo",indexes = {@Index(name = "index_pro_mobile_report_consumeinfo_taskid", columnList = "taskId")})
public class ProMobileReportConsumeinfo extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String sumFee;
	private String maxPay;
	private String payCount;
	private String newestBill;
	private String flowFee;
	private String voiceFee;
	private String messageFee;
	private String functionFee;
	private String otherFee;
	private String sumPay;
	private String dataType;
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getSumFee() {
		return sumFee;
	}
	public void setSumFee(String sumFee) {
		this.sumFee = sumFee;
	}
	public String getMaxPay() {
		return maxPay;
	}
	public void setMaxPay(String maxPay) {
		this.maxPay = maxPay;
	}
	public String getPayCount() {
		return payCount;
	}
	public void setPayCount(String payCount) {
		this.payCount = payCount;
	}
	public String getNewestBill() {
		return newestBill;
	}
	public void setNewestBill(String newestBill) {
		this.newestBill = newestBill;
	}
	public String getFlowFee() {
		return flowFee;
	}
	public void setFlowFee(String flowFee) {
		this.flowFee = flowFee;
	}
	public String getVoiceFee() {
		return voiceFee;
	}
	public void setVoiceFee(String voiceFee) {
		this.voiceFee = voiceFee;
	}
	public String getMessageFee() {
		return messageFee;
	}
	public void setMessageFee(String messageFee) {
		this.messageFee = messageFee;
	}
	public String getFunctionFee() {
		return functionFee;
	}
	public void setFunctionFee(String functionFee) {
		this.functionFee = functionFee;
	}
	public String getOtherFee() {
		return otherFee;
	}
	public void setOtherFee(String otherFee) {
		this.otherFee = otherFee;
	}
	public String getSumPay() {
		return sumPay;
	}
	public void setSumPay(String sumPay) {
		this.sumPay = sumPay;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	@Override
	public String toString() {
		return "ProMobileReportConsumeinfo [taskId=" + taskId + ", sumFee=" + sumFee + ", maxPay=" + maxPay
				+ ", payCount=" + payCount + ", newestBill=" + newestBill + ", flowFee=" + flowFee + ", voiceFee="
				+ voiceFee + ", messageFee=" + messageFee + ", functionFee=" + functionFee + ", otherFee=" + otherFee
				+ ", sumPay=" + sumPay + ", dataType=" + dataType + "]";
	}
	
}
