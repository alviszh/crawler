package com.microservice.dao.entity.crawler.housing.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.microservice.dao.entity.IdEntity;

/**
 * @description: 公积金疏浚统一表--用户信息
 */

@Entity
@Table(name="etl_housing_userinfo")
public class HousingUserInfo  extends IdEntity implements Serializable {
	
	private static final long serialVersionUID = -3207075026659390860L;
	
	@Column(name="task_id")
	private String taskId; //唯一标识
	private String userName; //姓名
	private String idNumber;	//身份证号
	private String userSex; //性别
	private String birthDate;	//出生日期
	private String joinWorkDate;	//参加工作日期
	private String residenceProperty;	//户口性质
	private String residenceLocation;	//户口所在地
	private String qualifications;	//学历
	private String mobilephoneNumber;	//手机号
	private String phoneNumber;	//座机号
	private String email;	//邮箱
	private String personalIdentifier;	//公积金个人编号
	private String houseFundCity;	//公积金缴纳城市
	private String houseFundStatus;	//公积金状态
	private String houseFundBaseMoney;	//公积金缴纳基数
	private String houseFundDeclareMoney;	//公积金月申报人民币
	private String companyName;	//公司名称
	private String companyOrganizationNumber;	//公司编号/组织机构代码
	private String balance;	//余额
	private String nationality;	//民族
	private String liveAddress;	//居住地址
	private String livePostcode;	//居住地址邮编
	private String marriageStatus;	//婚姻状况
	private String workersProperty;	//职工性质
	private String houseFundLastdate;	//最后缴纳记录时间
	private String yearDepositAmount; //当年缴存金额
	private String yearDrawAmount; //当年提取金额
	private String onlineTransferAmount; //线上结转金额
	private String transferAmount; //转出金额
	private String basicIdnumber; //用户表身份证号
	
	@JsonBackReference
	private String resource; //溯源字段

	@Override
	public String toString() {
		return "HousingUserInfo [taskId=" + taskId + ", userName=" + userName + ", idNumber=" + idNumber + ", userSex="
				+ userSex + ", birthDate=" + birthDate + ", joinWorkDate=" + joinWorkDate + ", residenceProperty="
				+ residenceProperty + ", residenceLocation=" + residenceLocation + ", qualifications=" + qualifications
				+ ", mobilephoneNumber=" + mobilephoneNumber + ", phoneNumber=" + phoneNumber + ", email=" + email
				+ ", personalIdentifier=" + personalIdentifier + ", houseFundCity=" + houseFundCity
				+ ", houseFundStatus=" + houseFundStatus + ", houseFundBaseMoney=" + houseFundBaseMoney
				+ ", houseFundDeclareMoney=" + houseFundDeclareMoney + ", companyName=" + companyName
				+ ", companyOrganizationNumber=" + companyOrganizationNumber + ", balance=" + balance + ", nationality="
				+ nationality + ", liveAddress=" + liveAddress + ", livePostcode=" + livePostcode + ", marriageStatus="
				+ marriageStatus + ", workersProperty=" + workersProperty + ", houseFundLastdate=" + houseFundLastdate
				+ ", yearDepositAmount=" + yearDepositAmount + ", yearDrawAmount=" + yearDrawAmount
				+ ", onlineTransferAmount=" + onlineTransferAmount + ", transferAmount=" + transferAmount
				+ ", basicIdnumber=" + basicIdnumber + ", resource=" + resource + "]";
	}

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

	public String getHouseFundCity() {
		return houseFundCity;
	}

	public void setHouseFundCity(String houseFundCity) {
		this.houseFundCity = houseFundCity;
	}

	public String getHouseFundStatus() {
		return houseFundStatus;
	}

	public void setHouseFundStatus(String houseFundStatus) {
		this.houseFundStatus = houseFundStatus;
	}

	public String getHouseFundBaseMoney() {
		return houseFundBaseMoney;
	}

	public void setHouseFundBaseMoney(String houseFundBaseMoney) {
		this.houseFundBaseMoney = houseFundBaseMoney;
	}

	public String getHouseFundDeclareMoney() {
		return houseFundDeclareMoney;
	}

	public void setHouseFundDeclareMoney(String houseFundDeclareMoney) {
		this.houseFundDeclareMoney = houseFundDeclareMoney;
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

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
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

	public String getHouseFundLastdate() {
		return houseFundLastdate;
	}

	public void setHouseFundLastdate(String houseFundLastdate) {
		this.houseFundLastdate = houseFundLastdate;
	}

	public String getYearDepositAmount() {
		return yearDepositAmount;
	}

	public void setYearDepositAmount(String yearDepositAmount) {
		this.yearDepositAmount = yearDepositAmount;
	}

	public String getYearDrawAmount() {
		return yearDrawAmount;
	}

	public void setYearDrawAmount(String yearDrawAmount) {
		this.yearDrawAmount = yearDrawAmount;
	}

	public String getOnlineTransferAmount() {
		return onlineTransferAmount;
	}

	public void setOnlineTransferAmount(String onlineTransferAmount) {
		this.onlineTransferAmount = onlineTransferAmount;
	}

	public String getTransferAmount() {
		return transferAmount;
	}

	public void setTransferAmount(String transferAmount) {
		this.transferAmount = transferAmount;
	}

	public String getBasicIdnumber() {
		return basicIdnumber;
	}

	public void setBasicIdnumber(String basicIdnumber) {
		this.basicIdnumber = basicIdnumber;
	}
	
	@Column(columnDefinition="text")
	public String getResource() {
		return resource;
	}
	
	public void setResource(String resource) {
		this.resource = resource;
	}

	
	
}
