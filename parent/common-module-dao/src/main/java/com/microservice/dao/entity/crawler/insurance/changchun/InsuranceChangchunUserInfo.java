package com.microservice.dao.entity.crawler.insurance.changchun;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_changchun_userinfo")
public class InsuranceChangchunUserInfo extends IdEntity{
	@Column(name="task_id")
	private String taskid;	//uuid 唯一标识
	private String personNumber;	//个人编号
	private String personName;	//姓名
	private String personID;	//身份证号
	private String personBirth;	//档案出生日期
	private String personSex;	//性别
	private String personNation;	//民族
	private String personKind;	//个人身份
	private String workTime;	//参加工作时间
	private String residenceKind;	//户口性质
	private String personStatus;	//人员状态
	private String companyName;	//单位名称
	private String firstEndowmentInsurance;	//养老参保时间
	private String endowmentInsuranceStatus;	//养老参保状态
	private String firstUnemploymentInsurance;	//失业参保时间
	private String unemploymentInsuranceStatus;	//失业参保状态
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getPersonNumber() {
		return personNumber;
	}
	public void setPersonNumber(String personNumber) {
		this.personNumber = personNumber;
	}
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public String getPersonID() {
		return personID;
	}
	public void setPersonID(String personID) {
		this.personID = personID;
	}
	public String getPersonBirth() {
		return personBirth;
	}
	public void setPersonBirth(String personBirth) {
		this.personBirth = personBirth;
	}
	public String getPersonSex() {
		return personSex;
	}
	public void setPersonSex(String personSex) {
		this.personSex = personSex;
	}
	public String getPersonNation() {
		return personNation;
	}
	public void setPersonNation(String personNation) {
		this.personNation = personNation;
	}
	public String getPersonKind() {
		return personKind;
	}
	public void setPersonKind(String personKind) {
		this.personKind = personKind;
	}
	public String getWorkTime() {
		return workTime;
	}
	public void setWorkTime(String workTime) {
		this.workTime = workTime;
	}
	public String getResidenceKind() {
		return residenceKind;
	}
	public void setResidenceKind(String residenceKind) {
		this.residenceKind = residenceKind;
	}
	public String getPersonStatus() {
		return personStatus;
	}
	public void setPersonStatus(String personStatus) {
		this.personStatus = personStatus;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getFirstEndowmentInsurance() {
		return firstEndowmentInsurance;
	}
	public void setFirstEndowmentInsurance(String firstEndowmentInsurance) {
		this.firstEndowmentInsurance = firstEndowmentInsurance;
	}
	public String getEndowmentInsuranceStatus() {
		return endowmentInsuranceStatus;
	}
	public void setEndowmentInsuranceStatus(String endowmentInsuranceStatus) {
		this.endowmentInsuranceStatus = endowmentInsuranceStatus;
	}
	public String getFirstUnemploymentInsurance() {
		return firstUnemploymentInsurance;
	}
	public void setFirstUnemploymentInsurance(String firstUnemploymentInsurance) {
		this.firstUnemploymentInsurance = firstUnemploymentInsurance;
	}
	public String getUnemploymentInsuranceStatus() {
		return unemploymentInsuranceStatus;
	}
	public void setUnemploymentInsuranceStatus(String unemploymentInsuranceStatus) {
		this.unemploymentInsuranceStatus = unemploymentInsuranceStatus;
	}
	@Override
	public String toString() {
		return "InsuranceChangchunUserInfo [taskid=" + taskid + ", personNumber=" + personNumber + ", personName="
				+ personName + ", personID=" + personID + ", personBirth=" + personBirth + ", personSex=" + personSex
				+ ", personNation=" + personNation + ", personKind=" + personKind + ", workTime=" + workTime
				+ ", residenceKind=" + residenceKind + ", personStatus=" + personStatus + ", companyName=" + companyName
				+ ", firstEndowmentInsurance=" + firstEndowmentInsurance + ", endowmentInsuranceStatus="
				+ endowmentInsuranceStatus + ", firstUnemploymentInsurance=" + firstUnemploymentInsurance
				+ ", unemploymentInsuranceStatus=" + unemploymentInsuranceStatus + "]";
	}
		
}
