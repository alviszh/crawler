package com.microservice.dao.entity.crawler.insurance.zhuhai;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 社保-珠海 用户信息表
 * @author zz
 *
 */
@Entity
@Table(name = "insurance_zhuhai_userinfo")
public class InsuranceZhuhaiUserinfo extends IdEntity {
	
	private String taskid;
	private String SSN;						//社会保障号
	private String name;					//姓名
	private String certificate;				//法人代码
	private String companyName;				//单位名称
	private String insuranceKind;			//参加险种
	private String declareSalary;			//最新申报工资
	private String pensionPayMonthTotal;	//养老保险累计缴费月数
	private String pensionCapital;		    //养老保险个人帐户本金
	private String pensionPayCapital;		//养老保险个人缴费本金
	private String householdType;			//户口类型
	private String employmentForm;			//用工形式
	private String InsuranceType;			//社保状态
	private String companySSN;				//单位社保号
	private String socialSecurityNum;		//社保卡号
	private String socialSecurityNumPerson;	//个人社保号
	
	
	@Override
	public String toString() {
		return "InsuranceZhuhaiUserinfo [taskid=" + taskid + ", SSN=" + SSN + ", name=" + name + ", certificate="
				+ certificate + ", companyName=" + companyName + ", insuranceKind=" + insuranceKind + ", declareSalary="
				+ declareSalary + ", pensionPayMonthTotal=" + pensionPayMonthTotal + ", pensionCapital="
				+ pensionCapital + ", pensionPayCapital=" + pensionPayCapital + ", householdType=" + householdType
				+ ", employmentForm=" + employmentForm + ", InsuranceType=" + InsuranceType + ", companySSN="
				+ companySSN + ", socialSecurityNum=" + socialSecurityNum + ", socialSecurityNumPerson="
				+ socialSecurityNumPerson + "]";
	}
	public String getInsuranceKind() {
		return insuranceKind;
	}
	public void setInsuranceKind(String insuranceKind) {
		this.insuranceKind = insuranceKind;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getSSN() {
		return SSN;
	}
	public void setSSN(String sSN) {
		SSN = sSN;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCertificate() {
		return certificate;
	}
	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getDeclareSalary() {
		return declareSalary;
	}
	public void setDeclareSalary(String declareSalary) {
		this.declareSalary = declareSalary;
	}
	public String getPensionPayMonthTotal() {
		return pensionPayMonthTotal;
	}
	public void setPensionPayMonthTotal(String pensionPayMonthTotal) {
		this.pensionPayMonthTotal = pensionPayMonthTotal;
	}
	public String getPensionCapital() {
		return pensionCapital;
	}
	public void setPensionCapital(String pensionCapital) {
		this.pensionCapital = pensionCapital;
	}
	public String getPensionPayCapital() {
		return pensionPayCapital;
	}
	public void setPensionPayCapital(String pensionPayCapital) {
		this.pensionPayCapital = pensionPayCapital;
	}
	public String getHouseholdType() {
		return householdType;
	}
	public void setHouseholdType(String householdType) {
		this.householdType = householdType;
	}
	public String getEmploymentForm() {
		return employmentForm;
	}
	public void setEmploymentForm(String employmentForm) {
		this.employmentForm = employmentForm;
	}
	public String getInsuranceType() {
		return InsuranceType;
	}
	public void setInsuranceType(String insuranceType) {
		InsuranceType = insuranceType;
	}
	public String getCompanySSN() {
		return companySSN;
	}
	public void setCompanySSN(String companySSN) {
		this.companySSN = companySSN;
	}
	public String getSocialSecurityNum() {
		return socialSecurityNum;
	}
	public void setSocialSecurityNum(String socialSecurityNum) {
		this.socialSecurityNum = socialSecurityNum;
	}
	public String getSocialSecurityNumPerson() {
		return socialSecurityNumPerson;
	}
	public void setSocialSecurityNumPerson(String socialSecurityNumPerson) {
		this.socialSecurityNumPerson = socialSecurityNumPerson;
	}


}
