package com.microservice.dao.entity.crawler.insurance.yantai;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 烟台社保 医保信息
 * @author qizhongbin
 *
 */
@Entity
@Table(name="insurance_yantai_yibao_info")
public class InsuranceYantaiYibaoInfo extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = -7225639204374657354L;
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	
	/** 险种 */
	@Column(name="name")
	private String name;
	
	/** 年月 */
	@Column(name="yearMonth")
	private String yearMonth;
	
	/** 缴费基数*/
	@Column(name="pay_cardinal")
	private String payCardinal;
	
	/** 个人交 */
	@Column(name="personal_pay")
	private String personalPay;
	
	/**单位编号*/
	@Column(name="unit_no")
	private String unitNo;
	
	/**单位名称*/
	@Column(name="unit_name")
	private String unitName;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getUnitNo() {
		return unitNo;
	}

	public void setUnitNo(String unitNo) {
		this.unitNo = unitNo;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
}