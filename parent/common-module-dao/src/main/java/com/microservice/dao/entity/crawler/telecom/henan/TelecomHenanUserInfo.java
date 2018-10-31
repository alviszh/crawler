package com.microservice.dao.entity.crawler.telecom.henan;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "telecom_henan_userinfo", indexes = {@Index(name = "index_telecom_henan_userinfo_taskid", columnList = "taskid")})
public class TelecomHenanUserInfo extends IdEntity {

	private String name;						//客户名称
		
	private String type;						//客户类型
	
	private String idNum;						//证件号码

	private String contactTel;					//联系电话
	
	private String palnName;					//套餐名
	
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

	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}

	public String getContactTel() {
		return contactTel;
	}

	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
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

	public String getPalnName() {
		return palnName;
	}

	public void setPalnName(String palnName) {
		this.palnName = palnName;
	}

	@Override
	public String toString() {
		return "TelecomHenanUserInfo [name=" + name + ", type=" + type + ", idNum=" + idNum + ", contactTel="
				+ contactTel + ", palnName=" + palnName + ", accountBalance=" + accountBalance + ", taskid=" + taskid
				+ "]";
	}

}