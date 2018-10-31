package com.microservice.dao.entity.crawler.insurance.rizhao;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "insurance_rizhao_userinfo",indexes = {@Index(name = "index_insurance_rizhao_userinfo_taskid", columnList = "taskid")})
public class InsuranceRiZhaoUserInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = 5463733733681486246L;
	private String taskid;
//	个人编号
	private String pernum;
//	姓名
	private String name;
//	单位名称
	private String unitname;
//	证件类型
	private String idtype;
//	公民身份证号码
	private String idnum;
//	性别
	private String gender;
//	民族
	private String nation;
//	出生日期
	private String birthday;
//	参加工作日期
	private String joinworkdate;
//	户口性质
	private String householdtype;
//	政治面貌
	private String politicalstatus;
//	婚姻状况
	private String marriage;
//	健康状况
	private String health;
//	户口详细地址
	private String housedetailaddr;
//	文化程度
	private String educationdegree;
//	个人身份
	private String peridentity;
//	用工形式
	private String workingform;
//	职业资格等级
	private String professionalgrade;
//	医疗人员类别
	private String medicalpersoncategory;
//	干部标志   （code返回中无相关含义）
	private String cadremark;
//	公务员标志
	private String civilservantmark;
//	行政职务
	private String administrativepost;
//	劳模标志
	private String modelworkermark;
//	农民工标志
	private String migrantworkermark;
//	军转人员标志
	private String militarypersonnelmark;
//	联系人
	private String linkman;
//	联系电话
	private String contactnum;
//	邮政编码
	private String postalcode;
//	电子邮箱
	private String email;
//	常住地地址
	private String liveaddress;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getPernum() {
		return pernum;
	}
	public void setPernum(String pernum) {
		this.pernum = pernum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUnitname() {
		return unitname;
	}
	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}
	public String getIdtype() {
		return idtype;
	}
	public void setIdtype(String idtype) {
		this.idtype = idtype;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
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
	public String getJoinworkdate() {
		return joinworkdate;
	}
	public void setJoinworkdate(String joinworkdate) {
		this.joinworkdate = joinworkdate;
	}
	public String getHouseholdtype() {
		return householdtype;
	}
	public void setHouseholdtype(String householdtype) {
		this.householdtype = householdtype;
	}
	public String getPoliticalstatus() {
		return politicalstatus;
	}
	public void setPoliticalstatus(String politicalstatus) {
		this.politicalstatus = politicalstatus;
	}
	public String getMarriage() {
		return marriage;
	}
	public void setMarriage(String marriage) {
		this.marriage = marriage;
	}
	public String getHealth() {
		return health;
	}
	public void setHealth(String health) {
		this.health = health;
	}
	public String getHousedetailaddr() {
		return housedetailaddr;
	}
	public void setHousedetailaddr(String housedetailaddr) {
		this.housedetailaddr = housedetailaddr;
	}
	public String getEducationdegree() {
		return educationdegree;
	}
	public void setEducationdegree(String educationdegree) {
		this.educationdegree = educationdegree;
	}
	public String getPeridentity() {
		return peridentity;
	}
	public void setPeridentity(String peridentity) {
		this.peridentity = peridentity;
	}
	public String getWorkingform() {
		return workingform;
	}
	public void setWorkingform(String workingform) {
		this.workingform = workingform;
	}
	public String getProfessionalgrade() {
		return professionalgrade;
	}
	public void setProfessionalgrade(String professionalgrade) {
		this.professionalgrade = professionalgrade;
	}
	public String getMedicalpersoncategory() {
		return medicalpersoncategory;
	}
	public void setMedicalpersoncategory(String medicalpersoncategory) {
		this.medicalpersoncategory = medicalpersoncategory;
	}
	public String getCadremark() {
		return cadremark;
	}
	public void setCadremark(String cadremark) {
		this.cadremark = cadremark;
	}
	public String getCivilservantmark() {
		return civilservantmark;
	}
	public void setCivilservantmark(String civilservantmark) {
		this.civilservantmark = civilservantmark;
	}
	public String getAdministrativepost() {
		return administrativepost;
	}
	public void setAdministrativepost(String administrativepost) {
		this.administrativepost = administrativepost;
	}
	public String getModelworkermark() {
		return modelworkermark;
	}
	public void setModelworkermark(String modelworkermark) {
		this.modelworkermark = modelworkermark;
	}
	public String getMigrantworkermark() {
		return migrantworkermark;
	}
	public void setMigrantworkermark(String migrantworkermark) {
		this.migrantworkermark = migrantworkermark;
	}
	public String getMilitarypersonnelmark() {
		return militarypersonnelmark;
	}
	public void setMilitarypersonnelmark(String militarypersonnelmark) {
		this.militarypersonnelmark = militarypersonnelmark;
	}
	public String getLinkman() {
		return linkman;
	}
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	public String getContactnum() {
		return contactnum;
	}
	public void setContactnum(String contactnum) {
		this.contactnum = contactnum;
	}
	public String getPostalcode() {
		return postalcode;
	}
	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLiveaddress() {
		return liveaddress;
	}
	public void setLiveaddress(String liveaddress) {
		this.liveaddress = liveaddress;
	}
	public InsuranceRiZhaoUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
