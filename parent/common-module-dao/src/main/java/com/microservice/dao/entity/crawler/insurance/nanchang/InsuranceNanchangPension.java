package com.microservice.dao.entity.crawler.insurance.nanchang;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * 南昌社保:养老缴费明细
 * @author tz
 *
 */
@Entity
@Table(name = "insurance_nanchang_pension" ,indexes = {@Index(name = "index_insurance_nanchang_pension_taskid", columnList = "taskid")})
public class InsuranceNanchangPension extends IdEntity{

	/**
	 * uuid 前端通过uuid访问状态结果
	 */
	private String taskid;

	/**
	 * 个人编号
	 */
	private String personalNumber;
	/**
	 * 费款所属期
	 */
	private String feePeriod;
	/**
	 * 缴费类型
	 */
	private String paymentType;
	/**
	 * 是否欠费
	 */
	private String onBills;
	/**
	 * 缴费基数
	 */
	private String paymentBase;
	/**
	 * 单位缴纳金额
	 */
	private String unitPayment;
	/**
	 * 个人缴纳金额
	 */
	private String personalPayment;
	/**
	 * 总金额
	 */
	private String totalMon;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getPersonalNumber() {
		return personalNumber;
	}

	public void setPersonalNumber(String personalNumber) {
		this.personalNumber = personalNumber;
	}

	public String getPaymentBase() {
		return paymentBase;
	}

	public void setPaymentBase(String paymentBase) {
		this.paymentBase = paymentBase;
	}

	public String getUnitPayment() {
		return unitPayment;
	}

	public void setUnitPayment(String unitPayment) {
		this.unitPayment = unitPayment;
	}

	public String getPersonalPayment() {
		return personalPayment;
	}

	public void setPersonalPayment(String personalPayment) {
		this.personalPayment = personalPayment;
	}

	public String getTotalMon() {
		return totalMon;
	}

	public void setTotalMon(String totalMon) {
		this.totalMon = totalMon;
	}

	public String getFeePeriod() {
		return feePeriod;
	}

	public void setFeePeriod(String feePeriod) {
		this.feePeriod = feePeriod;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getOnBills() {
		return onBills;
	}

	public void setOnBills(String onBills) {
		this.onBills = onBills;
	}

	public InsuranceNanchangPension(String taskid, String personalNumber, String feePeriod, String paymentType, String onBills, String paymentBase, String unitPayment, String personalPayment, String totalMon) {
		this.taskid = taskid;
		this.personalNumber = personalNumber;
		this.feePeriod = feePeriod;
		this.paymentType = paymentType;
		this.onBills = onBills;
		this.paymentBase = paymentBase;
		this.unitPayment = unitPayment;
		this.personalPayment = personalPayment;
		this.totalMon = totalMon;
	}

	public InsuranceNanchangPension() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
