package com.microservice.dao.entity.crawler.insurance.weihai;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_weihai_yibao_info")
public class InsuranceWeiHaiYibaoInfo extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = -7225639204374657354L;
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;

	/** 年月 */
	@Column(name="yearMonth")
	private String yearMonth;
	
	/**单位缴费基数*/
	@Column(name="company_pay_cardinal")
	private String company_pay_cardinal;
	
	/**个人缴费基数*/
	@Column(name="person_pay_cardinal")
	private String person_pay_cardinal;
	
	/** 单位交 */
	@Column(name="company_pay")
	private String companyPay;
	
	/** 个人交 */
	@Column(name="person_pay")
	private String personPay;
	
	/** 险种标志 */
	@Column(name="insurance")
	private String insurance;

	public String getInsurance() {
		return insurance;
	}

	public void setInsurance(String insurance) {
		this.insurance = insurance;
	}

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

	public String getCompany_pay_cardinal() {
		return company_pay_cardinal;
	}

	public void setCompany_pay_cardinal(String company_pay_cardinal) {
		this.company_pay_cardinal = company_pay_cardinal;
	}

	public String getPerson_pay_cardinal() {
		return person_pay_cardinal;
	}

	public void setPerson_pay_cardinal(String person_pay_cardinal) {
		this.person_pay_cardinal = person_pay_cardinal;
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
	
}