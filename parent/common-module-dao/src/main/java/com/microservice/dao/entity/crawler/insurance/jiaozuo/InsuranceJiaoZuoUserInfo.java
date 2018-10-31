package com.microservice.dao.entity.crawler.insurance.jiaozuo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "insurance_jiaozuo_userinfo",indexes = {@Index(name = "index_insurance_jiaozuo_userinfo_taskid", columnList = "taskid")})
public class InsuranceJiaoZuoUserInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = 5273473947864101643L;
	
	private String personID;//个人编号
	private String name;//姓名
	private String idCard;//身份证
	private String gender;//性别
	private String nation;//民族
	private String birthday;//出生日期
	private String personStatus;//人员状态
	private String unitId;//单位编号
	private String unitName;//单位名称
	private String firstInsuredDate;//参保日期
	private String createAccountDate;//参保年月
	private String insuredStatus;//参保状态
	private String shouldPayMonths;//应缴月数
	private String realPayMonths;//实缴月数
	private String address;//地址
	private String telephone;//联系方式
	private String taskid;
	public String getPersonID() {
		return personID;
	}
	public void setPersonID(String personID) {
		this.personID = personID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
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
	public String getPersonStatus() {
		return personStatus;
	}
	public void setPersonStatus(String personStatus) {
		this.personStatus = personStatus;
	}
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getFirstInsuredDate() {
		return firstInsuredDate;
	}
	public void setFirstInsuredDate(String firstInsuredDate) {
		this.firstInsuredDate = firstInsuredDate;
	}
	public String getCreateAccountDate() {
		return createAccountDate;
	}
	public void setCreateAccountDate(String createAccountDate) {
		this.createAccountDate = createAccountDate;
	}
	public String getInsuredStatus() {
		return insuredStatus;
	}
	public void setInsuredStatus(String insuredStatus) {
		this.insuredStatus = insuredStatus;
	}
	public String getShouldPayMonths() {
		return shouldPayMonths;
	}
	public void setShouldPayMonths(String shouldPayMonths) {
		this.shouldPayMonths = shouldPayMonths;
	}
	public String getRealPayMonths() {
		return realPayMonths;
	}
	public void setRealPayMonths(String realPayMonths) {
		this.realPayMonths = realPayMonths;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public InsuranceJiaoZuoUserInfo(String personID, String name, String idCard, String gender, String nation,
			String birthday, String personStatus, String unitId, String unitName, String firstInsuredDate,
			String createAccountDate, String insuredStatus, String shouldPayMonths, String realPayMonths,
			String address, String telephone, String taskid) {
		super();
		this.personID = personID;
		this.name = name;
		this.idCard = idCard;
		this.gender = gender;
		this.nation = nation;
		this.birthday = birthday;
		this.personStatus = personStatus;
		this.unitId = unitId;
		this.unitName = unitName;
		this.firstInsuredDate = firstInsuredDate;
		this.createAccountDate = createAccountDate;
		this.insuredStatus = insuredStatus;
		this.shouldPayMonths = shouldPayMonths;
		this.realPayMonths = realPayMonths;
		this.address = address;
		this.telephone = telephone;
		this.taskid = taskid;
	}
	public InsuranceJiaoZuoUserInfo() {
		super();
	}
	@Override
	public String toString() {
		return "InsuranceJiaoZuoUserInfo [personID=" + personID + ", name=" + name + ", idCard=" + idCard + ", gender="
				+ gender + ", nation=" + nation + ", birthday=" + birthday + ", personStatus=" + personStatus
				+ ", unitId=" + unitId + ", unitName=" + unitName + ", firstInsuredDate=" + firstInsuredDate
				+ ", createAccountDate=" + createAccountDate + ", insuredStatus=" + insuredStatus + ", shouldPayMonths="
				+ shouldPayMonths + ", realPayMonths=" + realPayMonths + ", address=" + address + ", telephone="
				+ telephone + ", taskid=" + taskid + "]";
	}
	
}
