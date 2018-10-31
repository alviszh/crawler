package com.microservice.dao.entity.crawler.insurance.chengdu;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;


/**
 * 成都社保个人信息
 * @author tz
 *
 */
@Entity
@Table(name = "insurance_chengdu_userinfo" ,indexes = {@Index(name = "index_insurance_chengdu_userinfo_taskid", columnList = "taskid")})
public class InsuranceChengduUserInfo extends IdEntity{
	
	/**
	 * uuid 前端通过uuid访问状态结果	
	 */
	private String taskid;
	
	/**
	 * 个人编号
	 */
	private String personalNumber;				
	/**
	 * 姓名
	 */
	private String name;							
	/**
	 * 身份证号
	 */
	private String idNum;							
	/**
	 * 性别
	 */
	private String sex;								
	/**
	 * 出生日期
	 */
	private String birthdate;
	/**
	 * 参工时间
	 */
	private String workDate;	
	/**
	 *人员状态			例：在职
	 */
	private String personnelStatus;
	/**
	 * 个人身份
	 */
	private String personalIdentity;	
	/**
	 * 经办机构
	 */
	private String handlingAgency;
	
	/**
	 * 开户银行
	 */
	private String openingBank;					
	/**
	 * 银行账户
	 */
	private String bankAccount;				
	/**
	 * 参保人电话
	 */
	private String mobilePhone;
	
	/**
	 * 医疗保险账户信息:账户余额
	 */
	private String medicalBalance;
	
	public String getMedicalBalance() {
		return medicalBalance;
	}
	public void setMedicalBalance(String medicalBalance) {
		this.medicalBalance = medicalBalance;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getPersonalNumber() {
		return personalNumber;
	}
	public void setPersonalNumber(String personalNumber) {
		this.personalNumber = personalNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdNum() {
		return idNum;
	}
	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}
	public String getWorkDate() {
		return workDate;
	}
	public void setWorkDate(String workDate) {
		this.workDate = workDate;
	}
	public String getPersonnelStatus() {
		return personnelStatus;
	}
	public void setPersonnelStatus(String personnelStatus) {
		this.personnelStatus = personnelStatus;
	}
	public String getPersonalIdentity() {
		return personalIdentity;
	}
	public void setPersonalIdentity(String personalIdentity) {
		this.personalIdentity = personalIdentity;
	}
	public String getHandlingAgency() {
		return handlingAgency;
	}
	public void setHandlingAgency(String handlingAgency) {
		this.handlingAgency = handlingAgency;
	}
	public String getOpeningBank() {
		return openingBank;
	}
	public void setOpeningBank(String openingBank) {
		this.openingBank = openingBank;
	}
	public String getBankAccount() {
		return bankAccount;
	}
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	@Override
	public String toString() {
		return "InsuranceChengduUserInfo [taskid=" + taskid + ", personalNumber=" + personalNumber + ", name=" + name
				+ ", idNum=" + idNum + ", sex=" + sex + ", birthdate=" + birthdate + ", workDate=" + workDate
				+ ", personnelStatus=" + personnelStatus + ", personalIdentity=" + personalIdentity
				+ ", handlingAgency=" + handlingAgency + ", openingBank=" + openingBank + ", bankAccount=" + bankAccount
				+ ", mobilePhone=" + mobilePhone + ", medicalBalance=" + medicalBalance + "]";
	}
	public InsuranceChengduUserInfo(String taskid, String personalNumber, String name, String idNum, String sex,
			String birthdate, String workDate, String personnelStatus, String personalIdentity, String handlingAgency,
			String openingBank, String bankAccount, String mobilePhone) {
		super();
		this.taskid = taskid;
		this.personalNumber = personalNumber;
		this.name = name;
		this.idNum = idNum;
		this.sex = sex;
		this.birthdate = birthdate;
		this.workDate = workDate;
		this.personnelStatus = personnelStatus;
		this.personalIdentity = personalIdentity;
		this.handlingAgency = handlingAgency;
		this.openingBank = openingBank;
		this.bankAccount = bankAccount;
		this.mobilePhone = mobilePhone;
	}
	public InsuranceChengduUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}					
	
	
}
