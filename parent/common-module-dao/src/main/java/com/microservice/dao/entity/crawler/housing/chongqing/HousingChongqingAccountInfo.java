package com.microservice.dao.entity.crawler.housing.chongqing;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 重庆公积金账户信息
 */
@Entity
@Table(name="housing_chongqing_accountinfo")
public class HousingChongqingAccountInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = -3207075026659390860L;
	private String idnum;//	身份证号码
	private String openTime;//	开户时间
	private String name;//	姓名
	private String companyName;//	单位名称
	private String personalPay;//	个人月缴额（元）
	private String companyPay;//	单位月缴额（元）
	private String personAccount;//	个人公积金账号
	private String personNum;//	个人序号（元）
	private String currentBalance;//	当前余额
	private String status;//	当前状态
	private String taskid;
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getOpenTime() {
		return openTime;
	}
	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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
	public String getPersonAccount() {
		return personAccount;
	}
	public void setPersonAccount(String personAccount) {
		this.personAccount = personAccount;
	}
	public String getPersonNum() {
		return personNum;
	}
	public void setPersonNum(String personNum) {
		this.personNum = personNum;
	}
	public String getCurrentBalance() {
		return currentBalance;
	}
	public void setCurrentBalance(String currentBalance) {
		this.currentBalance = currentBalance;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Override
	public String toString() {
		return "HousingChongqingAccountinfo [idnum=" + idnum + ", openTime=" + openTime + ", name=" + name
				+ ", companyName=" + companyName + ", personalPay=" + personalPay + ", companyPay=" + companyPay
				+ ", personAccount=" + personAccount + ", personNum=" + personNum + ", currentBalance=" + currentBalance
				+ ", status=" + status + ", taskid=" + taskid + "]";
	}
	
	public HousingChongqingAccountInfo(String idnum, String openTime, String name, String companyName,
			String personalPay, String companyPay, String personAccount, String personNum, String currentBalance,
			String status, String taskid) {
		super();
		this.idnum = idnum;
		this.openTime = openTime;
		this.name = name;
		this.companyName = companyName;
		this.personalPay = personalPay;
		this.companyPay = companyPay;
		this.personAccount = personAccount;
		this.personNum = personNum;
		this.currentBalance = currentBalance;
		this.status = status;
		this.taskid = taskid;
	}
	public HousingChongqingAccountInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
   
}
