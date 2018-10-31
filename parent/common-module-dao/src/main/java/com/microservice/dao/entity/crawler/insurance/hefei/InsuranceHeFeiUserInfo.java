package com.microservice.dao.entity.crawler.insurance.hefei;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 合肥市社保个人基本信息
 * @author zmy
 */
@Entity
@Table(name="insurance_hefei_userinfo")
public class InsuranceHeFeiUserInfo extends IdEntity{

    private String taskid;
    private String perNum;//个人编号
    private String name;//姓名
    private String sex;//性别
    private String persionStatus;//养老参保状态
    private String unemployStatus;//失业参保状态
    private String medicalStatus;//医疗参保状态
    private String injuryStatus;//工伤参保状态
    private String birthStatus;//生育参保状态
    private String companyName;//单位名称
    private String persionPayYear;//养老视同缴费年限
    private String socialSecurityNum;//社会保障卡卡号
    private String idNum;//身 份 证 号
    private String workTime;//参加工作时间
    private String persionPayBasenum;//养老缴费基数
    private String unemployPayBasenum;//失业缴费基数
    private String medicalPayBasenum;//医疗缴费基数
    private String injuryPayBasenum;//工伤缴费基数
    private String birthPayBasenum;//生育缴费基数
    private String workStatus;//工作状态
    private String unemployPayYear;//失业视同缴费年限

    public String getTaskid() {
     return taskid;
    }

    public void setTaskid(String taskid) {
     this.taskid = taskid;
    }

    public String getPerNum() {
     return perNum;
    }

    public void setPerNum(String perNum) {
     this.perNum = perNum;
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

    public String getPersionStatus() {
     return persionStatus;
    }

    public void setPersionStatus(String persionStatus) {
     this.persionStatus = persionStatus;
    }

    public String getUnemployStatus() {
     return unemployStatus;
    }

    public void setUnemployStatus(String unemployStatus) {
     this.unemployStatus = unemployStatus;
    }

    public String getMedicalStatus() {
     return medicalStatus;
    }

    public void setMedicalStatus(String medicalStatus) {
     this.medicalStatus = medicalStatus;
    }

    public String getInjuryStatus() {
     return injuryStatus;
    }

    public void setInjuryStatus(String injuryStatus) {
     this.injuryStatus = injuryStatus;
    }

    public String getBirthStatus() {
     return birthStatus;
    }

    public void setBirthStatus(String birthStatus) {
     this.birthStatus = birthStatus;
    }

    public String getCompanyName() {
     return companyName;
    }

    public void setCompanyName(String companyName) {
     this.companyName = companyName;
    }

    public String getPersionPayYear() {
     return persionPayYear;
    }

    public void setPersionPayYear(String persionPayYear) {
     this.persionPayYear = persionPayYear;
    }

    public String getSocialSecurityNum() {
     return socialSecurityNum;
    }

    public void setSocialSecurityNum(String socialSecurityNum) {
     this.socialSecurityNum = socialSecurityNum;
    }

    public String getIdNum() {
     return idNum;
    }

    public void setIdNum(String idNum) {
     this.idNum = idNum;
    }

    public String getWorkTime() {
     return workTime;
    }

    public void setWorkTime(String workTime) {
     this.workTime = workTime;
    }

    public String getPersionPayBasenum() {
     return persionPayBasenum;
    }

    public void setPersionPayBasenum(String persionPayBasenum) {
     this.persionPayBasenum = persionPayBasenum;
    }

    public String getUnemployPayBasenum() {
     return unemployPayBasenum;
    }

    public void setUnemployPayBasenum(String unemployPayBasenum) {
     this.unemployPayBasenum = unemployPayBasenum;
    }

    public String getMedicalPayBasenum() {
     return medicalPayBasenum;
    }

    public void setMedicalPayBasenum(String medicalPayBasenum) {
     this.medicalPayBasenum = medicalPayBasenum;
    }

    public String getInjuryPayBasenum() {
     return injuryPayBasenum;
    }

    public void setInjuryPayBasenum(String injuryPayBasenum) {
     this.injuryPayBasenum = injuryPayBasenum;
    }

    public String getBirthPayBasenum() {
     return birthPayBasenum;
    }

    public void setBirthPayBasenum(String birthPayBasenum) {
     this.birthPayBasenum = birthPayBasenum;
    }

    public String getWorkStatus() {
     return workStatus;
    }

    public void setWorkStatus(String workStatus) {
     this.workStatus = workStatus;
    }

    public String getUnemployPayYear() {
     return unemployPayYear;
    }

    public void setUnemployPayYear(String unemployPayYear) {
     this.unemployPayYear = unemployPayYear;
    }

    public InsuranceHeFeiUserInfo(String taskid,String perNum, String name, String sex, String persionStatus, String unemployStatus, String medicalStatus, String injuryStatus, String birthStatus, String companyName, String persionPayYear, String socialSecurityNum, String idNum, String workTime, String persionPayBasenum, String unemployPayBasenum, String medicalPayBasenum, String injuryPayBasenum, String birthPayBasenum, String workStatus, String unemployPayYear) {
     this.taskid = taskid;
     this.perNum = perNum;
     this.name = name;
     this.sex = sex;
     this.persionStatus = persionStatus;
     this.unemployStatus = unemployStatus;
     this.medicalStatus = medicalStatus;
     this.injuryStatus = injuryStatus;
     this.birthStatus = birthStatus;
     this.companyName = companyName;
     this.persionPayYear = persionPayYear;
     this.socialSecurityNum = socialSecurityNum;
     this.idNum = idNum;
     this.workTime = workTime;
     this.persionPayBasenum = persionPayBasenum;
     this.unemployPayBasenum = unemployPayBasenum;
     this.medicalPayBasenum = medicalPayBasenum;
     this.injuryPayBasenum = injuryPayBasenum;
     this.birthPayBasenum = birthPayBasenum;
     this.workStatus = workStatus;
     this.unemployPayYear = unemployPayYear;
    }

    @Override
    public String toString() {
        return "InsuranceHeFeiUserInfo{" +
                "taskid='" + taskid + '\'' +
                ", perNum='" + perNum + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", persionStatus='" + persionStatus + '\'' +
                ", unemployStatus='" + unemployStatus + '\'' +
                ", medicalStatus='" + medicalStatus + '\'' +
                ", injuryStatus='" + injuryStatus + '\'' +
                ", birthStatus='" + birthStatus + '\'' +
                ", companyName='" + companyName + '\'' +
                ", persionPayYear='" + persionPayYear + '\'' +
                ", socialSecurityNum='" + socialSecurityNum + '\'' +
                ", idNum='" + idNum + '\'' +
                ", workTime='" + workTime + '\'' +
                ", persionPayBasenum='" + persionPayBasenum + '\'' +
                ", unemployPayBasenum='" + unemployPayBasenum + '\'' +
                ", medicalPayBasenum='" + medicalPayBasenum + '\'' +
                ", injuryPayBasenum='" + injuryPayBasenum + '\'' +
                ", birthPayBasenum='" + birthPayBasenum + '\'' +
                ", workStatus='" + workStatus + '\'' +
                ", unemployPayYear='" + unemployPayYear + '\'' +
                '}';
    }

    public InsuranceHeFeiUserInfo() {
     super();
    }
}
