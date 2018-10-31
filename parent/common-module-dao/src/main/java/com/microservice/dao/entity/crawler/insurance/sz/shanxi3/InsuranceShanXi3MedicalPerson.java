package com.microservice.dao.entity.crawler.insurance.sz.shanxi3;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_sz_shanxi3_medical_person")
public class InsuranceShanXi3MedicalPerson extends IdEntity{
	
	private String taskid;
	private String uname;								//姓名
	private String idnum;								//公民身份证号
	private String personType;							//人员类别
	private String sex;									//类别
	private String nation;								//民族
	private String birthdate;							//出生日期
	private String phone;								//联系电话
	private String zipCode;								//邮政编码
	private String address;								//地址
	private String firstParticipationDate;				//首次参保日期
	private String participationStatus;					//参保状态
	private String accountBalance;						//当前个人帐户余额
	private String usageAmount;							//个人账户使用金额
	private String asAmount;							//个人账户划入金额
	
	@Override
	public String toString() {
		return "InsuranceShanXi3MedicalPerson [taskid=" + taskid + ", uname=" + uname + ", idnum=" + idnum
				+ ", personType=" + personType + ", sex=" + sex + ", nation=" + nation + ", birthdate=" + birthdate
				+ ", phone=" + phone + ", zipCode=" + zipCode + ", address=" + address + ", firstParticipationDate="
				+ firstParticipationDate + ", participationStatus=" + participationStatus + ", accountBalance="
				+ accountBalance + ", usageAmount=" + usageAmount + ", asAmount=" + asAmount + "]";
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getPersonType() {
		return personType;
	}
	public void setPersonType(String personType) {
		this.personType = personType;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getFirstParticipationDate() {
		return firstParticipationDate;
	}
	public void setFirstParticipationDate(String firstParticipationDate) {
		this.firstParticipationDate = firstParticipationDate;
	}
	public String getParticipationStatus() {
		return participationStatus;
	}
	public void setParticipationStatus(String participationStatus) {
		this.participationStatus = participationStatus;
	}
	public String getAccountBalance() {
		return accountBalance;
	}
	public void setAccountBalance(String accountBalance) {
		this.accountBalance = accountBalance;
	}
	public String getUsageAmount() {
		return usageAmount;
	}
	public void setUsageAmount(String usageAmount) {
		this.usageAmount = usageAmount;
	}
	public String getAsAmount() {
		return asAmount;
	}
	public void setAsAmount(String asAmount) {
		this.asAmount = asAmount;
	}

}
