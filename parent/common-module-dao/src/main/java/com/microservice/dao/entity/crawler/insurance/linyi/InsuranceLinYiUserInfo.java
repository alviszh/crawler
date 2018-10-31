package com.microservice.dao.entity.crawler.insurance.linyi;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name = "insurance_linyi_userinfo",indexes = {@Index(name = "index_insurance_linyi_userinfo_taskid", columnList = "taskid")})
public class InsuranceLinYiUserInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = 5463733733681486246L;
	private String taskid;
//	姓名
	private String name;
//	性别
	private String gender;
//	人员类别
	private String personcategory;
//	公民身份证号码
	private String idnum;
//	民族
	private String nation;
//	参加工作日期
	private String joinworkdate;
//	单位名称
	private String unitname;
//	出生日期
	private String birthday;
//	个人编号
	private String personalnum;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getPersoncategory() {
		return personcategory;
	}
	public void setPersoncategory(String personcategory) {
		this.personcategory = personcategory;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getJoinworkdate() {
		return joinworkdate;
	}
	public void setJoinworkdate(String joinworkdate) {
		this.joinworkdate = joinworkdate;
	}
	public String getUnitname() {
		return unitname;
	}
	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getPersonalnum() {
		return personalnum;
	}
	public void setPersonalnum(String personalnum) {
		this.personalnum = personalnum;
	}
	public InsuranceLinYiUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
}
