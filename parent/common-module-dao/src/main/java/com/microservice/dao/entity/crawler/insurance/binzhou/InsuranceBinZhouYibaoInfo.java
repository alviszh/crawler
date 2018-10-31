package com.microservice.dao.entity.crawler.insurance.binzhou;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 滨州社保 医保信息
 * @author qizhongbin
 *
 */
@Entity
@Table(name="insurance_binzhou_yibao_info")
public class InsuranceBinZhouYibaoInfo extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = -7225639204374657354L;
	
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;

	/** 年月 */
	@Column(name="yearMonth")
	private String yearMonth;
	

	/** 个人缴费基数*/
	@Column(name="person_pay_cardinal")
	private String person_pay_cardinal;
	
	/** 单位缴费基数*/
	@Column(name="unit_pay_cardinal")
	private String unit_pay_cardinal;
	
	/** 个人交 */
	@Column(name="personal_pay")
	private String personalPay;
	
	/** 单位交 */
	@Column(name="company_pay")
	private String companyPay;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}


	public String getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
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


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getPerson_pay_cardinal() {
		return person_pay_cardinal;
	}

	public void setPerson_pay_cardinal(String person_pay_cardinal) {
		this.person_pay_cardinal = person_pay_cardinal;
	}

	public String getUnit_pay_cardinal() {
		return unit_pay_cardinal;
	}

	public void setUnit_pay_cardinal(String unit_pay_cardinal) {
		this.unit_pay_cardinal = unit_pay_cardinal;
	}
}