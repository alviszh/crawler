package com.microservice.dao.entity.crawler.insurance.sz.guangxi;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "insurance_sz_guangxi_userinfo")
public class InsuranceSZGuangXiUserInfo extends IdEntity {
	
	private String username;		//姓名
	private String idnum;			//证件号码
	private String gender;			//性别
	private String nation;		  //民族
	private String householdAddress; //户籍地址
	private String companyname;	//单位名称
	private String firstworkDate;	//参加工作日期
	private String identity;		//个人身份
	private String insuranceState;//参保状态
	private String insuranceNum;//社保卡号
	private String taskid;
	

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getHouseholdAddress() {
		return householdAddress;
	}
	public void setHouseholdAddress(String householdAddress) {
		this.householdAddress = householdAddress;
	}
	public String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
	public String getFirstworkDate() {
		return firstworkDate;
	}
	public void setFirstworkDate(String firstworkDate) {
		this.firstworkDate = firstworkDate;
	}
	public String getIdentity() {
		return identity;
	}
	public void setIdentity(String identity) {
		this.identity = identity;
	}
	public String getInsuranceState() {
		return insuranceState;
	}
	public void setInsuranceState(String insuranceState) {
		this.insuranceState = insuranceState;
	}
	
	public String getInsuranceNum() {
		return insuranceNum;
	}
	public void setInsuranceNum(String insuranceNum) {
		this.insuranceNum = insuranceNum;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	@Override
	public String toString() {
		return "InsuranceSZGuangXiUserInfo [username=" + username + ", idnum=" + idnum + ", gender=" + gender
				+ ", nation=" + nation + ", householdAddress=" + householdAddress + ", companyname=" + companyname
				+ ", firstworkDate=" + firstworkDate + ", identity=" + identity + ", insuranceState=" + insuranceState
				+ ", insuranceNum=" + insuranceNum + ", taskid=" + taskid + "]";
	}
	
	public InsuranceSZGuangXiUserInfo(String username, String idnum, String gender, String nation,
			String householdAddress, String companyname, String firstworkDate, String identity, String insuranceState,
			String insuranceNum, String taskid) {
		super();
		this.username = username;
		this.idnum = idnum;
		this.gender = gender;
		this.nation = nation;
		this.householdAddress = householdAddress;
		this.companyname = companyname;
		this.firstworkDate = firstworkDate;
		this.identity = identity;
		this.insuranceState = insuranceState;
		this.insuranceNum = insuranceNum;
		this.taskid = taskid;
	}
	public InsuranceSZGuangXiUserInfo() {
		super();
	}
}
