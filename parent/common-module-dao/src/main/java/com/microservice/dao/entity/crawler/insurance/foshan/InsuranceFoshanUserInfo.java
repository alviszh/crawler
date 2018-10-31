package com.microservice.dao.entity.crawler.insurance.foshan;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_foshan_userinfo")
public class InsuranceFoshanUserInfo extends IdEntity{

	private String manageOrganization;						//管理的社保机构  
	private String insuranceNum;							//社会保障号
	private String personalInsuranceNum;					//个人社保号  
	private String name;									//姓名 
	private String gender;									//性别
	private String birthday;								//出生日期
	private String organizationName;						//现单位名称
	private String pension;									//养老  
	private String medical;									//医疗  
	private String bear;									//生育  
	private String injury;									//工伤
	private String unemployment;							//失业  
	private String arrearageMonth;							//欠费月数  
	private String pensionRealPayMonth;						//养老实际缴费月数  
	private String medicalRealPayMonth;						//医疗实际缴费月数  
	private String unemploymentRealPayMonth;				//失业实际缴费月数  
	private String taskid;
	public String getManageOrganization() {
		return manageOrganization;
	}
	public void setManageOrganization(String manageOrganization) {
		this.manageOrganization = manageOrganization;
	}
	public String getInsuranceNum() {
		return insuranceNum;
	}
	public void setInsuranceNum(String insuranceNum) {
		this.insuranceNum = insuranceNum;
	}
	public String getPersonalInsuranceNum() {
		return personalInsuranceNum;
	}
	public void setPersonalInsuranceNum(String personalInsuranceNum) {
		this.personalInsuranceNum = personalInsuranceNum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	public String getPension() {
		return pension;
	}
	public void setPension(String pension) {
		this.pension = pension;
	}
	public String getMedical() {
		return medical;
	}
	public void setMedical(String medical) {
		this.medical = medical;
	}
	public String getBear() {
		return bear;
	}
	public void setBear(String bear) {
		this.bear = bear;
	}
	public String getInjury() {
		return injury;
	}
	public void setInjury(String injury) {
		this.injury = injury;
	}
	public String getUnemployment() {
		return unemployment;
	}
	public void setUnemployment(String unemployment) {
		this.unemployment = unemployment;
	}
	public String getArrearageMonth() {
		return arrearageMonth;
	}
	public void setArrearageMonth(String arrearageMonth) {
		this.arrearageMonth = arrearageMonth;
	}
	public String getPensionRealPayMonth() {
		return pensionRealPayMonth;
	}
	public void setPensionRealPayMonth(String pensionRealPayMonth) {
		this.pensionRealPayMonth = pensionRealPayMonth;
	}
	public String getMedicalRealPayMonth() {
		return medicalRealPayMonth;
	}
	public void setMedicalRealPayMonth(String medicalRealPayMonth) {
		this.medicalRealPayMonth = medicalRealPayMonth;
	}
	public String getUnemploymentRealPayMonth() {
		return unemploymentRealPayMonth;
	}
	public void setUnemploymentRealPayMonth(String unemploymentRealPayMonth) {
		this.unemploymentRealPayMonth = unemploymentRealPayMonth;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Override
	public String toString() {
		return "InsuranceFoshanUserInfo [manageOrganization=" + manageOrganization + ", insuranceNum=" + insuranceNum
				+ ", personalInsuranceNum=" + personalInsuranceNum + ", name=" + name + ", gender=" + gender
				+ ", birthday=" + birthday + ", organizationName=" + organizationName + ", pension=" + pension
				+ ", medical=" + medical + ", bear=" + bear + ", injury=" + injury + ", unemployment=" + unemployment
				+ ", arrearageMonth=" + arrearageMonth + ", pensionRealPayMonth=" + pensionRealPayMonth
				+ ", medicalRealPayMonth=" + medicalRealPayMonth + ", unemploymentRealPayMonth="
				+ unemploymentRealPayMonth + ", taskid=" + taskid + "]";
	}
	
}
