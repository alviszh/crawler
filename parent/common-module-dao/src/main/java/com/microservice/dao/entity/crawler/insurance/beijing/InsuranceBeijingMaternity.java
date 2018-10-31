package com.microservice.dao.entity.crawler.insurance.beijing;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_beijing_maternity")
public class InsuranceBeijingMaternity extends IdEntity {
	
	private String payMonth;					//缴费年月
	private String payCardinality;				//缴费基数
	private String payDepartment;				//单位缴费
	private String taskid;
	
	@Override
	public String toString() {
		return "InsuranceBeijingMaternity [payMonth=" + payMonth + ", payCardinality=" + payCardinality
				+ ", payDepartment=" + payDepartment + ", taskid=" + taskid + "]";
	}
	public String getPayMonth() {
		return payMonth;
	}
	public void setPayMonth(String payMonth) {
		this.payMonth = payMonth;
	}
	public String getPayCardinality() {
		return payCardinality;
	}
	public void setPayCardinality(String payCardinality) {
		this.payCardinality = payCardinality;
	}
	public String getPayDepartment() {
		return payDepartment;
	}
	public void setPayDepartment(String payDepartment) {
		this.payDepartment = payDepartment;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

}
