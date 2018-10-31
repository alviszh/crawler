package com.microservice.dao.entity.crawler.housing.shaoguan;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_shaoguan_userinfo",indexes = {@Index(name = "index_housing_shaoguan_userinfo_taskid", columnList = "taskid")})
public class HousingFundShaoGuanUserInfo extends IdEntity{
	private String companyNum;//单位账号
	private String company;//单位
	private String personalNum;//个人账号
	private String name;//姓名
	private String IDNum;//身份证
	private String base;//工作基数
	private String personalRatio;//个人缴存率
	private String companyRatio;//单位缴存率
	private String monthPay;//月缴存额
	private String personalPay;//个人缴存额
	private String companyPay;//单位缴存额
	private String openDate;//开户日期
	private String payRatio;//缴存率
	private String status;//个人状态
	private String fee;//个人余额
	private String lastYear;//上年结转
	private String payYear;//本年缴存
	private String taskid;
	@Override
	public String toString() {
		return "HousingFundShaoGuanUserInfo [companyNum=" + companyNum + ", company=" + company + ", personalNum="
				+ personalNum + ", name=" + name + ", IDNum=" + IDNum + ", base=" + base + ", personalRatio="
				+ personalRatio + ", companyRatio=" + companyRatio + ", monthPay=" + monthPay + ", personalPay="
				+ personalPay + ", companyPay=" + companyPay + ", openDate=" + openDate + ", payRatio=" + payRatio
				+ ", status=" + status + ", fee=" + fee + ", lastYear=" + lastYear + ", payYear=" + payYear
				+ ", taskid=" + taskid + "]";
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
	public String getPersonalNum() {
		return personalNum;
	}
	public void setPersonalNum(String personalNum) {
		this.personalNum = personalNum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIDNum() {
		return IDNum;
	}
	public void setIDNum(String iDNum) {
		IDNum = iDNum;
	}
	public String getBase() {
		return base;
	}
	public void setBase(String base) {
		this.base = base;
	}
	public String getPersonalRatio() {
		return personalRatio;
	}
	public void setPersonalRatio(String personalRatio) {
		this.personalRatio = personalRatio;
	}
	public String getCompanyRatio() {
		return companyRatio;
	}
	public void setCompanyRatio(String companyRatio) {
		this.companyRatio = companyRatio;
	}
	public String getMonthPay() {
		return monthPay;
	}
	public void setMonthPay(String monthPay) {
		this.monthPay = monthPay;
	}
	public String getPersonalPay() {
		return personalPay;
	}
	public void setPersonalPay(String personalPay) {
		this.personalPay = personalPay;
	}
	public String getCompanyPay() {
		return companyPay;
	}
	public void setCompanyPay(String companyPay) {
		this.companyPay = companyPay;
	}
	public String getOpenDate() {
		return openDate;
	}
	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}
	public String getPayRatio() {
		return payRatio;
	}
	public void setPayRatio(String payRatio) {
		this.payRatio = payRatio;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getLastYear() {
		return lastYear;
	}
	public void setLastYear(String lastYear) {
		this.lastYear = lastYear;
	}
	public String getPayYear() {
		return payYear;
	}
	public void setPayYear(String payYear) {
		this.payYear = payYear;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
}
