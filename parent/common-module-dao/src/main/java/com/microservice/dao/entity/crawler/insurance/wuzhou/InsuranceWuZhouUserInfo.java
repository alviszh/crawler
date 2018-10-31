package com.microservice.dao.entity.crawler.insurance.wuzhou;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_wuzhou_userinfo",indexes = {@Index(name = "index_insurance_wuzhou_userinfo_taskid", columnList = "taskid")})
public class InsuranceWuZhouUserInfo extends IdEntity{
	private String taskid;                          //uuid 前端通过uuid访问状态结果	
	private String number;						    //个人编号
	private String name;				            //姓名
	private String idNum;							//身份证号
	private String nation;                          //民族
	private String sex;								//性别
	private String birth;                           //出生日期
	private String date;                            //参工日期
	private String type;                            //人员状态
	private String phone;                           //联系方式
	private String address;                         //户口所在地
	
	@Override
	public String toString() {
		return "InsuranceWuZhouUserInfo [taskid=" + taskid + ", name=" 
				+ name + ", idNum=" + idNum + ", sex=" + sex + ", number=" + number + ", phone=" + phone
				+ ", address=" + address + ", nation=" + nation + ", date=" + date + ", type=" + type
				 + ", birth=" + birth+ "]";
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

	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	
	
}
