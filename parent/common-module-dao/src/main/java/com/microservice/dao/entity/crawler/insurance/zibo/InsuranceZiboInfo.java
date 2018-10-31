package com.microservice.dao.entity.crawler.insurance.zibo;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_zibo_info")
public class InsuranceZiboInfo extends IdEntity{

	private String taskid;							//uuid 前端通过uuid访问状态结果 
	private String insuranceType;					//缴费险种
	private String payDate;							//缴费年月
	private String payBase;							//缴费基数
	private String personalPayBase;					//个人缴费基数
	private String companyPayBase;					//单位缴费基数
	private String companyPay;						//单位缴费额
	private String personalPay;						//个人缴费额
	private String type;							//社保类型
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getInsuranceType() {
		return insuranceType;
	}
	public void setInsuranceType(String insuranceType) {
		this.insuranceType = insuranceType;
	}
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	public String getPayBase() {
		return payBase;
	}
	public void setPayBase(String payBase) {
		this.payBase = payBase;
	}
	public String getPersonalPayBase() {
		return personalPayBase;
	}
	public void setPersonalPayBase(String personalPayBase) {
		this.personalPayBase = personalPayBase;
	}
	public String getCompanyPayBase() {
		return companyPayBase;
	}
	public void setCompanyPayBase(String companyPayBase) {
		this.companyPayBase = companyPayBase;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "InsuranceZiboInfo [taskid=" + taskid + ", insuranceType=" + insuranceType + ", payDate=" + payDate
				+ ", payBase=" + payBase + ", personalPayBase=" + personalPayBase + ", companyPayBase=" + companyPayBase
				+ ", companyPay=" + companyPay + ", personalPay=" + personalPay + ", type=" + type + "]";
	}
	
}
