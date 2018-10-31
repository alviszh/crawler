package com.microservice.dao.entity.crawler.insurance.hangzhou;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 * 杭州社保个人信息
 * @author zh
 *
 */
@Entity
@Table(name="insurance_hangzhou_userinfo",indexes = {@Index(name = "index_insurance_hangzhou_userinfo_taskid", columnList = "taskid")})
public class InsuranceHangzhouUserInfo extends IdEntity{
	private String taskid;//uuid 前端通过uuid访问状态结果	
	private String name;							//姓名
	private String idNum;							//公民身份证号
	private String personalNumber;                   //个人编号
	private String sex;								//性别
	private String nation;							//民族
	private String birthdate;						//出生日期
	private String workDate;						//参加工作日期
	private String personnelStatus;                 //人员状态            例：在职
	private String personnelType;                   //人员类型            例：普通人员
	private String politicalOutlook;                //政治面貌
	private String category;						//户口性质
	private String employmentForm;                  //用工形式
	private String education;						//文化程度
	private String hkadr;							//户口所在地
	private String unitNumber;                      //单位编号
	private String pensionStatus;                   //养老保险状态
	private String pensionthisTime;                 //本次参保时间
	private String pensionfirstTime;                //首次时间
	private String unemploymentStatus;                   //失业保险状态
	private String unemploymentthisTime;                 //本次参保时间
	private String unemploymentfirstTime;                //首次时间
	private String medicalStatus;                   //医疗保险状态
	private String medicalthisTime;                 //本次参保时间
	private String medicalfirstTime;                //首次时间
	private String injuryStatus;                   //工伤保险状态
	private String injurythisTime;                 //本次参保时间
	private String injuryfirstTime;                //首次时间
	private String birthStatus;                   //生育保险状态
	private String birththisTime;                 //本次参保时间
	private String birthfirstTime;                //首次时间
	@Override
	public String toString() {
		return "InsuranceHangzhouUserInfo [taskid=" + taskid + ", name=" + name + ",idNum="
				+ idNum + ", personalNumber=" + personalNumber + ", sex=" + sex + ", nation=" + nation + ", birthdate=" + birthdate
				+ ", workDate=" + workDate + ", personnelStatus=" + personnelStatus + ", personnelType=" + personnelType
				+ ", politicalOutlook=" + politicalOutlook + ", category=" + category + ", employmentForm=" + employmentForm + ", education="
				+ education + ", hkadr=" + hkadr + ", unitNumber="+ unitNumber 
				+", pensionStatus="+ pensionStatus +  ", pensionthisTime="+ pensionthisTime +", pensionfirstTime="+ pensionfirstTime 
				+", unemploymentStatus="+ unemploymentStatus +", unemploymentthisTime="+ unemploymentthisTime +",unemploymentfirstTime="+ unemploymentfirstTime
				+", medicalStatus="+ medicalStatus +", medicalthisTime="+ medicalthisTime +", medicalfirstTime="+ medicalfirstTime 
				+", injuryStatus="+ injuryStatus +", injurythisTime="+ injurythisTime +", injuryfirstTime="+ injuryfirstTime 
				+", birthStatus="+ birthStatus +", birththisTime="+birththisTime +", birthfirstTime="+ birthfirstTime +"]";
	}
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
	public String getIdNum() {
		return idNum;
	}
	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}
	public String getPersonalNumber() {
		return personalNumber;
	}
	public void setPersonalNumber(String personalNumber) {
		this.personalNumber = personalNumber;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
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
	public String getWorkDate() {
		return workDate;
	}
	public void setWorkDate(String workDate) {
		this.workDate = workDate;
	}
	public String getPersonnelStatus() {
		return personnelStatus;
	}
	public void setPersonnelStatus(String personnelStatus) {
		this.personnelStatus = personnelStatus;
	}
	public String getPersonnelType() {
		return personnelType;
	}
	public void setPersonnelType(String personnelType) {
		this.personnelType = personnelType;
	}
	public String getPoliticalOutlook() {
		return politicalOutlook;
	}
	public void setPoliticalOutlook(String politicalOutlook) {
		this.politicalOutlook = politicalOutlook;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getEmploymentForm() {
		return employmentForm;
	}
	public void setEmploymentForm(String employmentForm) {
		this.employmentForm = employmentForm;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public String getHkadr() {
		return hkadr;
	}
	public void setHkadr(String hkadr) {
		this.hkadr = hkadr;
	}
	public String getUnitNumber() {
		return unitNumber;
	}
	public void setUnitNumber(String unitNumber) {
		this.unitNumber = unitNumber;
	}
	public String getPensionStatus() {
		return pensionStatus;
	}
	public void setPensionStatus(String pensionStatus) {
		this.pensionStatus = pensionStatus;
	}
	public String getPensionthisTime() {
		return pensionthisTime;
	}
	public void setPensionthisTime(String pensionthisTime) {
		this.pensionthisTime = pensionthisTime;
	}
	public String getPensionfirstTime() {
		return pensionfirstTime;
	}
	public void setPensionfirstTime(String pensionfirstTime) {
		this.pensionfirstTime = pensionfirstTime;
	}
	public String getUnemploymentStatus() {
		return unemploymentStatus;
	}
	public void setUnemploymentStatus(String unemploymentStatus) {
		this.unemploymentStatus = unemploymentStatus;
	}
	public String getUnemploymentthisTime() {
		return unemploymentthisTime;
	}
	public void setUnemploymentthisTime(String unemploymentthisTime) {
		this.unemploymentthisTime = unemploymentthisTime;
	}
	public String getUnemploymentfirstTime() {
		return unemploymentfirstTime;
	}
	public void setUnemploymentfirstTime(String unemploymentfirstTime) {
		this.unemploymentfirstTime = unemploymentfirstTime;
	}
	public String getMedicalStatus() {
		return medicalStatus;
	}
	public void setMedicalStatus(String medicalStatus) {
		this.medicalStatus = medicalStatus;
	}
	public String getMedicalthisTime() {
		return medicalthisTime;
	}
	public void setMedicalthisTime(String medicalthisTime) {
		this.medicalthisTime = medicalthisTime;
	}
	public String getMedicalfirstTime() {
		return medicalfirstTime;
	}
	public void setMedicalfirstTime(String medicalfirstTime) {
		this.medicalfirstTime = medicalfirstTime;
	}
	public String getInjuryStatus() {
		return injuryStatus;
	}
	public void setInjuryStatus(String injuryStatus) {
		this.injuryStatus = injuryStatus;
	}
	public String getInjurythisTime() {
		return injurythisTime;
	}
	public void setInjurythisTime(String injurythisTime) {
		this.injurythisTime = injurythisTime;
	}
	public String getInjuryfirstTime() {
		return injuryfirstTime;
	}
	public void setInjuryfirstTime(String injuryfirstTime) {
		this.injuryfirstTime = injuryfirstTime;
	}
	public String getBirthStatus() {
		return birthStatus;
	}
	public void setBirthStatus(String birthStatus) {
		this.birthStatus = birthStatus;
	}
	public String getBirththisTime() {
		return birththisTime;
	}
	public void setBirththisTime(String birththisTime) {
		this.birththisTime = birththisTime;
	}
	public String getBirthfirstTime() {
		return birthfirstTime;
	}
	public void setBirthfirstTime(String birthfirstTime) {
		this.birthfirstTime = birthfirstTime;
	}
	

}
