package com.microservice.dao.entity.crawler.housing.bengbu;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_bengbu_userinfo",indexes = {@Index(name = "index_housing_bengbu_userinfo_taskid", columnList = "taskid")})
public class HousingFundBengBuUserInfo extends IdEntity implements Serializable{

	private String companyNum;//单位代码
	private String personalNum;//个人代码
	private String company;//单位名称
	private String name;//个人姓名
	private String bankNum;//单位银行账户
	private String personalBankNum;//个人银行账户
	private String IDNum;//身份证
	private String fee;//余额
	private String startDate;//起缴月份
	private String endDate;//缴至月份
	private String monthPay;//月应缴额
	private String status;//账户状态
	private String companyRadio;//单位缴存比例
	private String personalRadio;//个人缴存比例
	private String bank;//委托银行
	private String taskid;
	@Override
	public String toString() {
		return "HousingFundBengBuUserInfo [companyNum=" + companyNum + ", personalNum=" + personalNum + ", company="
				+ company + ", name=" + name + ", bankNum=" + bankNum + ", personalBankNum=" + personalBankNum
				+ ", IDNum=" + IDNum + ", fee=" + fee + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", monthPay=" + monthPay + ", status=" + status + ", companyRadio=" + companyRadio
				+ ", personalRadio=" + personalRadio + ", bank=" + bank + ", taskid=" + taskid + "]";
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
	public String getBankNum() {
		return bankNum;
	}
	public void setBankNum(String bankNum) {
		this.bankNum = bankNum;
	}
	public String getPersonalBankNum() {
		return personalBankNum;
	}
	public void setPersonalBankNum(String personalBankNum) {
		this.personalBankNum = personalBankNum;
	}
	public String getIDNum() {
		return IDNum;
	}
	public void setIDNum(String iDNum) {
		IDNum = iDNum;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
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
	public String getMonthPay() {
		return monthPay;
	}
	public void setMonthPay(String monthPay) {
		this.monthPay = monthPay;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCompanyRadio() {
		return companyRadio;
	}
	public void setCompanyRadio(String companyRadio) {
		this.companyRadio = companyRadio;
	}
	public String getPersonalRadio() {
		return personalRadio;
	}
	public void setPersonalRadio(String personalRadio) {
		this.personalRadio = personalRadio;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
}
