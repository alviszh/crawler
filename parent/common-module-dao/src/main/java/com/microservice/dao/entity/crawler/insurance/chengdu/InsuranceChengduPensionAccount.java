package com.microservice.dao.entity.crawler.insurance.chengdu;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;


/**
 * 成都社保:养老保险个人账户明细信息
 * @author tz
 *
 */
@Entity
@Table(name = "insurance_chengdu_pensionaccount" ,indexes = {@Index(name = "index_insurance_chengdu_pensionaccount_taskid", columnList = "taskid")})
public class InsuranceChengduPensionAccount extends IdEntity{
	
	/**
	 * uuid 前端通过uuid访问状态结果	
	 */
	private String taskid;
	
	/**
	 * 年度
	 */
	private String year;				
	/**
	 * 截至上年末累计缴费月数
	 */
	private String months;							
	/**
	 * 截止上年末个人帐户单位划拨部分累计本息
	 */
	private String yearCompanySum;
	/**
	 * 截止上年末个人帐户个人缴费部分累计本息
	 */
	private String yearPersonalSum;						
	/**
	 * 本年累计金额
	 */
	private String moneySum;
	/**
	 * 本年单位划入账户金额
	 */
	private String companyBalance;	
	/**
	 *本年个人划入账户金额
	 */
	private String personnelBalance;
	/**
	 * 本年缴纳单位缴费划拨部分本年计入利息
	 */
	private String yearCompanyInterest;	
	/**
	 * 本年缴纳帐户个人缴费部分本年计入利息
	 */
	private String yearPersonalInterest;
	
	/**
	 * 截止上年末个人帐户个人缴费部分累计本息在本年产生的利息
	 */
	private String lateLastYearPersonalInterest;					
	/**
	 * 截止上年末个人帐户单位划拨部分累计本息在本年产生的利息
	 */
	private String lateLastYearCompanyInterest;				
	/**
	 * 本年补退历年缴费月数
	 */
	private String yearRepairMonths;
	/**
	 * 本年补退历年单位划入累计本息
	 */
	private String yearRepairCompanySum;
	/**
	 * 本年补退历年个人划入累计本息
	 */
	private String yearRepairPersonalSum;
	
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonths() {
		return months;
	}
	public void setMonths(String months) {
		this.months = months;
	}
	public String getYearCompanySum() {
		return yearCompanySum;
	}
	public void setYearCompanySum(String yearCompanySum) {
		this.yearCompanySum = yearCompanySum;
	}
	public String getYearPersonalSum() {
		return yearPersonalSum;
	}
	public void setYearPersonalSum(String yearPersonalSum) {
		this.yearPersonalSum = yearPersonalSum;
	}
	public String getMoneySum() {
		return moneySum;
	}
	public void setMoneySum(String moneySum) {
		this.moneySum = moneySum;
	}
	public String getCompanyBalance() {
		return companyBalance;
	}
	public void setCompanyBalance(String companyBalance) {
		this.companyBalance = companyBalance;
	}
	public String getPersonnelBalance() {
		return personnelBalance;
	}
	public void setPersonnelBalance(String personnelBalance) {
		this.personnelBalance = personnelBalance;
	}
	public String getYearCompanyInterest() {
		return yearCompanyInterest;
	}
	public void setYearCompanyInterest(String yearCompanyInterest) {
		this.yearCompanyInterest = yearCompanyInterest;
	}
	public String getYearPersonalInterest() {
		return yearPersonalInterest;
	}
	public void setYearPersonalInterest(String yearPersonalInterest) {
		this.yearPersonalInterest = yearPersonalInterest;
	}
	public String getLateLastYearPersonalInterest() {
		return lateLastYearPersonalInterest;
	}
	public void setLateLastYearPersonalInterest(String lateLastYearPersonalInterest) {
		this.lateLastYearPersonalInterest = lateLastYearPersonalInterest;
	}
	public String getLateLastYearCompanyInterest() {
		return lateLastYearCompanyInterest;
	}
	public void setLateLastYearCompanyInterest(String lateLastYearCompanyInterest) {
		this.lateLastYearCompanyInterest = lateLastYearCompanyInterest;
	}
	public String getYearRepairMonths() {
		return yearRepairMonths;
	}
	public void setYearRepairMonths(String yearRepairMonths) {
		this.yearRepairMonths = yearRepairMonths;
	}
	public String getYearRepairCompanySum() {
		return yearRepairCompanySum;
	}
	public void setYearRepairCompanySum(String yearRepairCompanySum) {
		this.yearRepairCompanySum = yearRepairCompanySum;
	}
	public String getYearRepairPersonalSum() {
		return yearRepairPersonalSum;
	}
	public void setYearRepairPersonalSum(String yearRepairPersonalSum) {
		this.yearRepairPersonalSum = yearRepairPersonalSum;
	}
	@Override
	public String toString() {
		return "InsuranceChengduPensionAccount [taskid=" + taskid + ", year=" + year + ", months=" + months
				+ ", yearCompanySum=" + yearCompanySum + ", yearPersonalSum=" + yearPersonalSum + ", moneySum="
				+ moneySum + ", companyBalance=" + companyBalance + ", personnelBalance=" + personnelBalance
				+ ", yearCompanyInterest=" + yearCompanyInterest + ", yearPersonalInterest=" + yearPersonalInterest
				+ ", lateLastYearPersonalInterest=" + lateLastYearPersonalInterest + ", lateLastYearCompanyInterest="
				+ lateLastYearCompanyInterest + ", yearRepairMonths=" + yearRepairMonths + ", yearRepairCompanySum="
				+ yearRepairCompanySum + ", yearRepairPersonalSum=" + yearRepairPersonalSum + "]";
	}
	public InsuranceChengduPensionAccount(String taskid, String year, String months, String yearCompanySum,
			String yearPersonalSum, String moneySum, String companyBalance, String personnelBalance,
			String yearCompanyInterest, String yearPersonalInterest, String lateLastYearPersonalInterest,
			String lateLastYearCompanyInterest, String yearRepairMonths, String yearRepairCompanySum,
			String yearRepairPersonalSum) {
		super();
		this.taskid = taskid;
		this.year = year;
		this.months = months;
		this.yearCompanySum = yearCompanySum;
		this.yearPersonalSum = yearPersonalSum;
		this.moneySum = moneySum;
		this.companyBalance = companyBalance;
		this.personnelBalance = personnelBalance;
		this.yearCompanyInterest = yearCompanyInterest;
		this.yearPersonalInterest = yearPersonalInterest;
		this.lateLastYearPersonalInterest = lateLastYearPersonalInterest;
		this.lateLastYearCompanyInterest = lateLastYearCompanyInterest;
		this.yearRepairMonths = yearRepairMonths;
		this.yearRepairCompanySum = yearRepairCompanySum;
		this.yearRepairPersonalSum = yearRepairPersonalSum;
	}
	public InsuranceChengduPensionAccount() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
