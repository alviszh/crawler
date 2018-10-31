package com.microservice.dao.entity.crawler.insurance.foshan;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_foshan_info")
public class InsuranceFoshanInfo extends IdEntity{

	private String payStartEndTime;								//缴费起止时间
	private String organizationName;							//单位名称
	private String insuranceType;								//参保险种
	private String payBase;										//缴费基数
	private String personalPay;									//个人缴费(每月)(元)
	private String companyPay;									//单位缴费(每月)(元)
	private String monthPaySum;									//合计(每月)(元)
	private String payMonth;									//缴费月数
	private String personalPaySum;								//个人缴费总额（元）
	private String companyPaySum;								//单位缴费总额（元）
	private String total;										//合计
	private String personalPensionAccount;						//养老个人账户(元) //只有养老保险有该字段
	private String Type;										//社保类型，因五险数据内容一致，用该字段区分社保类型
	private String taskid;
	public String getPayStartEndTime() {
		return payStartEndTime;
	}
	public void setPayStartEndTime(String payStartEndTime) {
		this.payStartEndTime = payStartEndTime;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
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
	public String getPersonalPay() {
		return personalPay;
	}
	public void setPersonalPay(String personalPay) {
		this.personalPay = personalPay;
	}
	public String getCompanyPay() {
		return companyPay;
	}
	public void setCompanyPay(String companyPay) {
		this.companyPay = companyPay;
	}
	public String getMonthPaySum() {
		return monthPaySum;
	}
	public void setMonthPaySum(String monthPaySum) {
		this.monthPaySum = monthPaySum;
	}
	public String getPayMonth() {
		return payMonth;
	}
	public void setPayMonth(String payMonth) {
		this.payMonth = payMonth;
	}
	public String getPersonalPaySum() {
		return personalPaySum;
	}
	public void setPersonalPaySum(String personalPaySum) {
		this.personalPaySum = personalPaySum;
	}
	public String getCompanyPaySum() {
		return companyPaySum;
	}
	public void setCompanyPaySum(String companyPaySum) {
		this.companyPaySum = companyPaySum;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getPersonalPensionAccount() {
		return personalPensionAccount;
	}
	public void setPersonalPensionAccount(String personalPensionAccount) {
		this.personalPensionAccount = personalPensionAccount;
	}
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Override
	public String toString() {
		return "InsuranceFoshanInfo [payStartEndTime=" + payStartEndTime + ", organizationName=" + organizationName
				+ ", insuranceType=" + insuranceType + ", payBase=" + payBase + ", personalPay=" + personalPay
				+ ", companyPay=" + companyPay + ", monthPaySum=" + monthPaySum + ", payMonth=" + payMonth
				+ ", personalPaySum=" + personalPaySum + ", companyPaySum=" + companyPaySum + ", total=" + total
				+ ", personalPensionAccount=" + personalPensionAccount + ", Type=" + Type + ", taskid=" + taskid + "]";
	}
	

}
