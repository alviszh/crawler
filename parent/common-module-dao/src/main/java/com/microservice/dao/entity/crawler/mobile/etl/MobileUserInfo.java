package com.microservice.dao.entity.crawler.mobile.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="mobile_user_info",indexes = {@Index(name = "index_mobile_user_info_taskid", columnList = "taskId")})


/*
 * 用户信息
 */

public class MobileUserInfo extends IdEntity implements Serializable {
	
	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId; //唯一标识
	private String certificateType;	//证件类型
	private String certificateNumber;	//证件号码
	private String registerAddress;	//注册地址
	private String userName;	//用户姓名
	private String accoutBalance;	//账户余额
	private String telephoneNumber;	//电话号码
	private String carrieroperator;		//运营商
	private String ascriptionProvince;	//所属省份
	private String ascriptionCity;	//所属城市
	private String userSex;	//用户性别
	private String userEmail;	//用户电子邮箱
	@JsonBackReference
	private String resource; //溯源字段
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getCertificateType() {
		return certificateType;
	}
	public void setCertificateType(String certificateType) {
		this.certificateType = certificateType;
	}
	public String getCertificateNumber() {
		return certificateNumber;
	}
	public void setCertificateNumber(String certificateNumber) {
		this.certificateNumber = certificateNumber;
	}
	public String getRegisterAddress() {
		return registerAddress;
	}
	public void setRegisterAddress(String registerAddress) {
		this.registerAddress = registerAddress;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getAccoutBalance() {
		return accoutBalance;
	}
	public void setAccoutBalance(String accoutBalance) {
		this.accoutBalance = accoutBalance;
	}
	public String getTelephoneNumber() {
		return telephoneNumber;
	}
	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}
	public String getCarrieroperator() {
		return carrieroperator;
	}
	public void setCarrieroperator(String carrieroperator) {
		this.carrieroperator = carrieroperator;
	}
	public String getAscriptionProvince() {
		return ascriptionProvince;
	}
	public void setAscriptionProvince(String ascriptionProvince) {
		this.ascriptionProvince = ascriptionProvince;
	}
	public String getAscriptionCity() {
		return ascriptionCity;
	}
	public void setAscriptionCity(String ascriptionCity) {
		this.ascriptionCity = ascriptionCity;
	}
	public String getUserSex() {
		return userSex;
	}
	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
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
		return "MobileUserInfo [taskId=" + taskId + ", certificateType=" + certificateType + ", certificateNumber="
				+ certificateNumber + ", registerAddress=" + registerAddress + ", userName=" + userName
				+ ", accoutBalance=" + accoutBalance + ", telephoneNumber=" + telephoneNumber + ", carrieroperator="
				+ carrieroperator + ", ascriptionProvince=" + ascriptionProvince + ", ascriptionCity=" + ascriptionCity
				+ ", userSex=" + userSex + ", userEmail=" + userEmail + ", resource=" + resource + "]";
	}
	
	
	

}
