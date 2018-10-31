/**
  * Copyright 2018 bejson.com 
  */
package com.microservice.dao.entity.crawler.insurance.changzhou;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * Auto-generated: 2018-03-15 11:17:31
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Entity
@Table(name="insurance_changzhou_user",indexes = {@Index(name = "index_insurance_changzhou_user_taskid", columnList = "taskId")})
public class InsuranceChangZhouBasicUser extends IdEntity{

    @Override
	public String toString() {
		return "InsuranceChangZhouBasicUser [name=" + name + ", sex=" + sex + ", nation=" + nation + ", birthday="
				+ birthday + ", idType=" + idType + ", idNumber=" + idNumber + ", idCountry=" + idCountry
				+ ", workDate=" + workDate + ", retireStatus=" + retireStatus + ", retireDate=" + retireDate
				+ ", householdType=" + householdType + ", householdAddress=" + householdAddress + ", homePhone="
				+ homePhone + ", mobileNumber=" + mobileNumber + ", residentAddressAddress=" + residentAddressAddress
				+ ", postAddress=" + postAddress + ", zipCode=" + zipCode + ", employmentForm=" + employmentForm
				+ ", personalStatus=" + personalStatus + ", administrativePost=" + administrativePost
				+ ", educationLevel=" + educationLevel + ", mail=" + mail + ", agency=" + agency + ", medicalType="
				+ medicalType + ", taskId=" + taskId + "]";
	}
	private String name;//姓名
    private String sex;//性别
    private String nation;//民族 01 为汉族
    private String birthday;//出生日期
    private String idType;//01 身份证 证件类型
    private String idNumber;//证件号码
    private String idCountry;//所属国家
    private String workDate;//参加工作日期
    private String retireStatus;// 11 未知含义
    private String retireDate;//离退休日期
    private String householdType;//99 未知含义
    private String householdAddress;//户口所在地址
    private String homePhone;//电话号码
    private String mobileNumber;//手机号码
    private String residentAddressAddress;//
    private String postAddress;//
    private String zipCode;//
    private String employmentForm;// 用工形式 1合同制职工（企业险）  2行业统筹省编   3行业统筹地编  4编制内人员 5灵活就业人员 6见习大学生
    private String personalStatus;//
    private String administrativePost;//
    private String educationLevel;//教育程度
    private String mail;//
    private String agency;//社保编号
    private String medicalType;//
    private String taskId;//
    
    public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public void setName(String name) {
         this.name = name;
     }
     public String getName() {
         return name;
     }

    public void setSex(String sex) {
         this.sex = sex;
     }
     public String getSex() {
         return sex;
     }

    public void setNation(String nation) {
         this.nation = nation;
     }
     public String getNation() {
         return nation;
     }

    public void setBirthday(String birthday) {
         this.birthday = birthday;
     }
     public String getBirthday() {
         return birthday;
     }

    public void setIdType(String idType) {
         this.idType = idType;
     }
     public String getIdType() {
         return idType;
     }

    public void setIdNumber(String idNumber) {
         this.idNumber = idNumber;
     }
     public String getIdNumber() {
         return idNumber;
     }

    public void setIdCountry(String idCountry) {
         this.idCountry = idCountry;
     }
     public String getIdCountry() {
         return idCountry;
     }

    public void setWorkDate(String workDate) {
         this.workDate = workDate;
     }
     public String getWorkDate() {
         return workDate;
     }

    public void setRetireStatus(String retireStatus) {
         this.retireStatus = retireStatus;
     }
     public String getRetireStatus() {
         return retireStatus;
     }

    public void setRetireDate(String retireDate) {
         this.retireDate = retireDate;
     }
     public String getRetireDate() {
         return retireDate;
     }

    public void setHouseholdType(String householdType) {
         this.householdType = householdType;
     }
     public String getHouseholdType() {
         return householdType;
     }

    public void setHouseholdAddress(String householdAddress) {
         this.householdAddress = householdAddress;
     }
     public String getHouseholdAddress() {
         return householdAddress;
     }

    public void setHomePhone(String homePhone) {
         this.homePhone = homePhone;
     }
     public String getHomePhone() {
         return homePhone;
     }

    public void setMobileNumber(String mobileNumber) {
         this.mobileNumber = mobileNumber;
     }
     public String getMobileNumber() {
         return mobileNumber;
     }

    public void setResidentAddressAddress(String residentAddressAddress) {
         this.residentAddressAddress = residentAddressAddress;
     }
     public String getResidentAddressAddress() {
         return residentAddressAddress;
     }

    public void setPostAddress(String postAddress) {
         this.postAddress = postAddress;
     }
     public String getPostAddress() {
         return postAddress;
     }

    public void setZipCode(String zipCode) {
         this.zipCode = zipCode;
     }
     public String getZipCode() {
         return zipCode;
     }

    public void setEmploymentForm(String employmentForm) {
         this.employmentForm = employmentForm;
     }
     public String getEmploymentForm() {
         return employmentForm;
     }

    public void setPersonalStatus(String personalStatus) {
         this.personalStatus = personalStatus;
     }
     public String getPersonalStatus() {
         return personalStatus;
     }

    public void setAdministrativePost(String administrativePost) {
         this.administrativePost = administrativePost;
     }
     public String getAdministrativePost() {
         return administrativePost;
     }

    public void setEducationLevel(String educationLevel) {
         this.educationLevel = educationLevel;
     }
     public String getEducationLevel() {
         return educationLevel;
     }

    public void setMail(String mail) {
         this.mail = mail;
     }
     public String getMail() {
         return mail;
     }

    public void setAgency(String agency) {
         this.agency = agency;
     }
     public String getAgency() {
         return agency;
     }

    public void setMedicalType(String medicalType) {
         this.medicalType = medicalType;
     }
     public String getMedicalType() {
         return medicalType;
     }

}