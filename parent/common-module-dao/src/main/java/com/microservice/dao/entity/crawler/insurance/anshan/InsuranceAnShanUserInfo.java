package com.microservice.dao.entity.crawler.insurance.anshan;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_anshan_userinfo",indexes = {@Index(name = "index_insurance_anshan_userinfo_taskid", columnList = "taskid")})
public class InsuranceAnShanUserInfo extends IdEntity{
	private String taskid;//uuid 前端通过uuid访问状态结果	
	private String companyName;						//单位名称
	private String name;							//姓名
	private String company;                         //单位电脑编号
	private String personal;                        //个人电脑编号
	private String idCard;                          //身份证号
	
	@Override
	public String toString() {
		return "InsuranceAnShanUserInfo [companyName=" + companyName + ", name=" + name + ", company="
				+ company + ", personal=" + personal+ ", taskid=" + taskid + ",idCard="+ idCard +"]";
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getPersonal() {
		return personal;
	}

	public void setPersonal(String personal) {
		this.personal = personal;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	
}
