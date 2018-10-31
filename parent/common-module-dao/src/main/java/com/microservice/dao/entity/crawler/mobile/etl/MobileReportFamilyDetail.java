package com.microservice.dao.entity.crawler.mobile.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="mobile_report_familydetail",indexes = {@Index(name = "index_mobile_report_familydetail_taskid", columnList = "taskId")})


public class MobileReportFamilyDetail extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String familyNetworkNum;
	private String trombone;
	private String cornet;
	private String joinDate;
	private String memberType;
	private String expiryDate;
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getFamilyNetworkNum() {
		return familyNetworkNum;
	}
	public void setFamilyNetworkNum(String familyNetworkNum) {
		this.familyNetworkNum = familyNetworkNum;
	}
	public String getTrombone() {
		return trombone;
	}
	public void setTrombone(String trombone) {
		this.trombone = trombone;
	}
	public String getCornet() {
		return cornet;
	}
	public void setCornet(String cornet) {
		this.cornet = cornet;
	}
	public String getJoinDate() {
		return joinDate;
	}
	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}
	public String getMemberType() {
		return memberType;
	}
	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}
	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	@Override
	public String toString() {
		return "MobileReportFamilyDetail [taskId=" + taskId + ", familyNetworkNum=" + familyNetworkNum + ", trombone="
				+ trombone + ", cornet=" + cornet + ", joinDate=" + joinDate + ", memberType=" + memberType
				+ ", expiryDate=" + expiryDate + "]";
	}
	
	
}
