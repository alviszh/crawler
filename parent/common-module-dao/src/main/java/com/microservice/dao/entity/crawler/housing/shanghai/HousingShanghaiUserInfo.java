package com.microservice.dao.entity.crawler.housing.shanghai;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "housing_shanghai_userinfo" ,indexes = {@Index(name = "index_housing_shanghai_userinfo_taskid", columnList = "taskid")})
public class HousingShanghaiUserInfo extends IdEntity {

	/**
	 * taskid uuid 前端通过uuid访问状态结果
	 */
	private String taskid;

	/**
	 * 姓 名
	 */
	private String name;

	/**
	 * 开户日期
	 */
	private String openingDate;

	/**
	 * 公积金账号
	 */
	private String housingNum;

	/**
	 * 所属单位
	 */
	private String company;

	/**
	 * 末次缴存年月
	 */
	private String lastDepositDate;

	/**
	 * 账户余额
	 */
	private String balance;

	/**
	 * 月缴存额
	 */
	private String monthlyDeposit;

	/**
	 * 当前账户状态
	 */
	private String accountState;

	/**
	 * 绑定手机号
	 */
	private String phone;

	/**
	 * 实名认证状态
	 */
	private String state ;

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

	public String getOpeningDate() {
		return openingDate;
	}

	public void setOpeningDate(String openingDate) {
		this.openingDate = openingDate;
	}

	public String getHousingNum() {
		return housingNum;
	}

	public void setHousingNum(String housingNum) {
		this.housingNum = housingNum;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getLastDepositDate() {
		return lastDepositDate;
	}

	public void setLastDepositDate(String lastDepositDate) {
		this.lastDepositDate = lastDepositDate;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getMonthlyDeposit() {
		return monthlyDeposit;
	}

	public void setMonthlyDeposit(String monthlyDeposit) {
		this.monthlyDeposit = monthlyDeposit;
	}

	public String getAccountState() {
		return accountState;
	}

	public void setAccountState(String accountState) {
		this.accountState = accountState;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "HousingShanghaiUserInfo [taskid=" + taskid + ", name=" + name + ", openingDate=" + openingDate
				+ ", housingNum=" + housingNum + ", company=" + company + ", lastDepositDate=" + lastDepositDate
				+ ", balance=" + balance + ", monthlyDeposit=" + monthlyDeposit + ", accountState=" + accountState
				+ ", phone=" + phone + ", state=" + state + "]";
	}

	public HousingShanghaiUserInfo(String taskid, String name, String openingDate, String housingNum, String company,
			String lastDepositDate, String balance, String monthlyDeposit, String accountState, String phone,
			String state) {
		super();
		this.taskid = taskid;
		this.name = name;
		this.openingDate = openingDate;
		this.housingNum = housingNum;
		this.company = company;
		this.lastDepositDate = lastDepositDate;
		this.balance = balance;
		this.monthlyDeposit = monthlyDeposit;
		this.accountState = accountState;
		this.phone = phone;
		this.state = state;
	}

	public HousingShanghaiUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
