package com.microservice.dao.entity.crawler.insurance.shaoguan;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_shaoguan_medical",indexes = {@Index(name = "index_insurance_shaoguan_medical_taskid", columnList = "taskid")})
public class InsuranceShaoGuanMedical extends IdEntity{

	private String companyNum;//单位代码
	private String company;//单位名称
	private String datea;//账目年月
	private String sf;//是否到款
	private String base;//缴费工资
	private String companyRatio;//单位缴费比例
	private String companyPay;//单位缴费额
	private String personalRatio;//个人缴费比例
	private String persoanlPay;//个人缴费额
	private String companySend;//单位划拨款
	private String paySum;//缴费总金额
	private String sendDate;//划拨年月
	private String taskid;
	@Override
	public String toString() {
		return "InsuranceShaoGuanEndowment [companyNum=" + companyNum + ", company=" + company + ", datea=" + datea
				+ ", sf=" + sf + ", base=" + base + ", companyRatio=" + companyRatio + ", companyPay=" + companyPay
				+ ", personalRatio=" + personalRatio + ", persoanlPay=" + persoanlPay + ", companySend=" + companySend
				+ ", paySum=" + paySum + ", sendDate=" + sendDate + ", taskid=" + taskid + "]";
	}
	public String getCompanyNum() {
		return companyNum;
	}
	public void setCompanyNum(String companyNum) {
		this.companyNum = companyNum;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getDatea() {
		return datea;
	}
	public void setDatea(String datea) {
		this.datea = datea;
	}
	public String getSf() {
		return sf;
	}
	public void setSf(String sf) {
		this.sf = sf;
	}
	public String getBase() {
		return base;
	}
	public void setBase(String base) {
		this.base = base;
	}
	public String getCompanyRatio() {
		return companyRatio;
	}
	public void setCompanyRatio(String companyRatio) {
		this.companyRatio = companyRatio;
	}
	public String getCompanyPay() {
		return companyPay;
	}
	public void setCompanyPay(String companyPay) {
		this.companyPay = companyPay;
	}
	public String getPersonalRatio() {
		return personalRatio;
	}
	public void setPersonalRatio(String personalRatio) {
		this.personalRatio = personalRatio;
	}
	public String getPersoanlPay() {
		return persoanlPay;
	}
	public void setPersoanlPay(String persoanlPay) {
		this.persoanlPay = persoanlPay;
	}
	public String getCompanySend() {
		return companySend;
	}
	public void setCompanySend(String companySend) {
		this.companySend = companySend;
	}
	public String getPaySum() {
		return paySum;
	}
	public void setPaySum(String paySum) {
		this.paySum = paySum;
	}
	public String getSendDate() {
		return sendDate;
	}
	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
}
