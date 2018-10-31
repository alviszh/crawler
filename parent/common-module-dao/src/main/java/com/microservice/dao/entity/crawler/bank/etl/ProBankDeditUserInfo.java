package com.microservice.dao.entity.crawler.bank.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity

@Table(name="pro_bank_debit_user_info",indexes = {@Index(name = "index_pro_bank_debit_user_info_taskid", columnList = "taskId")})
public class ProBankDeditUserInfo extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String resource;
	private String userName;
	private String idNum;
	private String cardNum;
	private String accountStatus;
	private String openDate;
	private String openBank;
	private String telNum;
	private String birthday;
	private String nation;
	private String industry;
	private String certType;
	private String email;
	private String balance;
	private String basicIdNum;
	private String nativePlace;
	private String address;
	private String zipCode;
	private String basicUserName;
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getIdNum() {
		return idNum;
	}
	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public String getAccountStatus() {
		return accountStatus;
	}
	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}
	public String getOpenDate() {
		return openDate;
	}
	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}
	public String getOpenBank() {
		return openBank;
	}
	public void setOpenBank(String openBank) {
		this.openBank = openBank;
	}
	public String getTelNum() {
		return telNum;
	}
	public void setTelNum(String telNum) {
		this.telNum = telNum;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public String getCertType() {
		return certType;
	}
	public void setCertType(String certType) {
		this.certType = certType;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getBasicIdNum() {
		return basicIdNum;
	}
	public void setBasicIdNum(String basicIdNum) {
		this.basicIdNum = basicIdNum;
	}
	public String getNativePlace() {
		return nativePlace;
	}
	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getBasicUserName() {
		return basicUserName;
	}
	public void setBasicUserName(String basicUserName) {
		this.basicUserName = basicUserName;
	}
	@Override
	public String toString() {
		return "ProBankDeditUserInfo [taskId=" + taskId + ", resource=" + resource + ", userName=" + userName
				+ ", idNum=" + idNum + ", cardNum=" + cardNum + ", accountStatus=" + accountStatus + ", openDate="
				+ openDate + ", openBank=" + openBank + ", telNum=" + telNum + ", birthday=" + birthday + ", nation="
				+ nation + ", industry=" + industry + ", certType=" + certType + ", email=" + email + ", balance="
				+ balance + ", basicIdNum=" + basicIdNum + ", nativePlace=" + nativePlace + ", address=" + address
				+ ", zipCode=" + zipCode + ", basicUserName=" + basicUserName + "]";
	}
		
}
