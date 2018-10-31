package com.microservice.dao.entity.crawler.housing.ningbo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_ningbo_userinfo",indexes = {@Index(name = "index_housing_ningbo_userinfo_taskid", columnList = "taskid")})
public class HousingNingboUserInfo extends IdEntity implements Serializable{

	private String unitaccname;//单位名称
	
	private String accname;//姓名
	
	private String cardtype;//证件类型
	
	private String organization;//账户机构
	
	private String personalId;//个人公积金账号
	
	private String accountStatus;//账户状态
	
	private String saveBase;//缴存基数
	
	private String fee;//余额
	
	private String freezeStatus;//冻结状态
	
	private String freezeMoney;//冻结金额
	
	private String housingFundMonth;//公积金月缴存额
	
	private String subsidy;//按月补贴月缴存额
	
	private String moneySubsidy;//住房货币补贴（暂）月缴存额
	
	private String disposableMonth;// 一次性住房补贴月缴存额
	
	private String openDate;// 开户日期
	
	private String nowDate;//缴至年月
	
	private String housingFundFee;//公积金余额
	
	private String monthFee;//按月补贴余额
	
 	private String houseFee;//住房货币补贴（暂）余额
	
	private String disposableHousing;//一次性住房补贴余额
	
	private String taskid;

	@Override
	public String toString() {
		return "HousingNingboUserInfo [unitaccname=" + unitaccname + ", accname=" + accname + ", cardtype=" + cardtype
				+ ", organization=" + organization + ", personalId=" + personalId + ", accountStatus=" + accountStatus
				+ ", saveBase=" + saveBase + ", fee=" + fee + ", freezeStatus=" + freezeStatus + ", freezeMoney="
				+ freezeMoney + ", housingFundMonth=" + housingFundMonth + ", subsidy=" + subsidy + ", moneySubsidy="
				+ moneySubsidy + ", disposableMonth=" + disposableMonth + ", openDate=" + openDate + ", nowDate="
				+ nowDate + ", housingFundFee=" + housingFundFee + ", monthFee=" + monthFee + ", houseFee=" + houseFee
				+ ", disposableHousing=" + disposableHousing + ", taskid=" + taskid + "]";
	}

	public String getUnitaccname() {
		return unitaccname;
	}

	public void setUnitaccname(String unitaccname) {
		this.unitaccname = unitaccname;
	}

	public String getAccname() {
		return accname;
	}

	public void setAccname(String accname) {
		this.accname = accname;
	}

	public String getCardtype() {
		return cardtype;
	}

	public void setCardtype(String cardtype) {
		this.cardtype = cardtype;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getPersonalId() {
		return personalId;
	}

	public void setPersonalId(String personalId) {
		this.personalId = personalId;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	public String getSaveBase() {
		return saveBase;
	}

	public void setSaveBase(String saveBase) {
		this.saveBase = saveBase;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getFreezeStatus() {
		return freezeStatus;
	}

	public void setFreezeStatus(String freezeStatus) {
		this.freezeStatus = freezeStatus;
	}

	public String getFreezeMoney() {
		return freezeMoney;
	}

	public void setFreezeMoney(String freezeMoney) {
		this.freezeMoney = freezeMoney;
	}

	public String getHousingFundMonth() {
		return housingFundMonth;
	}

	public void setHousingFundMonth(String housingFundMonth) {
		this.housingFundMonth = housingFundMonth;
	}

	public String getSubsidy() {
		return subsidy;
	}

	public void setSubsidy(String subsidy) {
		this.subsidy = subsidy;
	}

	public String getMoneySubsidy() {
		return moneySubsidy;
	}

	public void setMoneySubsidy(String moneySubsidy) {
		this.moneySubsidy = moneySubsidy;
	}

	public String getDisposableMonth() {
		return disposableMonth;
	}

	public void setDisposableMonth(String disposableMonth) {
		this.disposableMonth = disposableMonth;
	}

	public String getOpenDate() {
		return openDate;
	}

	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}

	public String getNowDate() {
		return nowDate;
	}

	public void setNowDate(String nowDate) {
		this.nowDate = nowDate;
	}

	public String getHousingFundFee() {
		return housingFundFee;
	}

	public void setHousingFundFee(String housingFundFee) {
		this.housingFundFee = housingFundFee;
	}

	public String getMonthFee() {
		return monthFee;
	}

	public void setMonthFee(String monthFee) {
		this.monthFee = monthFee;
	}

	public String getHouseFee() {
		return houseFee;
	}

	public void setHouseFee(String houseFee) {
		this.houseFee = houseFee;
	}

	public String getDisposableHousing() {
		return disposableHousing;
	}

	public void setDisposableHousing(String disposableHousing) {
		this.disposableHousing = disposableHousing;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
}
