package com.microservice.dao.entity.crawler.insurance.zhangzhou;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_zhangzhou_medical",indexes = {@Index(name = "index_insurance_zhangzhou_medical_taskid", columnList = "taskid")})
public class InsuranceZhangZhouMedical extends IdEntity{
	

	private String datea;//建账年月
	private String company;//单位名称
	private String type;//账目类型
	private String personalPay;//个人缴费金额
	private String companyPay;//单位缴费金额
	private String money;//划入账户金额
	private String base;//缴费基数
	private String taskid;
	@Override
	public String toString() {
		return "InsuranceZhangZhouEndowment [datea=" + datea + ", company=" + company + ", type=" + type
				+ ", personalPay=" + personalPay + ", companyPay=" + companyPay + ", money=" + money + ", base=" + base
				+ ", taskid=" + taskid + "]";
	}
	public String getDatea() {
		return datea;
	}
	public void setDatea(String datea) {
		this.datea = datea;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getBase() {
		return base;
	}
	public void setBase(String base) {
		this.base = base;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	

}
