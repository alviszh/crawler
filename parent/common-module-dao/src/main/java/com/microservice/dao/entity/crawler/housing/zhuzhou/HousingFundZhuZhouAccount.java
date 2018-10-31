package com.microservice.dao.entity.crawler.housing.zhuzhou;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_zhuzhou_account",indexes = {@Index(name = "index_housing_zhuzhou_account_taskid", columnList = "taskid")})
public class HousingFundZhuZhouAccount extends IdEntity{
	
	private String datea;//交易日期
	private String personalNum;//个人账号
	private String name;//姓名
	private String companyNum;//单位账号
	private String company;//单位名称
	private String money;//发生额
	private String fee;//个人账户余额
	private String desce;//摘要
	private String startDate;//开始日期
	private String endDate;//终止日期
	private String people;//经办人
	private String taskid;
	@Override
	public String toString() {
		return "HousingFundZhuZhouAccount [datea=" + datea + ", personalNum=" + personalNum + ", name=" + name
				+ ", companyNum=" + companyNum + ", company=" + company + ", money=" + money + ", fee=" + fee
				+ ", desce=" + desce + ", startDate=" + startDate + ", endDate=" + endDate + ", people=" + people
				+ ", taskid=" + taskid + "]";
	}
	public String getDatea() {
		return datea;
	}
	public void setDatea(String datea) {
		this.datea = datea;
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
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getDesce() {
		return desce;
	}
	public void setDesce(String desce) {
		this.desce = desce;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getPeople() {
		return people;
	}
	public void setPeople(String people) {
		this.people = people;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
}
