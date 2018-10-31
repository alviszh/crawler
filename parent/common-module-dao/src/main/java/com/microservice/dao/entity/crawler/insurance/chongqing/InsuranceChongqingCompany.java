package com.microservice.dao.entity.crawler.insurance.chongqing;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 * 重庆社保参保单位
 * @author zcx
 *
 */
@Entity
@Table(name="insurance_chongqing_company",indexes = {@Index(name = "index_insurance_chongqing_company_taskid", columnList = "taskid")})
public class InsuranceChongqingCompany extends IdEntity {
	private String num;// 序号
	private String companyName;// 参保单位
	private String personNumber;// 个人编号
	private String idNum; // 身份证号
	private String name; // 姓名
	private String sex; // 性别
	private String taskid;

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getPersonNumber() {
		return personNumber;
	}

	public void setPersonNumber(String personNumber) {
		this.personNumber = personNumber;
	}

	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum;
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

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public InsuranceChongqingCompany() {
		super();
		// TODO Auto-generated constructor stub
	}

	public InsuranceChongqingCompany(String num, String companyName, String personNumber, String idNum, String name,
			String sex, String taskid) {
		super();
		this.num = num;
		this.companyName = companyName;
		this.personNumber = personNumber;
		this.idNum = idNum;
		this.name = name;
		this.sex = sex;
		this.taskid = taskid;
	}

	@Override
	public String toString() {
		return "InsuranceChongqingCompany [num=" + num + ", companyName=" + companyName + ", personNumber="
				+ personNumber + ", idNum=" + idNum + ", name=" + name + ", sex=" + sex + ", taskid=" + taskid + "]";
	}
	
}
