package com.microservice.dao.entity.crawler.insurance.weihai;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_weihai_yanglao_info")
public class InsuranceWeiHaiYanglaoInfo extends IdEntity implements Serializable{
	
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
	
	/** 个人交 */
	@Column(name="person_pay")
	private String personPay;

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


	public String getCompanyPay() {
		return companyPay;
	}

	public void setCompanyPay(String companyPay) {
		this.companyPay = companyPay;
	}

	public String getPersonPay() {
		return personPay;
	}

	public void setPersonPay(String personPay) {
		this.personPay = personPay;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getPay_cardinal() {
		return pay_cardinal;
	}

	public void setPay_cardinal(String pay_cardinal) {
		this.pay_cardinal = pay_cardinal;
	}
	
	
	
}