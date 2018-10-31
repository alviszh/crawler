package com.microservice.dao.entity.crawler.insurance.pingdingshan;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name="insurance_pingdingshan_userinfo",indexes = {@Index(name = "index_insurance_pingdingshan_userinfo_taskid", columnList = "taskid")}) 
public class InsurancePingDingShanUserInfo extends IdEntity{

	private String taskid;
	
	private String companyNum;//单位编号
	
	private String company;//单位名称
	
	private String name;//姓名
	
	private String sex;//性别 
	
	private String national;//民族
	
	private String birthday;//出生日期
	
	private String personalNum;//个人编号
	
	private String idNum;//身份证号码 
	
	private String joinDate;//参加工作时间
	
	private String personalDate;//个人缴费时间
	
	private String setDate;//建账户时间 
	
	private String status;//参保状态 
	
	private String month;//账户月数 
	
	private String sum;//账户本息合计
	
	private String companyMoney;//单位欠费本金 
	
	private String personalMoney;//个人欠费本金
	
	private String moneyMonth;//欠费月数
	
	private String moneySum;//欠费本金合计

	@Override
	public String toString() {
		return "InsurancePingDingShanUserInfo [taskid=" + taskid + ", companyNum=" + companyNum + ", company=" + company
				+ ", name=" + name + ", sex=" + sex + ", national=" + national + ", birthday=" + birthday
				+ ", personalNum=" + personalNum + ", idNum=" + idNum + ", joinDate=" + joinDate + ", personalDate="
				+ personalDate + ", setDate=" + setDate + ", status=" + status + ", month=" + month + ", sum=" + sum
				+ ", companyMoney=" + companyMoney + ", personalMoney=" + personalMoney + ", moneyMonth=" + moneyMonth
				+ ", moneySum=" + moneySum + "]";
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

	public String getPersonalNum() {
		return personalNum;
	}

	public void setPersonalNum(String personalNum) {
		this.personalNum = personalNum;
	}

	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}

	public String getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
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

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getSum() {
		return sum;
	}

	public void setSum(String sum) {
		this.sum = sum;
	}

	public String getCompanyMoney() {
		return companyMoney;
	}

	public void setCompanyMoney(String companyMoney) {
		this.companyMoney = companyMoney;
	}

	public String getPersonalMoney() {
		return personalMoney;
	}

	public void setPersonalMoney(String personalMoney) {
		this.personalMoney = personalMoney;
	}

	public String getMoneyMonth() {
		return moneyMonth;
	}

	public void setMoneyMonth(String moneyMonth) {
		this.moneyMonth = moneyMonth;
	}

	public String getMoneySum() {
		return moneySum;
	}

	public void setMoneySum(String moneySum) {
		this.moneySum = moneySum;
	}
	
	
}
