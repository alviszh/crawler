package com.microservice.dao.entity.crawler.insurance.leshan;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "insurance_leshan_userinfo")
public class InsuranceLeShanUserInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = 1L;

	private String taskid;
	
	private String sex;
	
	private String name;
	
	private String brithday;
	
	private String nation;
	
	private String hktype;
	
	private String state;
	
	private String age;
	
	private String yhtype;
	
	private String bankoutlets;
	
	private String username;
	
	private String banknum;
	
	private String tel;
	
	private String address;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBrithday() {
		return brithday;
	}

	public void setBrithday(String brithday) {
		this.brithday = brithday;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getHktype() {
		return hktype;
	}

	public void setHktype(String hktype) {
		this.hktype = hktype;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getYhtype() {
		return yhtype;
	}

	public void setYhtype(String yhtype) {
		this.yhtype = yhtype;
	}

	public String getBankoutlets() {
		return bankoutlets;
	}

	public void setBankoutlets(String bankoutlets) {
		this.bankoutlets = bankoutlets;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getBanknum() {
		return banknum;
	}

	public void setBanknum(String banknum) {
		this.banknum = banknum;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public InsuranceLeShanUserInfo(String taskid, String sex, String name, String brithday, String nation,
			String hktype, String state, String age, String yhtype, String bankoutlets, String username, String banknum,
			String tel, String address) {
		super();
		this.taskid = taskid;
		this.sex = sex;
		this.name = name;
		this.brithday = brithday;
		this.nation = nation;
		this.hktype = hktype;
		this.state = state;
		this.age = age;
		this.yhtype = yhtype;
		this.bankoutlets = bankoutlets;
		this.username = username;
		this.banknum = banknum;
		this.tel = tel;
		this.address = address;
	}

	public InsuranceLeShanUserInfo() {
		super();
	}

	@Override
	public String toString() {
		return "InsuranceLeShanUserInfo [taskid=" + taskid + ", sex=" + sex + ", name=" + name + ", brithday="
				+ brithday + ", nation=" + nation + ", hktype=" + hktype + ", state=" + state + ", age=" + age
				+ ", yhtype=" + yhtype + ", bankoutlets=" + bankoutlets + ", username=" + username + ", banknum="
				+ banknum + ", tel=" + tel + ", address=" + address + "]";
	}
	
	
}
