package com.microservice.dao.entity.crawler.insurance.zhaoqing;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_zhaoqing_Medical",indexes = {@Index(name = "index_insurance_zhaoqing_Medical_taskid", columnList = "taskid")})
public class InsuranceZhaoQingMedical extends IdEntity{
	
	private String datea;//费款所属期
	private String company;//单位名称
	
	private String companyPay;//单位划个账
	private String personlPay;//个人化个账
	private String base;//缴费基数
	private String companyMoney;//单位缴费
	private String sum;//合计
	private String jiGou;//机构
	private String taskid;
	@Override
	public String toString() {
		return "InsuranceZhaoQingMedical [datea=" + datea + ", company=" + company + ", companyPay=" + companyPay
				+ ", personlPay=" + personlPay + ", base=" + base + ", companyMoney=" + companyMoney + ", sum=" + sum
				+ ", jiGou=" + jiGou + ", taskid=" + taskid + "]";
	}
	public String getDatea() {
		return datea;
	}
	public void setDatea(String datea) {
		this.datea = datea;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getCompanyPay() {
		return companyPay;
	}
	public void setCompanyPay(String companyPay) {
		this.companyPay = companyPay;
	}
	public String getPersonlPay() {
		return personlPay;
	}
	public void setPersonlPay(String personlPay) {
		this.personlPay = personlPay;
	}
	public String getBase() {
		return base;
	}
	public void setBase(String base) {
		this.base = base;
	}
	public String getCompanyMoney() {
		return companyMoney;
	}
	public void setCompanyMoney(String companyMoney) {
		this.companyMoney = companyMoney;
	}
	public String getSum() {
		return sum;
	}
	public void setSum(String sum) {
		this.sum = sum;
	}
	public String getJiGou() {
		return jiGou;
	}
	public void setJiGou(String jiGou) {
		this.jiGou = jiGou;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
	
	
}
