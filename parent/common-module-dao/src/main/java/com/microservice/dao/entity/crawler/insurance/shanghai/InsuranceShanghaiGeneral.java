package com.microservice.dao.entity.crawler.insurance.shanghai;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 上海社保流水
 * 近期应缴情况
 * 反映参保个人最近十二个月内每月的社会保险费缴费基数及养保、医保、失保个人应缴金额。
 * @author tz
 *
 */
@Entity
@Table(name = "insurance_shanghai_general" ,indexes = {@Index(name = "index_insurance_shanghai_general_taskid", columnList = "taskid")})
public class InsuranceShanghaiGeneral  extends IdEntity{
	
	
	/**
	 * taskid  uuid 前端通过uuid访问状态结果
	 */
	private String taskid;
	
	/**
	 * 年月
	 */
	private String yearMonth;
	
	/**
	 * 缴费基数
	 */
	private String payBase;
	
	/**
	 * 养保个人缴费额	
	 */
	private String pension;
	
	/**
	 * 医保个人缴费额
	 */
	private String medical;
	
	/**
	 * 失保个人缴费额
	 */
	private String unemployment;

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

	public String getPayBase() {
		return payBase;
	}

	public void setPayBase(String payBase) {
		this.payBase = payBase;
	}

	public String getPension() {
		return pension;
	}

	public void setPension(String pension) {
		this.pension = pension;
	}

	public String getMedical() {
		return medical;
	}

	public void setMedical(String medical) {
		this.medical = medical;
	}

	public String getUnemployment() {
		return unemployment;
	}

	public void setUnemployment(String unemployment) {
		this.unemployment = unemployment;
	}


	@Override
	public String toString() {
		return "InsuranceShanghaiGeneral [taskid=" + taskid + ", yearMonth=" + yearMonth + ", payBase=" + payBase
				+ ", pension=" + pension + ", medical=" + medical + ", unemployment=" + unemployment + "]";
	}

	public InsuranceShanghaiGeneral(String taskid, String yearMonth, String payBase, String pension, String medical,
			String unemployment) {
		super();
		this.taskid = taskid;
		this.yearMonth = yearMonth;
		this.payBase = payBase;
		this.pension = pension;
		this.medical = medical;
		this.unemployment = unemployment;
	}

	public InsuranceShanghaiGeneral() {
		super();
	}
	

}
