package com.microservice.dao.entity.crawler.insurance.huaian;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_huaian_endowment",indexes = {@Index(name = "index_insurance_huaian_endowment_taskid", columnList = "taskid")}) 
public class InsuranceHuaiAnEndowment extends IdEntity{
    private String company;//单位名称
	
	private String name;//姓名
	
	private String status;//人员状态
	
	private String type;//种类
	private String payType;//缴费类型
	
	private String datea;//对应缴费期
	private String actualDate;//实际缴费期
	private String base;//缴费基数
	private String payFlag;//缴费标志
	private String getFlag;//到账标志
	private String personalPay;//个人缴金额
	private String companyPay;//单位缴金额
	private String taskid;
	private String personalBill;//个人缴划账户金额
	private String companyBill;//单位缴划账户金额
	@Override
	public String toString() {
		return "InsuranceHuaiAnUnemployment [company=" + company + ", name=" + name + ", status=" + status + ", type="
				+ type + ", payType=" + payType + ", datea=" + datea + ", actualDate=" + actualDate + ", base=" + base
				+ ", payFlag=" + payFlag + ", getFlag=" + getFlag + ", personalPay=" + personalPay + ", companyPay="
				+ companyPay + ", taskid=" + taskid + ", personalBill=" + personalBill + ", companyBill=" + companyBill
				+ "]";
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getDatea() {
		return datea;
	}
	public void setDatea(String datea) {
		this.datea = datea;
	}
	public String getActualDate() {
		return actualDate;
	}
	public void setActualDate(String actualDate) {
		this.actualDate = actualDate;
	}
	public String getBase() {
		return base;
	}
	public void setBase(String base) {
		this.base = base;
	}
	public String getPayFlag() {
		return payFlag;
	}
	public void setPayFlag(String payFlag) {
		this.payFlag = payFlag;
	}
	public String getGetFlag() {
		return getFlag;
	}
	public void setGetFlag(String getFlag) {
		this.getFlag = getFlag;
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
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getPersonalBill() {
		return personalBill;
	}
	public void setPersonalBill(String personalBill) {
		this.personalBill = personalBill;
	}
	public String getCompanyBill() {
		return companyBill;
	}
	public void setCompanyBill(String companyBill) {
		this.companyBill = companyBill;
	}
}
