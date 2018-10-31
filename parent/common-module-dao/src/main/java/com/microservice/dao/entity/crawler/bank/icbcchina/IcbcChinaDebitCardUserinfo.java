package com.microservice.dao.entity.crawler.bank.icbcchina;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="icbcchina_debitcard_userinfo" ,indexes = {@Index(name = "index_icbcchina_debitcard_userinfo_taskid", columnList = "taskid")})
public class IcbcChinaDebitCardUserinfo extends IdEntity{

	private String taskid;//uuid 前端通过uuid访问状态结果
	private String name;					//姓名
	private String level;					//客户级别
	private String gender;					//性别
	private String nation;					//民族
	private String nationality;				//国籍
	private String birthday;				//出生日期
	private String profession;				//职业
	private String industry;				//行业
	private String ranks;					//职称
	private String workUnit;				//工作单位
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
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
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
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public String getRanks() {
		return ranks;
	}
	public void setRanks(String ranks) {
		this.ranks = ranks;
	}
	public String getWorkUnit() {
		return workUnit;
	}
	public void setWorkUnit(String workUnit) {
		this.workUnit = workUnit;
	}
	@Override
	public String toString() {
		return "IcbcChinaDebitcardUserinfo [taskid=" + taskid + ", name=" + name + ", level=" + level + ", gender="
				+ gender + ", nation=" + nation + ", nationality=" + nationality + ", birthday=" + birthday
				+ ", profession=" + profession + ", industry=" + industry + ", ranks=" + ranks + ", workUnit="
				+ workUnit + "]";
	}
	
	
	
}
