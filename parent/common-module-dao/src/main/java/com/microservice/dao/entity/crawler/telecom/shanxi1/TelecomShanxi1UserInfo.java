package com.microservice.dao.entity.crawler.telecom.shanxi1;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;


/**
 * 山西电信用户信息
 * @author tz
 *
 */
@Entity
@Table(name = "telecom_shanxi1_userinfo" ,indexes = {@Index(name = "index_telecom_shanxi1_userinfo_taskid", columnList = "taskid")})
public class TelecomShanxi1UserInfo extends IdEntity {
	
	/**
	 * taskid
	 */
	private String taskid;

	
	/**
	 * 客户名称
	 */
	private String name;
	
	/**
	 * 客户类型
	 */
	private String customerType;
	
	/**
	 * 联系电话
	 */
	private String telephone;

	/**
	 * 证件类型
	 */
	private String documenType;

	/**
	 * 证件号码
	 */
	private String idNum;

	/**
	 * 通信地址
	 */
	private String address;

	/**
	 * 邮政编码
	 */
	private String postcode;
	
	/**
	 * E-mail
	 */
	private String email;

	/**
	 * 客户性别
	 */
	private String sex;
	
	/**
	 * 出生日期
	 */
	private String birthdate;
	
	/**
	 * 职业类别
	 */
	private String occupation;
	
	/**
	 * 学历
	 */
	private String education;
	
	/**
	 * 客户爱好
	 */
	private String hobby;
	
	/**
	 * 客户习惯
	 */
	private String habit;
	
	/**
	 * 家庭邮编
	 */
	private String familyCode;
	
	/**
	 * 住宅电话
	 */
	private String homePhone;
	
	/**
	 * 创建日期
	 */
	private String createDate;
	
	/**
	 * 余额
	 */
	private String totalBalance;
	
	/**
	 * 通用余额
	 */
	private String comboBalance;
	
	/**
	 * 专用余额
	 */
	private String specialUseBalance;
	
	/**
	 * 欠费金额
	 */
	private String arrearsSum;
	
	/**
	 * 本月已产生话费
	 */
	private String sumCharge;
	
	/**
	 * 积分
	 */
	private String integral;

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

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getDocumenType() {
		return documenType;
	}

	public void setDocumenType(String documenType) {
		this.documenType = documenType;
	}

	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getHobby() {
		return hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	public String getHabit() {
		return habit;
	}

	public void setHabit(String habit) {
		this.habit = habit;
	}

	public String getFamilyCode() {
		return familyCode;
	}

	public void setFamilyCode(String familyCode) {
		this.familyCode = familyCode;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getTotalBalance() {
		return totalBalance;
	}

	public void setTotalBalance(String totalBalance) {
		this.totalBalance = totalBalance;
	}

	public String getComboBalance() {
		return comboBalance;
	}

	public void setComboBalance(String comboBalance) {
		this.comboBalance = comboBalance;
	}

	public String getSpecialUseBalance() {
		return specialUseBalance;
	}

	public void setSpecialUseBalance(String specialUseBalance) {
		this.specialUseBalance = specialUseBalance;
	}

	public String getArrearsSum() {
		return arrearsSum;
	}

	public void setArrearsSum(String arrearsSum) {
		this.arrearsSum = arrearsSum;
	}

	public String getSumCharge() {
		return sumCharge;
	}

	public void setSumCharge(String sumCharge) {
		this.sumCharge = sumCharge;
	}

	public String getIntegral() {
		return integral;
	}

	public void setIntegral(String integral) {
		this.integral = integral;
	}

	@Override
	public String toString() {
		return "TelecomShanxi1UserInfo [taskid=" + taskid + ", name=" + name + ", customerType=" + customerType
				+ ", telephone=" + telephone + ", documenType=" + documenType + ", idNum=" + idNum + ", address="
				+ address + ", postcode=" + postcode + ", email=" + email + ", sex=" + sex + ", birthdate=" + birthdate
				+ ", occupation=" + occupation + ", education=" + education + ", hobby=" + hobby + ", habit=" + habit
				+ ", familyCode=" + familyCode + ", homePhone=" + homePhone + ", createDate=" + createDate
				+ ", totalBalance=" + totalBalance + ", comboBalance=" + comboBalance + ", specialUseBalance="
				+ specialUseBalance + ", arrearsSum=" + arrearsSum + ", sumCharge=" + sumCharge + ", integral="
				+ integral + "]";
	}

	public TelecomShanxi1UserInfo(String taskid, String name, String customerType, String telephone, String documenType,
			String idNum, String address, String postcode, String email, String sex, String birthdate,
			String occupation, String education, String hobby, String habit, String familyCode, String homePhone,
			String createDate, String totalBalance, String comboBalance, String specialUseBalance, String arrearsSum,
			String sumCharge, String integral) {
		super();
		this.taskid = taskid;
		this.name = name;
		this.customerType = customerType;
		this.telephone = telephone;
		this.documenType = documenType;
		this.idNum = idNum;
		this.address = address;
		this.postcode = postcode;
		this.email = email;
		this.sex = sex;
		this.birthdate = birthdate;
		this.occupation = occupation;
		this.education = education;
		this.hobby = hobby;
		this.habit = habit;
		this.familyCode = familyCode;
		this.homePhone = homePhone;
		this.createDate = createDate;
		this.totalBalance = totalBalance;
		this.comboBalance = comboBalance;
		this.specialUseBalance = specialUseBalance;
		this.arrearsSum = arrearsSum;
		this.sumCharge = sumCharge;
		this.integral = integral;
	}

	public TelecomShanxi1UserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

}