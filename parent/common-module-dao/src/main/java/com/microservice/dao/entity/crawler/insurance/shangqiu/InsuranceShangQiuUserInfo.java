package com.microservice.dao.entity.crawler.insurance.shangqiu;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description:   个人信息，此实体存储网页中的在职职工基本养老保险个人权益单信息，以及申报通讯地址信息
 * @author: sln 
 * @date: 2018年1月9日 下午2:41:35 
 */
@Entity
@Table(name = "insurance_shangqiu_userinfo",indexes = {@Index(name = "index_insurance_shangqiu_userinfo_taskid", columnList = "taskid")})
public class InsuranceShangQiuUserInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = -6068940611738508045L;
	private String taskid;
//	单位编号
	private String unitnum;
//	单位名称
	private String unitname;
//	姓名
	private String name;
//	性别
	private String gender;
//	民族
	private String nation;
//	出生日期
	private String birthday;
//	个人编号
	private String pernum;
//	身份证号码
	private String idnum;
//	参加工作时间
	private String joinworkdate;
//	个人缴费时间
	private String perchargedate;
//	建账户时间
	private String buildaccountdate;
//	参保状态
	private String insurstate;
//	账户月数
	private String accountmonths;
//	账户本息合计
	private String principalandinterest;
//	单位欠费本金
	private String unitarrears;
//	个人欠费本金
	private String perarrears;
//	欠费月数
	private String arrearsmonths;
//	欠费本金合计
	private String totalarrears;
//	通讯地址
	private String postaladdress;
//	邮政编码
	private String postalcode;
//	联系电话
	private String contactnum;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getUnitnum() {
		return unitnum;
	}
	public void setUnitnum(String unitnum) {
		this.unitnum = unitnum;
	}
	public String getUnitname() {
		return unitname;
	}
	public void setUnitname(String unitname) {
		this.unitname = unitname;
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
	public String getPernum() {
		return pernum;
	}
	public void setPernum(String pernum) {
		this.pernum = pernum;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getJoinworkdate() {
		return joinworkdate;
	}
	public void setJoinworkdate(String joinworkdate) {
		this.joinworkdate = joinworkdate;
	}
	public String getPerchargedate() {
		return perchargedate;
	}
	public void setPerchargedate(String perchargedate) {
		this.perchargedate = perchargedate;
	}
	public String getBuildaccountdate() {
		return buildaccountdate;
	}
	public void setBuildaccountdate(String buildaccountdate) {
		this.buildaccountdate = buildaccountdate;
	}
	public String getInsurstate() {
		return insurstate;
	}
	public void setInsurstate(String insurstate) {
		this.insurstate = insurstate;
	}
	public String getAccountmonths() {
		return accountmonths;
	}
	public void setAccountmonths(String accountmonths) {
		this.accountmonths = accountmonths;
	}
	public String getPrincipalandinterest() {
		return principalandinterest;
	}
	public void setPrincipalandinterest(String principalandinterest) {
		this.principalandinterest = principalandinterest;
	}
	public String getUnitarrears() {
		return unitarrears;
	}
	public void setUnitarrears(String unitarrears) {
		this.unitarrears = unitarrears;
	}
	public String getPerarrears() {
		return perarrears;
	}
	public void setPerarrears(String perarrears) {
		this.perarrears = perarrears;
	}
	public String getArrearsmonths() {
		return arrearsmonths;
	}
	public void setArrearsmonths(String arrearsmonths) {
		this.arrearsmonths = arrearsmonths;
	}
	public String getTotalarrears() {
		return totalarrears;
	}
	public void setTotalarrears(String totalarrears) {
		this.totalarrears = totalarrears;
	}
	public String getPostaladdress() {
		return postaladdress;
	}
	public void setPostaladdress(String postaladdress) {
		this.postaladdress = postaladdress;
	}
	public String getPostalcode() {
		return postalcode;
	}
	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}
	public String getContactnum() {
		return contactnum;
	}
	public void setContactnum(String contactnum) {
		this.contactnum = contactnum;
	}
	public InsuranceShangQiuUserInfo() {
		super();
	}
}
