package com.microservice.dao.entity.crawler.housing.luzhou;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_luzhou_userinfo",indexes = {@Index(name = "index_housing_luzhou_userinfo_taskid", columnList = "taskid")})
public class HousingLuZhouUserInfo extends IdEntity implements Serializable{
	private String personalAccount;  //个人帐号
	private String name;             //姓名
	private String idCard;           //证件号码
	private String sex;              //性别
	private String birth;            //出生年月
	private String fee;              //个人账户余额
	private String base;             //缴存基数
	private String companyAmount;    //单位月缴存额
	private String personalAmount;   //个人月缴存额
	private String state;            //个人账户状态
	private String date;             //开户日期
	private String phone;            //电话号码
	private String company;          //单位名称
	
	private String taskid;
	@Override
	public String toString() {
		return "HousingLuZhouUserInfo [personalAccount=" + personalAccount + ", name=" + name + ", idCard=" + idCard
				+",sex=" + sex + ", birth=" + birth + ", fee=" + fee
				+",base=" + base + ", companyAmount=" + companyAmount + ", personalAmount=" + personalAmount
				+",state=" + state + ", date=" + date + ", phone=" + phone
				+",company=" + company + ", taskid=" + taskid + "]";
	}
	public String getPersonalAccount() {
		return personalAccount;
	}
	public void setPersonalAccount(String personalAccount) {
		this.personalAccount = personalAccount;
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
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getBase() {
		return base;
	}
	public void setBase(String base) {
		this.base = base;
	}
	public String getCompanyAmount() {
		return companyAmount;
	}
	public void setCompanyAmount(String companyAmount) {
		this.companyAmount = companyAmount;
	}
	public String getPersonalAmount() {
		return personalAmount;
	}
	public void setPersonalAmount(String personalAmount) {
		this.personalAmount = personalAmount;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
}
