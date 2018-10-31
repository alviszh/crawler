package com.microservice.dao.entity.crawler.housing.foshan;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name  ="housing_foshan_userinfo")
public class HousingFoShanUserinfo  extends IdEntity implements Serializable {
		
	private String taskid;
	private String approvalNumber;								//审批单号
	private String name;										//姓名
	private String idNum;										//证件号码
	private String companyName;									//开设单位
	private String personalNum;									//个人明细编号
	private String setApprovalDate;								//开设审批日期
	private String realSetDate;									//实际开设日期
	private String personalStatus;								//个人明细状态
	private String balance;										//公积金余额
	private String payBase;										//计缴公积金工资基数
	private String depositAmount;								//缴存额
	private String companyPayPercent;							//单位缴存比例
	private String personalPayPercent;							//个人缴存比例
	private String isSubscribeSMS;								//订阅短信服务
	private String subscribeTel;								//订阅服务手机号码
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getApprovalNumber() {
		return approvalNumber;
	}
	public void setApprovalNumber(String approvalNumber) {
		approvalNumber = approvalNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdNum() {
		return idNum;
	}
	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getPersonalNum() {
		return personalNum;
	}
	public void setPersonalNum(String personalNum) {
		this.personalNum = personalNum;
	}
	public String getSetApprovalDate() {
		return setApprovalDate;
	}
	public void setSetApprovalDate(String setApprovalDate) {
		this.setApprovalDate = setApprovalDate;
	}
	public String getRealSetDate() {
		return realSetDate;
	}
	public void setRealSetDate(String realSetDate) {
		this.realSetDate = realSetDate;
	}
	public String getPersonalStatus() {
		return personalStatus;
	}
	public void setPersonalStatus(String personalStatus) {
		this.personalStatus = personalStatus;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getPayBase() {
		return payBase;
	}
	public void setPayBase(String payBase) {
		this.payBase = payBase;
	}
	public String getDepositAmount() {
		return depositAmount;
	}
	public void setDepositAmount(String depositAmount) {
		this.depositAmount = depositAmount;
	}
	public String getCompanyPayPercent() {
		return companyPayPercent;
	}
	public void setCompanyPayPercent(String companyPayPercent) {
		this.companyPayPercent = companyPayPercent;
	}
	public String getPersonalPayPercent() {
		return personalPayPercent;
	}
	public void setPersonalPayPercent(String personalPayPercent) {
		this.personalPayPercent = personalPayPercent;
	}
	public String getIsSubscribeSMS() {
		return isSubscribeSMS;
	}
	public void setIsSubscribeSMS(String isSubscribeSMS) {
		this.isSubscribeSMS = isSubscribeSMS;
	}
	public String getSubscribeTel() {
		return subscribeTel;
	}
	public void setSubscribeTel(String subscribeTel) {
		this.subscribeTel = subscribeTel;
	}
	@Override
	public String toString() {
		return "HousingFoShanUserinfo [taskid=" + taskid + ", approvalNumber=" + approvalNumber + ", name=" + name
				+ ", idNum=" + idNum + ", companyName=" + companyName + ", personalNum=" + personalNum
				+ ", setApprovalDate=" + setApprovalDate + ", realSetDate=" + realSetDate + ", personalStatus="
				+ personalStatus + ", balance=" + balance + ", payBase=" + payBase + ", depositAmount=" + depositAmount
				+ ", companyPayPercent=" + companyPayPercent + ", personalPayPercent=" + personalPayPercent
				+ ", isSubscribeSMS=" + isSubscribeSMS + ", subscribeTel=" + subscribeTel + "]";
	}
	
	

}
