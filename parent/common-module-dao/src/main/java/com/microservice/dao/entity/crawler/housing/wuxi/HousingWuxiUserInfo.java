package com.microservice.dao.entity.crawler.housing.wuxi;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 无锡公积金用户信息
 */
@Entity
@Table(name="housing_wuxi_userinfo")
public class HousingWuxiUserInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = 1221206979457794498L;
	private String accountNum;//职工账号
	private String username;//职工姓名
	private String idnum;//	证件号码
	private String companyName;//	单位名称
	private String monthpay;//	公积金月缴存额
	private String currentBalance;//	公积金账户余额
	private String monthpayBase;//	工资基数
	private String personalPay;//	个人缴比例
	private String companyPay;//	单位缴比例
	private String lastTime;//	公积金已缴年月
	private String openTime;//	公积金开户日期
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
	public String getCurrentBalance() {
		return currentBalance;
	}
	public void setCurrentBalance(String currentBalance) {
		this.currentBalance = currentBalance;
	}
	public String getMonthpayBase() {
		return monthpayBase;
	}
	public void setMonthpayBase(String monthpayBase) {
		this.monthpayBase = monthpayBase;
	}
	public String getPersonalPay() {
		return personalPay;
	}
	public void setPersonalPay(String personalPay) {
		this.personalPay = personalPay;
	}
	public String getCompanyPay() {
		return companyPay;
	}
	public void setCompanyPay(String companyPay) {
		this.companyPay = companyPay;
	}
	public String getLastTime() {
		return lastTime;
	}
	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}
	public String getOpenTime() {
		return openTime;
	}
	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	@Override
	public String toString() {
		return "HousingWuxiUserInfo [accountNum=" + accountNum + ", username=" + username + ", idnum=" + idnum
				+ ", companyName=" + companyName + ", monthpay=" + monthpay + ", currentBalance=" + currentBalance
				+ ", monthpayBase=" + monthpayBase + ", personalPay=" + personalPay + ", companyPay=" + companyPay
				+ ", lastTime=" + lastTime + ", openTime=" + openTime + ", taskid=" + taskid + "]";
	}
	public HousingWuxiUserInfo(String accountNum, String username, String idnum, String companyName, String monthpay,
			String currentBalance, String monthpayBase, String personalPay, String companyPay, String lastTime,
			String openTime, String taskid) {
		super();
		this.accountNum = accountNum;
		this.username = username;
		this.idnum = idnum;
		this.companyName = companyName;
		this.monthpay = monthpay;
		this.currentBalance = currentBalance;
		this.monthpayBase = monthpayBase;
		this.personalPay = personalPay;
		this.companyPay = companyPay;
		this.lastTime = lastTime;
		this.openTime = openTime;
		this.taskid = taskid;
	}
	public HousingWuxiUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}	
}
