package com.microservice.dao.entity.crawler.insurance.sanming;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @author zz
 * @create 2018-3-15
 * @Desc 社保-三明市 工伤和生育信息表
 */
@Entity
@Table(name = "insurance_sanming_injury")
public class InsuranceSanmingInjury extends IdEntity  {
	
	private String taskid;
	private String businessNum;				//业务流水号
	private String personCode;					//个人编号
	private String insuranceType;				//险种类别
	private String payType;					//缴费类型
	private String companyName;				//单位名称
	private String declarationDate;			//申报日期
	private String payWay;						//缴费方式
	private String companyPayCardinal;			//单位缴费基数
	private String personPayCardinal;			//个人缴费基数
	private String companyPayScale;			//单位缴费比例 
	private String personPayScale;				//个人缴费比例
	private String companyPayMoney;			//单位应缴金额
	private String personPayMoney;				//个人应缴金额
	private String payMoney;					//应缴金额
	private String noticeOrder;				//通知单流水号
	
	 public String getTaskid() {
		return taskid;
	}
	@Override
	public String toString() {
		return "InsuranceSanmingInjury [taskid=" + taskid + ", businessNum=" + businessNum + ", personCode="
				+ personCode + ", insuranceType=" + insuranceType + ", payType=" + payType + ", companyName="
				+ companyName + ", declarationDate=" + declarationDate + ", payWay=" + payWay + ", companyPayCardinal="
				+ companyPayCardinal + ", personPayCardinal=" + personPayCardinal + ", companyPayScale="
				+ companyPayScale + ", personPayScale=" + personPayScale + ", companyPayMoney=" + companyPayMoney
				+ ", personPayMoney=" + personPayMoney + ", payMoney=" + payMoney + ", noticeOrder=" + noticeOrder
				+ "]";
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getBusinessNum() {
		return businessNum;
	}
	public void setBusinessNum(String businessNum) {
		this.businessNum = businessNum;
	}
	public String getPersonCode() {
		return personCode;
	}
	public void setPersonCode(String personCode) {
		this.personCode = personCode;
	}
	public String getInsuranceType() {
		return insuranceType;
	}
	public void setInsuranceType(String insuranceType) {
		this.insuranceType = insuranceType;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getDeclarationDate() {
		return declarationDate;
	}
	public void setDeclarationDate(String declarationDate) {
		this.declarationDate = declarationDate;
	}
	public String getPayWay() {
		return payWay;
	}
	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}
	public String getCompanyPayCardinal() {
		return companyPayCardinal;
	}
	public void setCompanyPayCardinal(String companyPayCardinal) {
		this.companyPayCardinal = companyPayCardinal;
	}
	public String getPersonPayCardinal() {
		return personPayCardinal;
	}
	public void setPersonPayCardinal(String personPayCardinal) {
		this.personPayCardinal = personPayCardinal;
	}
	public String getCompanyPayScale() {
		return companyPayScale;
	}
	public void setCompanyPayScale(String companyPayScale) {
		this.companyPayScale = companyPayScale;
	}
	public String getPersonPayScale() {
		return personPayScale;
	}
	public void setPersonPayScale(String personPayScale) {
		this.personPayScale = personPayScale;
	}
	public String getCompanyPayMoney() {
		return companyPayMoney;
	}
	public void setCompanyPayMoney(String companyPayMoney) {
		this.companyPayMoney = companyPayMoney;
	}
	public String getPersonPayMoney() {
		return personPayMoney;
	}
	public void setPersonPayMoney(String personPayMoney) {
		this.personPayMoney = personPayMoney;
	}
	public String getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(String payMoney) {
		this.payMoney = payMoney;
	}
	public String getNoticeOrder() {
		return noticeOrder;
	}
	public void setNoticeOrder(String noticeOrder) {
		this.noticeOrder = noticeOrder;
	}
	 
}
