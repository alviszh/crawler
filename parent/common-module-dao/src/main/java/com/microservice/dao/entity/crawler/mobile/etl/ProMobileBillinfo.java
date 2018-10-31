package com.microservice.dao.entity.crawler.mobile.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="pro_mobile_bill_info",indexes = {@Index(name = "index_pro_mobile_bill_info_taskid", columnList = "taskId")})
public class ProMobileBillinfo extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String resource;
	private String basicFee;
	private String normalCallFee;
	private String roamCallFee;
	private String messageFee;
	private String flowFee;
	private String functionFee;
	private String otherFee;
	private String billMonth;
	private String sumFee;
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	public String getBasicFee() {
		return basicFee;
	}
	public void setBasicFee(String basicFee) {
		this.basicFee = basicFee;
	}
	public String getNormalCallFee() {
		return normalCallFee;
	}
	public void setNormalCallFee(String normalCallFee) {
		this.normalCallFee = normalCallFee;
	}
	public String getRoamCallFee() {
		return roamCallFee;
	}
	public void setRoamCallFee(String roamCallFee) {
		this.roamCallFee = roamCallFee;
	}
	public String getMessageFee() {
		return messageFee;
	}
	public void setMessageFee(String messageFee) {
		this.messageFee = messageFee;
	}
	public String getFlowFee() {
		return flowFee;
	}
	public void setFlowFee(String flowFee) {
		this.flowFee = flowFee;
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
	public String getBillMonth() {
		return billMonth;
	}
	public void setBillMonth(String billMonth) {
		this.billMonth = billMonth;
	}
	public String getSumFee() {
		return sumFee;
	}
	public void setSumFee(String sumFee) {
		this.sumFee = sumFee;
	}
	@Override
	public String toString() {
		return "ProMobileBillinfo [taskId=" + taskId + ", resource=" + resource + ", basicFee=" + basicFee
				+ ", normalCallFee=" + normalCallFee + ", roamCallFee=" + roamCallFee + ", messageFee=" + messageFee
				+ ", flowFee=" + flowFee + ", functionFee=" + functionFee + ", otherFee=" + otherFee + ", billMonth="
				+ billMonth + ", sumFee=" + sumFee + "]";
	}			
}
