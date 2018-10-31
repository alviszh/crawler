package com.microservice.dao.entity.crawler.telecom.hubei;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_hubei_userinfo" ,indexes = {@Index(name = "index_telecom_hubei_userinfo_taskid", columnList = "taskid")})
public class TelecomHubeiUserinfo extends IdEntity {

	private String username;// 机主姓名
	private String nickname;// 用户昵称
	private String identityType;// 证件类型
	private String identityNumber;// 证件号码
	private String contactNumber;// 联系电话
	private String emailAddress;// 通信地址
	private String zipcode;//邮政编码
	private String email;//email
	private String qq;// qq
	private String weibo;// 微博
	private String createDate;// 创建日期
	private String taskid;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
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
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getWeibo() {
		return weibo;
	}
	public void setWeibo(String weibo) {
		this.weibo = weibo;
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
		return "TelecomHubeiUserinfo [username=" + username + ", nickname=" + nickname + ", identityType="
				+ identityType + ", identityNumber=" + identityNumber + ", contactNumber=" + contactNumber
				+ ", emailAddress=" + emailAddress + ", zipcode=" + zipcode + ", email=" + email + ", qq=" + qq
				+ ", weibo=" + weibo + ", createDate=" + createDate + ", taskid=" + taskid + "]";
	}
	public TelecomHubeiUserinfo(String username, String nickname, String identityType, String identityNumber,
			String contactNumber, String emailAddress, String zipcode, String email, String qq, String weibo,
			String createDate, String taskid) {
		super();
		this.username = username;
		this.nickname = nickname;
		this.identityType = identityType;
		this.identityNumber = identityNumber;
		this.contactNumber = contactNumber;
		this.emailAddress = emailAddress;
		this.zipcode = zipcode;
		this.email = email;
		this.qq = qq;
		this.weibo = weibo;
		this.createDate = createDate;
		this.taskid = taskid;
	}
	public TelecomHubeiUserinfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}