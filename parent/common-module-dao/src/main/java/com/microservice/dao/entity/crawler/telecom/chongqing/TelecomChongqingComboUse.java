package com.microservice.dao.entity.crawler.telecom.chongqing;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;


/**
 * 重庆用户套餐使用情况
 * @author tz
 *
 */
@Entity
@Table(name = "telecom_chongqing_combouse" ,indexes = {@Index(name = "index_telecom_chongqing_combouse_taskid", columnList = "taskid")})
public class TelecomChongqingComboUse extends IdEntity {
	
	/**
	 * taskid
	 */
	private String taskid;
	
	/**
	 * 年月
	 */
	private String yearmonth;
	
	/**
	 * 套餐名称
	 */
	private String totalDesc;
	
	/**
	 * 套餐已使用数
	 */
	private String disctDealedAmount;
	
	/**
	 * 套餐未使用数
	 */
	private String disctUndealedAmount;
	
	/**
	 * 套餐内总量
	 */
	private String disctAmount;
	
	/**
	 * 单位类型
	 */
	private String disctUnit;
	
	/**
	 * 截止时间
	 */
	private String expDate;
	
	/**
	 * 月份
	 */
	private String month;
	
	/**
	 * month上个月结转流量
	 */
	private String carryoverAmount;
	
	
	/**
	 * month这个月套餐流量
	 */
	private String sm;


	public String getTaskid() {
		return taskid;
	}


	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}


	public String getYearmonth() {
		return yearmonth;
	}


	public void setYearmonth(String yearmonth) {
		this.yearmonth = yearmonth;
	}


	public String getTotalDesc() {
		return totalDesc;
	}


	public void setTotalDesc(String totalDesc) {
		this.totalDesc = totalDesc;
	}


	public String getDisctDealedAmount() {
		return disctDealedAmount;
	}


	public void setDisctDealedAmount(String disctDealedAmount) {
		this.disctDealedAmount = disctDealedAmount;
	}


	public String getDisctUndealedAmount() {
		return disctUndealedAmount;
	}


	public void setDisctUndealedAmount(String disctUndealedAmount) {
		this.disctUndealedAmount = disctUndealedAmount;
	}


	public String getDisctAmount() {
		return disctAmount;
	}


	public void setDisctAmount(String disctAmount) {
		this.disctAmount = disctAmount;
	}


	public String getDisctUnit() {
		return disctUnit;
	}


	public void setDisctUnit(String disctUnit) {
		this.disctUnit = disctUnit;
	}


	public String getExpDate() {
		return expDate;
	}


	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}


	public String getMonth() {
		return month;
	}


	public void setMonth(String month) {
		this.month = month;
	}


	public String getCarryoverAmount() {
		return carryoverAmount;
	}


	public void setCarryoverAmount(String carryoverAmount) {
		this.carryoverAmount = carryoverAmount;
	}


	public String getSm() {
		return sm;
	}


	public void setSm(String sm) {
		this.sm = sm;
	}


	@Override
	public String toString() {
		return "TelecomChongqingComboUse [taskid=" + taskid + ", yearmonth=" + yearmonth + ", totalDesc=" + totalDesc
				+ ", disctDealedAmount=" + disctDealedAmount + ", disctUndealedAmount=" + disctUndealedAmount
				+ ", disctAmount=" + disctAmount + ", disctUnit=" + disctUnit + ", expDate=" + expDate + ", month="
				+ month + ", carryoverAmount=" + carryoverAmount + ", sm=" + sm + "]";
	}


	public TelecomChongqingComboUse(String taskid, String yearmonth, String totalDesc, String disctDealedAmount,
			String disctUndealedAmount, String disctAmount, String disctUnit, String expDate, String month,
			String carryoverAmount, String sm) {
		super();
		this.taskid = taskid;
		this.yearmonth = yearmonth;
		this.totalDesc = totalDesc;
		this.disctDealedAmount = disctDealedAmount;
		this.disctUndealedAmount = disctUndealedAmount;
		this.disctAmount = disctAmount;
		this.disctUnit = disctUnit;
		this.expDate = expDate;
		this.month = month;
		this.carryoverAmount = carryoverAmount;
		this.sm = sm;
	}


	public TelecomChongqingComboUse() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}