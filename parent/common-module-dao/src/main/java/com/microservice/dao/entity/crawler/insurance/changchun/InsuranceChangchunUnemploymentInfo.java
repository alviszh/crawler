package com.microservice.dao.entity.crawler.insurance.changchun;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_changchun_unemploymentinfo")
public class InsuranceChangchunUnemploymentInfo extends IdEntity{
	
	@Column(name="task_id")
	private String taskid;	//uuid唯一标识		
	private String insuranceTime;	//费款所属期
	private String companyNumber;	//单位代码
	private String salary;	//缴费工资
	private String salaryBaseNumber;	//失业保险缴费基数
	private String personalFee;	//失业保险个人缴费金额
	private String companyFee;	//失业保险单位缴费金额
	private String isFlagPersonal;	//失业保险个人缴费标志
	private String isFlagCompany;	//失业保单位缴费标志
	
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getInsuranceTime() {
		return insuranceTime;
	}
	public void setInsuranceTime(String insuranceTime) {
		this.insuranceTime = insuranceTime;
	}
	public String getCompanyNumber() {
		return companyNumber;
	}
	public void setCompanyNumber(String companyNumber) {
		this.companyNumber = companyNumber;
	}
	public String getSalary() {
		return salary;
	}
	public void setSalary(String salary) {
		this.salary = salary;
	}
	public String getSalaryBaseNumber() {
		return salaryBaseNumber;
	}
	public void setSalaryBaseNumber(String salaryBaseNumber) {
		this.salaryBaseNumber = salaryBaseNumber;
	}
	public String getPersonalFee() {
		return personalFee;
	}
	public void setPersonalFee(String personalFee) {
		this.personalFee = personalFee;
	}
	public String getCompanyFee() {
		return companyFee;
	}
	public void setCompanyFee(String companyFee) {
		this.companyFee = companyFee;
	}
	public String getIsFlagPersonal() {
		return isFlagPersonal;
	}
	public void setIsFlagPersonal(String isFlagPersonal) {
		this.isFlagPersonal = isFlagPersonal;
	}
	public String getIsFlagCompany() {
		return isFlagCompany;
	}
	public void setIsFlagCompany(String isFlagCompany) {
		this.isFlagCompany = isFlagCompany;
	}
	@Override
	public String toString() {
		return "InsuranceChangchunUnemploymentInfo [taskid=" + taskid + ", insuranceTime=" + insuranceTime
				+ ", companyNumber=" + companyNumber + ", salary=" + salary + ", salaryBaseNumber=" + salaryBaseNumber
				+ ", personalFee=" + personalFee + ", companyFee=" + companyFee + ", isFlagPersonal=" + isFlagPersonal
				+ ", isFlagCompany=" + isFlagCompany + "]";
	}
	
	
}
