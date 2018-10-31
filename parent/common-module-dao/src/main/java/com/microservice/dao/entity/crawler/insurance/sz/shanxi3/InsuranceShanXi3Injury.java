package com.microservice.dao.entity.crawler.insurance.sz.shanxi3;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_sz_shanxi3_injury")
public class InsuranceShanXi3Injury extends IdEntity{
	
	private String taskid;
	private String year;								//缴费年月
	private String payType;								//缴费类型
	private String paymentBase;							//缴费基数
	private String organizationPayScale;				//单位缴费比例
	private String accountDate;							//到帐日期
	
	@Override
	public String toString() {
		return "InsuranceShanXi3Injury [taskid=" + taskid + ", year=" + year + ", payType=" + payType + ", paymentBase="
				+ paymentBase + ", organizationPayScale=" + organizationPayScale + ", accountDate=" + accountDate + "]";
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getPaymentBase() {
		return paymentBase;
	}
	public void setPaymentBase(String paymentBase) {
		this.paymentBase = paymentBase;
	}
	public String getOrganizationPayScale() {
		return organizationPayScale;
	}
	public void setOrganizationPayScale(String organizationPayScale) {
		this.organizationPayScale = organizationPayScale;
	}
	public String getAccountDate() {
		return accountDate;
	}
	public void setAccountDate(String accountDate) {
		this.accountDate = accountDate;
	}

}
