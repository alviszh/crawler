package com.microservice.dao.entity.crawler.housing.yanbian;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 延边公积金用户信息
 */
@Entity
@Table(name="housing_yanbian_paydetails")
public class HousingYanbianPaydetails extends IdEntity implements Serializable {
	private static final long serialVersionUID = -758773159050632619L;
	private String areaName;//	地区名称
	private String payTime;//	年月
	private String companyName;//	单位名称
	private String username;//	姓名
	private String idnum;//	身份证号码
	private String accountNum;//	个人账号
	private String monthpay;//	月缴存额
	private String personPay;//	个人缴存额
	private String companyPay;//	单位缴存额
	private String taskid;
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
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
	public String getAccountNum() {
		return accountNum;
	}
	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}
	public String getMonthpay() {
		return monthpay;
	}
	public void setMonthpay(String monthpay) {
		this.monthpay = monthpay;
	}
	public String getPersonPay() {
		return personPay;
	}
	public void setPersonPay(String personPay) {
		this.personPay = personPay;
	}
	public String getCompanyPay() {
		return companyPay;
	}
	public void setCompanyPay(String companyPay) {
		this.companyPay = companyPay;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Override
	public String toString() {
		return "HousingYanbianPaydetails [areaName=" + areaName + ", payTime=" + payTime + ", companyName="
				+ companyName + ", username=" + username + ", idnum=" + idnum + ", accountNum=" + accountNum
				+ ", monthpay=" + monthpay + ", personPay=" + personPay + ", companyPay=" + companyPay + ", taskid="
				+ taskid + "]";
	}
	public HousingYanbianPaydetails(String areaName, String payTime, String companyName, String username, String idnum,
			String accountNum, String monthpay, String personPay, String companyPay, String taskid) {
		super();
		this.areaName = areaName;
		this.payTime = payTime;
		this.companyName = companyName;
		this.username = username;
		this.idnum = idnum;
		this.accountNum = accountNum;
		this.monthpay = monthpay;
		this.personPay = personPay;
		this.companyPay = companyPay;
		this.taskid = taskid;
	}
	public HousingYanbianPaydetails() {
		super();
		// TODO Auto-generated constructor stub
	}	
}
