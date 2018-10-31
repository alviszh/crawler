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
@Table(name  ="housing_beijing_center_pay",indexes = {@Index(name = "index_housing_beijing_center_pay_taskid", columnList = "taskid")})
public class HousingBeiJingCenterPayBean extends IdEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	private String taskid;
    public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	private String date;// 业务发生日期
	private String companyNum;// 单位登记号
	private String company;// 账户名称
	private String businessType;// 业务类型
	private String payMonth;// 汇缴年月
	private String increasedNum;// 增加金额
	private String reduceNum;//减少金额
	private String balance;// 余额
	private String paymentName;// 缴款单位
	private String payment;// 支付方式
	private String ywls;// 业务流水号
	private String opdeck;// 操作平台
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getCompanyNum() {
		return companyNum;
	}
	public void setCompanyNum(String companyNum) {
		this.companyNum = companyNum;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public String getPayMonth() {
		return payMonth;
	}
	public void setPayMonth(String payMonth) {
		this.payMonth = payMonth;
	}
	public String getIncreasedNum() {
		return increasedNum;
	}
	public void setIncreasedNum(String increasedNum) {
		this.increasedNum = increasedNum;
	}
	public String getReduceNum() {
		return reduceNum;
	}
	public void setReduceNum(String reduceNum) {
		this.reduceNum = reduceNum;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getPaymentName() {
		return paymentName;
	}
	public void setPaymentName(String paymentName) {
		this.paymentName = paymentName;
	}
	public String getPayment() {
		return payment;
	}
	public void setPayment(String payment) {
		this.payment = payment;
	}
	public String getYwls() {
		return ywls;
	}
	public void setYwls(String ywls) {
		this.ywls = ywls;
	}
	public String getOpdeck() {
		return opdeck;
	}
	public void setOpdeck(String opdeck) {
		this.opdeck = opdeck;
	}
	@Override
	public String toString() {
		return "BeiJingCenterPayBean [taskid=" + taskid + ", date=" + date + ", companyNum=" + companyNum + ", company="
				+ company + ", businessType=" + businessType + ", payMonth=" + payMonth + ", increasedNum="
				+ increasedNum + ", reduceNum=" + reduceNum + ", balance=" + balance + ", paymentName=" + paymentName
				+ ", payment=" + payment + ", ywls=" + ywls + ", opdeck=" + opdeck + "]";
	}

	

}