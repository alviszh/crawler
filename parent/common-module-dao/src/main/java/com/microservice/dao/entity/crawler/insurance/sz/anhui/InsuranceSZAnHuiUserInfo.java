package com.microservice.dao.entity.crawler.insurance.sz.anhui;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_sz_anhui_userinfo")
public class InsuranceSZAnHuiUserInfo extends IdEntity{
	
	private String citynum;	//城市代码
	private String cardnum;	//社会保障卡卡号
	private String idnum;	//社会保障号码
	private String type;	//证件类型
	private String validdate;	//证件有效期
	private String username;	//姓名
	private String gender;	//性别
	private String nation;	//民族
	private String birthdate;	//出生日期
	private String firstdate;	//人员状态
	private String householdType;	//户口性质
	private String householdAddress;	//户口所在地址
	private String mobile;	//联系手机
	private String telphone;	//联系电话
	private String address;	//通讯地址
	private String postalcode;	//邮政编码
	private String mailaddress;	//邮箱地址
	private String usernum;	//个人编号
	private String applycardcompanynum;//	申请制卡单位编号
	private String applycardcompanyname;//	申请制卡单位名称
	private String taskid;
	public String getCitynum() {
		return citynum;
	}
	public void setCitynum(String citynum) {
		this.citynum = citynum;
	}
	public String getCardnum() {
		return cardnum;
	}
	public void setCardnum(String cardnum) {
		this.cardnum = cardnum;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValiddate() {
		return validdate;
	}
	public void setValiddate(String validdate) {
		this.validdate = validdate;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
	public String getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}
	public String getFirstdate() {
		return firstdate;
	}
	public void setFirstdate(String firstdate) {
		this.firstdate = firstdate;
	}
	public String getHouseholdType() {
		return householdType;
	}
	public void setHouseholdType(String householdType) {
		this.householdType = householdType;
	}
	public String getHouseholdAddress() {
		return householdAddress;
	}
	public void setHouseholdAddress(String householdAddress) {
		this.householdAddress = householdAddress;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getTelphone() {
		return telphone;
	}
	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPostalcode() {
		return postalcode;
	}
	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}
	public String getMailaddress() {
		return mailaddress;
	}
	public void setMailaddress(String mailaddress) {
		this.mailaddress = mailaddress;
	}
	public String getUsernum() {
		return usernum;
	}
	public void setUsernum(String usernum) {
		this.usernum = usernum;
	}
	public String getApplycardcompanynum() {
		return applycardcompanynum;
	}
	public void setApplycardcompanynum(String applycardcompanynum) {
		this.applycardcompanynum = applycardcompanynum;
	}
	public String getApplycardcompanyname() {
		return applycardcompanyname;
	}
	public void setApplycardcompanyname(String applycardcompanyname) {
		this.applycardcompanyname = applycardcompanyname;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public InsuranceSZAnHuiUserInfo(String citynum, String cardnum, String idnum, String type, String validdate,
			String username, String gender, String nation, String birthdate, String firstdate, String householdType,
			String householdAddress, String mobile, String telphone, String address, String postalcode,
			String mailaddress, String usernum, String applycardcompanynum, String applycardcompanyname,
			String taskid) {
		super();
		this.citynum = citynum;
		this.cardnum = cardnum;
		this.idnum = idnum;
		this.type = type;
		this.validdate = validdate;
		this.username = username;
		this.gender = gender;
		this.nation = nation;
		this.birthdate = birthdate;
		this.firstdate = firstdate;
		this.householdType = householdType;
		this.householdAddress = householdAddress;
		this.mobile = mobile;
		this.telphone = telphone;
		this.address = address;
		this.postalcode = postalcode;
		this.mailaddress = mailaddress;
		this.usernum = usernum;
		this.applycardcompanynum = applycardcompanynum;
		this.applycardcompanyname = applycardcompanyname;
		this.taskid = taskid;
	}	
	
}
