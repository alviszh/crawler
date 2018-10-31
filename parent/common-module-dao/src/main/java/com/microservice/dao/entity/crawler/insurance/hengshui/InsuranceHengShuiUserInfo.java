package com.microservice.dao.entity.crawler.insurance.hengshui;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "insurance_hengshui_userinfo",indexes = {@Index(name = "index_insurance_hengshui_userinfo_taskid", columnList = "taskid")})
public class InsuranceHengShuiUserInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = 5463733733681486246L;
	private String taskid;
//	个人参保编号
	private String perinsurnum;
//	身份证号
	private String idnum;
//	姓名
	private String name;
//	性别
	private String gender;
//	民族
	private String nation;
//	灵活就业标志
	private String flexibleworkmark;
//	出生日期
	private String birthday;
//	人员类别
	private String percategory;
//	参加工作日期
	private String joinworkdate;
//	用工形式
	private String workingform;
//	联系电话
	private String contactnum;
//	电子邮箱
	private String email;
//	邮政编码
	private String postalcode;
//	传真
	private String fax;
	/////////////////////////////////////
//	社保卡号
	private String insurnum;
//	社保卡有效期
	private String insurexpirydate;
//	公司名称
	private String unitname;
//	移动电话
	private String mobilenum;
//	地址
	private String address;
//	归属地行政区代码 (并没有爬取这个字段，因为从地址中就可以知道，且枚举类众多，很繁琐)
	/////////////////////////////////////
	
	public InsuranceHengShuiUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getPerinsurnum() {
		return perinsurnum;
	}

	public void setPerinsurnum(String perinsurnum) {
		this.perinsurnum = perinsurnum;
	}

	public String getIdnum() {
		return idnum;
	}

	public void setIdnum(String idnum) {
		this.idnum = idnum;
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

	public String getFlexibleworkmark() {
		return flexibleworkmark;
	}

	public void setFlexibleworkmark(String flexibleworkmark) {
		this.flexibleworkmark = flexibleworkmark;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getPercategory() {
		return percategory;
	}

	public void setPercategory(String percategory) {
		this.percategory = percategory;
	}

	public String getJoinworkdate() {
		return joinworkdate;
	}

	public void setJoinworkdate(String joinworkdate) {
		this.joinworkdate = joinworkdate;
	}

	public String getWorkingform() {
		return workingform;
	}

	public void setWorkingform(String workingform) {
		this.workingform = workingform;
	}

	public String getContactnum() {
		return contactnum;
	}

	public void setContactnum(String contactnum) {
		this.contactnum = contactnum;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPostalcode() {
		return postalcode;
	}

	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getInsurnum() {
		return insurnum;
	}

	public void setInsurnum(String insurnum) {
		this.insurnum = insurnum;
	}

	public String getInsurexpirydate() {
		return insurexpirydate;
	}

	public void setInsurexpirydate(String insurexpirydate) {
		this.insurexpirydate = insurexpirydate;
	}

	public String getUnitname() {
		return unitname;
	}

	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}

	public String getMobilenum() {
		return mobilenum;
	}

	public void setMobilenum(String mobilenum) {
		this.mobilenum = mobilenum;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
}
