package com.microservice.dao.entity.crawler.e_commerce.jingdong;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "e_commerce_jd_user",indexes = {@Index(name = "index_e_commerce_jd_user_taskid", columnList = "taskid")})
public class JDUserInfo extends IdEntity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username; //用户名
	private String loginname; //登录名
	private String nickname; //昵称
	private String  email; //邮箱
	private String birthday; //生日
	private int sex; //性别
	private String hobbyList; //爱好
	private String taskid; //唯一标识

	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getHobbyList() {
		return hobbyList;
	}
	public void setHobbyList(String hobbyList) {
		this.hobbyList = hobbyList;
	}
	@Override
	public String toString() {
		return "JDUserInfo [username=" + username + ", loginname=" + loginname + ", nickname=" + nickname + ", email="
				+ email + ", birthday=" + birthday + ", sex=" + sex + ", hobbyList=" + hobbyList + ", taskid=" + taskid
				+ "]";
	}
	
	
}
