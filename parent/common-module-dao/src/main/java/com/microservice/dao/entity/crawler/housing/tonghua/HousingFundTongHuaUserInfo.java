package com.microservice.dao.entity.crawler.housing.tonghua;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_tonghua_userinfo",indexes = {@Index(name = "index_housing_tonghua_userinfo_taskid", columnList = "taskid")})
public class HousingFundTongHuaUserInfo extends IdEntity implements Serializable{

	private String comNum;//单位账号
	
	private String company;//单位名称
	
	private String personalNum;//个人账号 
	
	private String name;//姓名 
	
	private String sex;//性别
	
	private String idCard;//身份证号 
	
	private String birthday;//出生年月
	
	private String department;//部门 
	
	private String personalRatio;//个人缴存率(%)
	
	private String unitRatio;//单位缴存率(%) 
	
	private String govemmentRatio;//财政缴存率(%)
	
	private String personalPay;//个人缴存额(元) 
	
	private String unitPay;//单位缴存额(元) 
	
	private String govementPay;//财政缴存额(元) 
	
	private String monthPay;//月缴存额(元) 
	
	private String base;//工资基数(元) 
	
	private String personalStatus;//个人状态 
	
	private String personalFee;//个人余额(元)
	
	private String openDate;//开户日期
	
	private String taskid;

	@Override
	public String toString() {
		return "HousingFundTongHuaUserInfo [comNum=" + comNum + ", company=" + company + ", personalNum=" + personalNum
				+ ", name=" + name + ", sex=" + sex + ", idCard=" + idCard + ", birthday=" + birthday + ", department="
				+ department + ", personalRatio=" + personalRatio + ", unitRatio=" + unitRatio + ", govemmentRatio="
				+ govemmentRatio + ", personalPay=" + personalPay + ", unitPay=" + unitPay + ", govementPay="
				+ govementPay + ", monthPay=" + monthPay + ", base=" + base + ", personalStatus=" + personalStatus
				+ ", personalFee=" + personalFee + ", openDate=" + openDate + ", taskid=" + taskid + "]";
	}

	public String getComNum() {
		return comNum;
	}

	public void setComNum(String comNum) {
		this.comNum = comNum;
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

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getPersonalRatio() {
		return personalRatio;
	}

	public void setPersonalRatio(String personalRatio) {
		this.personalRatio = personalRatio;
	}

	public String getUnitRatio() {
		return unitRatio;
	}

	public void setUnitRatio(String unitRatio) {
		this.unitRatio = unitRatio;
	}

	public String getGovemmentRatio() {
		return govemmentRatio;
	}

	public void setGovemmentRatio(String govemmentRatio) {
		this.govemmentRatio = govemmentRatio;
	}

	public String getPersonalPay() {
		return personalPay;
	}

	public void setPersonalPay(String personalPay) {
		this.personalPay = personalPay;
	}

	public String getUnitPay() {
		return unitPay;
	}

	public void setUnitPay(String unitPay) {
		this.unitPay = unitPay;
	}

	public String getGovementPay() {
		return govementPay;
	}

	public void setGovementPay(String govementPay) {
		this.govementPay = govementPay;
	}

	public String getMonthPay() {
		return monthPay;
	}

	public void setMonthPay(String monthPay) {
		this.monthPay = monthPay;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getPersonalStatus() {
		return personalStatus;
	}

	public void setPersonalStatus(String personalStatus) {
		this.personalStatus = personalStatus;
	}

	public String getPersonalFee() {
		return personalFee;
	}

	public void setPersonalFee(String personalFee) {
		this.personalFee = personalFee;
	}

	public String getOpenDate() {
		return openDate;
	}

	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
	
}
