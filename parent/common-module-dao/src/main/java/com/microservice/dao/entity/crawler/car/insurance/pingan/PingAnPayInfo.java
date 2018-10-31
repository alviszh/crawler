package com.microservice.dao.entity.crawler.car.insurance.pingan;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntityAndCookie;
@Entity
@Table(name="car_insurance_pingan_payinfo")
public class PingAnPayInfo extends IdEntityAndCookie implements Serializable{

	private String taskid;
	
	private String insuranceType;//承保险别
	
	private String insuranceMoney;//保险金额
	
	private String premiumMoney;//保费金额
	
	private String coefficient;//费率系数

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getInsuranceType() {
		return insuranceType;
	}

	public void setInsuranceType(String insuranceType) {
		this.insuranceType = insuranceType;
	}

	public String getInsuranceMoney() {
		return insuranceMoney;
	}

	public void setInsuranceMoney(String insuranceMoney) {
		this.insuranceMoney = insuranceMoney;
	}

	public String getPremiumMoney() {
		return premiumMoney;
	}

	public void setPremiumMoney(String premiumMoney) {
		this.premiumMoney = premiumMoney;
	}

	public String getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(String coefficient) {
		this.coefficient = coefficient;
	}

	@Override
	public String toString() {
		return "PingAnPayInfo [taskid=" + taskid + ", insuranceType=" + insuranceType + ", insuranceMoney="
				+ insuranceMoney + ", premiumMoney=" + premiumMoney + ", coefficient=" + coefficient + "]";
	}
	
	
}
