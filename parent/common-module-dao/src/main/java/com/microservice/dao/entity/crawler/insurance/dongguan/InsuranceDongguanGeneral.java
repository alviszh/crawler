package com.microservice.dao.entity.crawler.insurance.dongguan;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 * 东莞社保:东莞社保月缴费情况查询
 * @author tz
 *
 */
@Entity
@Table(name = "insurance_dongguan_general" ,indexes = {@Index(name = "index_insurance_dongguan_general_taskid", columnList = "taskid")})
public class InsuranceDongguanGeneral extends IdEntity {
	
	/**
	 * uuid 前端通过uuid访问状态结果	
	 */
	private String taskid;
	
	/**
	 * 组织名称
	 */
	private String company;	
	/**
	 * 缴费月份
	 */
	private String payMonth;
	/**
	 * 险种类型   
	 */
	private String insuranceType;	
	/**
	 * 缴费基数
	 */
	private String payBase;	
	/**
	 * 单位缴费
	 */
	private String companyPay;	
	/**
	 * 单位缴费比例
	 */
	private String companyProportion;
	/**
	 * 个人缴费
	 */
	private String personalPay;
	/**
	 *个人缴费比例
	 */
	private String personnelProportion;
	/**
	 * 财政补助
	 */
	private String financialAid;
	/**
	 * 财政补助比例
	 */
	private String financialAidProportion;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getPayMonth() {
		return payMonth;
	}
	public void setPayMonth(String payMonth) {
		this.payMonth = payMonth;
	}
	public String getInsuranceType() {
		return insuranceType;
	}
	public void setInsuranceType(String insuranceType) {
		this.insuranceType = insuranceType;
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
	public String getCompanyProportion() {
		return companyProportion;
	}
	public void setCompanyProportion(String companyProportion) {
		this.companyProportion = companyProportion;
	}
	public String getPersonalPay() {
		return personalPay;
	}
	public void setPersonalPay(String personalPay) {
		this.personalPay = personalPay;
	}
	public String getPersonnelProportion() {
		return personnelProportion;
	}
	public void setPersonnelProportion(String personnelProportion) {
		this.personnelProportion = personnelProportion;
	}
	public String getFinancialAid() {
		return financialAid;
	}
	public void setFinancialAid(String financialAid) {
		this.financialAid = financialAid;
	}
	public String getFinancialAidProportion() {
		return financialAidProportion;
	}
	public void setFinancialAidProportion(String financialAidProportion) {
		this.financialAidProportion = financialAidProportion;
	}
	@Override
	public String toString() {
		return "InsuranceDongguanGeneral [taskid=" + taskid + ", company=" + company + ", payMonth=" + payMonth
				+ ", insuranceType=" + insuranceType + ", payBase=" + payBase + ", companyPay=" + companyPay
				+ ", companyProportion=" + companyProportion + ", personalPay=" + personalPay + ", personnelProportion="
				+ personnelProportion + ", financialAid=" + financialAid + ", financialAidProportion="
				+ financialAidProportion + "]";
	}
	public InsuranceDongguanGeneral(String taskid, String company, String payMonth, String insuranceType,
			String payBase, String companyPay, String companyProportion, String personalPay, String personnelProportion,
			String financialAid, String financialAidProportion) {
		super();
		this.taskid = taskid;
		this.company = company;
		this.payMonth = payMonth;
		this.insuranceType = insuranceType;
		this.payBase = payBase;
		this.companyPay = companyPay;
		this.companyProportion = companyProportion;
		this.personalPay = personalPay;
		this.personnelProportion = personnelProportion;
		this.financialAid = financialAid;
		this.financialAidProportion = financialAidProportion;
	}
	public InsuranceDongguanGeneral() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
