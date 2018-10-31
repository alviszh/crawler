package com.microservice.dao.entity.crawler.housing.yanbian;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 延边公积金用户信息
 */
@Entity
@Table(name="housing_yanbian_userinfo")
public class HousingYanbianUserInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = 1221206979457794498L;

	private String username;//姓名
	private String idnum;//	证件号码
	private String accountNum;//个人账号
	private String companyCode;//	单位代码
	private String sex;//	性别
	private String lastmonthpay;//	上年月平均工资额
	private String monthpay;//	月应教额
	private String balance;//	公积金余额
	private String companyPay;//	单位月缴存额
	private String personPay;//	个人月缴存额
	private String companyRatio;//	单位月缴比例
	private String personRatio;//	个人月缴比例
	private String depositSituation;//	缴存情况
	private String companyDepositSituation;//	单位缴存情况
	private String telephone;//	手机
	private String openTime;//	账户开户时间
	private String lastPaytime;//	最后缴存时间
	private String maxDefaultperiod ;//	最大连续违约期数
	private String sumNum;//	累计次数
	private String area;//	地区
	private String companyName;//	单位
	private String taskid;
	public String getAccountNum() {
		return accountNum;
	}
	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getMonthpay() {
		return monthpay;
	}
	public void setMonthpay(String monthpay) {
		this.monthpay = monthpay;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getLastmonthpay() {
		return lastmonthpay;
	}
	public void setLastmonthpay(String lastmonthpay) {
		this.lastmonthpay = lastmonthpay;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
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
	public String getDepositSituation() {
		return depositSituation;
	}
	public void setDepositSituation(String depositSituation) {
		this.depositSituation = depositSituation;
	}
	public String getCompanyDepositSituation() {
		return companyDepositSituation;
	}
	public void setCompanyDepositSituation(String companyDepositSituation) {
		this.companyDepositSituation = companyDepositSituation;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getOpenTime() {
		return openTime;
	}
	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}
	public String getLastPaytime() {
		return lastPaytime;
	}
	public void setLastPaytime(String lastPaytime) {
		this.lastPaytime = lastPaytime;
	}
	public String getMaxDefaultperiod() {
		return maxDefaultperiod;
	}
	public void setMaxDefaultperiod(String maxDefaultperiod) {
		this.maxDefaultperiod = maxDefaultperiod;
	}
	public String getSumNum() {
		return sumNum;
	}
	public void setSumNum(String sumNum) {
		this.sumNum = sumNum;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Override
	public String toString() {
		return "HousingYanBianUserInfo [username=" + username + ", idnum=" + idnum + ", accountNum=" + accountNum
				+ ", companyCode=" + companyCode + ", sex=" + sex + ", lastmonthpay=" + lastmonthpay + ", monthpay="
				+ monthpay + ", balance=" + balance + ", companyPay=" + companyPay + ", personPay=" + personPay
				+ ", companyRatio=" + companyRatio + ", personRatio=" + personRatio + ", depositSituation="
				+ depositSituation + ", companyDepositSituation=" + companyDepositSituation + ", telephone=" + telephone
				+ ", openTime=" + openTime + ", lastPaytime=" + lastPaytime + ", maxDefaultperiod=" + maxDefaultperiod
				+ ", sumNum=" + sumNum + ", area=" + area + ", companyName=" + companyName + ", taskid=" + taskid + "]";
	}
	public HousingYanbianUserInfo(String username, String idnum, String accountNum, String companyCode, String sex,
			String lastmonthpay, String monthpay, String balance, String companyPay, String personPay,
			String companyRatio, String personRatio, String depositSituation, String companyDepositSituation,
			String telephone, String openTime, String lastPaytime, String maxDefaultperiod, String sumNum, String area,
			String companyName, String taskid) {
		super();
		this.username = username;
		this.idnum = idnum;
		this.accountNum = accountNum;
		this.companyCode = companyCode;
		this.sex = sex;
		this.lastmonthpay = lastmonthpay;
		this.monthpay = monthpay;
		this.balance = balance;
		this.companyPay = companyPay;
		this.personPay = personPay;
		this.companyRatio = companyRatio;
		this.personRatio = personRatio;
		this.depositSituation = depositSituation;
		this.companyDepositSituation = companyDepositSituation;
		this.telephone = telephone;
		this.openTime = openTime;
		this.lastPaytime = lastPaytime;
		this.maxDefaultperiod = maxDefaultperiod;
		this.sumNum = sumNum;
		this.area = area;
		this.companyName = companyName;
		this.taskid = taskid;
	}
	
	public HousingYanbianUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}	
}
