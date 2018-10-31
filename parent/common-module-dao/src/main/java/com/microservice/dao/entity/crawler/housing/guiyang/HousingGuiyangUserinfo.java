package com.microservice.dao.entity.crawler.housing.guiyang;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

/**
 * 公积金 - 贵阳 - 用户信息表
 * @author zz
 *
 */
@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name  ="housing_guiyang_userinfo",indexes = {@Index(name = "index_housing_guiyang_userinfo_taskid", columnList = "taskid")})
public class HousingGuiyangUserinfo extends IdEntity{
	
	private String taskid;
	private String companyName;				//公司名称
	private String companyAccount;			//单位账号
	private String personalAccount;			//个人公积金账户
	private	String name;					//姓名
	private String idnum;					//身份证号
	private String sex;						//性别
	private String phoneNum;				//手机号
	private String cardNum;					//卡号
	private String wageBase;				//工资基数
	private String depositAmount;			//月缴存额
	private String companyPayPercent;		//单位缴存比例
	private String employeeDepositAmount;	//职工月应缴存额
	private String openDate;				//开户日期
	private String startYear;				//起缴年月
	private String employeePayYear;			//职工汇缴年月
	private String remitStatus;				//汇缴状态
	private String managementDepartment;	//所属管理部
	private String closingAccountDate;		//汇缴销户时间
	private String isFreeze;				//是否冻结
	private String balance;					//余额
	private String isLoans;					//是否贷款
	private String companyOperator;			//单位经办人
	private String companyLegalPerson;		//单位法人
	private String companyAddress;			//单位地址
	private String employeePayPercent;		//职工缴存比例
	private String companyDepositAmount;	//单位月应缴存额
	
	public String getPersonalAccount() {
		return personalAccount;
	}
	public void setPersonalAccount(String personalAccount) {
		this.personalAccount = personalAccount;
	}
	public String getCompanyAccount() {
		return companyAccount;
	}
	public void setCompanyAccount(String companyAccount) {
		this.companyAccount = companyAccount;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public String getWageBase() {
		return wageBase;
	}
	public void setWageBase(String wageBase) {
		this.wageBase = wageBase;
	}
	public String getDepositAmount() {
		return depositAmount;
	}
	public void setDepositAmount(String depositAmount) {
		this.depositAmount = depositAmount;
	}
	public String getCompanyPayPercent() {
		return companyPayPercent;
	}
	public void setCompanyPayPercent(String companyPayPercent) {
		this.companyPayPercent = companyPayPercent;
	}
	public String getEmployeePayPercent() {
		return employeePayPercent;
	}
	public void setEmployeePayPercent(String employeePayPercent) {
		this.employeePayPercent = employeePayPercent;
	}
	public String getCompanyDepositAmount() {
		return companyDepositAmount;
	}
	public void setCompanyDepositAmount(String companyDepositAmount) {
		this.companyDepositAmount = companyDepositAmount;
	}
	public String getEmployeeDepositAmount() {
		return employeeDepositAmount;
	}
	public void setEmployeeDepositAmount(String employeeDepositAmount) {
		this.employeeDepositAmount = employeeDepositAmount;
	}
	public String getOpenDate() {
		return openDate;
	}
	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}
	public String getStartYear() {
		return startYear;
	}
	public void setStartYear(String startYear) {
		this.startYear = startYear;
	}
	public String getEmployeePayYear() {
		return employeePayYear;
	}
	public void setEmployeePayYear(String employeePayYear) {
		this.employeePayYear = employeePayYear;
	}
	public String getRemitStatus() {
		return remitStatus;
	}
	public void setRemitStatus(String remitStatus) {
		this.remitStatus = remitStatus;
	}
	public String getManagementDepartment() {
		return managementDepartment;
	}
	public void setManagementDepartment(String managementDepartment) {
		this.managementDepartment = managementDepartment;
	}
	public String getClosingAccountDate() {
		return closingAccountDate;
	}
	public void setClosingAccountDate(String closingAccountDate) {
		this.closingAccountDate = closingAccountDate;
	}
	public String getIsFreeze() {
		return isFreeze;
	}
	public void setIsFreeze(String isFreeze) {
		this.isFreeze = isFreeze;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getIsLoans() {
		return isLoans;
	}
	public void setIsLoans(String isLoans) {
		this.isLoans = isLoans;
	}
	public String getCompanyOperator() {
		return companyOperator;
	}
	public void setCompanyOperator(String companyOperator) {
		this.companyOperator = companyOperator;
	}
	public String getCompanyLegalPerson() {
		return companyLegalPerson;
	}
	public void setCompanyLegalPerson(String companyLegalPerson) {
		this.companyLegalPerson = companyLegalPerson;
	}
	public String getCompanyAddress() {
		return companyAddress;
	}
	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}
	
	
}
