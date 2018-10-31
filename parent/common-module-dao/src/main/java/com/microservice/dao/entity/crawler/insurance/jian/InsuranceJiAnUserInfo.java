package com.microservice.dao.entity.crawler.insurance.jian;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "insurance_jian_userinfo")
public class InsuranceJiAnUserInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = 5273473947864101643L;

	private String usernum;//个人编号
	private String username;//姓名
	private String idnum;//身份证号
	private String gender;//性别
	private String insureplace;//参保所在地
	private String companyName;//单位名称
	private String insuranceType;//参加险种
	private String state;//险种状态

	private String taskid;
	
	public String getUsernum() {
		return usernum;
	}
	public void setUsernum(String usernum) {
		this.usernum = usernum;
	}
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
	public String getInsureplace() {
		return insureplace;
	}
	public void setInsureplace(String insureplace) {
		this.insureplace = insureplace;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getInsuranceType() {
		return insuranceType;
	}
	public void setInsuranceType(String insuranceType) {
		this.insuranceType = insuranceType;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	public InsuranceJiAnUserInfo(String usernum, String username, String idnum, String gender, String insureplace,
			String companyName, String insuranceType, String state, String taskid) {
		super();
		this.usernum = usernum;
		this.username = username;
		this.idnum = idnum;
		this.gender = gender;
		this.insureplace = insureplace;
		this.companyName = companyName;
		this.insuranceType = insuranceType;
		this.state = state;
		this.taskid = taskid;
	}
	public InsuranceJiAnUserInfo() {
		super();
	}
	@Override
	public String toString() {
		return "InsuranceJiAnUserInfo [usernum=" + usernum + ", username=" + username + ", idnum=" + idnum + ", gender="
				+ gender + ", insureplace=" + insureplace + ", companyName=" + companyName + ", insuranceType="
				+ insuranceType + ", state=" + state + ", taskid=" + taskid + "]";
	}
}
