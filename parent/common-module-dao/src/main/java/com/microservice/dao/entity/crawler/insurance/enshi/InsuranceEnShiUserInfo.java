package com.microservice.dao.entity.crawler.insurance.enshi;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_enshi_userinfo",indexes = {@Index(name = "index_insurance_enshi_userinfo_taskid", columnList = "taskid")})
public class InsuranceEnShiUserInfo extends IdEntity{

	private String name;//姓名
	private String sex;//性别
	private String birth;//出生日期
	private String personal;//户口性质
	private String national;//民族
	private String level;//文化程度
	private String status;//人员状态
	private String IDNum;//身份证号
	private String joinDate;//参加工作日期
	private String personalNews;//个人身份
	private String phone;//联系电话
	private String company;//工作单位
	private String companyLevel;//行政职务
	private String yongGong;//用工形式
	private String endDate;//离退休日期
	private String professonal;//专业技术职务
	private String worker;//工人技术等级
	private String special;//特殊工种标识
	private String highMoney;//医保本年度基本医疗统筹支付最高限额
	private String houseLand;//居住地
	private String homeLand;//户口所在地
	private String cb;//参保所在地
	private String sbjg;//社保机构
	private String taskid;
	@Override
	public String toString() {
		return "InsuranceEnShiUserInfo [name=" + name + ", sex=" + sex + ", birth=" + birth + ", personal=" + personal
				+ ", national=" + national + ", level=" + level + ", status=" + status + ", IDNum=" + IDNum
				+ ", joinDate=" + joinDate + ", personalNews=" + personalNews + ", phone=" + phone + ", company="
				+ company + ", companyLevel=" + companyLevel + ", yongGong=" + yongGong + ", endDate=" + endDate
				+ ", professonal=" + professonal + ", worker=" + worker + ", special=" + special + ", highMoney="
				+ highMoney + ", houseLand=" + houseLand + ", homeLand=" + homeLand + ", cb=" + cb + ", sbjg=" + sbjg
				+ "]";
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	public String getPersonal() {
		return personal;
	}
	public void setPersonal(String personal) {
		this.personal = personal;
	}
	public String getNational() {
		return national;
	}
	public void setNational(String national) {
		this.national = national;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getIDNum() {
		return IDNum;
	}
	public void setIDNum(String iDNum) {
		IDNum = iDNum;
	}
	public String getJoinDate() {
		return joinDate;
	}
	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}
	public String getPersonalNews() {
		return personalNews;
	}
	public void setPersonalNews(String personalNews) {
		this.personalNews = personalNews;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getCompanyLevel() {
		return companyLevel;
	}
	public void setCompanyLevel(String companyLevel) {
		this.companyLevel = companyLevel;
	}
	public String getYongGong() {
		return yongGong;
	}
	public void setYongGong(String yongGong) {
		this.yongGong = yongGong;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getProfessonal() {
		return professonal;
	}
	public void setProfessonal(String professonal) {
		this.professonal = professonal;
	}
	public String getWorker() {
		return worker;
	}
	public void setWorker(String worker) {
		this.worker = worker;
	}
	public String getSpecial() {
		return special;
	}
	public void setSpecial(String special) {
		this.special = special;
	}
	public String getHighMoney() {
		return highMoney;
	}
	public void setHighMoney(String highMoney) {
		this.highMoney = highMoney;
	}
	public String getHouseLand() {
		return houseLand;
	}
	public void setHouseLand(String houseLand) {
		this.houseLand = houseLand;
	}
	public String getHomeLand() {
		return homeLand;
	}
	public void setHomeLand(String homeLand) {
		this.homeLand = homeLand;
	}
	public String getCb() {
		return cb;
	}
	public void setCb(String cb) {
		this.cb = cb;
	}
	public String getSbjg() {
		return sbjg;
	}
	public void setSbjg(String sbjg) {
		this.sbjg = sbjg;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
	
	
}
