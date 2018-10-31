package com.microservice.dao.entity.crawler.insurance.maoming;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_maoming_userinfo",indexes = {@Index(name = "index_insurance_maoming_userinfo_taskid", columnList = "taskid")})
public class InsuranceMaoMingUserInfo extends IdEntity{

	private String cardNum;//社保卡号
	private String name;//姓名
	private String IDNum;//证件号码
	private String birth;//出生日期
	private String sex;//性别
	private String national;//民族
	private String status;//状态
	private String personalNum;//个人编号
	private String companyNum;//单位编号
	private String company;//单位名称
	private String num;//联系电话
	private String phone;//联系手机
	private String homeAddr;//户口地址
	private String addr;//通讯地址
	private String taskid;
	@Override
	public String toString() {
		return "InsuranceMaoMingUserInfo [cardNum=" + cardNum + ", name=" + name + ", IDNum=" + IDNum + ", birth="
				+ birth + ", sex=" + sex + ", national=" + national + ", status=" + status + ", personalNum="
				+ personalNum + ", companyNum=" + companyNum + ", company=" + company + ", num=" + num + ", phone="
				+ phone + ", homeAddr=" + homeAddr + ", addr=" + addr + ", taskid=" + taskid + "]";
	}
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
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
	public String getNational() {
		return national;
	}
	public void setNational(String national) {
		this.national = national;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPersonalNum() {
		return personalNum;
	}
	public void setPersonalNum(String personalNum) {
		this.personalNum = personalNum;
	}
	public String getCompanyNum() {
		return companyNum;
	}
	public void setCompanyNum(String companyNum) {
		this.companyNum = companyNum;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
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
	public String getHomeAddr() {
		return homeAddr;
	}
	public void setHomeAddr(String homeAddr) {
		this.homeAddr = homeAddr;
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
