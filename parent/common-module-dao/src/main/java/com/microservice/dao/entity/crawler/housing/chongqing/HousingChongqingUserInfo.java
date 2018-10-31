package com.microservice.dao.entity.crawler.housing.chongqing;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 重庆公积金用户信息
 */
@Entity
@Table(name="housing_chongqing_userinfo")
public class HousingChongqingUserInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = 1221206979457794498L;
	private String imgurl;//会员头像
	private String username;//会员名称
	private String loginName;//	登陆名称
	private String userType;//	会员类型
	private String idnum;//	身份证号
	private String email;//	电子邮件
	private String telephone;//	手机号码
	private String zipcode;//	邮政编码
	private String address;//	联系地址
	private String registerTime;//	注册时间
	private String lastloginTime;//	最后登陆时间
	private String totalNum;//	总共登陆次数
	private String taskid;
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getRegisterTime() {
		return registerTime;
	}
	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}
	public String getLastloginTime() {
		return lastloginTime;
	}
	public void setLastloginTime(String lastloginTime) {
		this.lastloginTime = lastloginTime;
	}
	public String getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(String totalNum) {
		this.totalNum = totalNum;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	@Override
	public String toString() {
		return "HousingChongqingUserinfo [imgurl=" + imgurl + ", username=" + username + ", loginName=" + loginName
				+ ", userType=" + userType + ", idnum=" + idnum + ", email=" + email + ", telephone=" + telephone
				+ ", zipcode=" + zipcode + ", address=" + address + ", registerTime=" + registerTime
				+ ", lastloginTime=" + lastloginTime + ", totalNum=" + totalNum + ", taskid=" + taskid + "]";
	}
	public HousingChongqingUserInfo(String imgurl, String username, String loginName, String userType, String idnum,
			String email, String telephone, String zipcode, String address, String registerTime, String lastloginTime,
			String totalNum, String taskid) {
		super();
		this.imgurl = imgurl;
		this.username = username;
		this.loginName = loginName;
		this.userType = userType;
		this.idnum = idnum;
		this.email = email;
		this.telephone = telephone;
		this.zipcode = zipcode;
		this.address = address;
		this.registerTime = registerTime;
		this.lastloginTime = lastloginTime;
		this.totalNum = totalNum;
		this.taskid = taskid;
	}
	public HousingChongqingUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}	
}
