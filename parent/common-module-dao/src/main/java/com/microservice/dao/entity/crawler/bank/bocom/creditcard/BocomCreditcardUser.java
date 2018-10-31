/**
  * Copyright 2017 bejson.com 
  */
package com.microservice.dao.entity.crawler.bank.bocom.creditcard;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * Auto-generated: 2017-11-22 15:52:8
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Entity
@Table(name="bocom_creditcard_user",indexes = {@Index(name = "index_bocom_creditcard_user_taskid", columnList = "taskid")})
public class BocomCreditcardUser extends IdEntity{

    private String userName;//姓名
    private String nickName;//昵称
    private String mobile;
    private String email;
    
    private String passwdStatus;//未知含义
    private String modelCard;//卡号
    
	private String taskid;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPasswdStatus() {
		return passwdStatus;
	}
	public void setPasswdStatus(String passwdStatus) {
		this.passwdStatus = passwdStatus;
	}
	public String getModelCard() {
		return modelCard;
	}
	public void setModelCard(String modelCard) {
		this.modelCard = modelCard;
	}
	@Override
	public String toString() {
		return "User [userName=" + userName + ", nickName=" + nickName + ", mobile=" + mobile + ", email=" + email
				+ ", passwdStatus=" + passwdStatus + ", modelCard=" + modelCard + "]";
	}
    
    
}