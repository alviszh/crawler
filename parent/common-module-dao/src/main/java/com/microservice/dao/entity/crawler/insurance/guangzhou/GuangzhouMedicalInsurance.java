package com.microservice.dao.entity.crawler.insurance.guangzhou;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 * 医疗保险信息详情
 * @author Administrator
 *
 */
@Entity
@Table(name="insurance_guangzhou_medical")
public class GuangzhouMedicalInsurance extends IdEntity{

	private String organizationnum;				//历史单位编号
	private String payStartDate;				//开始缴费日期
	private String payEndDate;					//终止缴费日期
	private String monthCum;					//累计月数
	private String transferPay;					//转移/视同缴费 
	private String appendPay;					//补收/补缴 
	private String organizationPay;				//单位缴费 
	private String personalPay;					//个人缴费 
	private String govPay;						//政府资助
	private String payBase;						//缴费基数
	private String medicalType;					//医保类别（职工社会医疗保险/重大疾病医疗补助 ）
	private String taskid;
	public String getOrganizationnum() {
		return organizationnum;
	}
	public void setOrganizationnum(String organizationnum) {
		this.organizationnum = organizationnum;
	}
	public String getPayStartDate() {
		return payStartDate;
	}
	public void setPayStartDate(String payStartDate) {
		this.payStartDate = payStartDate;
	}
	public String getPayEndDate() {
		return payEndDate;
	}
	public void setPayEndDate(String payEndDate) {
		this.payEndDate = payEndDate;
	}
	public String getMonthCum() {
		return monthCum;
	}
	public void setMonthCum(String monthCum) {
		this.monthCum = monthCum;
	}
	public String getTransferPay() {
		return transferPay;
	}
	public void setTransferPay(String transferPay) {
		this.transferPay = transferPay;
	}
	public String getAppendPay() {
		return appendPay;
	}
	public void setAppendPay(String appendPay) {
		this.appendPay = appendPay;
	}
	public String getOrganizationPay() {
		return organizationPay;
	}
	public void setOrganizationPay(String organizationPay) {
		this.organizationPay = organizationPay;
	}
	public String getPersonalPay() {
		return personalPay;
	}
	public void setPersonalPay(String personalPay) {
		this.personalPay = personalPay;
	}
	public String getGovPay() {
		return govPay;
	}
	public void setGovPay(String govPay) {
		this.govPay = govPay;
	}
	public String getPayBase() {
		return payBase;
	}
	public void setPayBase(String payBase) {
		this.payBase = payBase;
	}
	
	public String getMedicalType() {
		return medicalType;
	}
	public void setMedicalType(String medicalType) {
		this.medicalType = medicalType;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Override
	public String toString() {
		return "GuangzhouMedicalInsurance [organizationnum=" + organizationnum + ", payStartDate=" + payStartDate
				+ ", payEndDate=" + payEndDate + ", monthCum=" + monthCum + ", transferPay=" + transferPay
				+ ", appendPay=" + appendPay + ", organizationPay=" + organizationPay + ", personalPay=" + personalPay
				+ ", govPay=" + govPay + ", payBase=" + payBase + ", medicalType=" + medicalType + ", taskid=" + taskid
				+ "]";
	}
	
	
	
	
}
