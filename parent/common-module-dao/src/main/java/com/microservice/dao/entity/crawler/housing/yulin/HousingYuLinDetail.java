package com.microservice.dao.entity.crawler.housing.yulin;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_yulin_detail",indexes = {@Index(name = "index_housing_yulin_detail_taskid", columnList = "taskid")})
public class HousingYuLinDetail extends IdEntity implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String taskid;
	private String payYaer ;//缴至年月
	private String businessType;//业务类型
	private String backMoney;//汇补缴金额（元）
	private String personPay;//个人缴存金额（元）
	private String companyPay;//单位缴存金额（元）
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getPayYaer() {
		return payYaer;
	}
	public void setPayYaer(String payYaer) {
		this.payYaer = payYaer;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public String getBackMoney() {
		return backMoney;
	}
	public void setBackMoney(String backMoney) {
		this.backMoney = backMoney;
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
	

	
}
