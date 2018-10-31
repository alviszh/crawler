package com.microservice.dao.entity.crawler.insurance.beijing;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_beijing_userinfo")
public class InsuranceBeijingUserInfo extends IdEntity{
	
	private String taskid;//uuid 前端通过uuid访问状态结果	
	private String companyName;						//单位名称
	private String organizationCode;				//组织机构代码
	private String insuranceRegisterNum;			//社会保险登记证编号
	private String county;							//所属区县
	private String name;							//姓名
	private String idNum;							//公民身份证号
	private String sex;								//性别
	private String birthdate;						//出生日期
	private String nation;							//民族
	private String country;							//国家
	private String personalIdentity;				//个人身份
	private String workDate;						//参加工作日期
	private String hkadr;							//户口所在地
	private String category;						//户口性质
	private String hkadrCode;						//户口所在地邮政编码
	private String residenceAddres;					//居住地(联系)地址
	private String residenceAddresCode;				//居住地（联系）邮政编码
	private String statementsType;					//获取对账单方式
	private String education;						//文化程度
	private String insuredPhone;					//参保人电话
	private String averageSalary;					//申报月均工资收入（元）
	private String issuingBank;						//委托代发银行名称
	private String issuingBankAccount;				//委托代发银行账号
	private String payPersonalCategory;				//缴费人员类别
	private String insuredType;						//医疗参保人员类别			例：在职员工
	private String specialDisease;					//是否患有特殊病
	
	@Override
	public String toString() {
		return "InsuranceBeijingUserInfo [taskid=" + taskid + ", companyName=" + companyName + ", organizationCode="
				+ organizationCode + ", insuranceRegisterNum=" + insuranceRegisterNum + ", county=" + county + ", name="
				+ name + ", idNum=" + idNum + ", sex=" + sex + ", birthdate=" + birthdate + ", nation=" + nation
				+ ", country=" + country + ", personalIdentity=" + personalIdentity + ", workDate=" + workDate
				+ ", hkadr=" + hkadr + ", category=" + category + ", hkadrCode=" + hkadrCode + ", residenceAddres="
				+ residenceAddres + ", residenceAddresCode=" + residenceAddresCode + ", statementsType="
				+ statementsType + ", education=" + education + ", insuredPhone=" + insuredPhone + ", averageSalary="
				+ averageSalary + ", issuingBank=" + issuingBank + ", issuingBankAccount=" + issuingBankAccount
				+ ", payPersonalCategory=" + payPersonalCategory + ", insuredType=" + insuredType + ", specialDisease="
				+ specialDisease + "]";
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getOrganizationCode() {
		return organizationCode;
	}
	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}
	public String getInsuranceRegisterNum() {
		return insuranceRegisterNum;
	}
	public void setInsuranceRegisterNum(String insuranceRegisterNum) {
		this.insuranceRegisterNum = insuranceRegisterNum;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
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
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getPersonalIdentity() {
		return personalIdentity;
	}
	public void setPersonalIdentity(String personalIdentity) {
		this.personalIdentity = personalIdentity;
	}
	public String getWorkDate() {
		return workDate;
	}
	public void setWorkDate(String workDate) {
		this.workDate = workDate;
	}
	public String getHkadr() {
		return hkadr;
	}
	public void setHkadr(String hkadr) {
		this.hkadr = hkadr;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getHkadrCode() {
		return hkadrCode;
	}
	public void setHkadrCode(String hkadrCode) {
		this.hkadrCode = hkadrCode;
	}
	public String getResidenceAddres() {
		return residenceAddres;
	}
	public void setResidenceAddres(String residenceAddres) {
		this.residenceAddres = residenceAddres;
	}
	public String getResidenceAddresCode() {
		return residenceAddresCode;
	}
	public void setResidenceAddresCode(String residenceAddresCode) {
		this.residenceAddresCode = residenceAddresCode;
	}
	public String getStatementsType() {
		return statementsType;
	}
	public void setStatementsType(String statementsType) {
		this.statementsType = statementsType;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public String getInsuredPhone() {
		return insuredPhone;
	}
	public void setInsuredPhone(String insuredPhone) {
		this.insuredPhone = insuredPhone;
	}
	public String getAverageSalary() {
		return averageSalary;
	}
	public void setAverageSalary(String averageSalary) {
		this.averageSalary = averageSalary;
	}
	public String getIssuingBank() {
		return issuingBank;
	}
	public void setIssuingBank(String issuingBank) {
		this.issuingBank = issuingBank;
	}
	public String getIssuingBankAccount() {
		return issuingBankAccount;
	}
	public void setIssuingBankAccount(String issuingBankAccount) {
		this.issuingBankAccount = issuingBankAccount;
	}
	public String getPayPersonalCategory() {
		return payPersonalCategory;
	}
	public void setPayPersonalCategory(String payPersonalCategory) {
		this.payPersonalCategory = payPersonalCategory;
	}
	public String getInsuredType() {
		return insuredType;
	}
	public void setInsuredType(String insuredType) {
		this.insuredType = insuredType;
	}
	public String getSpecialDisease() {
		return specialDisease;
	}
	public void setSpecialDisease(String specialDisease) {
		this.specialDisease = specialDisease;
	}

	
	
}
