package com.microservice.dao.entity.crawler.insurance.jinan;

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
@Table(name="insurance_jinan_shiye_info")
public class InsuranceJiNanShiyeInfo extends IdEntity implements Serializable{
	
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
	
	/** 个人交 */
	@Column(name="person_pay")
	private String personPay;
	
	/**缴费日期*/
	@Column(name="pay_date")
	private String pay_date;
	
	/** 缴费类型 */
	@Column(name="pay_type")
	private String pay_type;

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

	public String getPersonPay() {
		return personPay;
	}

	public void setPersonPay(String personPay) {
		this.personPay = personPay;
	}

	public String getPay_date() {
		return pay_date;
	}

	public void setPay_date(String pay_date) {
		this.pay_date = pay_date;
	}

	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}