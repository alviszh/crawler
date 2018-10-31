package com.microservice.dao.entity.crawler.insurance.ningbo;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_ningbo_bear",indexes = {@Index(name = "index_insurance_ningbo_bear_taskid", columnList = "taskid")}) 
public class InsuranceNingboBear extends IdEntity{

	private String taskid;
	private String payDate; // 缴费年月
	private String insuranceBase; // 月缴费基数
	private String personMoney; // 个人缴费金额
	private String getStatus; // 到账情况
	@Override
	public String toString() {
		return "InsuranceNingboBear [taskid=" + taskid + ", payDate=" + payDate + ", insuranceBase=" + insuranceBase
				+ ", personMoney=" + personMoney + ", getStatus=" + getStatus + "]";
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	public String getInsuranceBase() {
		return insuranceBase;
	}
	public void setInsuranceBase(String insuranceBase) {
		this.insuranceBase = insuranceBase;
	}
	public String getPersonMoney() {
		return personMoney;
	}
	public void setPersonMoney(String personMoney) {
		this.personMoney = personMoney;
	}
	public String getGetStatus() {
		return getStatus;
	}
	public void setGetStatus(String getStatus) {
		this.getStatus = getStatus;
	}
}
