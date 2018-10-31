package com.microservice.dao.entity.crawler.e_commerce.etl.jd;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="authinfo_jd") //交易明细
public class AuthInfoJD extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = -3207075026659390860L;

	@Column(name="task_id")
	private String taskId; //唯一标识
	private String realNme;
	private String idNumber;
	private String authTime;
	private String bandingPhonenumber;
	private String authChannel;
	private String financeService;
	
	@JsonBackReference
	private String resource; //溯源字段
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getRealNme() {
		return realNme;
	}
	public void setRealNme(String realNme) {
		this.realNme = realNme;
	}
	public String getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	public String getAuthTime() {
		return authTime;
	}
	public void setAuthTime(String authTime) {
		this.authTime = authTime;
	}
	public String getBandingPhonenumber() {
		return bandingPhonenumber;
	}
	public void setBandingPhonenumber(String bandingPhonenumber) {
		this.bandingPhonenumber = bandingPhonenumber;
	}
	public String getAuthChannel() {
		return authChannel;
	}
	public void setAuthChannel(String authChannel) {
		this.authChannel = authChannel;
	}
	public String getFinanceService() {
		return financeService;
	}
	public void setFinanceService(String financeService) {
		this.financeService = financeService;
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
		return "AuthInfoJD [taskId=" + taskId + ", realNme=" + realNme + ", idNumber=" + idNumber + ", authTime="
				+ authTime + ", bandingPhonenumber=" + bandingPhonenumber + ", authChannel=" + authChannel
				+ ", financeService=" + financeService + ", resource=" + resource + "]";
	}
	
	
	
	
}
