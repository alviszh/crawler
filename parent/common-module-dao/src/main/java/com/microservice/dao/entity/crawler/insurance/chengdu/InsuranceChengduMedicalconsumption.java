package com.microservice.dao.entity.crawler.insurance.chengdu;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 成都社保:医疗账户消费明细
 * @author tz
 *
 */
@Entity
@Table(name = "insurance_chengdu_medicalconsumption" ,indexes = {@Index(name = "index_insurance_chengdu_medicalconsumption_taskid", columnList = "taskid")})
public class InsuranceChengduMedicalconsumption extends IdEntity{
	
	/**
	 * uuid 前端通过uuid访问状态结果	
	 */
	private String taskid;
	
	/**
	 * 账户支付时间
	 */
	private String payData;				
	/**
	 * 医疗机构名称
	 */
	private String medicalName;							
	/**
	 * 支付金额
	 */
	private String payMoney;							
	/**
	 * 支付类别
	 */
	private String payType;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getPayData() {
		return payData;
	}
	public void setPayData(String payData) {
		this.payData = payData;
	}
	public String getMedicalName() {
		return medicalName;
	}
	public void setMedicalName(String medicalName) {
		this.medicalName = medicalName;
	}
	public String getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(String payMoney) {
		this.payMoney = payMoney;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	@Override
	public String toString() {
		return "InsuranceChengduMedicalconsumption [taskid=" + taskid + ", payData=" + payData + ", medicalName="
				+ medicalName + ", payMoney=" + payMoney + ", payType=" + payType + "]";
	}
	public InsuranceChengduMedicalconsumption(String taskid, String payData, String medicalName, String payMoney,
			String payType) {
		super();
		this.taskid = taskid;
		this.payData = payData;
		this.medicalName = medicalName;
		this.payMoney = payMoney;
		this.payType = payType;
	}
	public InsuranceChengduMedicalconsumption() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	
}
