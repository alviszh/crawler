package com.microservice.dao.entity.crawler.insurance.zhaoqing;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_zhaoqing_userinfo",indexes = {@Index(name = "index_insurance_zhaoqing_userinfo_taskid", columnList = "taskid")})
public class InsuranceZhaoQingUserInfo extends IdEntity{
	private String name;
	private String sex;
	private String national;
	private String birth;
	private String company;
	private String IDNum;
	private String taskid;
	@Override
	public String toString() {
		return "InsuranceZhaoQingUserInfo [name=" + name + ", sex=" + sex + ", national=" + national + ", birth="
				+ birth + ", company=" + company + ", IDNum=" + IDNum + ", taskid=" + taskid + "]";
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getNational() {
		return national;
	}
	public void setNational(String national) {
		this.national = national;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getIDNum() {
		return IDNum;
	}
	public void setIDNum(String iDNum) {
		IDNum = iDNum;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
}
