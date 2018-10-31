package com.microservice.dao.entity.crawler.cmcc;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="cmcc_userinfo")
public class CmccUserInfo extends IdEntity{
	
	private String name;				//用户姓名
	private String brand;				
	private String level;				
	private String status;				
	private String inNetDate;			//注册时间
	private String netAge;				//已使用时间
	private String email;				//邮箱
	private String address;				//家庭住址
	private String zipCode;				//邮编
	private String contactNum;			//手机号
	private String starLevel;			//星级
	private String starScore;
	private String starTime;
	private String realNameInfo;		//是否实名认证
//	private String vipInfo;				//是否是vip
	private String pointValue;			//积分
	private String curFee;				//可用余额
	private String curFeeTotal;			//账户总余额
	private String realFee;				//实时话费
	private String brandName;			//套餐名称
	private String curPlanId;
	private String curPlanName;			
	private String nextPlanId;
	private String nextPlanName;		
	@Column(name="task_id")
	private String taskId;
	
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getCurPlanId() {
		return curPlanId;
	}
	public void setCurPlanId(String curPlanId) {
		this.curPlanId = curPlanId;
	}
	public String getCurPlanName() {
		return curPlanName;
	}
	public void setCurPlanName(String curPlanName) {
		this.curPlanName = curPlanName;
	}
	public String getNextPlanId() {
		return nextPlanId;
	}
	public void setNextPlanId(String nextPlanId) {
		this.nextPlanId = nextPlanId;
	}
	public String getNextPlanName() {
		return nextPlanName;
	}
	public void setNextPlanName(String nextPlanName) {
		this.nextPlanName = nextPlanName;
	}
	
	public String getPointValue() {
		return pointValue;
	}
	public void setPointValue(String pointValue) {
		this.pointValue = pointValue;
	}
	public String getCurFee() {
		return curFee;
	}
	public void setCurFee(String curFee) {
		this.curFee = curFee;
	}
	public String getCurFeeTotal() {
		return curFeeTotal;
	}
	public void setCurFeeTotal(String curFeeTotal) {
		this.curFeeTotal = curFeeTotal;
	}
	public String getRealFee() {
		return realFee;
	}
	public void setRealFee(String realFee) {
		this.realFee = realFee;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getInNetDate() {
		return inNetDate;
	}
	public void setInNetDate(String inNetDate) {
		this.inNetDate = inNetDate;
	}
	public String getNetAge() {
		return netAge;
	}
	public void setNetAge(String netAge) {
		this.netAge = netAge;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public String getContactNum() {
		return contactNum;
	}
	public void setContactNum(String contactNum) {
		this.contactNum = contactNum;
	}
	public String getStarLevel() {
		return starLevel;
	}
	public void setStarLevel(String starLevel) {
		this.starLevel = starLevel;
	}
	public String getStarScore() {
		return starScore;
	}
	public void setStarScore(String starScore) {
		this.starScore = starScore;
	}
	public String getStarTime() {
		return starTime;
	}
	public void setStarTime(String starTime) {
		this.starTime = starTime;
	}
	public String getRealNameInfo() {
		return realNameInfo;
	}
	public void setRealNameInfo(String realNameInfo) {
		this.realNameInfo = realNameInfo;
	}
//	public String getVipInfo() {
//		return vipInfo;
//	}
//	public void setVipInfo(String vipInfo) {
//		this.vipInfo = vipInfo;
//	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	@Override
	public String toString() {
		return "CmccUserInfo [name=" + name + ", brand=" + brand + ", level=" + level + ", status=" + status
				+ ", inNetDate=" + inNetDate + ", netAge=" + netAge + ", email=" + email + ", address=" + address
				+ ", zipCode=" + zipCode + ", contactNum=" + contactNum + ", starLevel=" + starLevel + ", starScore="
				+ starScore + ", starTime=" + starTime + ", realNameInfo=" + realNameInfo + ", brandName=" +  ", taskId=" + taskId + "]";
	}
	
	
	
}
