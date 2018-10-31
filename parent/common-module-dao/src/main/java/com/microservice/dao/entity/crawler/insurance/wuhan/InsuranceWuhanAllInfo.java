package com.microservice.dao.entity.crawler.insurance.wuhan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_wuhan_all")
public class InsuranceWuhanAllInfo extends IdEntity{
	
	@Column(name="task_id")
	private String taskid;	//uuid唯一标识
	private String type; //险种类型
	private String payTime; //缴费年月
	private String ledger;//台账年月
	private String payType;//应缴类型
	private String payMode;//缴费方式
	private String payBase;//缴费基数
	private String payMoney;//应缴金额
	private String personalPayMoney; //个人缴费金额
	private String personalAccountMoney;//划入个人账户金额
	private String integratedPlanningMoney;//划入统筹金额;
	private String backInterest;  //补缴利息
	private String lateFee;//滞纳金
	private String payFlag; //缴费标识
	private String companyNum;//单位编号
	private String companyName;//单位名称
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	public String getLedger() {
		return ledger;
	}
	public void setLedger(String ledger) {
		this.ledger = ledger;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getPayMode() {
		return payMode;
	}
	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}
	public String getPayBase() {
		return payBase;
	}	
	public void setPayBase(String payBase) {
		this.payBase = payBase;
	}
	public String getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(String payMoney) {
		this.payMoney = payMoney;
	}
	public String getPersonalPayMoney() {
		return personalPayMoney;
	}
	public void setPersonalPayMoney(String personalPayMoney) {
		this.personalPayMoney = personalPayMoney;
	}
	public String getPersonalAccountMoney() {
		return personalAccountMoney;
	}
	public void setPersonalAccountMoney(String personalAccountMoney) {
		this.personalAccountMoney = personalAccountMoney;
	}
	public String getIntegratedPlanningMoney() {
		return integratedPlanningMoney;
	}
	public void setIntegratedPlanningMoney(String integratedPlanningMoney) {
		this.integratedPlanningMoney = integratedPlanningMoney;
	}
	public String getBackInterest() {
		return backInterest;
	}
	public void setBackInterest(String backInterest) {
		this.backInterest = backInterest;
	}
	public String getLateFee() {
		return lateFee;
	}
	public void setLateFee(String lateFee) {
		this.lateFee = lateFee;
	}
	public String getPayFlag() {
		return payFlag;
	}
	public void setPayFlag(String payFlag) {
		this.payFlag = payFlag;
	}
	public String getCompanyNum() {
		return companyNum;
	}
	public void setCompanyNum(String companyNum) {
		this.companyNum = companyNum;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	
	
	
	
	
	
	
}
