package com.microservice.dao.entity.crawler.insurance.taian;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "insurance_taian_userinfo",indexes = {@Index(name = "index_insurance_taian_userinfo_taskid", columnList = "taskid")})
public class InsuranceTaiAnUserInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = 5463733733681486246L;
	private String taskid;
//	身份证号码
	private String idnum;
//	姓名
	private String name;
//	性别
	private String gender;
//	出生日期
	private String birthday;
//	户口性质
	private String householdtype;
//	文化程度
	private String educationdegree;
//	婚姻状况
	private String marriage;
//	通讯地址
	private String linkaddress;
//	家庭住址
	private String homeaddress;
//	民族
	private String nation;
//	联系人
	private String linkman;
//	联系电话（注册电话）
	private String contactnum;
//	行政职务
	private String administrativepost;
//	邮政编码
	private String postalcode;
//	参保单位（所在单位名称）
	private String insurunit;
//	户口所在地(户籍)
	private String householdregister;
	
	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getIdnum() {
		return idnum;
	}

	public void setIdnum(String idnum) {
		this.idnum = idnum;
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

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getHouseholdtype() {
		return householdtype;
	}

	public void setHouseholdtype(String householdtype) {
		this.householdtype = householdtype;
	}

	public String getEducationdegree() {
		return educationdegree;
	}

	public void setEducationdegree(String educationdegree) {
		this.educationdegree = educationdegree;
	}

	public String getMarriage() {
		return marriage;
	}

	public void setMarriage(String marriage) {
		this.marriage = marriage;
	}

	public String getLinkaddress() {
		return linkaddress;
	}

	public void setLinkaddress(String linkaddress) {
		this.linkaddress = linkaddress;
	}

	public String getHomeaddress() {
		return homeaddress;
	}

	public void setHomeaddress(String homeaddress) {
		this.homeaddress = homeaddress;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getContactnum() {
		return contactnum;
	}

	public void setContactnum(String contactnum) {
		this.contactnum = contactnum;
	}

	public String getAdministrativepost() {
		return administrativepost;
	}

	public void setAdministrativepost(String administrativepost) {
		this.administrativepost = administrativepost;
	}

	public String getPostalcode() {
		return postalcode;
	}

	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}

	public String getInsurunit() {
		return insurunit;
	}

	public void setInsurunit(String insurunit) {
		this.insurunit = insurunit;
	}

	public String getHouseholdregister() {
		return householdregister;
	}

	public void setHouseholdregister(String householdregister) {
		this.householdregister = householdregister;
	}

	public InsuranceTaiAnUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
