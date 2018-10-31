package com.microservice.dao.entity.crawler.insurance.nanyang;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_nanyang_userinfo",indexes = {@Index(name = "index_insurance_nanyang_userinfo_taskid", columnList = "taskid")}) 
public class InsuranceNanYangUserInfo extends IdEntity{

private String taskid;
	
	private String companyNum;//单位编号/社会保险登记编号
	
	private String company;//单位名称/社会保障号
	
	private String name;//姓名
	
	private String sex;//性别 
	
	private String cardStatus;//证件类型
	
	private String idNum;//身份证号码 
	
	private String national;//民族
	
	private String birthday;//出生日期
	
	private String joinDate;//参加工作时间
	
	private String personalNum;//个人编号
	
	private String personalDate;//个人缴费时间/参保时间
	
	private String setDate;//建账户时间 
	
	private String status;//参保状态 
	
	private String payStatus;//缴费状态
	
	private String baseMoney;//当月缴费基数

	@Override
	public String toString() {
		return "InsuranceNanYangUserInfo [taskid=" + taskid + ", companyNum=" + companyNum + ", company=" + company
				+ ", name=" + name + ", sex=" + sex + ", cardStatus=" + cardStatus + ", idNum=" + idNum + ", national="
				+ national + ", birthday=" + birthday + ", joinDate=" + joinDate + ", personalNum=" + personalNum
				+ ", personalDate=" + personalDate + ", setDate=" + setDate + ", status=" + status + ", payStatus="
				+ payStatus + ", baseMoney=" + baseMoney + "]";
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
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

	public String getCardStatus() {
		return cardStatus;
	}

	public void setCardStatus(String cardStatus) {
		this.cardStatus = cardStatus;
	}

	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}

	public String getNational() {
		return national;
	}

	public void setNational(String national) {
		this.national = national;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}

	public String getPersonalNum() {
		return personalNum;
	}

	public void setPersonalNum(String personalNum) {
		this.personalNum = personalNum;
	}

	public String getPersonalDate() {
		return personalDate;
	}

	public void setPersonalDate(String personalDate) {
		this.personalDate = personalDate;
	}

	public String getSetDate() {
		return setDate;
	}

	public void setSetDate(String setDate) {
		this.setDate = setDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public String getBaseMoney() {
		return baseMoney;
	}

	public void setBaseMoney(String baseMoney) {
		this.baseMoney = baseMoney;
	}
	
	

	
	
}
