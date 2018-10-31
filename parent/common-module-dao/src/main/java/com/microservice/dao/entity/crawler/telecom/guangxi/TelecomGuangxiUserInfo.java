package com.microservice.dao.entity.crawler.telecom.guangxi;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="telecomGuangxi_userinfo",indexes = {@Index(name = "index_telecomGuangxi_userinfo_taskid", columnList = "taskid")}) 
public class TelecomGuangxiUserInfo extends IdEntity{

	private String ContactAdderss;//联系人地址
	
	private String WeiBo;//微博
	
	private String HomePhone;//家庭电话
	
	private String PhoneNo;//联系电话
	
	private String ContactName;//联系人
	
	private String ContactEmployer;//联系单位
	
	private String OfficePhone;//办公电话
	
	private String Email;//联系邮箱
	
	private String PostCode;//邮编
	
	private String EasyLetter;//易信
	
	private String QQNo;//QQ号码
	
	private String MicroLetter;//微信
	
	private String fax;//传真
	
	private String PontactAdderss;//通信地址

	private String balance;//余额
	private String star;//星级
	
	private String taskid;
	@Override
	public String toString() {
		return "TelecomGuangxiUserInfo [ContactAdderss=" + ContactAdderss + ", WeiBo=" + WeiBo + ", HomePhone="
				+ HomePhone + ", PhoneNo=" + PhoneNo + ", ContactName=" + ContactName + ", ContactEmployer="
				+ ContactEmployer + ", OfficePhone=" + OfficePhone + ", Email=" + Email + ", PostCode=" + PostCode
				+ ", EasyLetter=" + EasyLetter + ", QQNo=" + QQNo + ", MicroLetter=" + MicroLetter + ", fax=" + fax
				+ ", PontactAdderss=" + PontactAdderss + ", balance=" + balance + ", star=" + star + "]";
	}
	public String getContactAdderss() {
		return ContactAdderss;
	}
	public void setContactAdderss(String contactAdderss) {
		ContactAdderss = contactAdderss;
	}
	public String getWeiBo() {
		return WeiBo;
	}
	public void setWeiBo(String weiBo) {
		WeiBo = weiBo;
	}
	public String getHomePhone() {
		return HomePhone;
	}
	public void setHomePhone(String homePhone) {
		HomePhone = homePhone;
	}
	public String getPhoneNo() {
		return PhoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		PhoneNo = phoneNo;
	}
	public String getContactName() {
		return ContactName;
	}
	public void setContactName(String contactName) {
		ContactName = contactName;
	}
	public String getContactEmployer() {
		return ContactEmployer;
	}
	public void setContactEmployer(String contactEmployer) {
		ContactEmployer = contactEmployer;
	}
	public String getOfficePhone() {
		return OfficePhone;
	}
	public void setOfficePhone(String officePhone) {
		OfficePhone = officePhone;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public String getPostCode() {
		return PostCode;
	}
	public void setPostCode(String postCode) {
		PostCode = postCode;
	}
	public String getEasyLetter() {
		return EasyLetter;
	}
	public void setEasyLetter(String easyLetter) {
		EasyLetter = easyLetter;
	}
	public String getQQNo() {
		return QQNo;
	}
	public void setQQNo(String qQNo) {
		QQNo = qQNo;
	}
	public String getMicroLetter() {
		return MicroLetter;
	}
	public void setMicroLetter(String microLetter) {
		MicroLetter = microLetter;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getPontactAdderss() {
		return PontactAdderss;
	}
	public void setPontactAdderss(String pontactAdderss) {
		PontactAdderss = pontactAdderss;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getStar() {
		return star;
	}
	public void setStar(String star) {
		this.star = star;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
	
	
}
