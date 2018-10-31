package com.microservice.dao.entity.crawler.insurance.zhanjiang;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name = "insurance_zhanjiang_generalinfo")
public class InsuranceZhanJiangGeneralInfo extends IdEntity implements Serializable{
	private String taskid;
	private String personalPay;					//个人缴费金额
	private String accountMoney;				//划入账户金额
	private String idNum;						//证件号码
	private String payType;						//应缴类型
	private String overallMoney;				//划入统筹金额
	private String userName;					//姓名
	private String payDate;						//缴费年月
	private String checkDate;					//审核年月
	private String insuranceType;				//险种类型
	private String payBase;						//缴费基数
	private String companyPay;					//单位缴费金额
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getPersonalPay() {
		return personalPay;
	}
	public void setPersonalPay(String personalPay) {
		this.personalPay = personalPay;
	}
	public String getAccountMoney() {
		return accountMoney;
	}
	public void setAccountMoney(String accountMoney) {
		this.accountMoney = accountMoney;
	}
	public String getIdNum() {
		return idNum;
	}
	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getOverallMoney() {
		return overallMoney;
	}
	public void setOverallMoney(String overallMoney) {
		this.overallMoney = overallMoney;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	public String getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate;
	}
	public String getInsuranceType() {
		return insuranceType;
	}
	public void setInsuranceType(String insuranceType) {
		this.insuranceType = insuranceType;
	}
	public String getPayBase() {
		return payBase;
	}
	public void setPayBase(String payBase) {
		this.payBase = payBase;
	}
	public String getCompanyPay() {
		return companyPay;
	}
	public void setCompanyPay(String companyPay) {
		this.companyPay = companyPay;
	}
	@Override
	public String toString() {
		return "InsuranceZhanJiangGeneralInfo [taskid=" + taskid + ", personalPay=" + personalPay + ", accountMoney="
				+ accountMoney + ", idNum=" + idNum + ", payType=" + payType + ", overallMoney=" + overallMoney
				+ ", userName=" + userName + ", payDate=" + payDate + ", checkDate=" + checkDate + ", insuranceType="
				+ insuranceType + ", payBase=" + payBase + ", companyPay=" + companyPay + "]";
	}
	
	
	
}