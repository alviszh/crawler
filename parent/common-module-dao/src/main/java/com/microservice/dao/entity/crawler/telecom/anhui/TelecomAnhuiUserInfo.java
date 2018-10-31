package com.microservice.dao.entity.crawler.telecom.anhui;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="telecom_anhui_userinfo",indexes = {@Index(name = "index_telecom_anhui_userinfo_taskid", columnList = "taskid")}) 
public class TelecomAnhuiUserInfo extends IdEntity{

	private String name;//姓名
	
	private String city;//所在城市
	private String taskid;
	
	private String netTime;//入网时间
	
	private String birthday;//出生日期
	
	private String hobby;//爱好
	
	private String education;//教育程度
	
	private String job;//当前行业
	
	private String people;//联系人
	
	private String phone;//电话
	
	private String email;//电子邮箱

	private String sex;//性别
	
	private String cardType;//证件类型

	@Override
	public String toString() {
		return "TelecomAnhuiUserInfo [name=" + name + ", city=" + city + ", taskid=" + taskid + ", netTime=" + netTime
				+ ", birthday=" + birthday + ", hobby=" + hobby + ", education=" + education + ", job=" + job
				+ ", people=" + people + ", phone=" + phone + ", email=" + email + ", sex=" + sex + ", cardType="
				+ cardType + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getNetTime() {
		return netTime;
	}

	public void setNetTime(String netTime) {
		this.netTime = netTime;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getHobby() {
		return hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getPeople() {
		return people;
	}

	public void setPeople(String people) {
		this.people = people;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	
	
	
	
}
