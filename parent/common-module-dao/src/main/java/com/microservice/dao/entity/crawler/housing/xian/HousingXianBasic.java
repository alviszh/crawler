package com.microservice.dao.entity.crawler.housing.xian;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_xian_basic")
public class HousingXianBasic extends IdEntity implements Serializable{

	
	private String taskid;
	private String workNum;//职工账号
	private String monthBase;//月缴基数
	private String companyNum;//单位账号
	private String office;//所属办事处
	private String company;//所在单位 
	private String bank;//开户银行
	private String date;//开户日期
	private String nowState;//当前状态
	private String personPay;//个人缴存比例
	private String companyPay;//单位缴存比例
	private String lastYearBalance;//上年余额(含上年利息)
	private String yearPay;//本年缴交
	private String externalTransfer;//外部转入
	private String yearDraw;//本年支取
	private String lastYearInterest;//上年利息
	private String balance;//余额
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getWorkNum() {
		return workNum;
	}
	public void setWorkNum(String workNum) {
		this.workNum = workNum;
	}
	public String getMonthBase() {
		return monthBase;
	}
	public void setMonthBase(String monthBase) {
		this.monthBase = monthBase;
	}
	public String getCompanyNum() {
		return companyNum;
	}
	public void setCompanyNum(String companyNum) {
		this.companyNum = companyNum;
	}
	public String getOffice() {
		return office;
	}
	public void setOffice(String office) {
		this.office = office;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getNowState() {
		return nowState;
	}
	public void setNowState(String nowState) {
		this.nowState = nowState;
	}
	public String getPersonPay() {
		return personPay;
	}
	public void setPersonPay(String personPay) {
		this.personPay = personPay;
	}
	public String getCompanyPay() {
		return companyPay;
	}
	public void setCompanyPay(String companyPay) {
		this.companyPay = companyPay;
	}
	public String getLastYearBalance() {
		return lastYearBalance;
	}
	public void setLastYearBalance(String lastYearBalance) {
		this.lastYearBalance = lastYearBalance;
	}
	public String getYearPay() {
		return yearPay;
	}
	public void setYearPay(String yearPay) {
		this.yearPay = yearPay;
	}
	public String getExternalTransfer() {
		return externalTransfer;
	}
	public void setExternalTransfer(String externalTransfer) {
		this.externalTransfer = externalTransfer;
	}
	public String getYearDraw() {
		return yearDraw;
	}
	public void setYearDraw(String yearDraw) {
		this.yearDraw = yearDraw;
	}
	public String getLastYearInterest() {
		return lastYearInterest;
	}
	public void setLastYearInterest(String lastYearInterest) {
		this.lastYearInterest = lastYearInterest;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	
	
}
