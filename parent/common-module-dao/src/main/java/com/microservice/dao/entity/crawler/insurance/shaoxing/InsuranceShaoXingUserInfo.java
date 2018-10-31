package com.microservice.dao.entity.crawler.insurance.shaoxing;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_shaoxing_userinfo",indexes = {@Index(name = "index_insurance_shaoxing_userinfo_taskid", columnList = "taskid")}) 
public class InsuranceShaoXingUserInfo extends IdEntity{

	private String grbh;//个人编号
	private String status;//证件类型
	private String IDNum;//身份证
	private String name;//姓名
	private String sex;//性别
	private String birth;//生日
	private String companyNum;//公司代码
	private String company;//单位
	private String taskid;
	@Override
	public String toString() {
		return "InsuranceShaoXingUserInfo [grbh=" + grbh + ", status=" + status + ", IDNum=" + IDNum + ", name=" + name
				+ ", sex=" + sex + ", birth=" + birth + ", companyNum=" + companyNum + ", company=" + company
				+ ", taskid=" + taskid + "]";
	}
	public String getGrbh() {
		return grbh;
	}
	public void setGrbh(String grbh) {
		this.grbh = grbh;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getIDNum() {
		return IDNum;
	}
	public void setIDNum(String iDNum) {
		IDNum = iDNum;
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
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	public String getCompanyNum() {
		return companyNum;
	}
	public void setCompanyNum(String companyNum) {
		this.companyNum = companyNum;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
	
}
