package com.microservice.dao.entity.crawler.insurance.chongqing;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 * 重庆社保养老保险
 * @author zcx
 *
 */
@Entity
@Table(name="insurance_chongqing_persion",indexes = {@Index(name = "index_insurance_chongqing_persion_taskid", columnList = "taskid")})
public class InsuranceChongqingPersion extends IdEntity {
	private String feeMonth;//对应费款所属期
	private String companyName;//单位名称
	private String payPlace;//参保地
	private String payBase;//参保基数
	private String personalPay;//个人缴费金额(元)
	private String payMark;//缴费标志
	private String companyNum;//单位编号
	private String personNumber;//个人编号
	private String idNum;//身份证号
	private String name;//姓名
	private String interestAcrossYear;//补缴本人跨年利息
	private String interestYear;//补缴本人本年利息
	private String paymentType;//缴费类型
	private String accountDate;//到账日期
	private String taskid;//
	public String getFeeMonth() {
		return feeMonth;
	}
	public void setFeeMonth(String feeMonth) {
		this.feeMonth = feeMonth;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getPayPlace() {
		return payPlace;
	}
	public void setPayPlace(String payPlace) {
		this.payPlace = payPlace;
	}
	public String getPayBase() {
		return payBase;
	}
	public void setPayBase(String payBase) {
		this.payBase = payBase;
	}
	public String getPersonalPay() {
		return personalPay;
	}
	public void setPersonalPay(String personalPay) {
		this.personalPay = personalPay;
	}
	public String getPayMark() {
		return payMark;
	}
	public void setPayMark(String payMark) {
		this.payMark = payMark;
	}
	
	public String getCompanyNum() {
		return companyNum;
	}
	public void setCompanyNum(String companyNum) {
		this.companyNum = companyNum;
	}
	public String getPersonNumber() {
		return personNumber;
	}
	public void setPersonNumber(String personNumber) {
		this.personNumber = personNumber;
	}
	public String getIdNum() {
		return idNum;
	}
	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInterestAcrossYear() {
		return interestAcrossYear;
	}
	public void setInterestAcrossYear(String interestAcrossYear) {
		this.interestAcrossYear = interestAcrossYear;
	}
	public String getInterestYear() {
		return interestYear;
	}
	public void setInterestYear(String interestYear) {
		this.interestYear = interestYear;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getAccountDate() {
		return accountDate;
	}
	public void setAccountDate(String accountDate) {
		this.accountDate = accountDate;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	@Override
	public String toString() {
		return "InsuranceChongqingPersion [feeMonth=" + feeMonth + ", companyName=" + companyName + ", payPlace="
				+ payPlace + ", payBase=" + payBase + ", personalPay=" + personalPay + ", payMark=" + payMark
				+ ", companyNum=" + companyNum + ", personNumber=" + personNumber + ", idNum=" + idNum + ", name="
				+ name + ", interestAcrossYear=" + interestAcrossYear + ", interestYear=" + interestYear
				+ ", paymentType=" + paymentType + ", accountDate=" + accountDate + ", taskid=" + taskid + "]";
	}
	
	public InsuranceChongqingPersion() {
		super();
		// TODO Auto-generated constructor stub
	}
	public InsuranceChongqingPersion(String feeMonth, String companyName, String payPlace, String payBase,
			String personalPay, String payMark, String companyNum, String personNumber, String idNum, String name,
			String interestAcrossYear, String interestYear, String paymentType, String accountDate, String taskid) {
		super();
		this.feeMonth = feeMonth;
		this.companyName = companyName;
		this.payPlace = payPlace;
		this.payBase = payBase;
		this.personalPay = personalPay;
		this.payMark = payMark;
		this.companyNum = companyNum;
		this.personNumber = personNumber;
		this.idNum = idNum;
		this.name = name;
		this.interestAcrossYear = interestAcrossYear;
		this.interestYear = interestYear;
		this.paymentType = paymentType;
		this.accountDate = accountDate;
		this.taskid = taskid;
	}
	
}
