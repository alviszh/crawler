package com.microservice.dao.entity.crawler.housing.chongqing;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 重庆公积金用户信息
 */
@Entity
@Table(name="housing_chongqing_paydetails")
public class HousingChongqingPaydetails extends IdEntity implements Serializable {
	private static final long serialVersionUID = -758773159050632619L;
	private String paytime;//	时间
	private String remark;//	摘要
	private String personalPay;//	个人缴交额（元）
	private String companyPay;//	单位缴交额（元）
	private String currentBalance;//	当前余额（元）
	private String taskid;
	public String getPaytime() {
		return paytime;
	}
	public void setPaytime(String paytime) {
		this.paytime = paytime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public String getCurrentBalance() {
		return currentBalance;
	}
	public void setCurrentBalance(String currentBalance) {
		this.currentBalance = currentBalance;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	@Override
	public String toString() {
		return "HousingChongqingPaydetails [paytime=" + paytime + ", remark=" + remark + ", personalPay=" + personalPay
				+ ", companyPay=" + companyPay + ", currentBalance=" + currentBalance + ", taskid=" + taskid + "]";
	}
	public HousingChongqingPaydetails(String paytime, String remark, String personalPay, String companyPay,
			String currentBalance, String taskid) {
		super();
		this.paytime = paytime;
		this.remark = remark;
		this.personalPay = personalPay;
		this.companyPay = companyPay;
		this.currentBalance = currentBalance;
		this.taskid = taskid;
	}
	public HousingChongqingPaydetails() {
		super();
		// TODO Auto-generated constructor stub
	}	
    
	
}
