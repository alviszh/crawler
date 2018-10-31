package com.microservice.dao.entity.crawler.insurance.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="etl_insurance_userinfo")

/*
 * 社保个人信息汇总表
 */
public class ETLInsuranceUserinfo extends IdEntity implements Serializable	{
	
	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId; //唯一标识
	private String userName;	//姓名
	private String idNumber;	//身份证号
	private String userSex;	//性别
	private String birthDate;	//出生日期
	private String joinWorkDate;	//参加工作日期
	private String residenceProperty;	//户口性质
	private String residenceLocation;	//户口所在地
	private String qualifications;	//学历
	private String mobilephoneNumber;	//手机号
	private String phoneNumber;	//座机号
	private String email;	//邮箱
	private String personalIdentifier;	//社保个人编号
	private String socialSecurityCity;	//社保缴纳城市
	private String socialSecurityStatus;	//社保状态
	private String socialSecurityDeclareMoney;	//社保月申报人民币
	private String companyName;	//公司名称
	private String companyOrganizationNumber;	//公司编号/组织机构代码
	private String nationality;	//民族
	private String liveAddress;	//居住地址
	private String livePostcode;	//居住地址邮编
	private String marriageStatus;	//婚姻状况
	private String workersProperty;	//职工性质
	private String socialSecurityBegindate;	//参保缴费起始日期
	private String areaCode;	//地区编码
	private String cityName;	//所属城市名称
	private String socialSecurityLastdate;	//最后缴纳记录时间
	private String companyType;	//单位类型
	private String personStatus;	//人员状态
	private String insuredCompanyName;	//参保单位
	private String paymentMonths;	//缴存月数	
//	private String companyPaymentPercent;	//单位缴存比例
//	private String personalPaymentPercent;	//个人缴存比例
	private String personNumberSum; //个人编号汇总
	private String basic_idnumber;
	
	@JsonBackReference
	private String resource; //溯源字段

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getUserSex() {
		return userSex;
	}

	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getJoinWorkDate() {
		return joinWorkDate;
	}

	public void setJoinWorkDate(String joinWorkDate) {
		this.joinWorkDate = joinWorkDate;
	}

	public String getResidenceProperty() {
		return residenceProperty;
	}

	public void setResidenceProperty(String residenceProperty) {
		this.residenceProperty = residenceProperty;
	}

	public String getResidenceLocation() {
		return residenceLocation;
	}

	public void setResidenceLocation(String residenceLocation) {
		this.residenceLocation = residenceLocation;
	}

	public String getQualifications() {
		return qualifications;
	}

	public void setQualifications(String qualifications) {
		this.qualifications = qualifications;
	}

	public String getMobilephoneNumber() {
		return mobilephoneNumber;
	}

	public void setMobilephoneNumber(String mobilephoneNumber) {
		this.mobilephoneNumber = mobilephoneNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPersonalIdentifier() {
		return personalIdentifier;
	}

	public void setPersonalIdentifier(String personalIdentifier) {
		this.personalIdentifier = personalIdentifier;
	}

	public String getSocialSecurityCity() {
		return socialSecurityCity;
	}

	public void setSocialSecurityCity(String socialSecurityCity) {
		this.socialSecurityCity = socialSecurityCity;
	}

	public String getSocialSecurityStatus() {
		return socialSecurityStatus;
	}

	public void setSocialSecurityStatus(String socialSecurityStatus) {
		this.socialSecurityStatus = socialSecurityStatus;
	}

	public String getSocialSecurityDeclareMoney() {
		return socialSecurityDeclareMoney;
	}

	public void setSocialSecurityDeclareMoney(String socialSecurityDeclareMoney) {
		this.socialSecurityDeclareMoney = socialSecurityDeclareMoney;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyOrganizationNumber() {
		return companyOrganizationNumber;
	}

	public void setCompanyOrganizationNumber(String companyOrganizationNumber) {
		this.companyOrganizationNumber = companyOrganizationNumber;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getLiveAddress() {
		return liveAddress;
	}

	public void setLiveAddress(String liveAddress) {
		this.liveAddress = liveAddress;
	}

	public String getLivePostcode() {
		return livePostcode;
	}

	public void setLivePostcode(String livePostcode) {
		this.livePostcode = livePostcode;
	}

	public String getMarriageStatus() {
		return marriageStatus;
	}

	public void setMarriageStatus(String marriageStatus) {
		this.marriageStatus = marriageStatus;
	}

	public String getWorkersProperty() {
		return workersProperty;
	}

	public void setWorkersProperty(String workersProperty) {
		this.workersProperty = workersProperty;
	}

	public String getSocialSecurityBegindate() {
		return socialSecurityBegindate;
	}

	public void setSocialSecurityBegindate(String socialSecurityBegindate) {
		this.socialSecurityBegindate = socialSecurityBegindate;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getSocialSecurityLastdate() {
		return socialSecurityLastdate;
	}

	public void setSocialSecurityLastdate(String socialSecurityLastdate) {
		this.socialSecurityLastdate = socialSecurityLastdate;
	}

	public String getCompanyType() {
		return companyType;
	}

	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}

	public String getPersonStatus() {
		return personStatus;
	}

	public void setPersonStatus(String personStatus) {
		this.personStatus = personStatus;
	}

	public String getInsuredCompanyName() {
		return insuredCompanyName;
	}

	public void setInsuredCompanyName(String insuredCompanyName) {
		this.insuredCompanyName = insuredCompanyName;
	}

	public String getPaymentMonths() {
		return paymentMonths;
	}

	public void setPaymentMonths(String paymentMonths) {
		this.paymentMonths = paymentMonths;
	}

	public String getPersonNumberSum() {
		return personNumberSum;
	}

	public void setPersonNumberSum(String personNumberSum) {
		this.personNumberSum = personNumberSum;
	}

	public String getBasic_idnumber() {
		return basic_idnumber;
	}

	public void setBasic_idnumber(String basic_idnumber) {
		this.basic_idnumber = basic_idnumber;
	}
	
	@Column(columnDefinition="text")
	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	@Override
	public String toString() {
		return "ETLInsuranceUserinfo [taskId=" + taskId + ", userName=" + userName + ", idNumber=" + idNumber
				+ ", userSex=" + userSex + ", birthDate=" + birthDate + ", joinWorkDate=" + joinWorkDate
				+ ", residenceProperty=" + residenceProperty + ", residenceLocation=" + residenceLocation
				+ ", qualifications=" + qualifications + ", mobilephoneNumber=" + mobilephoneNumber + ", phoneNumber="
				+ phoneNumber + ", email=" + email + ", personalIdentifier=" + personalIdentifier
				+ ", socialSecurityCity=" + socialSecurityCity + ", socialSecurityStatus=" + socialSecurityStatus
				+ ", socialSecurityDeclareMoney=" + socialSecurityDeclareMoney + ", companyName=" + companyName
				+ ", companyOrganizationNumber=" + companyOrganizationNumber + ", nationality=" + nationality
				+ ", liveAddress=" + liveAddress + ", livePostcode=" + livePostcode + ", marriageStatus="
				+ marriageStatus + ", workersProperty=" + workersProperty + ", socialSecurityBegindate="
				+ socialSecurityBegindate + ", areaCode=" + areaCode + ", cityName=" + cityName
				+ ", socialSecurityLastdate=" + socialSecurityLastdate + ", companyType=" + companyType
				+ ", personStatus=" + personStatus + ", insuredCompanyName=" + insuredCompanyName + ", paymentMonths="
				+ paymentMonths + ", personNumberSum=" + personNumberSum + ", basic_idnumber=" + basic_idnumber
				+ ", resource=" + resource + "]";
	}

	
}
