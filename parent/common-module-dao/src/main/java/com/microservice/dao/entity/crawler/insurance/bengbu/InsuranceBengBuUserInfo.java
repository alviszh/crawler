package com.microservice.dao.entity.crawler.insurance.bengbu;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;


@Entity
@Table(name="insurance_bengbu_userinfo",indexes = {@Index(name = "index_insurance_bengbu_userinfo_taskid", columnList = "taskid")})
public class InsuranceBengBuUserInfo extends IdEntity{
	private String personalNum;//个人编号
	private String cardNum;//社保卡号
	private String companyNum;//单位编号
	private String IDNum;
	private String name;
	private String sex;
	private String national;
	private String birth;
	private String datea;
	private String status;//人员参保状态
	private String connectPerson;//联系人员
	private String code;
	private String phone;
	private String addr;
	private String taskid;
	@Override
	public String toString() {
		return "InsuranceBengBuUserInfo [personalNum=" + personalNum + ", cardNum=" + cardNum + ", companyNum="
				+ companyNum + ", IDNum=" + IDNum + ", name=" + name + ", sex=" + sex + ", national=" + national
				+ ", birth=" + birth + ", datea=" + datea + ", status=" + status + ", connectPerson=" + connectPerson
				+ ", code=" + code + ", phone=" + phone + ", addr=" + addr + ", taskid=" + taskid + "]";
	}
	public String getPersonalNum() {
		return personalNum;
	}
	public void setPersonalNum(String personalNum) {
		this.personalNum = personalNum;
	}
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public String getCompanyNum() {
		return companyNum;
	}
	public void setCompanyNum(String companyNum) {
		this.companyNum = companyNum;
	}
	public String getIDNum() {
		return IDNum;
	}
	public void setIDNum(String iDNum) {
		IDNum = iDNum;
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
	public String getDatea() {
		return datea;
	}
	public void setDatea(String datea) {
		this.datea = datea;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getConnectPerson() {
		return connectPerson;
	}
	public void setConnectPerson(String connectPerson) {
		this.connectPerson = connectPerson;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
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
