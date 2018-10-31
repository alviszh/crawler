package com.microservice.dao.entity.crawler.e_commerce.suning;

import com.microservice.dao.entity.IdEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "e_commerce_suning_user_info" ,indexes = {@Index(name = "index_e_commerce_suning_user_info_taskid", columnList = "taskid")})
public class SuNingUserInfo extends IdEntity implements Serializable {

    private String taskid;
    private String vipid;					//会员编号
    private String username;				//用户名
    private String levelNum;				//用户等级
    private String name;					//真实姓名
    private String nickname;				//昵称
    private String gender;					//性别
    private String telNum;					//手机
    private String email;					//邮箱
    private String birthday;				//生日
    private String constellation;			//星座
    private String address;					//居住地址
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getVipid() {
		return vipid;
	}
	public void setVipid(String vipid) {
		this.vipid = vipid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getLevelNum() {
		return levelNum;
	}
	public void setLevelNum(String levelNum) {
		this.levelNum = levelNum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getTelNum() {
		return telNum;
	}
	public void setTelNum(String telNum) {
		this.telNum = telNum;
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
	public String getConstellation() {
		return constellation;
	}
	public void setConstellation(String constellation) {
		this.constellation = constellation;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Override
	public String toString() {
		return "SuNingUserInfo [taskid=" + taskid + ", vipid=" + vipid + ", username=" + username + ", levelNum="
				+ levelNum + ", name=" + name + ", nickname=" + nickname + ", gender=" + gender + ", telNum=" + telNum
				+ ", email=" + email + ", birthday=" + birthday + ", constellation=" + constellation + ", address="
				+ address + "]";
	}
    
}
