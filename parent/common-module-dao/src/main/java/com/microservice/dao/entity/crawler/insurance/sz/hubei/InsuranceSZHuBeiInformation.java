package com.microservice.dao.entity.crawler.insurance.sz.hubei;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "insurance_sz_hubei_information",indexes = {@Index(name = "index_insurance_sz_hubei_information_taskid", columnList = "taskid")})
public class InsuranceSZHuBeiInformation extends IdEntity{
	
	private String taskid;
	private String name;                  //真实姓名
	private String idCard;                //身份证号
	private String num;                   //社保卡号
	private String phone;                 //手 机 号
	private String username;              //用户昵称
	private String grade;                 //用户等级

	@Override
	public String toString() {
		return "InsuranceSZHuBeiInformation [taskid=" + taskid + ", name=" + name + ", idCard=" + idCard
				+ ", num=" + num + ", phone=" + phone + ", username=" + username
				+ ", grade=" + grade + "]";
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

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}
	
	
}
