package com.microservice.dao.entity.crawler.insurance.weifang;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 济南社保 工伤信息
 * @author qizhongbin
 *
 */
@Entity
@Table(name="insurance_weifang_gongshang_info")
public class InsuranceWeiFangGongShangInfo extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = -7225639204374657354L;
	
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	/** 年月 */
	@Column(name="year_month")
	private String year_month;
	
	/** 单位名称 */
	@Column(name="company_name")
	private String company_name;
	
	/**缴费基数*/
	@Column(name="pay_cardinal")
	private String pay_cardinal;
	
	/** 缴费金额 */
	@Column(name="pay_charge")
	private String pay_charge;
	
	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getYear_month() {
		return year_month;
	}

	public void setYear_month(String year_month) {
		this.year_month = year_month;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public String getPay_cardinal() {
		return pay_cardinal;
	}

	public void setPay_cardinal(String pay_cardinal) {
		this.pay_cardinal = pay_cardinal;
	}

	public String getPay_charge() {
		return pay_charge;
	}

	public void setPay_charge(String pay_charge) {
		this.pay_charge = pay_charge;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
}