package com.microservice.dao.entity.crawler.housing.dalian;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name  ="housing_dalian_userinfo",indexes = {@Index(name = "index_housing_dalian_userinfo_taskid", columnList = "taskid")})
public class HousingDaLianUserinfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = 1221206979457794498L;
	private String personalAccount;         //个人账号
	private String unitAccount;             //单位账号
	private String mechanism;               //所属机构
	private String jointNameCard;           //联名卡卡号
	private String balance;                 //公积金可用余额
	private String openAnAccount;           //开户日期
	private String monthlyRemittance;       //公积金月汇缴额
	private String base;                    //公积金缴存基数
	private String annualRemittance;        //公积金年汇缴额
	private String extract;                 //公积金年提取额
	private String name;                    //个人姓名
	private String idCard;                  //证件号码
	private String unitName;                //单位名称
	private String state;                   //个人账户状态描述
	private String sex;                     //性别
	private String birth;                   //出生日期
	private String homeAddress;             //家庭住址
	private String maritalStatus;           //婚姻状况
	private String phone;                   //手机号码
    private String email;                   //电子邮箱

	private String taskid;

	
	@Override
	public String toString() {
		return "HousingDaLianUserinfo [personalAccount=" + personalAccount + ", unitAccount=" + unitAccount + ", mechanism=" + mechanism
				+ ", jointNameCard=" + jointNameCard + ", balance=" + balance + ", openAnAccount=" + openAnAccount + ", monthlyRemittance=" + monthlyRemittance
				+ ", base=" + base + ", annualRemittance=" + annualRemittance + ", extract=" + extract+ ", name=" + name
				+ ", idCard=" + idCard + ", unitName=" + unitName + ", state=" + state+ ", sex=" + sex
				+ ", birth=" + birth + ", homeAddress=" + homeAddress + ", maritalStatus=" + maritalStatus+ ", phone=" + phone
				+ ", email=" + email+ ", taskid=" + taskid + "]";
	}
	
	public String getPersonalAccount() {
		return personalAccount;
	}

	public void setPersonalAccount(String personalAccount) {
		this.personalAccount = personalAccount;
	}

	public String getUnitAccount() {
		return unitAccount;
	}

	public void setUnitAccount(String unitAccount) {
		this.unitAccount = unitAccount;
	}

	public String getMechanism() {
		return mechanism;
	}

	public void setMechanism(String mechanism) {
		this.mechanism = mechanism;
	}

	public String getJointNameCard() {
		return jointNameCard;
	}

	public void setJointNameCard(String jointNameCard) {
		this.jointNameCard = jointNameCard;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getOpenAnAccount() {
		return openAnAccount;
	}

	public void setOpenAnAccount(String openAnAccount) {
		this.openAnAccount = openAnAccount;
	}

	public String getMonthlyRemittance() {
		return monthlyRemittance;
	}

	public void setMonthlyRemittance(String monthlyRemittance) {
		this.monthlyRemittance = monthlyRemittance;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getAnnualRemittance() {
		return annualRemittance;
	}

	public void setAnnualRemittance(String annualRemittance) {
		this.annualRemittance = annualRemittance;
	}

	public String getExtract() {
		return extract;
	}

	public void setExtract(String extract) {
		this.extract = extract;
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

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
	
	
}
