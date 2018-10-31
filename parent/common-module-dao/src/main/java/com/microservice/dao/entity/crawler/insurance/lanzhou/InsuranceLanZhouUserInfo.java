package com.microservice.dao.entity.crawler.insurance.lanzhou;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "insurance_lanzhou_userinfo",indexes = {@Index(name = "index_insurance_lanzhou_userinfo_taskid", columnList = "taskid")})
public class InsuranceLanZhouUserInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = 5463733733681486246L;
	private String taskid;
//	个人编码
	private String pernum;
//	姓名
	private String name;
//	身份证件号码
	private String idnum;
//	性别
	private String gender;
//	民族
	private String nation;
//	出生日期
	private String birthday;
//	参加工作日期
	private String joinworkdate;
//	社会保障卡卡号
	private String insurcardnum;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getPernum() {
		return pernum;
	}
	public void setPernum(String pernum) {
		this.pernum = pernum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getJoinworkdate() {
		return joinworkdate;
	}
	public void setJoinworkdate(String joinworkdate) {
		this.joinworkdate = joinworkdate;
	}
	public String getInsurcardnum() {
		return insurcardnum;
	}
	public void setInsurcardnum(String insurcardnum) {
		this.insurcardnum = insurcardnum;
	}
	public InsuranceLanZhouUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
}
