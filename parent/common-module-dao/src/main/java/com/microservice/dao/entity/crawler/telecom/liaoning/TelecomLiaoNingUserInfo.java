package com.microservice.dao.entity.crawler.telecom.liaoning;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name = "telecom_liaoning_userinfo",indexes = {@Index(name = "index_telecom_liaoning_userinfo_taskid", columnList = "taskid")})
public class TelecomLiaoNingUserInfo extends IdEntity{

	private String userName;          //客户名称
	
	private String indentNbrType;        //证件类型
	
	private String indentCode;         //证件号码
	
	private String userAddress;       //地址
	
	private String partyId;    //客户标识码
	
	private String custContactPhone; //联系电话
	
	private String acceptDate ; //入网时间
	
	private String email; //邮箱
	
	private String servStatus ;//状态
	
	private String brandIdDesc ; //品牌
	
	private String taskid;

	
	
	public String getBrandIdDesc() {
		return brandIdDesc;
	}

	public void setBrandIdDesc(String brandIdDesc) {
		this.brandIdDesc = brandIdDesc;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIndentNbrType() {
		return indentNbrType;
	}

	public void setIndentNbrType(String indentNbrType) {
		this.indentNbrType = indentNbrType;
	}

	public String getIndentCode() {
		return indentCode;
	}

	public void setIndentCode(String indentCode) {
		this.indentCode = indentCode;
	}

	public String getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public String getCustContactPhone() {
		return custContactPhone;
	}

	public void setCustContactPhone(String custContactPhone) {
		this.custContactPhone = custContactPhone;
	}

	public String getAcceptDate() {
		return acceptDate;
	}

	public void setAcceptDate(String acceptDate) {
		this.acceptDate = acceptDate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getServStatus() {
		return servStatus;
	}

	public void setServStatus(String servStatus) {
		this.servStatus = servStatus;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	@Override
	public String toString() {
		return "TelecomLiaoNingUserInfo [userName=" + userName + ", indentNbrType=" + indentNbrType + ", indentCode="
				+ indentCode + ", userAddress=" + userAddress + ", partyId=" + partyId + ", custContactPhone="
				+ custContactPhone + ", acceptDate=" + acceptDate + ", email=" + email + ", servStatus=" + servStatus
				+ ", taskid=" + taskid + "]";
	}



}
