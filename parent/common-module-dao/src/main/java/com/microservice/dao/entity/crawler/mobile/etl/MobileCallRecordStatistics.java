package com.microservice.dao.entity.crawler.mobile.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.microservice.dao.entity.IdEntity;

@Entity
//@Table(name="mobile_call_record_statistics",indexes = {@Index(name = "index_mobile_call_record_statistics_taskid", columnList = "taskId")})
@Table(name="mobile_call_record_statistics")

/*
 * 手机通话记录统计信息
 */
public class MobileCallRecordStatistics extends IdEntity implements Serializable {
	
	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;			//唯一标识
	private String communicationPeriod;		//通话周期
	private String costSum;		//通话总花费
	private String localCost;	//本地花费
	private String roamingCost;	//漫游花费
	private String longDistanceCost;	//长途费
	private String offMainlandLongDistanceCost;	//非大陆地区长途费
	private String offMainlandRoamingCost;	//非大陆地区漫游费
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
	public String getCostSum() {
		return costSum;
	}
	public void setCostSum(String costSum) {
		this.costSum = costSum;
	}
	public String getLocalCost() {
		return localCost;
	}
	public void setLocalCost(String localCost) {
		this.localCost = localCost;
	}
	public String getRoamingCost() {
		return roamingCost;
	}
	public void setRoamingCost(String roamingCost) {
		this.roamingCost = roamingCost;
	}
	public String getLongDistanceCost() {
		return longDistanceCost;
	}
	public void setLongDistanceCost(String longDistanceCost) {
		this.longDistanceCost = longDistanceCost;
	}
	public String getOffMainlandLongDistanceCost() {
		return offMainlandLongDistanceCost;
	}
	public void setOffMainlandLongDistanceCost(String offMainlandLongDistanceCost) {
		this.offMainlandLongDistanceCost = offMainlandLongDistanceCost;
	}
	public String getOffMainlandRoamingCost() {
		return offMainlandRoamingCost;
	}
	public void setOffMainlandRoamingCost(String offMainlandRoamingCost) {
		this.offMainlandRoamingCost = offMainlandRoamingCost;
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
		return "MobileCallRecordStatistics [taskId=" + taskId + ", communicationPeriod=" + communicationPeriod
				+ ", costSum=" + costSum + ", localCost=" + localCost + ", roamingCost=" + roamingCost
				+ ", longDistanceCost=" + longDistanceCost + ", offMainlandLongDistanceCost="
				+ offMainlandLongDistanceCost + ", offMainlandRoamingCost=" + offMainlandRoamingCost
				+ ", telephoneNumber=" + telephoneNumber + ", resource=" + resource + "]";
	}
	
	
	
}
