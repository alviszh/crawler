package com.microservice.dao.entity.crawler.insurance.haikou;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_haikou_userinfo",indexes = {@Index(name = "index_insurance_haikou_userinfo_taskid", columnList = "taskid")})
public class InsuranceHaiKouUserInfo extends IdEntity{
	private String name;//姓名
	private String IDNum;//身份证
	private String sex;//性别
	private String national;//民族
	private String birth;//出生日期
	private String joinDate;//参加工作日期
	private String homeLand;//户口所在地
	private String addr;//详细地址
	private String endDate;//离退休日期
	private String code;//邮政编码
	private String school;//学历
	private String marry;//婚姻状况
	private String taskid;
	@Override
	public String toString() {
		return "InsuranceHaiKouUserInfo [name=" + name + ", IDNum=" + IDNum + ", sex=" + sex + ", national=" + national
				+ ", birth=" + birth + ", joinDate=" + joinDate + ", homeLand=" + homeLand + ", addr=" + addr
				+ ", endDate=" + endDate + ", code=" + code + ", school=" + school + ", marry=" + marry + ", taskid="
				+ taskid + "]";
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIDNum() {
		return IDNum;
	}
	public void setIDNum(String iDNum) {
		IDNum = iDNum;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getNational() {
		return national;
	}
	public void setNational(String national) {
		this.national = national;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	public String getJoinDate() {
		return joinDate;
	}
	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}
	public String getHomeLand() {
		return homeLand;
	}
	public void setHomeLand(String homeLand) {
		this.homeLand = homeLand;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public String getMarry() {
		return marry;
	}
	public void setMarry(String marry) {
		this.marry = marry;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
}
