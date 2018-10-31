package com.microservice.dao.entity.crawler.insurance.xingtai;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_xingtai_userinfo",indexes = {@Index(name = "index_insurance_xingtai_userinfo_taskid", columnList = "taskid")})
public class InsuranceXingTaiUserInfo extends IdEntity{
	private String taskid;                          //uuid 前端通过uuid访问状态结果	
	private String name;				            //姓名
	private String accountNum;						//个人编号
	private String idNum;							//身份证号
	private String sex;								//性别
	private String companyNum;                      //单位编号
	private String company;                         //单位名称
	
	@Override
	public String toString() {
		return "InsuranceXingTaiUserInfo [taskid=" + taskid + ", name=" + name + ", idNum=" + idNum 
				+ ", sex=" + sex + ", accountNum=" + accountNum 
				+ ", companyNum=" + companyNum + ", company=" + company + "]";
	}

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

	public String getAccountNum() {
		return accountNum;
	}

	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}

	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
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

	
}
