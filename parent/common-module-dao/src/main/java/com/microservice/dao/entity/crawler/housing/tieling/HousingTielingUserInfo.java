package com.microservice.dao.entity.crawler.housing.tieling;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 铁岭公积金用户信息
 */
@Entity
@Table(name="housing_tieling_userinfo",indexes = {@Index(name = "index_housing_tieling_userinfo_taskid", columnList = "taskid")})
public class HousingTielingUserInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = 1221206979457794498L;

	private String companyName;    //	单位名称
	private String companyCode;    //	单位帐号
	private String accountNum;     //   个人公积金账号
	private String username;       //   姓名
	private String mechanism;      //   所属机构
	private String state;          //   账户状态
    private String base;           //   缴存基数
    private String companyRatio;   //	单位缴存比例
	private String personRatio;    //	个人缴存比例
	private String monthpay;       //	月应缴额
	private String balance;        //	当前余额
	private String deposit;        //   当年缴存额
	private String extract;        //   当年提取额
	private String monthly;        //   最后汇缴月	
	
	private String taskid;
	
	@Override
	public String toString() {
		return "HousingTielingUserInfo [companyName=" + companyName + ", companyCode=" + companyCode +",accountNum=" +accountNum
				+ ", username=" + username + ", mechanism=" + mechanism + ", state=" + state
				+ ", base="+ base + ", companyRatio=" +companyRatio + ",personRatio=" + personRatio
				+ ", monthpay=" +monthpay + ",balance=" + balance+ ", deposit=" + deposit
				+ ", extract=" +extract + ",monthly=" + monthly+ ",taskid=" + taskid + "]";
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

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

	public String getMechanism() {
		return mechanism;
	}

	public void setMechanism(String mechanism) {
		this.mechanism = mechanism;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
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

	public String getMonthpay() {
		return monthpay;
	}

	public void setMonthpay(String monthpay) {
		this.monthpay = monthpay;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getDeposit() {
		return deposit;
	}

	public void setDeposit(String deposit) {
		this.deposit = deposit;
	}

	public String getExtract() {
		return extract;
	}

	public void setExtract(String extract) {
		this.extract = extract;
	}

	public String getMonthly() {
		return monthly;
	}

	public void setMonthly(String monthly) {
		this.monthly = monthly;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}	
	
	
}
