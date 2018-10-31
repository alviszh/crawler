package com.microservice.dao.entity.crawler.housing.zhaoqing;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name  ="housing_zhaoqing_userinfo",indexes = {@Index(name = "index_housing_zhaoqing_userinfo_taskid", columnList = "taskid")})
public class HousingZhaoQingUserinfo extends IdEntity implements Serializable {
	private String companyNum;             //单位账号
	private String companyName;			   //单位名称
	private String accountNum;             //个人账号
	private String name;				   //姓名
	private String idNum;				   //证件号码
//	private String state;				   //个人账户状态
//	private String openTime;               //开户日期
//	private String lastPaytime;            //最后缴存年月
	private String balance;				   //个人账户余额
	private String base;				   //个人缴存基数
	private String companyRatio;           //单位缴存比例
	private String personRatio;            //个人缴存比例
	private String companyPay;             //单位月缴存额
	private String personPay;              //个人月缴存额
	private String taskid;
	
	@Override
	public String toString() {
		return "HousingZhaoQingUserinfo [companyNum=" + companyNum + ", companyName=" + companyName + ", accountNum=" + accountNum
				+ ", name=" + name + ", idNum=" + idNum + ", balance=" + balance
				+ ", base=" + base + ", companyRatio=" + companyRatio + ", personRatio=" + personRatio
				+ ", companyPay=" + companyPay + ", personPay=" + personPay + ", taskid=" + taskid + "]";
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

	public String getAccountNum() {
		return accountNum;
	}

	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
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

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
}
