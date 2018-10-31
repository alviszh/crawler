package com.microservice.dao.entity.crawler.insurance.wuxi;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author zhangyongjie
 * @create 2017-09-22 18:47
 * @Desc
 */
@Entity
@Table(name = "insurance_wuxi_userinfo")
public class InsuranceWuxiUserInfo extends IdEntity {
    /**
     * taskid  uuid 前端通过uuid访问状态结果
     */
    private String taskid;

    /**
     * 个人编码
     */
    private String personalCode;


    /**
     * 身份证号
     */
    private String idNumber;

    /**
     * 姓名
     */
    private String name;

    /**
     * 民族
     */
    private String nation;

    /**
     * 出生日期
     */
    private String birthday;


    /**
     * 联系电话
     */
    private String telphone;

    /**
     * 性别
     */
    private String sex;

    /**
     * 户口性质
     */
    private String householdType;

    /**
     * 文化程度
     */
    private String education;

    /**
     * 政治面貌
     */
    private String political;

    /**
     * 个人状态
     */
    private String personalStatus;

    /**
     * 人员征缴类别
     */
    private String personnelCollectionType;

    /**
     * 用工形式
     */
    private String employmentType;



    /**
     * 医疗人员类别
     */
    private String medicalPersonnelType;


    /**
     * 银行行号
     */
    private String bankCode;

    /**
     * 银行账号
     */
    private String bankAccount;

    /**
     * 工作日期
     */
    private String workDate;

    /**
     * 预期退休日期
     */
    private String estimateRetirementDate;

    /**
     * 个人身份
     */
    private String personalIdentity;

    /**
     * 行政职务
     */
    private String administrativeDuty;

    /**
     * 专业技术职务
     */
    private String technicalPosition;

    /**
     * 工人技术等级
     */
    private String workersTechnicalLevel;

    /**
     * 军转人员
     */
    private String militaryPersonnel;

    /**
     * 特殊工种标识
     */
    private String specialWorkFlag;

    /**
     * 证件类型
     */
    private String documentType;

    /**
     * 所属区
     */
    private String subordinateArea;
	
	/**
	 *社区号
	 */
	private String communityId;
	

    /**
     * 所在街道号
     */
    private String streetNumber;

    /**
     * 联系人
     */
    private String contacts;

    /**
     * 劳动模范
     */
    private String modelWorker;

    /**
     * 1-4级工伤人员标志
     */
    private String injuryMark;

    /**
     * 特殊照顾人群类别
     */
    private String specialType;

    /**
     * 居外异地安置标志
     */
    private String resettlementMark;

    /**
     * 从事特殊工种累计月数
     */
    private String specialJobsMonthlys;

    /**
     * 养老保险视同缴费月数
     */
    private String pensionMonthlys;

    /**
     * 医疗保险视同缴费月数
     */
    private String medicalMonthlys;

    /**
     * 失业保险视同缴费月数
     */
    private String unemploymentMonthlys;

    /**
     * 手机号码
     */
    private String mobilePhone;

    /**
     * 通信地址
     */
    private String communicationAddress;

    /**
     * 户口所在地
     */
    private String registeredResidence;

    /**
     * 现居住地址
     */
    private String livingAddress;

    /**
     * 邮件地址
     */
    private String mailingAddress;

    /**
     * 备注
     */
    private String remarks;

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getPersonalCode() {
        return personalCode;
    }

    public void setPersonalCode(String personalCode) {
        this.personalCode = personalCode;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHouseholdType() {
        return householdType;
    }

    public void setHouseholdType(String householdType) {
        this.householdType = householdType;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getPolitical() {
        return political;
    }

    public void setPolitical(String political) {
        this.political = political;
    }

    public String getPersonalStatus() {
        return personalStatus;
    }

    public void setPersonalStatus(String personalStatus) {
        this.personalStatus = personalStatus;
    }

    public String getPersonnelCollectionType() {
        return personnelCollectionType;
    }

    public void setPersonnelCollectionType(String personnelCollectionType) {
        this.personnelCollectionType = personnelCollectionType;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public String getMedicalPersonnelType() {
        return medicalPersonnelType;
    }

    public void setMedicalPersonnelType(String medicalPersonnelType) {
        this.medicalPersonnelType = medicalPersonnelType;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getWorkDate() {
        return workDate;
    }

    public void setWorkDate(String workDate) {
        this.workDate = workDate;
    }

    public String getEstimateRetirementDate() {
        return estimateRetirementDate;
    }

    public void setEstimateRetirementDate(String estimateRetirementDate) {
        this.estimateRetirementDate = estimateRetirementDate;
    }

    public String getPersonalIdentity() {
        return personalIdentity;
    }

    public void setPersonalIdentity(String personalIdentity) {
        this.personalIdentity = personalIdentity;
    }

    public String getAdministrativeDuty() {
        return administrativeDuty;
    }

    public void setAdministrativeDuty(String administrativeDuty) {
        this.administrativeDuty = administrativeDuty;
    }

    public String getTechnicalPosition() {
        return technicalPosition;
    }

    public void setTechnicalPosition(String technicalPosition) {
        this.technicalPosition = technicalPosition;
    }

    public String getWorkersTechnicalLevel() {
        return workersTechnicalLevel;
    }

    public void setWorkersTechnicalLevel(String workersTechnicalLevel) {
        this.workersTechnicalLevel = workersTechnicalLevel;
    }

    public String getMilitaryPersonnel() {
        return militaryPersonnel;
    }

    public void setMilitaryPersonnel(String militaryPersonnel) {
        this.militaryPersonnel = militaryPersonnel;
    }

    public String getSpecialWorkFlag() {
        return specialWorkFlag;
    }

    public void setSpecialWorkFlag(String specialWorkFlag) {
        this.specialWorkFlag = specialWorkFlag;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getSubordinateArea() {
        return subordinateArea;
    }

    public void setSubordinateArea(String subordinateArea) {
        this.subordinateArea = subordinateArea;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getModelWorker() {
        return modelWorker;
    }

    public void setModelWorker(String modelWorker) {
        this.modelWorker = modelWorker;
    }

    public String getInjuryMark() {
        return injuryMark;
    }

    public void setInjuryMark(String injuryMark) {
        this.injuryMark = injuryMark;
    }

    public String getSpecialType() {
        return specialType;
    }

    public void setSpecialType(String specialType) {
        this.specialType = specialType;
    }

    public String getResettlementMark() {
        return resettlementMark;
    }

    public void setResettlementMark(String resettlementMark) {
        this.resettlementMark = resettlementMark;
    }

    public String getSpecialJobsMonthlys() {
        return specialJobsMonthlys;
    }

    public void setSpecialJobsMonthlys(String specialJobsMonthlys) {
        this.specialJobsMonthlys = specialJobsMonthlys;
    }

    public String getPensionMonthlys() {
        return pensionMonthlys;
    }

    public void setPensionMonthlys(String pensionMonthlys) {
        this.pensionMonthlys = pensionMonthlys;
    }

    public String getMedicalMonthlys() {
        return medicalMonthlys;
    }

    public void setMedicalMonthlys(String medicalMonthlys) {
        this.medicalMonthlys = medicalMonthlys;
    }

    public String getUnemploymentMonthlys() {
        return unemploymentMonthlys;
    }

    public void setUnemploymentMonthlys(String unemploymentMonthlys) {
        this.unemploymentMonthlys = unemploymentMonthlys;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getCommunicationAddress() {
        return communicationAddress;
    }

    public void setCommunicationAddress(String communicationAddress) {
        this.communicationAddress = communicationAddress;
    }

    public String getRegisteredResidence() {
        return registeredResidence;
    }

    public void setRegisteredResidence(String registeredResidence) {
        this.registeredResidence = registeredResidence;
    }

    public String getLivingAddress() {
        return livingAddress;
    }

    public void setLivingAddress(String livingAddress) {
        this.livingAddress = livingAddress;
    }

    public String getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(String mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public InsuranceWuxiUserInfo() {
    }
}
