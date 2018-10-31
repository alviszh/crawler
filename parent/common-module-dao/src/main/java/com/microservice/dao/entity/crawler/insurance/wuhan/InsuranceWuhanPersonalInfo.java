package com.microservice.dao.entity.crawler.insurance.wuhan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_wuhan_personal")
public class InsuranceWuhanPersonalInfo extends IdEntity{
	
	@Column(name="task_id")
	private String taskid;	//uuid唯一标识
	private String type; //险种类型
	private String personalInsuranceStatus; //个人参保状态
	private String personalFirstTime;//个人首次参保年月
	private String payMonths; //实际缴费月数
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPersonalInsuranceStatus() {
		return personalInsuranceStatus;
	}
	public void setPersonalInsuranceStatus(String personalInsuranceStatus) {
		this.personalInsuranceStatus = personalInsuranceStatus;
	}
	public String getPersonalFirstTime() {
		return personalFirstTime;
	}
	public void setPersonalFirstTime(String personalFirstTime) {
		this.personalFirstTime = personalFirstTime;
	}
	public String getPayMonths() {
		return payMonths;
	}
	public void setPayMonths(String payMonths) {
		this.payMonths = payMonths;
	}
	
	
	
}
