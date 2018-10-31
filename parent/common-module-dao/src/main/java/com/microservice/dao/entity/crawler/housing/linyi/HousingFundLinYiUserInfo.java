package com.microservice.dao.entity.crawler.housing.linyi;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_linyi_userinfo",indexes = {@Index(name = "index_housing_linyi_userinfo_taskid", columnList = "taskid")})
public class HousingFundLinYiUserInfo extends IdEntity implements Serializable{

	private String idNum;//身份证号
	
	private String fundNum;//公积金账号
	
	private String monthPay;//月应缴额（元）
	
	private String fee;//余额（元）
	
	private String payDate;//缴至日期
	
	private String taskid;

	@Override
	public String toString() {
		return "HousingFundLinYiUserInfo [idNum=" + idNum + ", fundNum=" + fundNum + ", monthPay=" + monthPay + ", fee="
				+ fee + ", payDate=" + payDate + ", taskid=" + taskid + "]";
	}

	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}

	public String getFundNum() {
		return fundNum;
	}

	public void setFundNum(String fundNum) {
		this.fundNum = fundNum;
	}

	public String getMonthPay() {
		return monthPay;
	}

	public void setMonthPay(String monthPay) {
		this.monthPay = monthPay;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getPayDate() {
		return payDate;
	}

	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
}
