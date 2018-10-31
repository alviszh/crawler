package com.microservice.dao.entity.crawler.insurance.haikou;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_haikou_unemployment",indexes = {@Index(name = "index_insurance_haikou_unemployment_taskid", columnList = "taskid")})
public class InsuranceHaiKouUnemployment extends IdEntity{
	private String company;//单位名称
	private String status;//险种类型
	private String datea;//费款所属期
	private String base;//缴费基数
	private String companyPay;//单位缴费金额
	private String personalPay;//个人缴费金额
	private String otherPay;//其他缴费金额
	private String sum;//合计
	private String taskid;
	@Override
	public String toString() {
		return "InsuranceHaiKouUnemployment [company=" + company + ", status=" + status + ", datea=" + datea + ", base="
				+ base + ", companyPay=" + companyPay + ", personalPay=" + personalPay + ", otherPay=" + otherPay
				+ ", sum=" + sum + ", taskid=" + taskid + "]";
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDatea() {
		return datea;
	}
	public void setDatea(String datea) {
		this.datea = datea;
	}
	public String getBase() {
		return base;
	}
	public void setBase(String base) {
		this.base = base;
	}
	public String getCompanyPay() {
		return companyPay;
	}
	public void setCompanyPay(String companyPay) {
		this.companyPay = companyPay;
	}
	public String getPersonalPay() {
		return personalPay;
	}
	public void setPersonalPay(String personalPay) {
		this.personalPay = personalPay;
	}
	public String getOtherPay() {
		return otherPay;
	}
	public void setOtherPay(String otherPay) {
		this.otherPay = otherPay;
	}
	public String getSum() {
		return sum;
	}
	public void setSum(String sum) {
		this.sum = sum;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
}
