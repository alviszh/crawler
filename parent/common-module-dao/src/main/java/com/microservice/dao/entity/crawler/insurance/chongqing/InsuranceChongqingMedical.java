package com.microservice.dao.entity.crawler.insurance.chongqing;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 * 重庆医保
 * @author zcx
 *
 */
@Entity
@Table(name="insurance_chongqing_medical",indexes = {@Index(name = "index_insurance_chongqing_medical_taskid", columnList = "taskid")})
public class InsuranceChongqingMedical extends IdEntity {
	
	private String feeMonth; // 姓名	
	private String companyName; // 单位名称
	private String payPlace; // 参保地
	private String payBase; // 缴费基数(元)
	private String personalPay;//个人缴费金额(元)
	private String payMark; // 缴费标记
	private String payType;//费款科目	
	private String dealDate;//经办日期
	private String idNum;//身份证号
	private String name;//姓名
	private String personNumber;//个人编号
	private String socialSecurityCardNum;//社保卡号
	private String paymentType;//缴费类型
	private String feeMonth2;//费款所属期
	private String companyNum;//单位编号
	private String taskid;
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
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	
	public String getDealDate() {
		return dealDate;
	}
	public void setDealDate(String dealDate) {
		this.dealDate = dealDate;
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
	public String getPersonNumber() {
		return personNumber;
	}
	public void setPersonNumber(String personNumber) {
		this.personNumber = personNumber;
	}
	public String getSocialSecurityCardNum() {
		return socialSecurityCardNum;
	}
	public void setSocialSecurityCardNum(String socialSecurityCardNum) {
		this.socialSecurityCardNum = socialSecurityCardNum;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getFeeMonth2() {
		return feeMonth2;
	}
	public void setFeeMonth2(String feeMonth2) {
		this.feeMonth2 = feeMonth2;
	}
	public String getCompanyNum() {
		return companyNum;
	}
	public void setCompanyNum(String companyNum) {
		this.companyNum = companyNum;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	@Override
	public String toString() {
		return "InsuranceChongqingMedical [feeMonth=" + feeMonth + ", companyName=" + companyName + ", payPlace="
				+ payPlace + ", payBase=" + payBase + ", personalPay=" + personalPay + ", payMark=" + payMark
				+ ", payType=" + payType + ", dealDate=" + dealDate + ", idNum=" + idNum + ", name=" + name
				+ ", personNumber=" + personNumber + ", socialSecurityCardNum=" + socialSecurityCardNum
				+ ", paymentType=" + paymentType + ", feeMonth2=" + feeMonth2 + ", companyNum=" + companyNum
				+ ", taskid=" + taskid + "]";
	}
	public InsuranceChongqingMedical() {
		super();
		// TODO Auto-generated constructor stub
	}
	public InsuranceChongqingMedical(String feeMonth, String companyName, String payPlace, String payBase,
			String personalPay, String payMark, String payType, String dealDate, String idNum, String name,
			String personNumber, String socialSecurityCardNum, String paymentType, String feeMonth2, String companyNum,
			String taskid) {
		super();
		this.feeMonth = feeMonth;
		this.companyName = companyName;
		this.payPlace = payPlace;
		this.payBase = payBase;
		this.personalPay = personalPay;
		this.payMark = payMark;
		this.payType = payType;
		this.dealDate = dealDate;
		this.idNum = idNum;
		this.name = name;
		this.personNumber = personNumber;
		this.socialSecurityCardNum = socialSecurityCardNum;
		this.paymentType = paymentType;
		this.feeMonth2 = feeMonth2;
		this.companyNum = companyNum;
		this.taskid = taskid;
	}
	
}
