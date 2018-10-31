package com.microservice.dao.entity.crawler.housing.suqian;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 宿迁公积金用户信息
 */
@Entity
@Table(name="housing_suqian_userinfo")
public class HousingSuQianUserInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = 1221206979457794498L;

	private String username;// 客户姓名
	private String userAccount;// 个人账号
	private String idtype;// 证件类型
	private String idnum;// 身份证号码
	private String telephone;// 手机号
	private String companyTel;// 单位电话
	private String companyAccount;// 单位账号
	private String companyName;// 单位名称
	private String cardbankName;// 联名卡发卡行
	private String cardbankNum;// 联名卡号码
	private String staffName;// 所属管理部
	private String manageBank;// 经办银行
	private String state;// 汇缴状态
	private String openDate;// 开户日期
	private String firstpayMonth;// 起缴年月
	private String lastpayMonth;// 缴至年月
	private String companyRatio;// 单位缴存比例
	private String personRatio;// 个人缴存比例

	private String supplyRatio;// 补贴缴存比例
	private String payBase;// 缴存基数
	private String monthpayAmount;// 月缴存额
	private String balance;// 账户余额
	private String isLoan;// 是否贷款
	private String isMortgage;// 是否抵押

	private String isFreeze;// 是否冻结
	private String isDeduction;// 是否按月抵扣
	private String familyAddress;// 家庭住址
	private String workAddress;// 单位地址

	private String taskid;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String getIdtype() {
		return idtype;
	}

	public void setIdtype(String idtype) {
		this.idtype = idtype;
	}

	public String getIdnum() {
		return idnum;
	}

	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getCompanyTel() {
		return companyTel;
	}

	public void setCompanyTel(String companyTel) {
		this.companyTel = companyTel;
	}

	public String getCompanyAccount() {
		return companyAccount;
	}

	public void setCompanyAccount(String companyAccount) {
		this.companyAccount = companyAccount;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCardbankName() {
		return cardbankName;
	}

	public void setCardbankName(String cardbankName) {
		this.cardbankName = cardbankName;
	}

	public String getCardbankNum() {
		return cardbankNum;
	}

	public void setCardbankNum(String cardbankNum) {
		this.cardbankNum = cardbankNum;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getManageBank() {
		return manageBank;
	}

	public void setManageBank(String manageBank) {
		this.manageBank = manageBank;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getOpenDate() {
		return openDate;
	}

	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}

	public String getFirstpayMonth() {
		return firstpayMonth;
	}

	public void setFirstpayMonth(String firstpayMonth) {
		this.firstpayMonth = firstpayMonth;
	}

	public String getLastpayMonth() {
		return lastpayMonth;
	}

	public void setLastpayMonth(String lastpayMonth) {
		this.lastpayMonth = lastpayMonth;
	}

	public String getCompanyRatio() {
		return companyRatio;
	}

	public void setCompanyRatio(String companyRatio) {
		this.companyRatio = companyRatio;
	}

	public String getPersonRatio() {
		return personRatio;
	}

	public void setPersonRatio(String personRatio) {
		this.personRatio = personRatio;
	}

	public String getSupplyRatio() {
		return supplyRatio;
	}

	public void setSupplyRatio(String supplyRatio) {
		this.supplyRatio = supplyRatio;
	}

	public String getPayBase() {
		return payBase;
	}

	public void setPayBase(String payBase) {
		this.payBase = payBase;
	}

	public String getMonthpayAmount() {
		return monthpayAmount;
	}

	public void setMonthpayAmount(String monthpayAmount) {
		this.monthpayAmount = monthpayAmount;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getIsLoan() {
		return isLoan;
	}

	public void setIsLoan(String isLoan) {
		this.isLoan = isLoan;
	}

	public String getIsMortgage() {
		return isMortgage;
	}

	public void setIsMortgage(String isMortgage) {
		this.isMortgage = isMortgage;
	}

	public String getIsFreeze() {
		return isFreeze;
	}

	public void setIsFreeze(String isFreeze) {
		this.isFreeze = isFreeze;
	}

	public String getIsDeduction() {
		return isDeduction;
	}

	public void setIsDeduction(String isDeduction) {
		this.isDeduction = isDeduction;
	}

	public String getFamilyAddress() {
		return familyAddress;
	}

	public void setFamilyAddress(String familyAddress) {
		this.familyAddress = familyAddress;
	}

	public String getWorkAddress() {
		return workAddress;
	}

	public void setWorkAddress(String workAddress) {
		this.workAddress = workAddress;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	@Override
	public String toString() {
		return "HousingSuQianUserInfo [username=" + username + ", userAccount=" + userAccount + ", idtype=" + idtype
				+ ", idnum=" + idnum + ", telephone=" + telephone + ", companyTel=" + companyTel + ", companyAccount="
				+ companyAccount + ", companyName=" + companyName + ", cardbankName=" + cardbankName + ", cardbankNum="
				+ cardbankNum + ", staffName=" + staffName + ", manageBank=" + manageBank + ", state=" + state
				+ ", openDate=" + openDate + ", firstpayMonth=" + firstpayMonth + ", lastpayMonth=" + lastpayMonth
				+ ", companyRatio=" + companyRatio + ", personRatio=" + personRatio + ", supplyRatio=" + supplyRatio
				+ ", payBase=" + payBase + ", monthpayAmount=" + monthpayAmount + ", balance=" + balance + ", isLoan="
				+ isLoan + ", isMortgage=" + isMortgage + ", isFreeze=" + isFreeze + ", isDeduction=" + isDeduction
				+ ", familyAddress=" + familyAddress + ", workAddress=" + workAddress + ", taskid=" + taskid + ", id="
				+ id + ", createtime=" + createtime + ", getUsername()=" + getUsername() + ", getUserAccount()="
				+ getUserAccount() + ", getIdtype()=" + getIdtype() + ", getIdnum()=" + getIdnum() + ", getTelephone()="
				+ getTelephone() + ", getCompanyTel()=" + getCompanyTel() + ", getCompanyAccount()="
				+ getCompanyAccount() + ", getCompanyName()=" + getCompanyName() + ", getCardbankName()="
				+ getCardbankName() + ", getCardbankNum()=" + getCardbankNum() + ", getStaffName()=" + getStaffName()
				+ ", getManageBank()=" + getManageBank() + ", getState()=" + getState() + ", getOpenDate()="
				+ getOpenDate() + ", getFirstpayMonth()=" + getFirstpayMonth() + ", getLastpayMonth()="
				+ getLastpayMonth() + ", getCompanyRatio()=" + getCompanyRatio() + ", getPersonRatio()="
				+ getPersonRatio() + ", getSupplyRatio()=" + getSupplyRatio() + ", getPayBase()=" + getPayBase()
				+ ", getMonthpayAmount()=" + getMonthpayAmount() + ", getBalance()=" + getBalance() + ", getIsLoan()="
				+ getIsLoan() + ", getIsMortgage()=" + getIsMortgage() + ", getIsFreeze()=" + getIsFreeze()
				+ ", getIsDeduction()=" + getIsDeduction() + ", getFamilyAddress()=" + getFamilyAddress()
				+ ", getWorkAddress()=" + getWorkAddress() + ", getTaskid()=" + getTaskid() + ", getId()=" + getId()
				+ ", getCreatetime()=" + getCreatetime() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
    
	public HousingSuQianUserInfo(String username, String userAccount, String idtype, String idnum, String telephone,
			String companyTel, String companyAccount, String companyName, String cardbankName, String cardbankNum,
			String staffName, String manageBank, String state, String openDate, String firstpayMonth,
			String lastpayMonth, String companyRatio, String personRatio, String supplyRatio, String payBase,
			String monthpayAmount, String balance, String isLoan, String isMortgage, String isFreeze,
			String isDeduction, String familyAddress, String workAddress, String taskid) {
		super();
		this.username = username;
		this.userAccount = userAccount;
		this.idtype = idtype;
		this.idnum = idnum;
		this.telephone = telephone;
		this.companyTel = companyTel;
		this.companyAccount = companyAccount;
		this.companyName = companyName;
		this.cardbankName = cardbankName;
		this.cardbankNum = cardbankNum;
		this.staffName = staffName;
		this.manageBank = manageBank;
		this.state = state;
		this.openDate = openDate;
		this.firstpayMonth = firstpayMonth;
		this.lastpayMonth = lastpayMonth;
		this.companyRatio = companyRatio;
		this.personRatio = personRatio;
		this.supplyRatio = supplyRatio;
		this.payBase = payBase;
		this.monthpayAmount = monthpayAmount;
		this.balance = balance;
		this.isLoan = isLoan;
		this.isMortgage = isMortgage;
		this.isFreeze = isFreeze;
		this.isDeduction = isDeduction;
		this.familyAddress = familyAddress;
		this.workAddress = workAddress;
		this.taskid = taskid;
	}

	public HousingSuQianUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
}
