package com.microservice.dao.entity.crawler.insurance.nanchang;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * 南昌社保个人信息
 * @author tz
 *
 */
@Entity
@Table(name = "insurance_nanchang_userinfo" ,indexes = {@Index(name = "index_insurance_nanchang_userinfo_taskid", columnList = "taskid")})
public class InsuranceNanchangUserInfo extends IdEntity {

    /**
     * uuid 前端通过uuid访问状态结果
     */
    private String taskid;

    /**
     * 个人编号
     */
    private String personalNumber;
    /**
     * 姓名
     */
    private String name;
    /**
     * 身份证号
     */
    private String idNum;

    /**
     * 医疗保险单位编号
     */
    private String medicalUnitNumber;
    /**
     * 医保卡号
     */
    private String healthCardNumber;
    /**
     * 医疗保险单位名称
     */
    private String nameMedicalInsUnit;
    /**
     * 医疗保险参保状态
     */
    private String healthInsStatus;
    /**
     *医疗保险人员状态			例：在职待遇
     */
    private String personnelStatus;
    /**
     * 医疗保险账户状态
     */
    private String healthInsAccStatus;
    /**
     * 医疗保险账户余额
     */
    private String medicalBalance;
    /**
     * 养老保险单位编号
     */
    private String olderUnitNumber;
    /**
     * 养老保险单位名称
     */
    private String olderUnitName;
    /**
     * 养老保险参保状态
     */
    private String olderInsStatus;
    /**
     * 养老保险退休状态
     */
    private String olderInsRetiresStatus;
    /**
     * 养老保险缴费基数
     */
    private String olderInsExpBase;
    /**
     * 养老保险账户金额
     */
    private String olderInsAccAmount;
    /**
     * 养老保险缴费月数
     */
    private String olderInsMonthNumber;
    /**
     * 近两年详细信息的url
     */
    private String urldetinfo;
    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(String personalNumber) {
        this.personalNumber = personalNumber;
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

    public String getMedicalUnitNumber() {
        return medicalUnitNumber;
    }

    public void setMedicalUnitNumber(String medicalUnitNumber) {
        this.medicalUnitNumber = medicalUnitNumber;
    }

    public String getHealthCardNumber() {
        return healthCardNumber;
    }

    public void setHealthCardNumber(String healthCardNumber) {
        this.healthCardNumber = healthCardNumber;
    }

    public String getNameMedicalInsUnit() {
        return nameMedicalInsUnit;
    }

    public void setNameMedicalInsUnit(String nameMedicalInsUnit) {
        this.nameMedicalInsUnit = nameMedicalInsUnit;
    }

    public String getHealthInsStatus() {
        return healthInsStatus;
    }

    public void setHealthInsStatus(String healthInsStatus) {
        this.healthInsStatus = healthInsStatus;
    }

    public String getPersonnelStatus() {
        return personnelStatus;
    }

    public void setPersonnelStatus(String personnelStatus) {
        this.personnelStatus = personnelStatus;
    }

    public String getHealthInsAccStatus() {
        return healthInsAccStatus;
    }

    public void setHealthInsAccStatus(String healthInsAccStatus) {
        this.healthInsAccStatus = healthInsAccStatus;
    }

    public String getMedicalBalance() {
        return medicalBalance;
    }

    public void setMedicalBalance(String medicalBalance) {
        this.medicalBalance = medicalBalance;
    }

    public String getOlderUnitNumber() {
        return olderUnitNumber;
    }

    public void setOlderUnitNumber(String olderUnitNumber) {
        this.olderUnitNumber = olderUnitNumber;
    }

    public String getOlderUnitName() {
        return olderUnitName;
    }

    public void setOlderUnitName(String olderUnitName) {
        this.olderUnitName = olderUnitName;
    }

    public String getOlderInsStatus() {
        return olderInsStatus;
    }

    public void setOlderInsStatus(String olderInsStatus) {
        this.olderInsStatus = olderInsStatus;
    }

    public String getOlderInsRetiresStatus() {
        return olderInsRetiresStatus;
    }

    public void setOlderInsRetiresStatus(String olderInsRetiresStatus) {
        this.olderInsRetiresStatus = olderInsRetiresStatus;
    }

    public String getOlderInsExpBase() {
        return olderInsExpBase;
    }

    public void setOlderInsExpBase(String olderInsExpBase) {
        this.olderInsExpBase = olderInsExpBase;
    }

    public String getOlderInsAccAmount() {
        return olderInsAccAmount;
    }

    public void setOlderInsAccAmount(String olderInsAccAmount) {
        this.olderInsAccAmount = olderInsAccAmount;
    }

    public String getOlderInsMonthNumber() {
        return olderInsMonthNumber;
    }

    public void setOlderInsMonthNumber(String olderInsMonthNumber) {
        this.olderInsMonthNumber = olderInsMonthNumber;
    }

    public String getUrldetinfo() {
        return urldetinfo;
    }

    public void setUrldetinfo(String urldetinfo) {
        this.urldetinfo = urldetinfo;
    }

    @Override
    public String toString() {
        return "InsuranceNanchangUserInfo{" +
                "taskid='" + taskid + '\'' +
                ", personalNumber='" + personalNumber + '\'' +
                ", name='" + name + '\'' +
                ", idNum='" + idNum + '\'' +
                ", medicalUnitNumber='" + medicalUnitNumber + '\'' +
                ", healthCardNumber='" + healthCardNumber + '\'' +
                ", nameMedicalInsUnit='" + nameMedicalInsUnit + '\'' +
                ", healthInsStatus='" + healthInsStatus + '\'' +
                ", personnelStatus='" + personnelStatus + '\'' +
                ", healthInsAccStatus='" + healthInsAccStatus + '\'' +
                ", medicalBalance='" + medicalBalance + '\'' +
                ", olderUnitNumber='" + olderUnitNumber + '\'' +
                ", olderUnitName='" + olderUnitName + '\'' +
                ", olderInsStatus='" + olderInsStatus + '\'' +
                ", olderInsRetiresStatus='" + olderInsRetiresStatus + '\'' +
                ", olderInsExpBase='" + olderInsExpBase + '\'' +
                ", olderInsAccAmount='" + olderInsAccAmount + '\'' +
                ", olderInsMonthNumber='" + olderInsMonthNumber + '\'' +
                '}';
    }

    public InsuranceNanchangUserInfo(String taskid, String personalNumber, String name, String idNum, String medicalUnitNumber, String healthCardNumber, String nameMedicalInsUnit, String healthInsStatus, String personnelStatus, String healthInsAccStatus, String medicalBalance, String olderUnitNumber, String olderUnitName, String olderInsStatus, String olderInsRetiresStatus, String olderInsExpBase, String olderInsAccAmount, String olderInsMonthNumber,String urldetinfo) {
        this.taskid = taskid;
        this.personalNumber = personalNumber;
        this.name = name;
        this.idNum = idNum;
        this.medicalUnitNumber = medicalUnitNumber;
        this.healthCardNumber = healthCardNumber;
        this.nameMedicalInsUnit = nameMedicalInsUnit;
        this.healthInsStatus = healthInsStatus;
        this.personnelStatus = personnelStatus;
        this.healthInsAccStatus = healthInsAccStatus;
        this.medicalBalance = medicalBalance;
        this.olderUnitNumber = olderUnitNumber;
        this.olderUnitName = olderUnitName;
        this.olderInsStatus = olderInsStatus;
        this.olderInsRetiresStatus = olderInsRetiresStatus;
        this.olderInsExpBase = olderInsExpBase;
        this.olderInsAccAmount = olderInsAccAmount;
        this.olderInsMonthNumber = olderInsMonthNumber;
        this.urldetinfo=urldetinfo;
    }


    public InsuranceNanchangUserInfo() {
        super();
        // TODO Auto-generated constructor stub
    }


}
