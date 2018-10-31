package com.microservice.dao.entity.crawler.insurance.huizhou;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;
/**
 * 惠州医疗保险
 * @author zcx
 *
 */
@Entity
@Table(name="insurance_huizhou_medical")
public class InsuranceHuiZhouMedical extends IdEntity {
	
	private String unitname;//单位名称
	private String beginmonth;//开始年月
	private String endmonth;//终止年月
	private String monthcount;//月数
	private String type;//险种类型
	private String paybase;//缴费基数
	private String personalPay;//个人应缴金额/月
	private String unitpersionPay;//单位划入个人账号应缴金额/月
	private String unitoverallPay;//单位划入统筹金额/月
	private String medicalPay;//医保个账应缴金额/月	
	private String taskid;//
	
	public String getUnitname() {
		return unitname;
	}
	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}
	public String getBeginmonth() {
		return beginmonth;
	}
	public void setBeginmonth(String beginmonth) {
		this.beginmonth = beginmonth;
	}
	public String getEndmonth() {
		return endmonth;
	}
	public void setEndmonth(String endmonth) {
		this.endmonth = endmonth;
	}
	public String getMonthcount() {
		return monthcount;
	}
	public void setMonthcount(String monthcount) {
		this.monthcount = monthcount;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPaybase() {
		return paybase;
	}
	public void setPaybase(String paybase) {
		this.paybase = paybase;
	}
	public String getPersonalPay() {
		return personalPay;
	}
	public void setPersonalPay(String personalPay) {
		this.personalPay = personalPay;
	}
	public String getUnitpersionPay() {
		return unitpersionPay;
	}
	public void setUnitpersionPay(String unitpersionPay) {
		this.unitpersionPay = unitpersionPay;
	}
	public String getUnitoverallPay() {
		return unitoverallPay;
	}
	public void setUnitoverallPay(String unitoverallPay) {
		this.unitoverallPay = unitoverallPay;
	}
	public String getMedicalPay() {
		return medicalPay;
	}
	public void setMedicalPay(String medicalPay) {
		this.medicalPay = medicalPay;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public InsuranceHuiZhouMedical(String unitname, String beginmonth, String endmonth, String monthcount, String type,
			String paybase, String personalPay, String unitpersionPay, String unitoverallPay, String medicalPay,
			String taskid) {
		super();
		this.unitname = unitname;
		this.beginmonth = beginmonth;
		this.endmonth = endmonth;
		this.monthcount = monthcount;
		this.type = type;
		this.paybase = paybase;
		this.personalPay = personalPay;
		this.unitpersionPay = unitpersionPay;
		this.unitoverallPay = unitoverallPay;
		this.medicalPay = medicalPay;
		this.taskid = taskid;
	}
	public InsuranceHuiZhouMedical() {
		super();
	}
}
