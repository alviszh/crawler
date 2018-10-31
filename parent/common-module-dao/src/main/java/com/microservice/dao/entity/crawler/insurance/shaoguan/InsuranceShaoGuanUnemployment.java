package com.microservice.dao.entity.crawler.insurance.shaoguan;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_shaoguan_unemployment",indexes = {@Index(name = "index_insurance_shaoguan_unemployment_taskid", columnList = "taskid")})
public class InsuranceShaoGuanUnemployment extends IdEntity{

	private String companyNum;//单位编号
	private String month;//征收月份
	private String paySum;//缴费自从金额
	private String companyPay;//缴费总金额单位
	private String personalPay;//缴费总金额个人
	private String endowmentSum;//失业总金额
	private String endowmentCompany;//失业单位
	private String endowmentPersonal;//失业个人
	private String taskid;
	@Override
	public String toString() {
		return "InsuranceShaoGuanEndowment [companyNum=" + companyNum + ", month=" + month + ", paySum=" + paySum
				+ ", companyPay=" + companyPay + ", personalPay=" + personalPay + ", endowmentSum=" + endowmentSum
				+ ", endowmentCompany=" + endowmentCompany + ", endowmentPersonal=" + endowmentPersonal + ", taskid="
				+ taskid + "]";
	}
	public String getCompanyNum() {
		return companyNum;
	}
	public void setCompanyNum(String companyNum) {
		this.companyNum = companyNum;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getPaySum() {
		return paySum;
	}
	public void setPaySum(String paySum) {
		this.paySum = paySum;
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
	public String getEndowmentSum() {
		return endowmentSum;
	}
	public void setEndowmentSum(String endowmentSum) {
		this.endowmentSum = endowmentSum;
	}
	public String getEndowmentCompany() {
		return endowmentCompany;
	}
	public void setEndowmentCompany(String endowmentCompany) {
		this.endowmentCompany = endowmentCompany;
	}
	public String getEndowmentPersonal() {
		return endowmentPersonal;
	}
	public void setEndowmentPersonal(String endowmentPersonal) {
		this.endowmentPersonal = endowmentPersonal;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
}
