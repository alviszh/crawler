package com.microservice.dao.entity.crawler.insurance.zhongshan;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_zhongshan_pensiondetail")
public class InsuranceZhongShanPensionDetail extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = -7225639204374657354L;
	
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	
	/** 开始年月 */
	@Column(name="start_year ")
	private String startYear;
	
	/** 截止年月 */
	@Column(name="end_year")
	private String endYear;
	
	/**缴费月数 */
	@Column(name="pay_month")
	private String payMonth;

	/** 缴费工资 */
	@Column(name="pay_wages")
	private String payWages;
	
	/** 单位缴纳额*/
	@Column(name="company_pay")
	private String conmpanyPay;
	/**单位入账户额*/
	@Column(name="unit_delimit")
	private String unitDelimit;
	/**个人缴纳额*/
	@Column(name="personal_payment")
	private String personalPayment;
	/**险种*/
	@Column(name="insurance_type")
	private String insuranceType;
	
	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}



	public String getStartYear() {
		return startYear;
	}

	public void setStartYear(String startYear) {
		this.startYear = startYear;
	}

	public String getEndYear() {
		return endYear;
	}

	public void setEndYear(String endYear) {
		this.endYear = endYear;
	}

	public String getPayMonth() {
		return payMonth;
	}

	public void setPayMonth(String payMonth) {
		this.payMonth = payMonth;
	}

	public String getPayWages() {
		return payWages;
	}

	public void setPayWages(String payWages) {
		this.payWages = payWages;
	}

	public String getConmpanyPay() {
		return conmpanyPay;
	}

	public void setConmpanyPay(String conmpanyPay) {
		this.conmpanyPay = conmpanyPay;
	}

	public String getUnitDelimit() {
		return unitDelimit;
	}

	public void setUnitDelimit(String unitDelimit) {
		this.unitDelimit = unitDelimit;
	}

	public String getPersonalPayment() {
		return personalPayment;
	}

	public void setPersonalPayment(String personalPayment) {
		this.personalPayment = personalPayment;
	}

	public String getInsuranceType() {
		return insuranceType;
	}

	public void setInsuranceType(String insuranceType) {
		this.insuranceType = insuranceType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}