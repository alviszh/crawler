package com.microservice.dao.entity.crawler.housing.fuzhou2;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_fuzhou2_basic")
public class HousingFuzhou2Basic extends IdEntity implements Serializable{

	
	private String taskid;
	private String name;//姓名
	private String birthday;//出生年月
	private String cardType;//个人证件类型
	private String cardNum;//个人证件号
	private String accountType;//账号类型
	private String mobilePhone;//移动电话
	private String homePhone;//家庭电话
	private String homeAddress;//家庭地址
	private String companyPhone;//单位电话
	private String postOfficeCode;//邮政编码
	private String jointCardCode;//联名卡卡号
	private String accumulatedOverTheYears;//历年贷方累计
	private String annualBorrowerTotal;//年借方累计
	private String lastYearBalance;//上年结转余额
	private String yearLendersTotal;//年贷方累计
	private String liAnnualBorrowerTotal;//历年借方累计
	private String payYear;//缴至年月
	private String realIncome;//实际收入
	private String companyPay;//单位缴交率
	private String personPay;//个人缴交率
	private String companyPayable;//单位应缴额
	private String personPayable;//职工应缴额
	private String monthPayable;//月应缴额
	private String dataUpdateDate;//数据更新日期
	private String housingFundNum;//个人公积金账号
	private String openAccountDate;//开户日期
	private String housingAccountStatus;//公积金账户状态
	private String housingAccountBalance;//公积金账户余额
	private String personSubsidyNum;//个人补贴账号
	private String companyHousingNum;//单位公积金账号;
	private String subsidyAccountStatus;//补贴账户状态
	private String dataUpdateDateAccount;//本账户数据更新日期
	private String subsidyAccountBalance;//补贴账户余额
	private String housingMonthWages;//公积金核定月工资额
	private String subsidyMonthWages;//补贴核定月工资额
	private String housingMonthPay;//公积金核定月缴交额
	private String subsidyMonthPay;//补贴核定月缴交额
	private String freezeMark;//冻结标志
	private String companyName;//单位名称
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getHomePhone() {
		return homePhone;
	}
	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}
	public String getHomeAddress() {
		return homeAddress;
	}
	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}
	public String getCompanyPhone() {
		return companyPhone;
	}
	public void setCompanyPhone(String companyPhone) {
		this.companyPhone = companyPhone;
	}
	public String getPostOfficeCode() {
		return postOfficeCode;
	}
	public void setPostOfficeCode(String postOfficeCode) {
		this.postOfficeCode = postOfficeCode;
	}
	public String getJointCardCode() {
		return jointCardCode;
	}
	public void setJointCardCode(String jointCardCode) {
		this.jointCardCode = jointCardCode;
	}
	public String getAccumulatedOverTheYears() {
		return accumulatedOverTheYears;
	}
	public void setAccumulatedOverTheYears(String accumulatedOverTheYears) {
		this.accumulatedOverTheYears = accumulatedOverTheYears;
	}
	public String getAnnualBorrowerTotal() {
		return annualBorrowerTotal;
	}
	public void setAnnualBorrowerTotal(String annualBorrowerTotal) {
		this.annualBorrowerTotal = annualBorrowerTotal;
	}
	public String getLastYearBalance() {
		return lastYearBalance;
	}
	public void setLastYearBalance(String lastYearBalance) {
		this.lastYearBalance = lastYearBalance;
	}
	public String getYearLendersTotal() {
		return yearLendersTotal;
	}
	public void setYearLendersTotal(String yearLendersTotal) {
		this.yearLendersTotal = yearLendersTotal;
	}
	public String getLiAnnualBorrowerTotal() {
		return liAnnualBorrowerTotal;
	}
	public void setLiAnnualBorrowerTotal(String liAnnualBorrowerTotal) {
		this.liAnnualBorrowerTotal = liAnnualBorrowerTotal;
	}
	public String getPayYear() {
		return payYear;
	}
	public void setPayYear(String payYear) {
		this.payYear = payYear;
	}
	public String getRealIncome() {
		return realIncome;
	}
	public void setRealIncome(String realIncome) {
		this.realIncome = realIncome;
	}
	public String getCompanyPay() {
		return companyPay;
	}
	public void setCompanyPay(String companyPay) {
		this.companyPay = companyPay;
	}
	public String getPersonPay() {
		return personPay;
	}
	public void setPersonPay(String personPay) {
		this.personPay = personPay;
	}
	public String getCompanyPayable() {
		return companyPayable;
	}
	public void setCompanyPayable(String companyPayable) {
		this.companyPayable = companyPayable;
	}
	public String getPersonPayable() {
		return personPayable;
	}
	public void setPersonPayable(String personPayable) {
		this.personPayable = personPayable;
	}
	public String getMonthPayable() {
		return monthPayable;
	}
	public void setMonthPayable(String monthPayable) {
		this.monthPayable = monthPayable;
	}
	public String getDataUpdateDate() {
		return dataUpdateDate;
	}
	public void setDataUpdateDate(String dataUpdateDate) {
		this.dataUpdateDate = dataUpdateDate;
	}
	public String getHousingFundNum() {
		return housingFundNum;
	}
	public void setHousingFundNum(String housingFundNum) {
		this.housingFundNum = housingFundNum;
	}
	public String getOpenAccountDate() {
		return openAccountDate;
	}
	public void setOpenAccountDate(String openAccountDate) {
		this.openAccountDate = openAccountDate;
	}
	public String getHousingAccountStatus() {
		return housingAccountStatus;
	}
	public void setHousingAccountStatus(String housingAccountStatus) {
		this.housingAccountStatus = housingAccountStatus;
	}
	public String getHousingAccountBalance() {
		return housingAccountBalance;
	}
	public void setHousingAccountBalance(String housingAccountBalance) {
		this.housingAccountBalance = housingAccountBalance;
	}
	public String getPersonSubsidyNum() {
		return personSubsidyNum;
	}
	public void setPersonSubsidyNum(String personSubsidyNum) {
		this.personSubsidyNum = personSubsidyNum;
	}
	public String getCompanyHousingNum() {
		return companyHousingNum;
	}
	public void setCompanyHousingNum(String companyHousingNum) {
		this.companyHousingNum = companyHousingNum;
	}
	public String getSubsidyAccountStatus() {
		return subsidyAccountStatus;
	}
	public void setSubsidyAccountStatus(String subsidyAccountStatus) {
		this.subsidyAccountStatus = subsidyAccountStatus;
	}
	public String getDataUpdateDateAccount() {
		return dataUpdateDateAccount;
	}
	public void setDataUpdateDateAccount(String dataUpdateDateAccount) {
		this.dataUpdateDateAccount = dataUpdateDateAccount;
	}
	public String getSubsidyAccountBalance() {
		return subsidyAccountBalance;
	}
	public void setSubsidyAccountBalance(String subsidyAccountBalance) {
		this.subsidyAccountBalance = subsidyAccountBalance;
	}
	public String getHousingMonthWages() {
		return housingMonthWages;
	}
	public void setHousingMonthWages(String housingMonthWages) {
		this.housingMonthWages = housingMonthWages;
	}
	public String getSubsidyMonthWages() {
		return subsidyMonthWages;
	}
	public void setSubsidyMonthWages(String subsidyMonthWages) {
		this.subsidyMonthWages = subsidyMonthWages;
	}
	public String getHousingMonthPay() {
		return housingMonthPay;
	}
	public void setHousingMonthPay(String housingMonthPay) {
		this.housingMonthPay = housingMonthPay;
	}
	public String getSubsidyMonthPay() {
		return subsidyMonthPay;
	}
	public void setSubsidyMonthPay(String subsidyMonthPay) {
		this.subsidyMonthPay = subsidyMonthPay;
	}
	public String getFreezeMark() {
		return freezeMark;
	}
	public void setFreezeMark(String freezeMark) {
		this.freezeMark = freezeMark;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	
}
