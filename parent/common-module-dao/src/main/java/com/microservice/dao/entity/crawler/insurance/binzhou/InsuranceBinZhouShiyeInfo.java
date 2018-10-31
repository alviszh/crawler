package com.microservice.dao.entity.crawler.insurance.binzhou;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 滨州社保 失业信息
 * @author qizhongbin
 *
 */
@Entity
@Table(name="insurance_binzhou_shiye_info")
public class InsuranceBinZhouShiyeInfo extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = -7225639204374657354L;
	
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	
	/** 年月 */
	@Column(name="yearMonth")
	private String yearMonth;
	
	/** 缴费基数*/
	@Column(name="pay_cardinal")
	private String payCardinal;
	
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

	public String getPayCardinal() {
		return payCardinal;
	}

	public void setPayCardinal(String payCardinal) {
		this.payCardinal = payCardinal;
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

	
}