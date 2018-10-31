package com.microservice.dao.entity.crawler.housing.yiwu;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_yiwu_basic")
public class HousingYiWuBasic extends IdEntity implements Serializable{

	
	private String taskid;
	private String name;//职工姓名
	private String workNum;//职工账号
	private String num;//身份证号
	private String phoneNum;//手机号
	private String openDate;//开户日期
	private String nowStatus;//当前状态
	private String bankNum;//银行账号
	private String cancelDate;//销户日期
	private String sealedDate;//封存日期
	private String compNum;//单位账号
	private String companyName;//单位名称
	private String lastYearCarryover;//上年结转
	private String nowBalance;//当前余额
	private String monthBase;//月缴基数
	private String person;//个人缴存比例
	private String company;//单位缴存比例
	private String supplement;//补充缴存率
	private String monthPay;//月缴金额
	private String companyPay;//单位月缴额
	private String personPay;//个人月缴额
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getWorkNum() {
		return workNum;
	}
	public void setWorkNum(String workNum) {
		this.workNum = workNum;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getOpenDate() {
		return openDate;
	}
	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}
	public String getNowStatus() {
		return nowStatus;
	}
	public void setNowStatus(String nowStatus) {
		this.nowStatus = nowStatus;
	}
	public String getBankNum() {
		return bankNum;
	}
	public void setBankNum(String bankNum) {
		this.bankNum = bankNum;
	}
	public String getCancelDate() {
		return cancelDate;
	}
	public void setCancelDate(String cancelDate) {
		this.cancelDate = cancelDate;
	}
	public String getSealedDate() {
		return sealedDate;
	}
	public void setSealedDate(String sealedDate) {
		this.sealedDate = sealedDate;
	}
	public String getCompNum() {
		return compNum;
	}
	public void setCompNum(String compNum) {
		this.compNum = compNum;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getLastYearCarryover() {
		return lastYearCarryover;
	}
	public void setLastYearCarryover(String lastYearCarryover) {
		this.lastYearCarryover = lastYearCarryover;
	}
	public String getNowBalance() {
		return nowBalance;
	}
	public void setNowBalance(String nowBalance) {
		this.nowBalance = nowBalance;
	}
	public String getMonthBase() {
		return monthBase;
	}
	public void setMonthBase(String monthBase) {
		this.monthBase = monthBase;
	}
	public String getPerson() {
		return person;
	}
	public void setPerson(String person) {
		this.person = person;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getSupplement() {
		return supplement;
	}
	public void setSupplement(String supplement) {
		this.supplement = supplement;
	}
	public String getMonthPay() {
		return monthPay;
	}
	public void setMonthPay(String monthPay) {
		this.monthPay = monthPay;
	}
	public String getCompanyPay() {
		return companyPay;
	}
	public void setCompanyPay(String companyPay) {
		this.companyPay = companyPay;
	}
	public String getPersonPay() {
		return personPay;
	}
	public void setPersonPay(String personPay) {
		this.personPay = personPay;
	}
	
}
