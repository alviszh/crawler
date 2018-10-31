package com.microservice.dao.entity.crawler.insurance.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="etl_insurance_detail")
public class ETLInsuranceDetail extends IdEntity implements Serializable {
	
	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId; //唯一标识
	private String idNumber;	//身份证号
	private String tranDate;	//交易日期
	private String paymentCurrentMonth;	//缴存月份
	private String paymentBegindate;	//汇缴起始日期
	private String paymentEnddate;	//汇缴结束日期
	private String paymentBaseMoney;	//缴纳基数
	private String companyPaymentMoney;	//单位缴费金额
	private String personalPaymentMoney;	//个人缴费金额
	private String balance;	//余额
	private String monthTotalMoney;	//月总缴额
	private String companyName;	//公司名称
	private String paymentType;	//缴费类型
	private String insuranceType;	//保险类型
	private String companyPaymentPercent;	//单位缴存比例
	private String personalPaymentPercent;	//个人缴存比例
	private String changeMoney;	//变动金额（入账金额）
	private String basic_idnumber;
	@JsonBackReference
	private String resource; //溯源字段
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	public String getTranDate() {
		return tranDate;
	}
	public void setTranDate(String tranDate) {
		this.tranDate = tranDate;
	}
	public String getPaymentCurrentMonth() {
		return paymentCurrentMonth;
	}
	public void setPaymentCurrentMonth(String paymentCurrentMonth) {
		this.paymentCurrentMonth = paymentCurrentMonth;
	}
	public String getPaymentBegindate() {
		return paymentBegindate;
	}
	public void setPaymentBegindate(String paymentBegindate) {
		this.paymentBegindate = paymentBegindate;
	}
	public String getPaymentEnddate() {
		return paymentEnddate;
	}
	public void setPaymentEnddate(String paymentEnddate) {
		this.paymentEnddate = paymentEnddate;
	}
	public String getPaymentBaseMoney() {
		return paymentBaseMoney;
	}
	public void setPaymentBaseMoney(String paymentBaseMoney) {
		this.paymentBaseMoney = paymentBaseMoney;
	}
	public String getCompanyPaymentMoney() {
		return companyPaymentMoney;
	}
	public void setCompanyPaymentMoney(String companyPaymentMoney) {
		this.companyPaymentMoney = companyPaymentMoney;
	}
	public String getPersonalPaymentMoney() {
		return personalPaymentMoney;
	}
	public void setPersonalPaymentMoney(String personalPaymentMoney) {
		this.personalPaymentMoney = personalPaymentMoney;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getMonthTotalMoney() {
		return monthTotalMoney;
	}
	public void setMonthTotalMoney(String monthTotalMoney) {
		this.monthTotalMoney = monthTotalMoney;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getInsuranceType() {
		return insuranceType;
	}
	public void setInsuranceType(String insuranceType) {
		this.insuranceType = insuranceType;
	}
	public String getCompanyPaymentPercent() {
		return companyPaymentPercent;
	}
	public void setCompanyPaymentPercent(String companyPaymentPercent) {
		this.companyPaymentPercent = companyPaymentPercent;
	}
	public String getPersonalPaymentPercent() {
		return personalPaymentPercent;
	}
	public void setPersonalPaymentPercent(String personalPaymentPercent) {
		this.personalPaymentPercent = personalPaymentPercent;
	}
	public String getChangeMoney() {
		return changeMoney;
	}
	public void setChangeMoney(String changeMoney) {
		this.changeMoney = changeMoney;
	}
	public String getBasic_idnumber() {
		return basic_idnumber;
	}
	public void setBasic_idnumber(String basic_idnumber) {
		this.basic_idnumber = basic_idnumber;
	}
	@Column(columnDefinition="text")
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	@Override
	public String toString() {
		return "ETLInsuranceDetail [taskId=" + taskId + ", idNumber=" + idNumber + ", tranDate=" + tranDate
				+ ", paymentCurrentMonth=" + paymentCurrentMonth + ", paymentBegindate=" + paymentBegindate
				+ ", paymentEnddate=" + paymentEnddate + ", paymentBaseMoney=" + paymentBaseMoney
				+ ", companyPaymentMoney=" + companyPaymentMoney + ", personalPaymentMoney=" + personalPaymentMoney
				+ ", balance=" + balance + ", monthTotalMoney=" + monthTotalMoney + ", companyName=" + companyName
				+ ", paymentType=" + paymentType + ", insuranceType=" + insuranceType + ", companyPaymentPercent="
				+ companyPaymentPercent + ", personalPaymentPercent=" + personalPaymentPercent + ", changeMoney="
				+ changeMoney + ", basic_idnumber=" + basic_idnumber + ", resource=" + resource + "]";
	}
	
	
}
