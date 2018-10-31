package com.microservice.dao.entity.crawler.insurance.liuzhou;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_liuzhou_userinfo",indexes = {@Index(name = "index_insurance_liuzhou_userinfo_taskid", columnList = "taskid")})
public class InsuranceLiuZhouUserInfo  extends IdEntity{
	private String taskid;                          //uuid 前端通过uuid访问状态结果	
	private String number;						    //个人社保编号
	private String name;				            //姓名
	private String sex;								//性别
	private String idNum;							//身份证号
	private String address;                         //联系地址
	private String phone;                           //电话
	private String unitNumber;                      //当前单位编号
	private String unitName;                        //当前单位名称
	private String type;                            //人员状态
	private String pension;                         //城镇企业职工养老保险
	private String medical;                         //医疗保险
	private String unemployment;                    //失业保险
	private String injury;                          //工伤保险	
	private String birth;                           //生育保险
	
	@Override
	public String toString() {
		return "InsuranceLiuZhouUserInfo [taskid=" + taskid + ", name=" 
				+ name + ", idNum=" + idNum + ", sex=" + sex + ", number=" + number + ", phone=" + phone
				+ ", address=" + address + ", unitNumber=" + unitNumber + ", unitName=" + unitName + ", type=" + type
				+ ", pension=" + pension + ", medical=" + medical + ", unemployment=" + unemployment
				+ ", injury=" + injury + ", birth=" + birth+ "]";
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

	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUnitNumber() {
		return unitNumber;
	}

	public void setUnitNumber(String unitNumber) {
		this.unitNumber = unitNumber;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPension() {
		return pension;
	}

	public void setPension(String pension) {
		this.pension = pension;
	}

	public String getMedical() {
		return medical;
	}

	public void setMedical(String medical) {
		this.medical = medical;
	}

	public String getUnemployment() {
		return unemployment;
	}

	public void setUnemployment(String unemployment) {
		this.unemployment = unemployment;
	}

	public String getInjury() {
		return injury;
	}

	public void setInjury(String injury) {
		this.injury = injury;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}
	
	
}
