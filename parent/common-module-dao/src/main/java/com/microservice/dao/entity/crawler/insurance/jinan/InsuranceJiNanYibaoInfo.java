package com.microservice.dao.entity.crawler.insurance.jinan;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 济南社保 医保信息
 * @author qizhongbin
 *
 */
@Entity
@Table(name="insurance_jinan_yibao_info")
public class InsuranceJiNanYibaoInfo extends IdEntity implements Serializable{
	
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
	
	/**记账额*/
	@Column(name="charge")
	private String charge;
	
	/**门诊统筹扣减额*/
	@Column(name="mztckje")
	private String mztckje;
	
	/**大额扣减金额*/
	@Column(name="dekjje")
	private String dekjje;
	
	/**发生日期*/
	@Column(name="date")
	private String date;

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

	public String getCharge() {
		return charge;
	}

	public void setCharge(String charge) {
		this.charge = charge;
	}

	public String getMztckje() {
		return mztckje;
	}

	public void setMztckje(String mztckje) {
		this.mztckje = mztckje;
	}

	public String getDekjje() {
		return dekjje;
	}

	public void setDekjje(String dekjje) {
		this.dekjje = dekjje;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}