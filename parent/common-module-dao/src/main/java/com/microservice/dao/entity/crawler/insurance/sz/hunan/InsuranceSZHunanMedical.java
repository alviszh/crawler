package com.microservice.dao.entity.crawler.insurance.sz.hunan;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_sz_hunan_medical")
public class InsuranceSZHunanMedical extends IdEntity{

	private String payDate;											//费款所属期
	private String organizationName;								//参保单位
	private String paySalary;										//缴费工资
	private String personalPay;										//个人缴费
	private String organizationPay;									//单位缴费
	private String taskid;
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	public String getPaySalary() {
		return paySalary;
	}
	public void setPaySalary(String paySalary) {
		this.paySalary = paySalary;
	}
	public String getPersonalPay() {
		return personalPay;
	}
	public void setPersonalPay(String personalPay) {
		this.personalPay = personalPay;
	}
	public String getOrganizationPay() {
		return organizationPay;
	}
	public void setOrganizationPay(String organizationPay) {
		this.organizationPay = organizationPay;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Override
	public String toString() {
		return "InsuranceSZHunanInfo [payDate=" + payDate + ", organizationName=" + organizationName + ", paySalary="
				+ paySalary + ", personalPay=" + personalPay + ", organizationPay=" + organizationPay + ", taskid="
				+ taskid + "]";
	}
	
	

}
