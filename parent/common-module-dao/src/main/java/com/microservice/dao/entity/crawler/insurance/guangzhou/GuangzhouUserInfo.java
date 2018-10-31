package com.microservice.dao.entity.crawler.insurance.guangzhou;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 * 用户基本信息
 * @author wpy
 *
 */
@Entity
@Table(name="insurance_guangzhou_userinfo")
public class GuangzhouUserInfo extends IdEntity{

	private String name;						//姓名
	private String idnum;						//身份证号
	private String personalnum;					//社保个人编号
	private String gender;						//性别
	private String nation;						//民族
	private String birthday;					//出生日期
	private String residenceType;				//户口性质
	private String residenceArea;				//户口所在地
	private String address;						//居住地址
	private String phonenum;					//手机号
	private String postcode;					//邮政编码
	private String email;						//邮箱
	private String currentOrganizationname;		//现单位名称
	private String currentOrganizationnum;		//现单位编号
	private String organizationname;			//参保组织机构名称
	private String organizationnum;				//组织机构编号
	private String retirestatus;				//离退休状态
	private String insuredIndate;				//参保缴费起始日期
	private String insuredStatus;				//参保状态
	private String insuredArea;					//社保缴纳城市
	private String taskid;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getPersonalnum() {
		return personalnum;
	}
	public void setPersonalnum(String personalnum) {
		this.personalnum = personalnum;
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
	public String getResidenceType() {
		return residenceType;
	}
	public void setResidenceType(String residenceType) {
		this.residenceType = residenceType;
	}
	public String getResidenceArea() {
		return residenceArea;
	}
	public void setResidenceArea(String residenceArea) {
		this.residenceArea = residenceArea;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhonenum() {
		return phonenum;
	}
	public void setPhonenum(String phonenum) {
		this.phonenum = phonenum;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCurrentOrganizationname() {
		return currentOrganizationname;
	}
	public void setCurrentOrganizationname(String currentOrganizationname) {
		this.currentOrganizationname = currentOrganizationname;
	}
	public String getCurrentOrganizationnum() {
		return currentOrganizationnum;
	}
	public void setCurrentOrganizationnum(String currentOrganizationnum) {
		this.currentOrganizationnum = currentOrganizationnum;
	}
	public String getOrganizationname() {
		return organizationname;
	}
	public void setOrganizationname(String organizationname) {
		this.organizationname = organizationname;
	}
	public String getOrganizationnum() {
		return organizationnum;
	}
	public void setOrganizationnum(String organizationnum) {
		this.organizationnum = organizationnum;
	}
	public String getRetirestatus() {
		return retirestatus;
	}
	public void setRetirestatus(String retirestatus) {
		this.retirestatus = retirestatus;
	}
	public String getInsuredIndate() {
		return insuredIndate;
	}
	public void setInsuredIndate(String insuredIndate) {
		this.insuredIndate = insuredIndate;
	}
	public String getInsuredStatus() {
		return insuredStatus;
	}
	public void setInsuredStatus(String insuredStatus) {
		this.insuredStatus = insuredStatus;
	}
	public String getInsuredArea() {
		return insuredArea;
	}
	public void setInsuredArea(String insuredArea) {
		this.insuredArea = insuredArea;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Override
	public String toString() {
		return "GuangZhouUserInfo [name=" + name + ", idnum=" + idnum + ", personalnum=" + personalnum + ", gender="
				+ gender + ", nation=" + nation + ", birthday=" + birthday + ", residenceType=" + residenceType
				+ ", residenceArea=" + residenceArea + ", address=" + address + ", phonenum=" + phonenum + ", postcode="
				+ postcode + ", email=" + email + ", currentOrganizationname=" + currentOrganizationname
				+ ", currentOrganizationnum=" + currentOrganizationnum + ", organizationname=" + organizationname
				+ ", organizationnum=" + organizationnum + ", retirestatus=" + retirestatus + ", insuredIndate="
				+ insuredIndate + ", insuredStatus=" + insuredStatus + ", insuredArea=" + insuredArea + ", taskid="
				+ taskid + "]";
	}


}