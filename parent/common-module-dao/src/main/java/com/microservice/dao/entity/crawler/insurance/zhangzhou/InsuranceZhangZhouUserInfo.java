package com.microservice.dao.entity.crawler.insurance.zhangzhou;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_zhangzhou_userinfo",indexes = {@Index(name = "index_insurance_zhangzhou_userinfo_taskid", columnList = "taskid")})
public class InsuranceZhangZhouUserInfo extends IdEntity{
	private String name;//姓名
	private String IDNum;//身份证
	
	private String sex;//性别
	private String national;//民族
	private String birth;//三日
	private String homeLand;//户口所在地
	private String phone;//手机
	private String telephone;//固话
	private String taskid;//
	
	private String addr;//地址
	private String code;//邮编
	private String Email;//邮箱
	private String cardNum;//社保卡号
	private String status;//状态
	@Override
	public String toString() {
		return "InsuranceZhangZhouUserInfo [name=" + name + ", IDNum=" + IDNum + ", sex=" + sex + ", national="
				+ national + ", birth=" + birth + ", homeLand=" + homeLand + ", phone=" + phone + ", telephone="
				+ telephone + ", taskid=" + taskid + ", addr=" + addr + ", code=" + code + ", Email=" + Email
				+ ", cardNum=" + cardNum + ", status=" + status + "]";
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
	public String getHomeLand() {
		return homeLand;
	}
	public void setHomeLand(String homeLand) {
		this.homeLand = homeLand;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
}
