package com.microservice.dao.entity.crawler.mobile.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="pro_mobile_report_stability",indexes = {@Index(name = "index_pro_mobile_report_stability_taskid", columnList = "taskId")})

public class ProMobileReportStability extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String isFamily;
	private String isFamilyHost;
	private String payContinuityMonth;
	private String isAddrEqualCity;
	private String communicateCountsEqualMonth;
	private String communicateCountsEqualMonths;
	private String dataType;
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getIsFamily() {
		return isFamily;
	}
	public void setIsFamily(String isFamily) {
		this.isFamily = isFamily;
	}
	public String getIsFamilyHost() {
		return isFamilyHost;
	}
	public void setIsFamilyHost(String isFamilyHost) {
		this.isFamilyHost = isFamilyHost;
	}
	public String getPayContinuityMonth() {
		return payContinuityMonth;
	}
	public void setPayContinuityMonth(String payContinuityMonth) {
		this.payContinuityMonth = payContinuityMonth;
	}
	public String getIsAddrEqualCity() {
		return isAddrEqualCity;
	}
	public void setIsAddrEqualCity(String isAddrEqualCity) {
		this.isAddrEqualCity = isAddrEqualCity;
	}
	public String getCommunicateCountsEqualMonth() {
		return communicateCountsEqualMonth;
	}
	public void setCommunicateCountsEqualMonth(String communicateCountsEqualMonth) {
		this.communicateCountsEqualMonth = communicateCountsEqualMonth;
	}
	public String getCommunicateCountsEqualMonths() {
		return communicateCountsEqualMonths;
	}
	public void setCommunicateCountsEqualMonths(String communicateCountsEqualMonths) {
		this.communicateCountsEqualMonths = communicateCountsEqualMonths;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	@Override
	public String toString() {
		return "ProMobileReportStability [taskId=" + taskId + ", isFamily=" + isFamily + ", isFamilyHost="
				+ isFamilyHost + ", payContinuityMonth=" + payContinuityMonth + ", isAddrEqualCity=" + isAddrEqualCity
				+ ", communicateCountsEqualMonth=" + communicateCountsEqualMonth + ", communicateCountsEqualMonths="
				+ communicateCountsEqualMonths + ", dataType=" + dataType + "]";
	}
		
}
