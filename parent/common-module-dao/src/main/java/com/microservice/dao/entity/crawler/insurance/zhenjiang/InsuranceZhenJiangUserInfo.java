package com.microservice.dao.entity.crawler.insurance.zhenjiang;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 爬取个人信息时，响应回来的数据要比页面上显示的多
 * 
 * @author sln
 *
 */
@Entity
@Table(name = "insurance_zhenjiang_userinfo",indexes = {@Index(name = "index_insurance_zhenjiang_userinfo_taskid", columnList = "taskid")})
public class InsuranceZhenJiangUserInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = 5463733733681486246L;
	private String taskid;
//	姓名
	private String name;
//	性别
	private String gender;
//	出身日期
	private String birthday;
//	国籍
	private String nationality;
//	民族
	private String nation;
//	电话
	private String phonenum;
//	家庭住址
	private String homeaddress;
//	社会保障卡号
	private String insurcardnum;
//	人员识别号
	private String identificationnum; 
//	发卡日期
	private String cardservicedate;
//	身份证号
	private String idnum;
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
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getPhonenum() {
		return phonenum;
	}
	public void setPhonenum(String phonenum) {
		this.phonenum = phonenum;
	}
	public String getHomeaddress() {
		return homeaddress;
	}
	public void setHomeaddress(String homeaddress) {
		this.homeaddress = homeaddress;
	}
	public String getInsurcardnum() {
		return insurcardnum;
	}
	public void setInsurcardnum(String insurcardnum) {
		this.insurcardnum = insurcardnum;
	}
	public String getIdentificationnum() {
		return identificationnum;
	}
	public void setIdentificationnum(String identificationnum) {
		this.identificationnum = identificationnum;
	}
	public String getCardservicedate() {
		return cardservicedate;
	}
	public void setCardservicedate(String cardservicedate) {
		this.cardservicedate = cardservicedate;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public InsuranceZhenJiangUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public InsuranceZhenJiangUserInfo(String taskid, String name, String gender, String birthday, String nationality,
			String nation, String phonenum, String homeaddress, String insurcardnum, String identificationnum,
			String cardservicedate, String idnum) {
		super();
		this.taskid = taskid;
		this.name = name;
		this.gender = gender;
		this.birthday = birthday;
		this.nationality = nationality;
		this.nation = nation;
		this.phonenum = phonenum;
		this.homeaddress = homeaddress;
		this.insurcardnum = insurcardnum;
		this.identificationnum = identificationnum;
		this.cardservicedate = cardservicedate;
		this.idnum = idnum;
	}
	
}
