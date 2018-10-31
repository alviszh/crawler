package com.microservice.dao.entity.crawler.insurance.ganzhou;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;


/**
 * 赣州社保信息用户
 * @author zcx
 *
 */
@Entity
@Table(name = "insurance_ganzhou_userinfo")
public class InsuranceGanZhouUserInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = 3499962561560605116L;
	private String taskid;
	private String useraccount;	//个人编号
	private String companyname;//单位名称
	private String username;//姓名
	private String idnum;//身份证号
	private String state;//人员状态
	private String sex;//性别
	private String nation;//民族
	private String birthdate;//出生日期
	private String medicalCategory;//医疗人员类别
	private String firstdate;//参加工作日期
	private String retiredate;//离退休日期
	private String balance;//账户余额
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	public String getUseraccount() {
		return useraccount;
	}
	public void setUseraccount(String useraccount) {
		this.useraccount = useraccount;
	}
	public String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(String companyname) {
		this.companyname = companyname;
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
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}
	public String getMedicalCategory() {
		return medicalCategory;
	}
	public void setMedicalCategory(String medicalCategory) {
		this.medicalCategory = medicalCategory;
	}
	public String getFirstdate() {
		return firstdate;
	}
	public void setFirstdate(String firstdate) {
		this.firstdate = firstdate;
	}
	public String getRetiredate() {
		return retiredate;
	}
	public void setRetiredate(String retiredate) {
		this.retiredate = retiredate;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	
	@Override
	public String toString() {
		return "InsuranceGanZhouUserInfo [taskid=" + taskid + ", useraccount=" + useraccount + ", companyname="
				+ companyname + ", username=" + username + ", idnum=" + idnum + ", state=" + state + ", sex=" + sex
				+ ", nation=" + nation + ", birthdate=" + birthdate + ", medicalCategory=" + medicalCategory
				+ ", firstdate=" + firstdate + ", retiredate=" + retiredate + ", balance=" + balance + "]";
	}
	public InsuranceGanZhouUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
}
