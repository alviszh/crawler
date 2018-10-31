package com.microservice.dao.entity.crawler.insurance.tieling;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description:
 * @author: sln 
 * @date: 2017年12月12日 下午3:52:26 
 */
@Entity
@Table(name = "insurance_tieling_userinfo",indexes = {@Index(name = "index_insurance_tieling_userinfo_taskid", columnList = "taskid")})
public class InsuranceTieLingUserInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = 3499962561560605116L;
	private String taskid;
//	社会保障卡号
	private String insurcardno;
//	个人编号
	private String personalnum;
//	社会保障号码
	private String insurno;
//	姓名
	private String name;
//	性别
	private String gender;
//	民族
	private String nation;
//	出生日期
	private String birthday;
//	离退休状态
	private String retirestate;
//	医疗退休状态
	private String medicalretire;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getInsurcardno() {
		return insurcardno;
	}
	public void setInsurcardno(String insurcardno) {
		this.insurcardno = insurcardno;
	}
	public String getPersonalnum() {
		return personalnum;
	}
	public void setPersonalnum(String personalnum) {
		this.personalnum = personalnum;
	}
	public String getInsurno() {
		return insurno;
	}
	public void setInsurno(String insurno) {
		this.insurno = insurno;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getRetirestate() {
		return retirestate;
	}
	public void setRetirestate(String retirestate) {
		this.retirestate = retirestate;
	}
	public String getMedicalretire() {
		return medicalretire;
	}
	public void setMedicalretire(String medicalretire) {
		this.medicalretire = medicalretire;
	}
	public InsuranceTieLingUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
}
