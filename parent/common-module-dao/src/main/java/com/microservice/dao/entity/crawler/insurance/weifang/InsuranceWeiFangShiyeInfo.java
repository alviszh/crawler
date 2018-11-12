package com.microservice.dao.entity.crawler.insurance.weifang;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 济南社保 失业信息
 * @author qizhongbin
 *
 */
@Entity
@Table(name="insurance_weifang_shiye_info")
public class InsuranceWeiFangShiyeInfo extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = -7225639204374657354L;
	
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	/** 年月 */
	@Column(name="year_month")
	private String yearMonth;
	/** 单位名称 */
	@Column(name="company_name")
	private String company_name;
	
	/**单位缴费基数*/
	@Column(name="pay_cardinal")
	private String pay_cardinal;
	
	/**缴费基数*/
	@Column(name="company_pay")
	private String company_pay;
	
	/**缴费基数*/
	@Column(name="persion_pay")
	private String persion_pay;
	
	
	public String getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public String getCompany_pay() {
		return company_pay;
	}

	public void setCompany_pay(String company_pay) {
		this.company_pay = company_pay;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getPay_cardinal() {
		return pay_cardinal;
	}

	public void setPay_cardinal(String pay_cardinal) {
		this.pay_cardinal = pay_cardinal;
	}
      
	public String getPersion_pay() {
		return persion_pay;
	}

	public void setPersion_pay(String persion_pay) {
		this.persion_pay = persion_pay;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}