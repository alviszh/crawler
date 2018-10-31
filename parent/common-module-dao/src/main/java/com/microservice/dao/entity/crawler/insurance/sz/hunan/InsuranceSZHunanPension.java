package com.microservice.dao.entity.crawler.insurance.sz.hunan;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_sz_hunan_pension")
public class InsuranceSZHunanPension extends IdEntity{

	private String payStartDate;									//费款开始月份
	private String payEndDate;										//费款结束月份
	private String payMonthSum;										//缴费月数
	private String organizationName;								//参保单位
	private String paySalary;										//缴费工资
	private String personalPay;										//个人缴费
	private String organizationPay;									//单位缴费
	private String payMonth;
	private String payIdentity;										//缴费身份
	private String status;											//状态
	private String payMoney;										//缴费金额
	private String taskid;
	public String getPayStartDate() {
		return payStartDate;
	}
	public void setPayStartDate(String payStartDate) {
		this.payStartDate = payStartDate;
	}
	public String getPayEndDate() {
		return payEndDate;
	}
	public void setPayEndDate(String payEndDate) {
		this.payEndDate = payEndDate;
	}
	public String getPayMonthSum() {
		return payMonthSum;
	}
	public void setPayMonthSum(String payMonthSum) {
		this.payMonthSum = payMonthSum;
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
	public String getPayIdentity() {
		return payIdentity;
	}
	public void setPayIdentity(String payIdentity) {
		this.payIdentity = payIdentity;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(String payMoney) {
		this.payMoney = payMoney;
	}
	public String getPayMonth() {
		return payMonth;
	}
	public void setPayMonth(String payMonth) {
		this.payMonth = payMonth;
	}
	@Override
	public String toString() {
		return "InsuranceSZHunanPension [payStartDate=" + payStartDate + ", payEndDate=" + payEndDate + ", payMonthSum="
				+ payMonthSum + ", organizationName=" + organizationName + ", paySalary=" + paySalary + ", personalPay="
				+ personalPay + ", organizationPay=" + organizationPay + ", payMonth=" + payMonth + ", payIdentity="
				+ payIdentity + ", status=" + status + ", payMoney=" + payMoney + ", taskid=" + taskid + "]";
	}
	
	
}
