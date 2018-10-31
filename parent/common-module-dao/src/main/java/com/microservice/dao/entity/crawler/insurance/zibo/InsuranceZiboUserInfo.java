package com.microservice.dao.entity.crawler.insurance.zibo;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_zibo_userinfo")
public class InsuranceZiboUserInfo extends IdEntity{
	
	private String taskid;							//uuid 前端通过uuid访问状态结果
	private String name;							//姓名
	private String idNum;							//身份证号
	private String gender;							//性别
	private String birthday;						//出生日期
	private String nation;							//民族
	private String marriage;						//婚姻状况
	private String educationLevels;					//文化程度
	private String residenceType;					//户口性质
	private String administrativeDuty;				//行政职务
	private String contact;							//联系人
	private String signupTel;						//注册电话
	private String postcode;						//邮政编码
	private String familyAddress;					//家庭住址
	private String postAddress;						//通讯地址
	private String residenceArea;					//户口所在地
	private String organizationname;				//所在单位名称

	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdNum() {
		return idNum;
	}
	public void setIdNum(String idNum) {
		this.idNum = idNum;
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
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getMarriage() {
		return marriage;
	}
	public void setMarriage(String marriage) {
		this.marriage = marriage;
	}
	public String getEducationLevels() {
		return educationLevels;
	}
	public void setEducationLevels(String educationLevels) {
		this.educationLevels = educationLevels;
	}
	public String getResidenceType() {
		return residenceType;
	}
	public void setResidenceType(String residenceType) {
		this.residenceType = residenceType;
	}
	public String getAdministrativeDuty() {
		return administrativeDuty;
	}
	public void setAdministrativeDuty(String administrativeDuty) {
		this.administrativeDuty = administrativeDuty;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getSignupTel() {
		return signupTel;
	}
	public void setSignupTel(String signupTel) {
		this.signupTel = signupTel;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getFamilyAddress() {
		return familyAddress;
	}
	public void setFamilyAddress(String familyAddress) {
		this.familyAddress = familyAddress;
	}
	public String getPostAddress() {
		return postAddress;
	}
	public void setPostAddress(String postAddress) {
		this.postAddress = postAddress;
	}
	public String getResidenceArea() {
		return residenceArea;
	}
	public void setResidenceArea(String residenceArea) {
		this.residenceArea = residenceArea;
	}
	public String getOrganizationname() {
		return organizationname;
	}
	public void setOrganizationname(String organizationname) {
		this.organizationname = organizationname;
	}
	@Override
	public String toString() {
		return "InsuranceZiboUserInfo [taskid=" + taskid + ", name=" + name + ", idNum=" + idNum + ", gender=" + gender
				+ ", birthday=" + birthday + ", nation=" + nation + ", marriage=" + marriage + ", educationLevels="
				+ educationLevels + ", residenceType=" + residenceType + ", administrativeDuty=" + administrativeDuty
				+ ", contact=" + contact + ", signupTel=" + signupTel + ", postcode=" + postcode + ", familyAddress="
				+ familyAddress + ", postAddress=" + postAddress + ", residenceArea=" + residenceArea
				+ ", organizationname=" + organizationname + "]";
	}
	
	
}
