package com.microservice.dao.entity.crawler.insurance.shenzhen;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 深圳社保 参保基本信息
 * @author rongshengxu
 *
 */
@Entity
@Table(name="insurance_shenzhen_baseinfo")
public class InsuranceShenzhenBaseInfo extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = -7225639204374657354L;
	
	/** 登录名 */
	@Column(name="login_name")
	private String loginName;
	
	/** 爬取批次号 */
	@Column(name="task_id")
	private String taskId;
	
	/** 姓名 */
	@Column(name="name")
	private String name;
	
	/** 社保个人电脑号 */
	@Column(name="insurance_code")
	private String insuranceCode;
	
	/** 身份证号 */
	@Column(name="card_id")
	private String cardId;
	
	/** 性别 */
	@Column(name="sex")
	private String sex;
	
	/** 出生日期 */
	@Column(name="birth_date")
	private String birthDate;
	
	/** 户籍类别 */
	@Column(name="census_register_type")
	private String censusRegisterType;
	
	/** 医疗证号 */
	@Column(name="medical_card_id")
	private String medicalCardId;
	
	/** 卡状态 */
	@Column(name="card_state")
	private String cardState;
	
	/** 职工性质 */
	@Column(name="employee_type")
	private String employeeType;
	
	/** 保健对象 */
	@Column(name="health_object")
	private String healthObject;
	
	/** 职别 */
	@Column(name="pisition_level")
	private String pisitionLevel;
	
	/** 备注 */
	@Column(name="remark")
	private String remark;
	
	/** 参保单位 */
	@Column(name="insured_company")
	private String insuredCompany;
	
	/** 当前缴费情况 */
	@Column(name="current_pay_info")
	private String currentPayInfo;
	
	/** 缴费工资 */
	@Column(name="pay_quota")
	private String payQuota;
	
	/** 养老参保情况 */
	@Column(name="aged_insured_info")
	private String agedInsuredInfo;
	
	/** 医疗参保情况 */
	@Column(name="medical_insured_info")
	private String medicalInsuredInfo;
	
	/** 工伤参保情况 */
	@Column(name="injury_insured_info")
	private String injuryInsuredInfo;
	
	/** 失业参保情况 */
	@Column(name="unemployment_insured_info")
	private String unemploymentInsuredInfo;
	
	/** 生育参保情况 */
	@Column(name="maternity_insured_info")
	private String maternityInsuredInfo;
	

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInsuranceCode() {
		return insuranceCode;
	}

	public void setInsuranceCode(String insuranceCode) {
		this.insuranceCode = insuranceCode;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getCensusRegisterType() {
		return censusRegisterType;
	}

	public void setCensusRegisterType(String censusRegisterType) {
		this.censusRegisterType = censusRegisterType;
	}

	public String getMedicalCardId() {
		return medicalCardId;
	}

	public void setMedicalCardId(String medicalCardId) {
		this.medicalCardId = medicalCardId;
	}

	public String getCardState() {
		return cardState;
	}

	public void setCardState(String cardState) {
		this.cardState = cardState;
	}

	public String getEmployeeType() {
		return employeeType;
	}

	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}

	public String getHealthObject() {
		return healthObject;
	}

	public void setHealthObject(String healthObject) {
		this.healthObject = healthObject;
	}

	public String getPisitionLevel() {
		return pisitionLevel;
	}

	public void setPisitionLevel(String pisitionLevel) {
		this.pisitionLevel = pisitionLevel;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getInsuredCompany() {
		return insuredCompany;
	}

	public void setInsuredCompany(String insuredCompany) {
		this.insuredCompany = insuredCompany;
	}

	public String getCurrentPayInfo() {
		return currentPayInfo;
	}

	public void setCurrentPayInfo(String currentPayInfo) {
		this.currentPayInfo = currentPayInfo;
	}

	public String getPayQuota() {
		return payQuota;
	}

	public void setPayQuota(String payQuota) {
		this.payQuota = payQuota;
	}

	public String getAgedInsuredInfo() {
		return agedInsuredInfo;
	}

	public void setAgedInsuredInfo(String agedInsuredInfo) {
		this.agedInsuredInfo = agedInsuredInfo;
	}

	public String getMedicalInsuredInfo() {
		return medicalInsuredInfo;
	}

	public void setMedicalInsuredInfo(String medicalInsuredInfo) {
		this.medicalInsuredInfo = medicalInsuredInfo;
	}

	public String getInjuryInsuredInfo() {
		return injuryInsuredInfo;
	}

	public void setInjuryInsuredInfo(String injuryInsuredInfo) {
		this.injuryInsuredInfo = injuryInsuredInfo;
	}

	public String getUnemploymentInsuredInfo() {
		return unemploymentInsuredInfo;
	}

	public void setUnemploymentInsuredInfo(String unemploymentInsuredInfo) {
		this.unemploymentInsuredInfo = unemploymentInsuredInfo;
	}

	public String getMaternityInsuredInfo() {
		return maternityInsuredInfo;
	}

	public void setMaternityInsuredInfo(String maternityInsuredInfo) {
		this.maternityInsuredInfo = maternityInsuredInfo;
	}

	@Override
	public String toString() {
		return "InsuranceShenzhenBaseInfo [loginName=" + loginName + ", taskId=" + taskId + ", name=" + name
				+ ", insuranceCode=" + insuranceCode + ", cardId=" + cardId + ", sex=" + sex + ", birthDate="
				+ birthDate + ", censusRegisterType=" + censusRegisterType + ", medicalCardId=" + medicalCardId
				+ ", cardState=" + cardState + ", employeeType=" + employeeType + ", healthObject=" + healthObject
				+ ", pisitionLevel=" + pisitionLevel + ", remark=" + remark + ", insuredCompany=" + insuredCompany
				+ ", currentPayInfo=" + currentPayInfo + ", payQuota=" + payQuota + ", agedInsuredInfo="
				+ agedInsuredInfo + ", medicalInsuredInfo=" + medicalInsuredInfo + ", injuryInsuredInfo="
				+ injuryInsuredInfo + ", unemploymentInsuredInfo=" + unemploymentInsuredInfo + ", maternityInsuredInfo="
				+ maternityInsuredInfo + "]";
	}
	
	

}
