package com.microservice.dao.entity.crawler.insurance.huangshi;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "insurance_huangshi_payinfo")
public class InsuranceHuangShiPayinfo extends IdEntity {

	private String companyName;							//单位名称
	private String insuranceType;						//险种类型
	private String accountDate;							//台账年月
	private String feeBelongsDate;						//费款所属期
	private String payBase;								//缴费基数
	private String feeType;								//款项
	private String feeSource;							//基金来源
	private String payType;								//缴费类型
	private String thisPay;								//本期应缴
	private String toPersonalAccountMoney;						//划入个人账户金额
	private String payMark;								//缴费标志
	private String arriveDate;							//到账日期
	private String debitMark;							//划帐标志
	private String debitDate;							//划帐日期
	private String personalStatus;						//人员状态
	private String medicalPaymentGrade;					//医疗缴费档次
	private String taskid;
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getInsuranceType() {
		return insuranceType;
	}
	public void setInsuranceType(String insuranceType) {
		this.insuranceType = insuranceType;
	}
	public String getAccountDate() {
		return accountDate;
	}
	public void setAccountDate(String accountDate) {
		this.accountDate = accountDate;
	}
	public String getFeeBelongsDate() {
		return feeBelongsDate;
	}
	public void setFeeBelongsDate(String feeBelongsDate) {
		this.feeBelongsDate = feeBelongsDate;
	}
	public String getPayBase() {
		return payBase;
	}
	public void setPayBase(String payBase) {
		this.payBase = payBase;
	}
	public String getFeeType() {
		return feeType;
	}
	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	public String getFeeSource() {
		return feeSource;
	}
	public void setFeeSource(String feeSource) {
		this.feeSource = feeSource;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getThisPay() {
		return thisPay;
	}
	public void setThisPay(String thisPay) {
		this.thisPay = thisPay;
	}
	public String getToPersonalAccountMoney() {
		return toPersonalAccountMoney;
	}
	public void setToPersonalAccountMoney(String toPersonalAccountMoney) {
		this.toPersonalAccountMoney = toPersonalAccountMoney;
	}
	public String getPayMark() {
		return payMark;
	}
	public void setPayMark(String payMark) {
		this.payMark = payMark;
	}
	public String getArriveDate() {
		return arriveDate;
	}
	public void setArriveDate(String arriveDate) {
		this.arriveDate = arriveDate;
	}
	public String getDebitMark() {
		return debitMark;
	}
	public void setDebitMark(String debitMark) {
		this.debitMark = debitMark;
	}
	public String getDebitDate() {
		return debitDate;
	}
	public void setDebitDate(String debitDate) {
		this.debitDate = debitDate;
	}
	public String getPersonalStatus() {
		return personalStatus;
	}
	public void setPersonalStatus(String personalStatus) {
		this.personalStatus = personalStatus;
	}
	public String getMedicalPaymentGrade() {
		return medicalPaymentGrade;
	}
	public void setMedicalPaymentGrade(String medicalPaymentGrade) {
		this.medicalPaymentGrade = medicalPaymentGrade;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Override
	public String toString() {
		return "InsuranceHuangShiPayinfo [companyName=" + companyName + ", insuranceType=" + insuranceType
				+ ", accountDate=" + accountDate + ", feeBelongsDate=" + feeBelongsDate + ", payBase=" + payBase
				+ ", feeType=" + feeType + ", feeSource=" + feeSource + ", payType=" + payType + ", thisPay=" + thisPay
				+ ", toPersonalAccountMoney=" + toPersonalAccountMoney + ", payMark=" + payMark + ", arriveDate="
				+ arriveDate + ", debitMark=" + debitMark + ", debitDate=" + debitDate + ", personalStatus="
				+ personalStatus + ", medicalPaymentGrade=" + medicalPaymentGrade + ", taskid=" + taskid + "]";
	}
	
}