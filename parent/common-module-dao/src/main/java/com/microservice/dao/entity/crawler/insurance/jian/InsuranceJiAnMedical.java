package com.microservice.dao.entity.crawler.insurance.jian;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "insurance_jian_medical")
public class InsuranceJiAnMedical extends IdEntity implements Serializable{
	private static final long serialVersionUID = 5273473947864101643L;
	
	private String usernum;//个人编号
	private String username;//姓名
	private String type;//险种类型
	private String personalBase;//个人缴费基数
	private String personalPay;//个人缴费金额
	private String unitPay;//单位缴费金额
	private String financePay;//财政缴费金额
	private String intoPerson;//划入个人账户金额
	private String paymonth;//费款所属期
	private String taskid;
	
	public String getUsernum() {
		return usernum;
	}
	public void setUsernum(String usernum) {
		this.usernum = usernum;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPersonalBase() {
		return personalBase;
	}
	public void setPersonalBase(String personalBase) {
		this.personalBase = personalBase;
	}
	public String getPersonalPay() {
		return personalPay;
	}
	public void setPersonalPay(String personalPay) {
		this.personalPay = personalPay;
	}
	public String getUnitPay() {
		return unitPay;
	}
	public void setUnitPay(String unitPay) {
		this.unitPay = unitPay;
	}
	public String getFinancePay() {
		return financePay;
	}
	public void setFinancePay(String financePay) {
		this.financePay = financePay;
	}
	public String getIntoPerson() {
		return intoPerson;
	}
	public void setIntoPerson(String intoPerson) {
		this.intoPerson = intoPerson;
	}
	public String getPaymonth() {
		return paymonth;
	}
	public void setPaymonth(String paymonth) {
		this.paymonth = paymonth;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	public InsuranceJiAnMedical(String usernum, String username, String type, String personalBase, String personalPay,
			String unitPay, String financePay, String intoPerson, String paymonth, String taskid) {
		super();
		this.usernum = usernum;
		this.username = username;
		this.type = type;
		this.personalBase = personalBase;
		this.personalPay = personalPay;
		this.unitPay = unitPay;
		this.financePay = financePay;
		this.intoPerson = intoPerson;
		this.paymonth = paymonth;
		this.taskid = taskid;
	}
	public InsuranceJiAnMedical() {
		super();
	}
	@Override
	public String toString() {
		return "InsuranceJiAnMedical [usernum=" + usernum + ", username=" + username + ", type=" + type
				+ ", personalBase=" + personalBase + ", personalPay=" + personalPay + ", unitPay=" + unitPay
				+ ", financePay=" + financePay + ", intoPerson=" + intoPerson + ", paymonth=" + paymonth + ", taskid="
				+ taskid + "]";
	}
}
