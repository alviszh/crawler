package com.microservice.dao.entity.crawler.insurance.beijing;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @Des 北京社保-失业保险表
 * @author Administrator
 *
 */
@Entity
@Table(name="insurance_beijing_unemployment")
public class InsuranceBeijingUnemployment extends IdEntity{
	
	private String taskid;
	private String payMonth;					//缴费年月
	private String payCardinality;				//缴费基数
	private String payDepartment;				//单位缴费
	private String payPerson;					//个人缴费
	
	@Override
	public String toString() {
		return "InsuranceBeijingUnemployment [taskid=" + taskid + ", payMonth=" + payMonth + ", payCardinality="
				+ payCardinality + ", payDepartment=" + payDepartment + ", payPerson=" + payPerson + "]";
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
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
	public String getPayPerson() {
		return payPerson;
	}
	public void setPayPerson(String payPerson) {
		this.payPerson = payPerson;
	}

}
