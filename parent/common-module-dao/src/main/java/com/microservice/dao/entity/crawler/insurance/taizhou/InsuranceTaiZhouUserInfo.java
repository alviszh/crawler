package com.microservice.dao.entity.crawler.insurance.taizhou;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_taizhou_userinfo",indexes = {@Index(name = "index_insurance_taizhou_userinfo_taskid", columnList = "taskid")})
public class InsuranceTaiZhouUserInfo extends IdEntity{

	private String personalNum;//个人编号
	private String name;//姓名
	private String birth;//出生日期
	private String sex;//性别
	private String personalStatus;//个人状态
	private String type;//用工形式
	private String IdNum;//身份证号
	private String national;//民族
	private String phone;//联系电话
	private String medical;//医疗人员类别
	private String home;//户口所在地
	private String addr;//通信地址
	private String taskid;
	@Override
	public String toString() {
		return "InsuranceTaiZhouUserInfo [personalNum=" + personalNum + ", name=" + name + ", birth=" + birth + ", sex="
				+ sex + ", personalStatus=" + personalStatus + ", type=" + type + ", IdNum=" + IdNum + ", national="
				+ national + ", phone=" + phone + ", medical=" + medical + ", home=" + home + ", addr=" + addr
				+ ", taskid=" + taskid + "]";
	}
	public String getPersonalNum() {
		return personalNum;
	}
	public void setPersonalNum(String personalNum) {
		this.personalNum = personalNum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getPersonalStatus() {
		return personalStatus;
	}
	public void setPersonalStatus(String personalStatus) {
		this.personalStatus = personalStatus;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIdNum() {
		return IdNum;
	}
	public void setIdNum(String idNum) {
		IdNum = idNum;
	}
	public String getNational() {
		return national;
	}
	public void setNational(String national) {
		this.national = national;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMedical() {
		return medical;
	}
	public void setMedical(String medical) {
		this.medical = medical;
	}
	public String getHome() {
		return home;
	}
	public void setHome(String home) {
		this.home = home;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
}
