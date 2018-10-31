package com.microservice.dao.entity.crawler.insurance.anqing;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 * 安庆社保个人信息
 * @author zcx
 *
 */
@Entity
@Table(name="insurance_anqing_userinfo")
public class InsuranceAnQingUserInfo extends IdEntity {
	private String usernum; // 个人编号
	private String idnum; //公民身份证号码
	private String username; // 姓名
	private String gender; // 性别
	private String nation; // 民族
	private String birthdate; // 出生日期
	private String firstworddate; // 参加工作日期
	private String state; // 人员状态
	private String household;//户口性质
	
	private String householdplace;//户口所在地
	private String education;//文化程度
	private String personalidentity;//个人身份
	private String workstate;//用工形式
	private String technicalposition;//专业技术职务
	private String technicalgrde;//工人技术等级
	private String contactnum;//联系电话
	private String specicalworksign;//特殊工种标识
	private String administjob;//行政职务
	private String farmersign;//农民工标识
	private String taskid; // 任务ID
	public String getUsernum() {
		return usernum;
	}
	public void setUsernum(String usernum) {
		this.usernum = usernum;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
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
	public String getFirstworddate() {
		return firstworddate;
	}
	public void setFirstworddate(String firstworddate) {
		this.firstworddate = firstworddate;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getHousehold() {
		return household;
	}
	public void setHousehold(String household) {
		this.household = household;
	}
	public String getHouseholdplace() {
		return householdplace;
	}
	public void setHouseholdplace(String householdplace) {
		this.householdplace = householdplace;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public String getPersonalidentity() {
		return personalidentity;
	}
	public void setPersonalidentity(String personalidentity) {
		this.personalidentity = personalidentity;
	}
	public String getWorkstate() {
		return workstate;
	}
	public void setWorkstate(String workstate) {
		this.workstate = workstate;
	}
	public String getTechnicalposition() {
		return technicalposition;
	}
	public void setTechnicalposition(String technicalposition) {
		this.technicalposition = technicalposition;
	}
	public String getTechnicalgrde() {
		return technicalgrde;
	}
	public void setTechnicalgrde(String technicalgrde) {
		this.technicalgrde = technicalgrde;
	}
	public String getContactnum() {
		return contactnum;
	}
	public void setContactnum(String contactnum) {
		this.contactnum = contactnum;
	}
	public String getSpecicalworksign() {
		return specicalworksign;
	}
	public void setSpecicalworksign(String specicalworksign) {
		this.specicalworksign = specicalworksign;
	}
	public String getAdministjob() {
		return administjob;
	}
	public void setAdministjob(String administjob) {
		this.administjob = administjob;
	}
	public String getFarmersign() {
		return farmersign;
	}
	public void setFarmersign(String farmersign) {
		this.farmersign = farmersign;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	@Override
	public String toString() {
		return "InsuranceAnQingUserInfo [usernum=" + usernum + ", idnum=" + idnum + ", username=" + username
				+ ", gender=" + gender + ", nation=" + nation + ", birthdate=" + birthdate + ", firstworddate="
				+ firstworddate + ", state=" + state + ", household=" + household + ", householdplace=" + householdplace
				+ ", education=" + education + ", personalidentity=" + personalidentity + ", workstate=" + workstate
				+ ", technicalposition=" + technicalposition + ", technicalgrde=" + technicalgrde + ", contactnum="
				+ contactnum + ", specicalworksign=" + specicalworksign + ", administjob=" + administjob
				+ ", farmersign=" + farmersign + ", taskid=" + taskid + "]";
	}
	public InsuranceAnQingUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public InsuranceAnQingUserInfo(String usernum, String idnum, String username, String gender, String nation,
			String birthdate, String firstworddate, String state, String household, String householdplace,
			String education, String personalidentity, String workstate, String technicalposition, String technicalgrde,
			String contactnum, String specicalworksign, String administjob, String farmersign, String taskid) {
		super();
		this.usernum = usernum;
		this.idnum = idnum;
		this.username = username;
		this.gender = gender;
		this.nation = nation;
		this.birthdate = birthdate;
		this.firstworddate = firstworddate;
		this.state = state;
		this.household = household;
		this.householdplace = householdplace;
		this.education = education;
		this.personalidentity = personalidentity;
		this.workstate = workstate;
		this.technicalposition = technicalposition;
		this.technicalgrde = technicalgrde;
		this.contactnum = contactnum;
		this.specicalworksign = specicalworksign;
		this.administjob = administjob;
		this.farmersign = farmersign;
		this.taskid = taskid;
	}
}
