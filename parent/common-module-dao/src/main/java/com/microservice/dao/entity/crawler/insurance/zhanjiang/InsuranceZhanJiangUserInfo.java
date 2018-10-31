package com.microservice.dao.entity.crawler.insurance.zhanjiang;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "insurance_zhanjiang_userinfo")
public class InsuranceZhanJiangUserInfo extends IdEntity implements Serializable{
	private String taskid;
	private String idNum;							//证件号码
	private String userName;						//姓名
	private String isSpecial;						//特殊人员与否
	private String institutionName;					//社保机构(所属机构)
	private String institutionNum;					//组织ID
	private String relation;						//与户主关系
	private String familyNum;						//单位编号（家庭医保编号）
	private String unitType;						//单位类型（居民户类型）
	private String specialType;						//特殊人员类别
	private String gender;							//性别
	private String hostName;						//单位名称(户主姓名)
	private String personalNum;						//个人顺序号
	private String personalType;					//个人参保状态
	private String payType;							//缴费方式
	private String address;							//家庭户地址
	private String birthday;						//出生日期
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getIdNum() {
		return idNum;
	}
	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getIsSpecial() {
		return isSpecial;
	}
	public void setIsSpecial(String isSpecial) {
		this.isSpecial = isSpecial;
	}
	public String getInstitutionName() {
		return institutionName;
	}
	public void setInstitutionName(String institutionName) {
		this.institutionName = institutionName;
	}
	public String getInstitutionNum() {
		return institutionNum;
	}
	public void setInstitutionNum(String institutionNum) {
		this.institutionNum = institutionNum;
	}
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}
	public String getFamilyNum() {
		return familyNum;
	}
	public void setFamilyNum(String familyNum) {
		this.familyNum = familyNum;
	}
	public String getUnitType() {
		return unitType;
	}
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}
	public String getSpecialType() {
		return specialType;
	}
	public void setSpecialType(String specialType) {
		this.specialType = specialType;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getPersonalNum() {
		return personalNum;
	}
	public void setPersonalNum(String personalNum) {
		this.personalNum = personalNum;
	}
	public String getPersonalType() {
		return personalType;
	}
	public void setPersonalType(String personalType) {
		this.personalType = personalType;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	@Override
	public String toString() {
		return "InsuranceZhanJiangUserInfo [taskid=" + taskid + ", idNum=" + idNum + ", userName=" + userName
				+ ", isSpecial=" + isSpecial + ", institutionName=" + institutionName + ", institutionNum="
				+ institutionNum + ", relation=" + relation + ", familyNum=" + familyNum + ", unitType=" + unitType
				+ ", specialType=" + specialType + ", gender=" + gender + ", hostName=" + hostName + ", personalNum="
				+ personalNum + ", personalType=" + personalType + ", payType=" + payType + ", address=" + address
				+ ", birthday=" + birthday + "]";
	}
	
}
