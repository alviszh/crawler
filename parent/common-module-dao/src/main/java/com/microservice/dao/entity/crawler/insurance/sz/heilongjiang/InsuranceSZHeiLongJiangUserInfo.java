package com.microservice.dao.entity.crawler.insurance.sz.heilongjiang;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_sz_heilongjiang_userinfo",indexes = {@Index(name = "index_insurance_sz_heilongjiang_userinfo_taskid", columnList = "taskid")}) 
public class InsuranceSZHeiLongJiangUserInfo extends IdEntity{

	private String taskid;
	
	private String companyNum;//单位编号
	
	private String company;//单位名称
	
	private String personalNum;//个人编号
	
	private String name;//姓名
	private String idCard;//公民身份号码
	
	private String sex;//性别
	
	private String national;//民族
	
	private String birth;//出生日期
	
	private String birthday;//档案出生日期
	
	private String culture;//文化程度
	
	private String marry;//婚姻状况
	
	private String personal;//个人身份
	
	private String joinDate;//参加工作日期
	
	private String houseHold;//户口性质
	
	private String insuranceStatus;//社会保险状态
	
	private String workStatus;//就业状态
	
	private String profession;//专业技术职务
	
	private String useWork;//用工形式
	
	private String special;//特殊工种
	
	private String changeFloor;//转业前级别
	
	private String administration;//行政级别
	
	private String phone;//联系电话
	private String addr;//地址
	
	private String code;//邮政编码
	private String homeland;//户口所在地
	
	private String countryFloor;//国家职业资格等级(工人技术等级)
	
	private String judgeTime;//技师评定时间
	
	private String sign;//并轨标志
	
	private String workerNum;//职工序号

	@Override
	public String toString() {
		return "InsuranceSZHeiLongJiangUserInfo [taskid=" + taskid + ", companyNum=" + companyNum + ", company="
				+ company + ", personalNum=" + personalNum + ", name=" + name + ", idCard=" + idCard + ", sex=" + sex
				+ ", national=" + national + ", birth=" + birth + ", birthday=" + birthday + ", culture=" + culture
				+ ", marry=" + marry + ", personal=" + personal + ", joinDate=" + joinDate + ", houseHold=" + houseHold
				+ ", insuranceStatus=" + insuranceStatus + ", workStatus=" + workStatus + ", profession=" + profession
				+ ", useWork=" + useWork + ", special=" + special + ", changeFloor=" + changeFloor + ", administration="
				+ administration + ", phone=" + phone + ", addr=" + addr + ", code=" + code + ", homeland=" + homeland
				+ ", countryFloor=" + countryFloor + ", judgeTime=" + judgeTime + ", sign=" + sign + ", workerNum="
				+ workerNum + "]";
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
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

	public String getPersonalNum() {
		return personalNum;
	}

	public void setPersonalNum(String personalNum) {
		this.personalNum = personalNum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
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

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getCulture() {
		return culture;
	}

	public void setCulture(String culture) {
		this.culture = culture;
	}

	public String getMarry() {
		return marry;
	}

	public void setMarry(String marry) {
		this.marry = marry;
	}

	public String getPersonal() {
		return personal;
	}

	public void setPersonal(String personal) {
		this.personal = personal;
	}

	public String getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}

	public String getHouseHold() {
		return houseHold;
	}

	public void setHouseHold(String houseHold) {
		this.houseHold = houseHold;
	}

	public String getInsuranceStatus() {
		return insuranceStatus;
	}

	public void setInsuranceStatus(String insuranceStatus) {
		this.insuranceStatus = insuranceStatus;
	}

	public String getWorkStatus() {
		return workStatus;
	}

	public void setWorkStatus(String workStatus) {
		this.workStatus = workStatus;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getUseWork() {
		return useWork;
	}

	public void setUseWork(String useWork) {
		this.useWork = useWork;
	}

	public String getSpecial() {
		return special;
	}

	public void setSpecial(String special) {
		this.special = special;
	}

	public String getChangeFloor() {
		return changeFloor;
	}

	public void setChangeFloor(String changeFloor) {
		this.changeFloor = changeFloor;
	}

	public String getAdministration() {
		return administration;
	}

	public void setAdministration(String administration) {
		this.administration = administration;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getHomeland() {
		return homeland;
	}

	public void setHomeland(String homeland) {
		this.homeland = homeland;
	}

	public String getCountryFloor() {
		return countryFloor;
	}

	public void setCountryFloor(String countryFloor) {
		this.countryFloor = countryFloor;
	}

	public String getJudgeTime() {
		return judgeTime;
	}

	public void setJudgeTime(String judgeTime) {
		this.judgeTime = judgeTime;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getWorkerNum() {
		return workerNum;
	}

	public void setWorkerNum(String workerNum) {
		this.workerNum = workerNum;
	}

	
	
	
}
