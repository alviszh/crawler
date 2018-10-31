package com.microservice.dao.entity.crawler.e_commerce.etl.taobao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="userinfo_taobao") //交易明细
public class UserInfoTaoBao extends IdEntity implements Serializable {
	
	private static final long serialVersionUID = -3207075026659390860L;

	@Column(name="task_id")
	private String taskId; //唯一标识
	private String accountName;
	private String creditLimit;
	private String buyerLevel;
	private String evaluatePercent;
	private String lastLoginTime;
	private String birthday;
	private String liveAddr;
	private String hometown;
	private String growth;
	private String gold;
	private String alipayName;
	private String alipayType;
	private String alipayStatus;
	private String payStatus;
	private String phone;
	private String alipayBalance;
	private String zipCode;
	private String fixedNumber;
	private String tmallAccout;
	private String tmallPoints;
	private String tmallAccountLevel;
	private String tmallExperience;
	private String rightsAmount;
	private String tmallDuration;
	private String usualReceiveAddr;

	@JsonBackReference
	private String resource; //溯源字段

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(String creditLimit) {
		this.creditLimit = creditLimit;
	}

	public String getBuyerLevel() {
		return buyerLevel;
	}

	public void setBuyerLevel(String buyerLevel) {
		this.buyerLevel = buyerLevel;
	}

	public String getEvaluatePercent() {
		return evaluatePercent;
	}

	public void setEvaluatePercent(String evaluatePercent) {
		this.evaluatePercent = evaluatePercent;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getLiveAddr() {
		return liveAddr;
	}

	public void setLiveAddr(String liveAddr) {
		this.liveAddr = liveAddr;
	}

	public String getHometown() {
		return hometown;
	}

	public void setHometown(String hometown) {
		this.hometown = hometown;
	}

	public String getGrowth() {
		return growth;
	}

	public void setGrowth(String growth) {
		this.growth = growth;
	}

	public String getGold() {
		return gold;
	}

	public void setGold(String gold) {
		this.gold = gold;
	}

	public String getAlipayName() {
		return alipayName;
	}

	public void setAlipayName(String alipayName) {
		this.alipayName = alipayName;
	}

	public String getAlipayType() {
		return alipayType;
	}

	public void setAlipayType(String alipayType) {
		this.alipayType = alipayType;
	}

	public String getAlipayStatus() {
		return alipayStatus;
	}

	public void setAlipayStatus(String alipayStatus) {
		this.alipayStatus = alipayStatus;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAlipayBalance() {
		return alipayBalance;
	}

	public void setAlipayBalance(String alipayBalance) {
		this.alipayBalance = alipayBalance;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getFixedNumber() {
		return fixedNumber;
	}

	public void setFixedNumber(String fixedNumber) {
		this.fixedNumber = fixedNumber;
	}

	public String getTmallAccout() {
		return tmallAccout;
	}

	public void setTmallAccout(String tmallAccout) {
		this.tmallAccout = tmallAccout;
	}

	public String getTmallPoints() {
		return tmallPoints;
	}

	public void setTmallPoints(String tmallPoints) {
		this.tmallPoints = tmallPoints;
	}

	public String getTmallAccountLevel() {
		return tmallAccountLevel;
	}

	public void setTmallAccountLevel(String tmallAccountLevel) {
		this.tmallAccountLevel = tmallAccountLevel;
	}

	public String getTmallExperience() {
		return tmallExperience;
	}

	public void setTmallExperience(String tmallExperience) {
		this.tmallExperience = tmallExperience;
	}

	public String getRightsAmount() {
		return rightsAmount;
	}

	public void setRightsAmount(String rightsAmount) {
		this.rightsAmount = rightsAmount;
	}

	public String getTmallDuration() {
		return tmallDuration;
	}

	public void setTmallDuration(String tmallDuration) {
		this.tmallDuration = tmallDuration;
	}

	public String getUsualReceiveAddr() {
		return usualReceiveAddr;
	}

	public void setUsualReceiveAddr(String usualReceiveAddr) {
		this.usualReceiveAddr = usualReceiveAddr;
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
		return "UserInfoTaoBao [taskId=" + taskId + ", accountName=" + accountName + ", creditLimit=" + creditLimit
				+ ", buyerLevel=" + buyerLevel + ", evaluatePercent=" + evaluatePercent + ", lastLoginTime="
				+ lastLoginTime + ", birthday=" + birthday + ", liveAddr=" + liveAddr + ", hometown=" + hometown
				+ ", growth=" + growth + ", gold=" + gold + ", alipayName=" + alipayName + ", alipayType=" + alipayType
				+ ", alipayStatus=" + alipayStatus + ", payStatus=" + payStatus + ", phone=" + phone
				+ ", alipayBalance=" + alipayBalance + ", zipCode=" + zipCode + ", fixedNumber=" + fixedNumber
				+ ", tmallAccout=" + tmallAccout + ", tmallPoints=" + tmallPoints + ", tmallAccountLevel="
				+ tmallAccountLevel + ", tmallExperience=" + tmallExperience + ", rightsAmount=" + rightsAmount
				+ ", tmallDuration=" + tmallDuration + ", usualReceiveAddr=" + usualReceiveAddr + ", resource="
				+ resource + "]";
	}
	
	
}
