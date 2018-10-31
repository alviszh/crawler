package com.microservice.dao.entity.crawler.insurance.suzhou;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_suzhou_unemployment")
public class InsuranceSuzhouUnemployment extends IdEntity {
	
	@Column(name="task_id")
	private String taskid;	//uuid唯一标识
	private String settlementTime; //结算期；
	private String companyName; //单位名称
	private String payType; //应缴类型
	private String payBase; //缴费基数
	private String months;  //月数
	private String companyPay; // 单位缴纳
	private String personal ; //个人缴纳
	private String arrivalFlag;  //到账标记
	private String arrivalTime; //到账日期
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getSettlementTime() {
		return settlementTime;
	}
	public void setSettlementTime(String settlementTime) {
		this.settlementTime = settlementTime;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getPayBase() {
		return payBase;
	}
	public void setPayBase(String payBase) {
		this.payBase = payBase;
	}
	public String getMonths() {
		return months;
	}
	public void setMonths(String months) {
		this.months = months;
	}
	public String getCompanyPay() {
		return companyPay;
	}
	public void setCompanyPay(String companyPay) {
		this.companyPay = companyPay;
	}
	public String getPersonal() {
		return personal;
	}
	public void setPersonal(String personal) {
		this.personal = personal;
	}
	public String getArrivalFlag() {
		return arrivalFlag;
	}
	public void setArrivalFlag(String arrivalFlag) {
		this.arrivalFlag = arrivalFlag;
	}
	public String getArrivalTime() {
		return arrivalTime;
	}
	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	
	
	
}
