package com.microservice.dao.entity.crawler.insurance.sz.guangxi;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 * 缴费明细
 * @author zcx
 *
 */
@Entity
@Table(name="insurance_sz_guangxi_paydetails")
public class InsuranceSZGuangXipaydetails extends IdEntity {
	private String companyname;//单位名称
	private String paymonth;//费款所属年月
	private String type;//险种类型
	private String paybase;//缴费基数
	private String persionpayAmount;//个人数
	private String unitpayAmount;//单位数
	private String personalPay;//划入个人账号金额
	private String unitPay;//单位缴费标志
	private String unitpaySign;//单位缴费标志
	private String persionpaySign;//单位缴费标志
	private String taskid;	
	public String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
	public String getPaymonth() {
		return paymonth;
	}
	public void setPaymonth(String paymonth) {
		this.paymonth = paymonth;
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
	public String getPersionpayAmount() {
		return persionpayAmount;
	}
	public void setPersionpayAmount(String persionpayAmount) {
		this.persionpayAmount = persionpayAmount;
	}
	public String getUnitpayAmount() {
		return unitpayAmount;
	}
	public void setUnitpayAmount(String unitpayAmount) {
		this.unitpayAmount = unitpayAmount;
	}
	public String getPersonalPay() {
		return personalPay;
	}
	public void setPersonalPay(String personalPay) {
		this.personalPay = personalPay;
	}
	public String getUnitPay() {
		return unitPay;
	}
	public void setUnitPay(String unitPay) {
		this.unitPay = unitPay;
	}
	public String getUnitpaySign() {
		return unitpaySign;
	}
	public void setUnitpaySign(String unitpaySign) {
		this.unitpaySign = unitpaySign;
	}
	public String getPersionpaySign() {
		return persionpaySign;
	}
	public void setPersionpaySign(String persionpaySign) {
		this.persionpaySign = persionpaySign;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}	
	
	public InsuranceSZGuangXipaydetails(String companyname, String paymonth, String type, String paybase,
			String persionpayAmount, String unitpayAmount, String personalPay, String unitPay, String unitpaySign,
			String persionpaySign, String taskid) {
		super();
		this.companyname = companyname;
		this.paymonth = paymonth;
		this.type = type;
		this.paybase = paybase;
		this.persionpayAmount = persionpayAmount;
		this.unitpayAmount = unitpayAmount;
		this.personalPay = personalPay;
		this.unitPay = unitPay;
		this.unitpaySign = unitpaySign;
		this.persionpaySign = persionpaySign;
		this.taskid = taskid;
	}
	public InsuranceSZGuangXipaydetails() {
		super();
	}
}
