package com.microservice.dao.entity.crawler.insurance.wuhan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_wuhan_user")
public class InsuranceWuhanUserInfo extends IdEntity{
	
	@Column(name="task_id")
	private String taskid;	//uuid唯一标识
	private String mechanism;	//社保机构
	private String companyNum;	//单位编号
	private String companyName; //单位名称
	private String personalNum;	//个人编号
	private String name;	//姓名
	private String sex;	//性别
	private String cardNume;	//公民身份证号码
	private String birthDate;		//档案出生日期
	private String residenceNature; //户口性质
	private String nation; //民族
	private String cultureDegree; //文化程度
	private String workTime; //参加工作时间
	private String health; //健康状况
	private String maritalStatus; //婚姻状况
	private String personalStatus; //个人状态
	private String employeeCategory;// 职工类别
	private String civilServiceCategory;//公务员类别
	private String remotePersonnelFlag; //异地人员标志
	private String retirementCategory; //离退休类别
	private String retirementTime;//离退休日期
	private String address; //居住地地址
	private String addressZipCode; //居住地邮政编码;
	private String landlineNum; // 参保人联系电话
	private String phoneNum;  //参保人联系手机
	private String type; //险种类型
	private String personalInsuranceStatus; //个人参保状态
	private String personalFirstTime;//个人首次参保年月
	private String payMonths; //实际缴费月数
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPersonalInsuranceStatus() {
		return personalInsuranceStatus;
	}
	public void setPersonalInsuranceStatus(String personalInsuranceStatus) {
		this.personalInsuranceStatus = personalInsuranceStatus;
	}
	public String getPersonalFirstTime() {
		return personalFirstTime;
	}
	public void setPersonalFirstTime(String personalFirstTime) {
		this.personalFirstTime = personalFirstTime;
	}
	public String getPayMonths() {
		return payMonths;
	}
	public void setPayMonths(String payMonths) {
		this.payMonths = payMonths;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getMechanism() {
		return mechanism;
	}
	public void setMechanism(String mechanism) {
		this.mechanism = mechanism;
	}
	public String getCompanyNum() {
		return companyNum;
	}
	public void setCompanyNum(String companyNum) {
		this.companyNum = companyNum;
	}
	public String getPersonalNum() {
		return personalNum;
	}
	public void setPersonalNum(String personalNum) {
		this.personalNum = personalNum;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getCardNume() {
		return cardNume;
	}
	public void setCardNume(String cardNume) {
		this.cardNume = cardNume;
	}
	public String getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	public String getResidenceNature() {
		return residenceNature;
	}
	public void setResidenceNature(String residenceNature) {
		this.residenceNature = residenceNature;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getCultureDegree() {
		return cultureDegree;
	}
	public void setCultureDegree(String cultureDegree) {
		this.cultureDegree = cultureDegree;
	}
	public String getWorkTime() {
		return workTime;
	}
	public void setWorkTime(String workTime) {
		this.workTime = workTime;
	}
	public String getHealth() {
		return health;
	}
	public void setHealth(String health) {
		this.health = health;
	}
	public String getMaritalStatus() {
		return maritalStatus;
	}
	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
	public String getPersonalStatus() {
		return personalStatus;
	}
	public void setPersonalStatus(String personalStatus) {
		this.personalStatus = personalStatus;
	}
	public String getEmployeeCategory() {
		return employeeCategory;
	}
	public void setEmployeeCategory(String employeeCategory) {
		this.employeeCategory = employeeCategory;
	}
	public String getCivilServiceCategory() {
		return civilServiceCategory;
	}
	public void setCivilServiceCategory(String civilServiceCategory) {
		this.civilServiceCategory = civilServiceCategory;
	}
	public String getRemotePersonnelFlag() {
		return remotePersonnelFlag;
	}
	public void setRemotePersonnelFlag(String remotePersonnelFlag) {
		this.remotePersonnelFlag = remotePersonnelFlag;
	}
	public String getRetirementCategory() {
		return retirementCategory;
	}
	public void setRetirementCategory(String retirementCategory) {
		this.retirementCategory = retirementCategory;
	}
	public String getRetirementTime() {
		return retirementTime;
	}
	public void setRetirementTime(String retirementTime) {
		this.retirementTime = retirementTime;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAddressZipCode() {
		return addressZipCode;
	}
	public void setAddressZipCode(String addressZipCode) {
		this.addressZipCode = addressZipCode;
	}
	public String getLandlineNum() {
		return landlineNum;
	}
	public void setLandlineNum(String landlineNum) {
		this.landlineNum = landlineNum;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	
	
}
