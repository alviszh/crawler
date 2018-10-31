package com.microservice.dao.entity.crawler.insurance.dongguan;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;


/**
 * 东莞社保个人信息
 * @author tz
 *
 */
@Entity
@Table(name = "insurance_dongguan_userinfo" ,indexes = {@Index(name = "index_insurance_dongguan_userinfo_taskid", columnList = "taskid")})
public class InsuranceDongguanUserInfo extends IdEntity{
	
	/**
	 * uuid 前端通过uuid访问状态结果	
	 */
	private String taskid;
	/**
	 * 姓名
	 */
	private String name;	
	/**
	 * 证件号码
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
	 * 参加工作日期
	 */
	private String workDate;	
	/**
	 * 手机号码
	 */
	private String mobilePhone;
	/**
	 * 社保机构
	 */
	private String insuranceAgency;
	/**
	 * 当前参保单位
	 */
	private String company;
	/**
	 * 基本养老帐户本金
	 */
	private String pensionMoney;				
	/**
	 * 基本养老帐户利息
	 */
	private String pensionInterest;
	/**
	 * 发卡银行及网点
	 */
	private String bank;
	/**
	 * 第一就医点
	 */
	private String firstHospital;	
	/**
	 * 第一就医点地址
	 */
	private String firstHospitalAddress;				
	/**
	 * 第二就医点名称
	 */
	private String twoHospital;
	/**
	 * 有效时间
	 */
	private String effectiveTime;
	/**
	 * 第二就医点地址
	 */
	private String twoHospitalAddress;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
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
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getInsuranceAgency() {
		return insuranceAgency;
	}
	public void setInsuranceAgency(String insuranceAgency) {
		this.insuranceAgency = insuranceAgency;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getPensionMoney() {
		return pensionMoney;
	}
	public void setPensionMoney(String pensionMoney) {
		this.pensionMoney = pensionMoney;
	}
	public String getPensionInterest() {
		return pensionInterest;
	}
	public void setPensionInterest(String pensionInterest) {
		this.pensionInterest = pensionInterest;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getFirstHospital() {
		return firstHospital;
	}
	public void setFirstHospital(String firstHospital) {
		this.firstHospital = firstHospital;
	}
	public String getFirstHospitalAddress() {
		return firstHospitalAddress;
	}
	public void setFirstHospitalAddress(String firstHospitalAddress) {
		this.firstHospitalAddress = firstHospitalAddress;
	}
	public String getTwoHospital() {
		return twoHospital;
	}
	public void setTwoHospital(String twoHospital) {
		this.twoHospital = twoHospital;
	}
	public String getEffectiveTime() {
		return effectiveTime;
	}
	public void setEffectiveTime(String effectiveTime) {
		this.effectiveTime = effectiveTime;
	}
	public String getTwoHospitalAddress() {
		return twoHospitalAddress;
	}
	public void setTwoHospitalAddress(String twoHospitalAddress) {
		this.twoHospitalAddress = twoHospitalAddress;
	}
	@Override
	public String toString() {
		return "InsuranceDongguanUserInfo [taskid=" + taskid + ", name=" + name + ", idNum=" + idNum + ", sex=" + sex
				+ ", birthdate=" + birthdate + ", workDate=" + workDate + ", mobilePhone=" + mobilePhone
				+ ", insuranceAgency=" + insuranceAgency + ", company=" + company + ", pensionMoney=" + pensionMoney
				+ ", pensionInterest=" + pensionInterest + ", bank=" + bank + ", firstHospital=" + firstHospital
				+ ", firstHospitalAddress=" + firstHospitalAddress + ", twoHospital=" + twoHospital + ", effectiveTime="
				+ effectiveTime + ", twoHospitalAddress=" + twoHospitalAddress + "]";
	}
	public InsuranceDongguanUserInfo(String taskid, String name, String idNum, String sex, String birthdate,
			String workDate, String mobilePhone, String insuranceAgency, String company, String pensionMoney,
			String pensionInterest, String bank, String firstHospital, String firstHospitalAddress, String twoHospital,
			String effectiveTime, String twoHospitalAddress) {
		super();
		this.taskid = taskid;
		this.name = name;
		this.idNum = idNum;
		this.sex = sex;
		this.birthdate = birthdate;
		this.workDate = workDate;
		this.mobilePhone = mobilePhone;
		this.insuranceAgency = insuranceAgency;
		this.company = company;
		this.pensionMoney = pensionMoney;
		this.pensionInterest = pensionInterest;
		this.bank = bank;
		this.firstHospital = firstHospital;
		this.firstHospitalAddress = firstHospitalAddress;
		this.twoHospital = twoHospital;
		this.effectiveTime = effectiveTime;
		this.twoHospitalAddress = twoHospitalAddress;
	}
	public InsuranceDongguanUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	
}
