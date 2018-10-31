package com.microservice.dao.entity.crawler.insurance.yangzhou;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;


/**
 * 扬州社保信息用户基本信息
 * @author zcx
 *
 */
@Entity
@Table(name = "insurance_yangzhou_userinfo")
public class InsuranceYangZhouUserInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = 3499962561560605116L;
	private String taskid;
	private String username;//姓名
	private String idnum;//身份证号码
	private String useraccount;//个人代码
	private String birthdate;//出生年月
	private String sex;//性别
	private String companyname;//单位名称
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
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getUseraccount() {
		return useraccount;
	}
	public void setUseraccount(String useraccount) {
		this.useraccount = useraccount;
	}
	public String getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
	
	@Override
	public String toString() {
		return "InsuranceYangZhouUserInfo [taskid=" + taskid + ", username=" + username + ", idnum=" + idnum
				+ ", useraccount=" + useraccount + ", birthdate=" + birthdate + ", sex=" + sex + ", companyname="
				+ companyname + "]";
	}
	public InsuranceYangZhouUserInfo(String taskid, String username, String idnum, String useraccount, String birthdate,
			String sex, String companyname) {
		super();
		this.taskid = taskid;
		this.username = username;
		this.idnum = idnum;
		this.useraccount = useraccount;
		this.birthdate = birthdate;
		this.sex = sex;
		this.companyname = companyname;
	}
	public InsuranceYangZhouUserInfo() {
		super();
	}
}
