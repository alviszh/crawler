package com.microservice.dao.entity.crawler.housing.chifeng;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_chifeng_userinfo",indexes = {@Index(name = "index_housing_chifeng_userinfo_taskid", columnList = "taskid")})
public class HousingFundChiFengUserInfo extends IdEntity{

private String taskid;
	
	private String companyNum;//单位账号
	
	private String personalNum;//职工账号
	
	private String status;//账户状态
	
	private String phone;//移动电话
	
	private String bank;//开户支行
	
	private String openDate;//开户日期
	
	private String company;//单位名称
	
	private String name;//职工姓名
	
	private String cardNum;//证件号码
	
	private String addr;//家庭住址
	
	private String bankNum;//银行卡号

	@Override
	public String toString() {
		return "HousingFundZhuMaDianUserInfo [taskid=" + taskid + ", companyNum=" + companyNum + ", personalNum="
				+ personalNum + ", status=" + status + ", phone=" + phone + ", bank=" + bank + ", openDate=" + openDate
				+ ", company=" + company + ", name=" + name + ", cardNum=" + cardNum + ", addr=" + addr + ", bankNum="
				+ bankNum + "]";
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

	public String getPersonalNum() {
		return personalNum;
	}

	public void setPersonalNum(String personalNum) {
		this.personalNum = personalNum;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getOpenDate() {
		return openDate;
	}

	public void setOpenDate(String openDate) {
		this.openDate = openDate;
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

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getBankNum() {
		return bankNum;
	}

	public void setBankNum(String bankNum) {
		this.bankNum = bankNum;
	}
}
