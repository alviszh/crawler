package com.microservice.dao.entity.crawler.e_commerce.etl.jd;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="userinfo_jd") //交易明细
public class UserInfoJD extends IdEntity implements Serializable{

	private static final long serialVersionUID = -3207075026659390860L;
	
	@Column(name="task_id")
	private String taskId; //唯一标识
	private String loginName;
	private String userName;
	private String nickName;
	private String sex;
	private String birthday;
	private String hobby;
	private String email;
	private String realName;
	private String marriage;
	private String monthIncome;
	private String idNumber;
	private String education;
	private String vocation;
	private String isQq;
	private String isWeixin;
	private String accountLevel;
	private String accountType;
	
	@JsonBackReference
	private String resource; //溯源字段

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getHobby() {
		return hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getMarriage() {
		return marriage;
	}

	public void setMarriage(String marriage) {
		this.marriage = marriage;
	}

	public String getMonthIncome() {
		return monthIncome;
	}

	public void setMonthIncome(String monthIncome) {
		this.monthIncome = monthIncome;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getVocation() {
		return vocation;
	}

	public void setVocation(String vocation) {
		this.vocation = vocation;
	}

	public String getIsQq() {
		return isQq;
	}

	public void setIsQq(String isQq) {
		this.isQq = isQq;
	}

	public String getIsWeixin() {
		return isWeixin;
	}

	public void setIsWeixin(String isWeixin) {
		this.isWeixin = isWeixin;
	}

	public String getAccountLevel() {
		return accountLevel;
	}

	public void setAccountLevel(String accountLevel) {
		this.accountLevel = accountLevel;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
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
		return "UserInfoJD [taskId=" + taskId + ", loginName=" + loginName + ", userName=" + userName + ", nickName="
				+ nickName + ", sex=" + sex + ", birthday=" + birthday + ", hobby=" + hobby + ", email=" + email
				+ ", realName=" + realName + ", marriage=" + marriage + ", monthIncome=" + monthIncome + ", idNumber="
				+ idNumber + ", education=" + education + ", vocation=" + vocation + ", isQq=" + isQq + ", isWeixin="
				+ isWeixin + ", accountLevel=" + accountLevel + ", accountType=" + accountType + ", resource="
				+ resource + "]";
	}

	
	
	
	
	
}
