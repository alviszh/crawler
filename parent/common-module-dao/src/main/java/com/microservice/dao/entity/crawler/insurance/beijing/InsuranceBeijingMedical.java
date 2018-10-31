package com.microservice.dao.entity.crawler.insurance.beijing;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_beijing_medical")
public class InsuranceBeijingMedical extends IdEntity {
	
	private String payMonth;					//缴费年月
	private String payType;						//缴费类型
	private String payCardinality;				//缴费基数
	private String payDepartment;				//单位缴费
	private String payPerson;					//个人缴费
	private String payDepartmentName;			//缴费单位名称
	private String taskid;
	
	@Override
	public String toString() {
		return "InsuranceBeijingMedical [payMonth=" + payMonth + ", payType=" + payType + ", payCardinality="
				+ payCardinality + ", payDepartment=" + payDepartment + ", payPerson=" + payPerson
				+ ", payDepartmentName=" + payDepartmentName + ", taskid=" + taskid + "]";
	}
	public String getPayMonth() {
		return payMonth;
	}
	public void setPayMonth(String payMonth) {
		this.payMonth = payMonth;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
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
	public String getPayDepartmentName() {
		return payDepartmentName;
	}
	public void setPayDepartmentName(String payDepartmentName) {
		this.payDepartmentName = payDepartmentName;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	


}
