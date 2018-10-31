package com.microservice.dao.entity.crawler.insurance.chongqing;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 * 重庆社保个人信息
 * @author zcx
 *
 */
@Entity
@Table(name="insurance_chongqing_userinfo",indexes = {@Index(name = "index_insurance_chongqing_userinfo_taskid", columnList = "taskid")})
public class InsuranceChongqingUserInfo extends IdEntity {
	private String name; // 姓名
	private String sex; // 性别
	private String idNum; // 身份证号码
	private String birthdate; // 出生日期
	private String personNumber; // 个人编号
	private String nation; // 民族
	private String companyNum; // 所在单位编号
	private String category; // 户口性质
	private String taskid; // 任务ID

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

	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}

	public String getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	public String getPersonNumber() {
		return personNumber;
	}

	public void setPersonNumber(String personNumber) {
		this.personNumber = personNumber;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getCompanyNum() {
		return companyNum;
	}

	public void setCompanyNum(String companyNum) {
		this.companyNum = companyNum;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	@Override
	public String toString() {
		return "InsuranceChongqingUserInfo [name=" + name + ", sex=" + sex + ", idNum=" + idNum + ", birthdate="
				+ birthdate + ", personNumber=" + personNumber + ", nation=" + nation + ", companyNum=" + companyNum
				+ ", category=" + category + ", taskid=" + taskid + "]";
	}	
	public InsuranceChongqingUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public InsuranceChongqingUserInfo(String name, String sex, String idNum, String birthdate, String personNumber,
			String nation, String companyNum, String category, String taskid) {
		super();
		this.name = name;
		this.sex = sex;
		this.idNum = idNum;
		this.birthdate = birthdate;
		this.personNumber = personNumber;
		this.nation = nation;
		this.companyNum = companyNum;
		this.category = category;
		this.taskid = taskid;
	}
}
