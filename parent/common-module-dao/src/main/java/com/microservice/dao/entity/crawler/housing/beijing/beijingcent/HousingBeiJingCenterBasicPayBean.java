/**
  * Copyright 2018 bejson.com 
  */
package com.microservice.dao.entity.crawler.housing.beijing.beijingcent;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

/**
 * Auto-generated: 2018-08-13 16:19:3
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name  ="housing_beijing_center_basicpay",indexes = {@Index(name = "index_housing_beijing_center_basicpay_taskid", columnList = "taskid")})
public class HousingBeiJingCenterBasicPayBean extends IdEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	private String taskid;
    public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	private String name;// 姓名
	private String cardType;// 证件类型
	private String cardNum;// 证件号码
	private String gjjnum;// 公积金账号
	private String company;// 单位名称
	private String perbase;// 个人缴存基数
	private String companyPay;// 单位月缴存额
	private String perPay;// 个人月缴存额
	private String monthPay;// 月缴存额
	private String payStatue;// 缴存状态
	private String perBalance;// 个人账户余额
	private String frozen;// 冻结金额
	private String openDate;// 开户日期

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

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	public String getGjjnum() {
		return gjjnum;
	}

	public void setGjjnum(String gjjnum) {
		this.gjjnum = gjjnum;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getPerbase() {
		return perbase;
	}

	public void setPerbase(String perbase) {
		this.perbase = perbase;
	}

	public String getCompanyPay() {
		return companyPay;
	}

	public void setCompanyPay(String companyPay) {
		this.companyPay = companyPay;
	}

	public String getPerPay() {
		return perPay;
	}

	public void setPerPay(String perPay) {
		this.perPay = perPay;
	}

	public String getMonthPay() {
		return monthPay;
	}

	public void setMonthPay(String monthPay) {
		this.monthPay = monthPay;
	}

	public String getPayStatue() {
		return payStatue;
	}

	public void setPayStatue(String payStatue) {
		this.payStatue = payStatue;
	}

	public String getPerBalance() {
		return perBalance;
	}

	public void setPerBalance(String perBalance) {
		this.perBalance = perBalance;
	}

	public String getFrozen() {
		return frozen;
	}

	public void setFrozen(String frozen) {
		this.frozen = frozen;
	}

	public String getOpenDate() {
		return openDate;
	}

	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}

	@Override
	public String toString() {
		return "BeiJingCenterBasicPayBean [taskid=" + taskid + ", name=" + name + ", cardType=" + cardType
				+ ", cardNum=" + cardNum + ", gjjnum=" + gjjnum + ", company=" + company + ", perbase=" + perbase
				+ ", companyPay=" + companyPay + ", perPay=" + perPay + ", monthPay=" + monthPay + ", payStatue="
				+ payStatue + ", perBalance=" + perBalance + ", frozen=" + frozen + ", openDate=" + openDate + "]";
	}

}