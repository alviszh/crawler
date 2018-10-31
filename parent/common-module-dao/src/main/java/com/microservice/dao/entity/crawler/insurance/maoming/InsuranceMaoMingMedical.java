package com.microservice.dao.entity.crawler.insurance.maoming;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_maoming_medical",indexes = {@Index(name = "index_insurance_maoming_medical_taskid", columnList = "taskid")})
public class InsuranceMaoMingMedical extends IdEntity{

	private String company;//单位名称
	private String status;//险种类型
	private String datea;//费款所属期
	private String base;//人员缴费基数
	private String personalpPay;//个人应缴金额
	private String companyPay;//单位应缴金额
	private String companyMoney;//单位应缴划账户金额
	private String sum;//总金额
	private String flag;//足额到账标志
	private String getDate;//足额到账年月
	private String taskid;
	@Override
	public String toString() {
		return "InsuranceMaoMingEndowment [company=" + company + ", status=" + status + ", datea=" + datea + ", base="
				+ base + ", personalpPay=" + personalpPay + ", companyPay=" + companyPay + ", companyMoney="
				+ companyMoney + ", sum=" + sum + ", flag=" + flag + ", getDate=" + getDate + ", taskid=" + taskid
				+ "]";
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDatea() {
		return datea;
	}
	public void setDatea(String datea) {
		this.datea = datea;
	}
	public String getBase() {
		return base;
	}
	public void setBase(String base) {
		this.base = base;
	}
	public String getPersonalpPay() {
		return personalpPay;
	}
	public void setPersonalpPay(String personalpPay) {
		this.personalpPay = personalpPay;
	}
	public String getCompanyPay() {
		return companyPay;
	}
	public void setCompanyPay(String companyPay) {
		this.companyPay = companyPay;
	}
	public String getCompanyMoney() {
		return companyMoney;
	}
	public void setCompanyMoney(String companyMoney) {
		this.companyMoney = companyMoney;
	}
	public String getSum() {
		return sum;
	}
	public void setSum(String sum) {
		this.sum = sum;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getGetDate() {
		return getDate;
	}
	public void setGetDate(String getDate) {
		this.getDate = getDate;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
}
