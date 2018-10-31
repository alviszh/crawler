package com.microservice.dao.entity.crawler.telecom.shandong;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "telecom_shandong_userinfo", indexes = {@Index(name = "index_telecom_shandong_userinfo_taskid", columnList = "taskid")})
public class TelecomShandongUserInfo extends IdEntity {

	private String name;						//客户姓名
	
	private String idType;						//证件类型
	
	private String idNum;						//证件号码

	private String address;						//客户地址

	private String careerType;					//行业类型
	
	private String contacts;					//联系人
	
	private String contactTel;					//联系电话
	
	private String starLevel;					//星级
	
	private String planName;					//当前套餐

	private String accountBalance;				//可用余额

	private String taskid;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCareerType() {
		return careerType;
	}

	public void setCareerType(String careerType) {
		this.careerType = careerType;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getContactTel() {
		return contactTel;
	}

	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}

	public String getStarLevel() {
		return starLevel;
	}

	public void setStarLevel(String starLevel) {
		this.starLevel = starLevel;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(String accountBalance) {
		this.accountBalance = accountBalance;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	@Override
	public String toString() {
		return "TelecomShandongUserInfo [name=" + name + ", idType=" + idType + ", idNum=" + idNum + ", address="
				+ address + ", careerType=" + careerType + ", contacts=" + contacts + ", contactTel=" + contactTel
				+ ", starLevel=" + starLevel + ", planName=" + planName + ", accountBalance=" + accountBalance
				+ ", taskid=" + taskid + "]";
	}

}