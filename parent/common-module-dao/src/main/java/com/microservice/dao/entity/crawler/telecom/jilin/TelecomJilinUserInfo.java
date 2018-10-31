package com.microservice.dao.entity.crawler.telecom.jilin;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "telecom_jilin_userinfo", indexes = {@Index(name = "index_telecom_jilin_userinfo_taskid", columnList = "taskid")})
public class TelecomJilinUserInfo extends IdEntity {

	private String name;						//客户名称
		
	private String type;						//客户类型
	
	private String idType;						//证件类型

	private String idNum;						//证件号码

	private String address;						//通信地址

	private String contactTel;					//联系电话
	
	private String planName;					//当前套餐

	private String accountBalance;				//可用余额

	private String taskid;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getContactTel() {
		return contactTel;
	}

	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
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

	@Override
	public String toString() {
		return "TelecomJilinUserInfo [name=" + name + ", type=" + type + ", idType=" + idType + ", idNum=" + idNum
				+ ", address=" + address + ", contactTel=" + contactTel + ", planName=" + planName + ", accountBalance="
				+ accountBalance + ", taskid=" + taskid + "]";
	}


	
	
	

}