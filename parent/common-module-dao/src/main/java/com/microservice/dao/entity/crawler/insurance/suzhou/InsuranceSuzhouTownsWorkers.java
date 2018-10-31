package com.microservice.dao.entity.crawler.insurance.suzhou;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_suzhou_TownsWorkers")
public class InsuranceSuzhouTownsWorkers extends IdEntity{
	
	@Column(name="task_id")
	private String taskid;	//uuid唯一标识
	private String settlementTime;	//结算期
	private String companyName;	//单位名称
	private String supplementType;//补充医保类型
	private String payBase;	//缴费基数
	private String months;	//月数
	private String companyMoney;	//单位缴纳金额
	private String basic;	//基本医疗保险
	private String moneyByTimePersonalDate;		//补充医疗保险
	private String grand;      //大额共计基金
	private String account; //  计入个人账户金额
	private String arrivalFlag; //到账标记 
	private String arrivalTime; //到账日期
	
	
	
	
	
	public String getSupplementType() {
		return supplementType;
	}
	public void setSupplementType(String supplementType) {
		this.supplementType = supplementType;
	}
	public String getArrivalTime() {
		return arrivalTime;
	}
	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	public String getSettlementTime() {
		return settlementTime;
	}
	public void setSettlementTime(String settlementTime) {
		this.settlementTime = settlementTime;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getPayBase() {
		return payBase;
	}
	public void setPayBase(String payBase) {
		this.payBase = payBase;
	}
	public String getMonths() {
		return months;
	}
	public void setMonths(String months) {
		this.months = months;
	}
	public String getCompanyMoney() {
		return companyMoney;
	}
	public void setCompanyMoney(String companyMoney) {
		this.companyMoney = companyMoney;
	}
	public String getBasic() {
		return basic;
	}
	public void setBasic(String basic) {
		this.basic = basic;
	}
	public String getGrand() {
		return grand;
	}
	public void setGrand(String grand) {
		this.grand = grand;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getArrivalFlag() {
		return arrivalFlag;
	}
	public void setArrivalFlag(String arrivalFlag) {
		this.arrivalFlag = arrivalFlag;
	}
	public void setMoneyByTimePersonalDate(String moneyByTimePersonalDate) {
		this.moneyByTimePersonalDate = moneyByTimePersonalDate;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getMoneyByTimePersonalDate() {
		return moneyByTimePersonalDate;
	}
	
}
