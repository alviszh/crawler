package com.microservice.dao.entity.crawler.insurance.anshan;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @Des 鞍山社保-养老缴费表
 * @author zh
 *
 */
@Entity
@Table(name="insurance_anshan_pension",indexes = {@Index(name = "index_insurance_anshan_pension_taskid", columnList = "taskid")})
public class InsuranceAnShanPension extends IdEntity{
	
	private String payMonth;					//年度
	private String payCardinality;				//缴费基数
	private String payNumber ;					//实交月数
	private String payDepartment;				//单位缴费
	private String payPerson;					//个人缴费
	private String taskid;
	
	@Override
	public String toString() {
		return "InsuranceAnShanPension [payMonth=" + payMonth + ", payNumber=" + payNumber + ", payCardinality="
				+ payCardinality + ", payDepartment=" + payDepartment + ", payPerson=" + payPerson
				+ ", taskid=" + taskid + "]";
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

	public String getPayNumber() {
		return payNumber;
	}

	public void setPayNumber(String payNumber) {
		this.payNumber = payNumber;
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

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

}
