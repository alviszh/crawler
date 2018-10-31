package com.microservice.dao.entity.crawler.insurance.chengdu;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 * 成都社保:医疗保险缴费明细
 * @author tz
 *
 */
@Entity
@Table(name = "insurance_chengdu_medical" ,indexes = {@Index(name = "index_insurance_chengdu_medical_taskid", columnList = "taskid")})
public class InsuranceChengduMedical extends IdEntity {
	
	/**
	 * uuid 前端通过uuid访问状态结果	
	 */
	private String taskid;
	
	/**
	 * 缴费月份
	 */
	private String payMonth;				
	/**
	 * 单位名称
	 */
	private String company;							
	/**
	 * 缴费基数
	 */
	private String payBase;							
	/**
	 * 单位缴费金额
	 */
	private String companyPay;								
	/**
	 * 个人缴费金额
	 */
	private String personalPay;
	/**
	 * 划入账户金额    PS:医疗保险缴费明细字段
	 */
	private String includedMoney;
	/**
	 * 单位缴费比例
	 */
	private String companyProportion;	
	/**
	 *个人缴费比例
	 */
	private String personnelProportion;
	/**
	 * 实收时间
	 */
	private String receivablesDate;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getPayMonth() {
		return payMonth;
	}
	public void setPayMonth(String payMonth) {
		this.payMonth = payMonth;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getPayBase() {
		return payBase;
	}
	public void setPayBase(String payBase) {
		this.payBase = payBase;
	}
	public String getCompanyPay() {
		return companyPay;
	}
	public void setCompanyPay(String companyPay) {
		this.companyPay = companyPay;
	}
	public String getPersonalPay() {
		return personalPay;
	}
	public void setPersonalPay(String personalPay) {
		this.personalPay = personalPay;
	}
	public String getIncludedMoney() {
		return includedMoney;
	}
	public void setIncludedMoney(String includedMoney) {
		this.includedMoney = includedMoney;
	}
	public String getCompanyProportion() {
		return companyProportion;
	}
	public void setCompanyProportion(String companyProportion) {
		this.companyProportion = companyProportion;
	}
	public String getPersonnelProportion() {
		return personnelProportion;
	}
	public void setPersonnelProportion(String personnelProportion) {
		this.personnelProportion = personnelProportion;
	}
	public String getReceivablesDate() {
		return receivablesDate;
	}
	public void setReceivablesDate(String receivablesDate) {
		this.receivablesDate = receivablesDate;
	}
	@Override
	public String toString() {
		return "InsuranceChengduMedical [taskid=" + taskid + ", payMonth=" + payMonth + ", company=" + company
				+ ", payBase=" + payBase + ", companyPay=" + companyPay + ", personalPay=" + personalPay
				+ ", includedMoney=" + includedMoney + ", companyProportion=" + companyProportion
				+ ", personnelProportion=" + personnelProportion + ", receivablesDate=" + receivablesDate + "]";
	}
	public InsuranceChengduMedical(String taskid, String payMonth, String company, String payBase, String companyPay,
			String personalPay, String includedMoney, String companyProportion, String personnelProportion,
			String receivablesDate) {
		super();
		this.taskid = taskid;
		this.payMonth = payMonth;
		this.company = company;
		this.payBase = payBase;
		this.companyPay = companyPay;
		this.personalPay = personalPay;
		this.includedMoney = includedMoney;
		this.companyProportion = companyProportion;
		this.personnelProportion = personnelProportion;
		this.receivablesDate = receivablesDate;
	}
	public InsuranceChengduMedical() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
