package com.microservice.dao.entity.crawler.insurance.chengdu;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

/**
 * 成都社保总结
 * @author Administrator
 *
 */
@Entity
@Table(name = "insurance_chengdu_summary" ,indexes = {@Index(name = "index_insurance_chengdu_summary_taskid", columnList = "taskid")})
public class InsuranceChengduSummary extends IdEntity{
	
	/**
	 * uuid 前端通过uuid访问状态结果	
	 */
	private String taskid;
	
	/**
	 * 险种类型
	 */
	private String insuranceType;				
	/**
	 * 参保单位编号
	 */
	private String companyNum;				
	/**
	 * 参保单位名称
	 */
	private String company;							
	/**
	 * 参保状态
	 */
	private String insuranceState;								
	/**
	 * 初次参保日期
	 */
	private String firstInsuranceDate;
	/**
	 * 人员缴费类别
	 */
	private String personnelPayType;	
	/**
	 *参保经办机构
	 */
	private String handlingAgency;
	
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getInsuranceType() {
		return insuranceType;
	}
	public void setInsuranceType(String insuranceType) {
		this.insuranceType = insuranceType;
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
	public String getInsuranceState() {
		return insuranceState;
	}
	public void setInsuranceState(String insuranceState) {
		this.insuranceState = insuranceState;
	}
	public String getFirstInsuranceDate() {
		return firstInsuranceDate;
	}
	public void setFirstInsuranceDate(String firstInsuranceDate) {
		this.firstInsuranceDate = firstInsuranceDate;
	}
	public String getPersonnelPayType() {
		return personnelPayType;
	}
	public void setPersonnelPayType(String personnelPayType) {
		this.personnelPayType = personnelPayType;
	}
	public String getHandlingAgency() {
		return handlingAgency;
	}
	public void setHandlingAgency(String handlingAgency) {
		this.handlingAgency = handlingAgency;
	}
	@Override
	public String toString() {
		return "InsuranceChengduSummary [taskid=" + taskid + ", insuranceType=" + insuranceType + ", companyNum="
				+ companyNum + ", company=" + company + ", insuranceState=" + insuranceState + ", firstInsuranceDate="
				+ firstInsuranceDate + ", personnelPayType=" + personnelPayType + ", handlingAgency=" + handlingAgency
				+ "]";
	}
	public InsuranceChengduSummary(String taskid, String insuranceType, String companyNum, String company,
			String insuranceState, String firstInsuranceDate, String personnelPayType, String handlingAgency) {
		super();
		this.taskid = taskid;
		this.insuranceType = insuranceType;
		this.companyNum = companyNum;
		this.company = company;
		this.insuranceState = insuranceState;
		this.firstInsuranceDate = firstInsuranceDate;
		this.personnelPayType = personnelPayType;
		this.handlingAgency = handlingAgency;
	}
	public InsuranceChengduSummary() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
