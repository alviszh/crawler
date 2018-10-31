package com.microservice.dao.entity.crawler.housing.hangzhou;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name  ="housing_hangzhou_userinfo",indexes = {@Index(name = "index_housing_hangzhou_userinfo_taskid", columnList = "taskid")})
public class HousingHangZhouUserinfo extends IdEntity implements Serializable{
	
	private String cusNumber; //个人客户号
	private String name;      //姓名
	private String iDNumber;  //注册人身份证号
	private String userName;  //用户名
	private String phoneNumber;//手机号码
	private String email;      //E-mail
	private String address;    //通讯地址
	private String postalCode; //邮政编码
	
	private Integer userid;

	private String taskid;
	@Override
	public String toString() {
		return "HousingHangZhouUserinfo [cusNumber=" + cusNumber + ", name=" + name + ", iDNumber=" + iDNumber + ", userName="
				+ userName + ", phoneNumber=" + phoneNumber + ", email=" + email + ", address="
				+ address + ", postalCode=" + postalCode + ", useridr=" + userid + ",taskid=" +taskid + "]";
	}
	public String getCusNumber() {
		return cusNumber;
	}
	public void setCusNumber(String cusNumber) {
		this.cusNumber = cusNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getiDNumber() {
		return iDNumber;
	}
	public void setiDNumber(String iDNumber) {
		this.iDNumber = iDNumber;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
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
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
	

}
