package com.microservice.dao.entity.crawler.insurance.chengdu;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;


/**
 * 成都社保:大病补充医疗保险缴费明细
 * @author tz
 *
 */
@Entity
@Table(name = "insurance_chengdu_seriousillnessmedical" ,indexes = {@Index(name = "index_insurance_chengdu_seriousillnessmedical_taskid", columnList = "taskid")})
public class InsuranceChengduSeriousIllnessMedical extends IdEntity{
	
	/**
	 * uuid 前端通过uuid访问状态结果	
	 */
	private String taskid;
	
	/**
	 * 缴费月份
	 */
	private String payMonth;				
	/**
	 * 单位名称
	 */
	private String company;							
	/**
	 * 缴费基数
	 */
	private String payBase;	
	/**
	 * 缴费金额		PS:大病补充医疗保险缴费明细字段
	 */
	private String moneyPay;	
	/**
	 * 缴费类型		PS:大病补充医疗保险缴费明细字段
	 */
	private String payType;	
	/**
	 * 缴费比例		PS:大病补充医疗保险缴费明细字段
	 */
	private String payProportion;
	/**
	 * 实收时间
	 */
	private String receivablesDate;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getPayMonth() {
		return payMonth;
	}
	public void setPayMonth(String payMonth) {
		this.payMonth = payMonth;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getPayBase() {
		return payBase;
	}
	public void setPayBase(String payBase) {
		this.payBase = payBase;
	}
	public String getMoneyPay() {
		return moneyPay;
	}
	public void setMoneyPay(String moneyPay) {
		this.moneyPay = moneyPay;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getPayProportion() {
		return payProportion;
	}
	public void setPayProportion(String payProportion) {
		this.payProportion = payProportion;
	}
	public String getReceivablesDate() {
		return receivablesDate;
	}
	public void setReceivablesDate(String receivablesDate) {
		this.receivablesDate = receivablesDate;
	}
	@Override
	public String toString() {
		return "InsuranceChengduSeriousIllnessMedical [taskid=" + taskid + ", payMonth=" + payMonth + ", company="
				+ company + ", payBase=" + payBase + ", moneyPay=" + moneyPay + ", payType=" + payType
				+ ", payProportion=" + payProportion + ", receivablesDate=" + receivablesDate + "]";
	}
	public InsuranceChengduSeriousIllnessMedical(String taskid, String payMonth, String company, String payBase,
			String moneyPay, String payType, String payProportion, String receivablesDate) {
		super();
		this.taskid = taskid;
		this.payMonth = payMonth;
		this.company = company;
		this.payBase = payBase;
		this.moneyPay = moneyPay;
		this.payType = payType;
		this.payProportion = payProportion;
		this.receivablesDate = receivablesDate;
	}
	public InsuranceChengduSeriousIllnessMedical() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
