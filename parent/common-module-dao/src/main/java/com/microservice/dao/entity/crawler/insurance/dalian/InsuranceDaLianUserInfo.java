package com.microservice.dao.entity.crawler.insurance.dalian;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_dalian_userinfo",indexes = {@Index(name = "index_insurance_dalian_userinfo_taskid", columnList = "taskid")})
public class InsuranceDaLianUserInfo extends IdEntity{
	private String taskid;                          //uuid 前端通过uuid访问状态结果	
	private String number;						    //个人编号
	private String name;				            //姓名
	private String sex;								//性别
	private String nation;			                //民族  
	private String birthdate;						//出生日期
	private String idNum;							//本人证件号码
	private String phone;                           //手机号码
	
	
	
	@Override
	public String toString() {
		return "InsuranceDaLianUserInfo [taskid=" + taskid + ", name=" 
				+ name + ", idNum=" + idNum + ", sex=" + sex + ", birthdate=" + birthdate + ", nation=" + nation
				+ ", number=" + number + ", phone=" + phone + "]";
	}



	public String getTaskid() {
		return taskid;
	}



	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}



	public String getNumber() {
		return number;
	}



	public void setNumber(String number) {
		this.number = number;
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



	public String getIdNum() {
		return idNum;
	}



	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}



	public String getPhone() {
		return phone;
	}



	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
}
