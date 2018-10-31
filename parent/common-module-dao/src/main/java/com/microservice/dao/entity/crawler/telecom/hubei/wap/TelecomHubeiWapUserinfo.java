package com.microservice.dao.entity.crawler.telecom.hubei.wap;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_hubei_wap_userinfo" ,indexes = {@Index(name = "index_telecom_hubei_wap_userinfo_taskid", columnList = "taskid")})
public class TelecomHubeiWapUserinfo extends IdEntity {

	private String username;// 用户姓名
	private String telephone;// 业务号码
	private String address;// 联系地址
	private String identityType;// 证件类型
	private String identityNumber;// 证件号码
	private String createDate;// 创建日期
	private String taskid;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getIdentityType() {
		return identityType;
	}
	public void setIdentityType(String identityType) {
		this.identityType = identityType;
	}
	
	public String getIdentityNumber() {
		return identityNumber;
	}
	public void setIdentityNumber(String identityNumber) {
		this.identityNumber = identityNumber;
	}

	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	@Override
	public String toString() {
		return "TelecomHubeiWapUserinfo [username=" + username + ", telephone=" + telephone + ", address=" + address
				+ ", identityType=" + identityType + ", identityNumber=" + identityNumber + ", createDate=" + createDate
				+ ", taskid=" + taskid + "]";
	}
	public TelecomHubeiWapUserinfo(String username, String telephone, String address, String identityType,
			String identityNumber, String createDate, String taskid) {
		super();
		this.username = username;
		this.telephone = telephone;
		this.address = address;
		this.identityType = identityType;
		this.identityNumber = identityNumber;
		this.createDate = createDate;
		this.taskid = taskid;
	}
	public TelecomHubeiWapUserinfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}