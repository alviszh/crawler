package com.microservice.dao.entity.crawler.insurance.huangshi;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "insurance_huangshi_userinfo")
public class InsuranceHuangShiUserInfo extends IdEntity {

	private String personalNum;						//个人编号
	private String idNum;							//公民身份号码
	private String oldNum;							//原编号
	private String name;							//姓名
	private String gender;							//性别
	private String birthday;						//出生日期
	private String workDate;						//参加工作日期
	private String residentType;					//户口性质
	private String companyNum;						//单位编号
	private String companyName;						//单位名称
	private String workStatus;						//人员状态
	private String specialPeopleType;				//特殊人群类别
	private String taskid;
	public String getPersonalNum() {
		return personalNum;
	}
	public void setPersonalNum(String personalNum) {
		this.personalNum = personalNum;
	}
	public String getIdNum() {
		return idNum;
	}
	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}
	public String getOldNum() {
		return oldNum;
	}
	public void setOldNum(String oldNum) {
		this.oldNum = oldNum;
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
	public String getWorkDate() {
		return workDate;
	}
	public void setWorkDate(String workDate) {
		this.workDate = workDate;
	}
	public String getResidentType() {
		return residentType;
	}
	public void setResidentType(String residentType) {
		this.residentType = residentType;
	}
	public String getCompanyNum() {
		return companyNum;
	}
	public void setCompanyNum(String companyNum) {
		this.companyNum = companyNum;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getWorkStatus() {
		return workStatus;
	}
	public void setWorkStatus(String workStatus) {
		this.workStatus = workStatus;
	}
	public String getSpecialPeopleType() {
		return specialPeopleType;
	}
	public void setSpecialPeopleType(String specialPeopleType) {
		this.specialPeopleType = specialPeopleType;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Override
	public String toString() {
		return "TelecomHuangShiUserInfo [personalNum=" + personalNum + ", idNum=" + idNum + ", oldNum=" + oldNum
				+ ", name=" + name + ", gender=" + gender + ", birthday=" + birthday + ", workDate=" + workDate
				+ ", residentType=" + residentType + ", companyNum=" + companyNum + ", companyName=" + companyName
				+ ", workStatus=" + workStatus + ", specialPeopleType=" + specialPeopleType + ", taskid=" + taskid
				+ "]";
	}

}