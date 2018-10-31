package com.microservice.dao.entity.crawler.insurance.sz.hunan;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_sz_hunan_userinfo")
public class InsuranceSZHunanUserInfo extends IdEntity{

	private String idNum;							//证件号码
	private String name;							//姓名
	private String gender;							//性别
	private String nation;							//民族
	private String birthday;						//出生日期
	private String residenceArea;					//户口所在地址
	private String cardNum;							//封面卡号
	private String cardStartDate;					//发卡日期
	private String cardStatus;						//卡状态
	private String cardExpiry;						//卡有效期限
	private String organizationName;				//社会保险经办机构编码
	private String bankNum;							//银行编号
	private String contactTel;						//联系电话
	private String taskid;
	public String getIdNum() {
		return idNum;
	}
	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getResidenceArea() {
		return residenceArea;
	}
	public void setResidenceArea(String residenceArea) {
		this.residenceArea = residenceArea;
	}
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public String getCardStartDate() {
		return cardStartDate;
	}
	public void setCardStartDate(String cardStartDate) {
		this.cardStartDate = cardStartDate;
	}
	public String getCardStatus() {
		return cardStatus;
	}
	public void setCardStatus(String cardStatus) {
		this.cardStatus = cardStatus;
	}
	public String getCardExpiry() {
		return cardExpiry;
	}
	public void setCardExpiry(String cardExpiry) {
		this.cardExpiry = cardExpiry;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	public String getBankNum() {
		return bankNum;
	}
	public void setBankNum(String bankNum) {
		this.bankNum = bankNum;
	}
	public String getContactTel() {
		return contactTel;
	}
	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Override
	public String toString() {
		return "InsuranceSZHunanUserInfo [idNum=" + idNum + ", name=" + name + ", gender=" + gender + ", nation="
				+ nation + ", birthday=" + birthday + ", residenceArea=" + residenceArea + ", cardNum=" + cardNum
				+ ", cardStartDate=" + cardStartDate + ", cardStatus=" + cardStatus + ", cardExpiry=" + cardExpiry
				+ ", organizationName=" + organizationName + ", bankNum=" + bankNum + ", contactTel=" + contactTel
				+ ", taskid=" + taskid + "]";
	}
	
}
