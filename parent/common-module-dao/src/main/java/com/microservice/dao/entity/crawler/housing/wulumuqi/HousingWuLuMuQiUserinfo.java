package com.microservice.dao.entity.crawler.housing.wulumuqi;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name  ="housing_wulumuqi_userinfo")
public class HousingWuLuMuQiUserinfo  extends IdEntity implements Serializable {
		
	private String taskid;
	private String name;										//姓名
	private String cardType;									//证件类型
	private String idNum;										//证件号码
	private String birthday;									//出生年月
	private String gender;										//性别
	private String isMarry;										//婚姻状况
	private String postNum;										//邮政编码
	private String telNum;										//固定电话号码
	private String familyMonthIncome;							//家庭月收入
	private String address;										//家庭住址
	private String phoneNum;									//手机号码
	private String accountNum;									//账户账号
	private String accountStatus;								//账户状态
	private String accountBalance;								//账户余额
	private String openDate;									//开户日期
	private String companyName;									//单位名称
	private String loanStatus;									//贷款情况
	private String payPercent;									//缴存比例
	private String payBase;										//个人缴存基数
	private String monthPay;									//月缴存额
	private String bankName;									//个人存款账户开户银行名称
	private String bankNum;										//个人存款账户号码
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
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getIdNum() {
		return idNum;
	}
	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getIsMarry() {
		return isMarry;
	}
	public void setIsMarry(String isMarry) {
		this.isMarry = isMarry;
	}
	public String getPostNum() {
		return postNum;
	}
	public void setPostNum(String postNum) {
		this.postNum = postNum;
	}
	public String getTelNum() {
		return telNum;
	}
	public void setTelNum(String telNum) {
		this.telNum = telNum;
	}
	public String getFamilyMonthIncome() {
		return familyMonthIncome;
	}
	public void setFamilyMonthIncome(String familyMonthIncome) {
		this.familyMonthIncome = familyMonthIncome;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getAccountNum() {
		return accountNum;
	}
	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}
	public String getAccountStatus() {
		return accountStatus;
	}
	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}
	public String getAccountBalance() {
		return accountBalance;
	}
	public void setAccountBalance(String accountBalance) {
		this.accountBalance = accountBalance;
	}
	public String getOpenDate() {
		return openDate;
	}
	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getLoanStatus() {
		return loanStatus;
	}
	public void setLoanStatus(String loanStatus) {
		this.loanStatus = loanStatus;
	}
	public String getPayPercent() {
		return payPercent;
	}
	public void setPayPercent(String payPercent) {
		this.payPercent = payPercent;
	}
	public String getPayBase() {
		return payBase;
	}
	public void setPayBase(String payBase) {
		this.payBase = payBase;
	}
	public String getMonthPay() {
		return monthPay;
	}
	public void setMonthPay(String monthPay) {
		this.monthPay = monthPay;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankNum() {
		return bankNum;
	}
	public void setBankNum(String bankNum) {
		this.bankNum = bankNum;
	}
	@Override
	public String toString() {
		return "HousingZhuHaiUserinfo [taskid=" + taskid + ", name=" + name + ", cardType=" + cardType + ", idNum="
				+ idNum + ", birthday=" + birthday + ", gender=" + gender + ", isMarry=" + isMarry + ", postNum="
				+ postNum + ", telNum=" + telNum + ", familyMonthIncome=" + familyMonthIncome + ", address=" + address
				+ ", phoneNum=" + phoneNum + ", accountNum=" + accountNum + ", accountStatus=" + accountStatus
				+ ", accountBalance=" + accountBalance + ", openDate=" + openDate + ", companyName=" + companyName
				+ ", loanStatus=" + loanStatus + ", payPercent=" + payPercent + ", payBase=" + payBase + ", monthPay="
				+ monthPay + ", bankName=" + bankName + ", bankNum=" + bankNum + "]";
	}

}
