/**
  * Copyright 2018 bejson.com 
  */
package com.microservice.dao.entity.crawler.insurance.suqian;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * Auto-generated: 2018-03-13 14:59:18
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Entity
@Table(name = "insurance_suqian_pay",indexes = {@Index(name = "index_insurance_suqian_pay_taskid", columnList = "taskId")})
public class InsurancePay extends IdEntity {

	private String month;  //月份
	private String paymentType; //缴费类型 10 为正常缴费
	private String paymentFlag; //缴费标志 1为已实缴
	private String paymentBase;//缴费基数
	private String pensionUnit;//企业职工基本养老单位缴纳
	private String pensionPerson;//企业职工基本养老个人缴纳
	private String medicalUnit;//基本医疗保险单位缴纳
	private String medicalPerson;//基本医疗保险个人缴纳
	private String diseaseUnit;//大额医疗保险单位缴纳
	private String diseasePerson;//大额医疗保险个人缴纳
	private String injurePayment;//工伤保险单位缴纳
	private String maternityUnit;//生育保险单位缴纳
	private String unemployeeUnit;//失业保险单位缴纳
	private String unemployeePerson;//失业保险个人缴纳
	
	private String city;//城市
	
	private String taskId;//
	
	private String persinoId;//网站的个人id
	
	
	public String getPersinoId() {
		return persinoId;
	}

	public void setPersinoId(String persinoId) {
		this.persinoId = persinoId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getMonth() {
		return month;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentFlag(String paymentFlag) {
		this.paymentFlag = paymentFlag;
	}

	public String getPaymentFlag() {
		return paymentFlag;
	}

	public void setPaymentBase(String paymentBase) {
		this.paymentBase = paymentBase;
	}

	public String getPaymentBase() {
		return paymentBase;
	}

	public void setPensionUnit(String pensionUnit) {
		this.pensionUnit = pensionUnit;
	}

	public String getPensionUnit() {
		return pensionUnit;
	}

	public void setPensionPerson(String pensionPerson) {
		this.pensionPerson = pensionPerson;
	}

	public String getPensionPerson() {
		return pensionPerson;
	}

	public void setMedicalUnit(String medicalUnit) {
		this.medicalUnit = medicalUnit;
	}

	public String getMedicalUnit() {
		return medicalUnit;
	}

	public void setMedicalPerson(String medicalPerson) {
		this.medicalPerson = medicalPerson;
	}

	public String getMedicalPerson() {
		return medicalPerson;
	}

	public void setDiseaseUnit(String diseaseUnit) {
		this.diseaseUnit = diseaseUnit;
	}

	public String getDiseaseUnit() {
		return diseaseUnit;
	}

	public void setDiseasePerson(String diseasePerson) {
		this.diseasePerson = diseasePerson;
	}

	public String getDiseasePerson() {
		return diseasePerson;
	}

	public void setInjurePayment(String injurePayment) {
		this.injurePayment = injurePayment;
	}

	public String getInjurePayment() {
		return injurePayment;
	}

	public void setMaternityUnit(String maternityUnit) {
		this.maternityUnit = maternityUnit;
	}

	public String getMaternityUnit() {
		return maternityUnit;
	}

	public void setUnemployeeUnit(String unemployeeUnit) {
		this.unemployeeUnit = unemployeeUnit;
	}

	public String getUnemployeeUnit() {
		return unemployeeUnit;
	}

	public void setUnemployeePerson(String unemployeePerson) {
		this.unemployeePerson = unemployeePerson;
	}

	public String getUnemployeePerson() {
		return unemployeePerson;
	}

	@Override
	public String toString() {
		return "InsurancePay [month=" + month + ", paymentType=" + paymentType + ", paymentFlag=" + paymentFlag
				+ ", paymentBase=" + paymentBase + ", pensionUnit=" + pensionUnit + ", pensionPerson=" + pensionPerson
				+ ", medicalUnit=" + medicalUnit + ", medicalPerson=" + medicalPerson + ", diseaseUnit=" + diseaseUnit
				+ ", diseasePerson=" + diseasePerson + ", injurePayment=" + injurePayment + ", maternityUnit="
				+ maternityUnit + ", unemployeeUnit=" + unemployeeUnit + ", unemployeePerson=" + unemployeePerson
				+ ", city=" + city + ", taskId=" + taskId + ", persinoId=" + persinoId + "]";
	}

}