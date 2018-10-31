package com.microservice.dao.entity.crawler.insurance.xinxiang;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_xinxiang_userinfo")
public class InsuranceXinXiangUserInfo extends IdEntity{

	private String name;//姓名
	private String personalNum;//个人编号
	private String IDNum;//身份证
	private String sex;//性别
	private String national;//民族
	private String birth;//生日
	private String companyType;//单位类型
	private String status;//单位状态
	private String jobStatus;//行业风险类型
	private String payName;//缴费单位专管员姓名
	private String payPhone;//缴费单位专管员电话
	private String joinDate;//参加工作时间
	private String companyNum;//单位编号
	private String company;//单位名称
	
	private String cardNum;//社保卡号
	private String personalPhone;//手机
	private String keeper;//监护人姓名
	private String addr;//通信地址
	private String community;//所在社区
	
	private String taskid;

	@Override
	public String toString() {
		return "InsuranceXinXiangUserInfo [name=" + name + ", personalNum=" + personalNum + ", IDNum=" + IDNum
				+ ", sex=" + sex + ", national=" + national + ", birth=" + birth + ", companyType=" + companyType
				+ ", status=" + status + ", jobStatus=" + jobStatus + ", payName=" + payName + ", payPhone=" + payPhone
				+ ", joinDate=" + joinDate + ", companyNum=" + companyNum + ", company=" + company + ", cardNum="
				+ cardNum + ", personalPhone=" + personalPhone + ", keeper=" + keeper + ", addr=" + addr
				+ ", community=" + community + ", taskid=" + taskid + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPersonalNum() {
		return personalNum;
	}

	public void setPersonalNum(String personalNum) {
		this.personalNum = personalNum;
	}

	public String getIDNum() {
		return IDNum;
	}

	public void setIDNum(String iDNum) {
		IDNum = iDNum;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getNational() {
		return national;
	}

	public void setNational(String national) {
		this.national = national;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getCompanyType() {
		return companyType;
	}

	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}

	public String getPayName() {
		return payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}

	public String getPayPhone() {
		return payPhone;
	}

	public void setPayPhone(String payPhone) {
		this.payPhone = payPhone;
	}

	public String getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}

	public String getCompanyNum() {
		return companyNum;
	}

	public void setCompanyNum(String companyNum) {
		this.companyNum = companyNum;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	public String getPersonalPhone() {
		return personalPhone;
	}

	public void setPersonalPhone(String personalPhone) {
		this.personalPhone = personalPhone;
	}

	public String getKeeper() {
		return keeper;
	}

	public void setKeeper(String keeper) {
		this.keeper = keeper;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getCommunity() {
		return community;
	}

	public void setCommunity(String community) {
		this.community = community;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
}
