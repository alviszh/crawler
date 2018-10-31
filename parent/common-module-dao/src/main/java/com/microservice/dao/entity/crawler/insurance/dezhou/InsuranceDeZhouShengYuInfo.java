package com.microservice.dao.entity.crawler.insurance.dezhou;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 德州社保 生育信息
 * @author qizhongbin
 *
 */
@Entity
@Table(name="insurance_dezhou_shengyu_info")
public class InsuranceDeZhouShengYuInfo extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = -7225639204374657354L;
	
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	
	/** 年月 */
	@Column(name="yearMonth")
	private String yearMonth;
	
	/**缴费基数*/
	@Column(name="pay_cardinal")
	private String pay_cardinal;
	
	/** 单位交 */
	@Column(name="company_pay")
	private String companyPay;
	
	/** 单位名称 */
	@Column(name="company_name")
	private String companyName;

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

	public String getPay_cardinal() {
		return pay_cardinal;
	}

	public void setPay_cardinal(String pay_cardinal) {
		this.pay_cardinal = pay_cardinal;
	}

	public String getCompanyPay() {
		return companyPay;
	}

	public void setCompanyPay(String companyPay) {
		this.companyPay = companyPay;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
}