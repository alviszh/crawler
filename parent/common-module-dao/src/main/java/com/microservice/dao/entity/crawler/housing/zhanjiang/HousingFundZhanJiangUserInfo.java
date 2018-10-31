package com.microservice.dao.entity.crawler.housing.zhanjiang;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_zhanjiang_userinfo",indexes = {@Index(name = "index_housing_zhanjiang_userinfo_taskid", columnList = "taskid")})
public class HousingFundZhanJiangUserInfo extends IdEntity{

	private String name;//姓名
	private String fundNum;//公积金账户
	private String openDate;//开户日期
	private String companyNum;//单位账户
	private String company;//单位名称
	private String status;//公积金状态
	private String cardStatus;//注册证件类型
	private String IDNum;//注册证件号码
	private String base;//缴存基数
	private String companyPay;//单位月缴额
	private String personalPay;//个人月缴额
	private String monthPay;//财政月缴额
	private String birth;//出生日期
	private String sex;//性别
	private String endDate;//缴存至年月
	private String phone;//手机号
	private String addr;//地址
	private String num;//固定号码
	private String taskid;
	@Override
	public String toString() {
		return "HousingFundZhanJiangUserInfo [name=" + name + ", fundNum=" + fundNum + ", openDate=" + openDate
				+ ", companyNum=" + companyNum + ", company=" + company + ", status=" + status + ", cardStatus="
				+ cardStatus + ", IDNum=" + IDNum + ", base=" + base + ", companyPay=" + companyPay + ", personalPay="
				+ personalPay + ", monthPay=" + monthPay + ", birth=" + birth + ", sex=" + sex + ", endDate=" + endDate
				+ ", phone=" + phone + ", addr=" + addr + ", num=" + num + ", taskid=" + taskid + "]";
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFundNum() {
		return fundNum;
	}
	public void setFundNum(String fundNum) {
		this.fundNum = fundNum;
	}
	public String getOpenDate() {
		return openDate;
	}
	public void setOpenDate(String openDate) {
		this.openDate = openDate;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCardStatus() {
		return cardStatus;
	}
	public void setCardStatus(String cardStatus) {
		this.cardStatus = cardStatus;
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
	public String getMonthPay() {
		return monthPay;
	}
	public void setMonthPay(String monthPay) {
		this.monthPay = monthPay;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
}
