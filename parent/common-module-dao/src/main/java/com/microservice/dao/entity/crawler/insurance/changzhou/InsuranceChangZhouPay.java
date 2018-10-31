/**
  * Copyright 2018 bejson.com 
  */
package com.microservice.dao.entity.crawler.insurance.changzhou;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * Auto-generated: 2018-03-15 13:57:19
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */

@Entity
@Table(name="insurance_changzhou_pay",indexes = {@Index(name = "index_insurance_changzhou_pay_taskid", columnList = "taskId")})
public class InsuranceChangZhouPay extends IdEntity{

    private long paymentDate; //缴费年月
    private String insuranceCode;//110 养老保险 310 医疗保险 410 工伤保险 510 生育保险 210 失业保险 380 居民医疗保险
    private String paymentType;//
    private String paymentBase;//缴费基数
    private String personalPayable;//个人应缴
    private String personalActualPay;//个人实缴
    private String companyPayable;//单位应缴
    private String companyActualPay;//单位应缴
    private String creditAccount;//
    private String totalPayment;//
    private String insuranceID;//
    private String personid;//
    private String grjfbl;//单位应缴比例
    private String dwjfbl;//个人应缴比例
    private String qfhj;//欠费合计
    private String companyId;//公司id
    private String companyName;//公司名
    private String taskId;//
    
    
    public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public void setPaymentDate(long paymentDate) {
         this.paymentDate = paymentDate;
     }
     public long getPaymentDate() {
         return paymentDate;
     }

    public void setInsuranceCode(String insuranceCode) {
         this.insuranceCode = insuranceCode;
     }
     public String getInsuranceCode() {
         return insuranceCode;
     }

    public void setPaymentType(String paymentType) {
         this.paymentType = paymentType;
     }
     public String getPaymentType() {
         return paymentType;
     }

    public void setPaymentBase(String paymentBase) {
         this.paymentBase = paymentBase;
     }
     public String getPaymentBase() {
         return paymentBase;
     }

    public void setPersonalPayable(String personalPayable) {
         this.personalPayable = personalPayable;
     }
     public String getPersonalPayable() {
         return personalPayable;
     }

    public void setPersonalActualPay(String personalActualPay) {
         this.personalActualPay = personalActualPay;
     }
     public String getPersonalActualPay() {
         return personalActualPay;
     }

    public void setCompanyPayable(String companyPayable) {
         this.companyPayable = companyPayable;
     }
     public String getCompanyPayable() {
         return companyPayable;
     }

    public void setCompanyActualPay(String companyActualPay) {
         this.companyActualPay = companyActualPay;
     }
     public String getCompanyActualPay() {
         return companyActualPay;
     }

    public void setCreditAccount(String creditAccount) {
         this.creditAccount = creditAccount;
     }
     public String getCreditAccount() {
         return creditAccount;
     }

    public void setTotalPayment(String totalPayment) {
         this.totalPayment = totalPayment;
     }
     public String getTotalPayment() {
         return totalPayment;
     }

    public void setInsuranceID(String insuranceID) {
         this.insuranceID = insuranceID;
     }
     public String getInsuranceID() {
         return insuranceID;
     }

    public void setPersonid(String personid) {
         this.personid = personid;
     }
     public String getPersonid() {
         return personid;
     }

    public void setGrjfbl(String grjfbl) {
         this.grjfbl = grjfbl;
     }
     public String getGrjfbl() {
         return grjfbl;
     }

    public void setDwjfbl(String dwjfbl) {
         this.dwjfbl = dwjfbl;
     }
     public String getDwjfbl() {
         return dwjfbl;
     }

    public void setQfhj(String qfhj) {
         this.qfhj = qfhj;
     }
     public String getQfhj() {
         return qfhj;
     }

    public void setCompanyId(String companyId) {
         this.companyId = companyId;
     }
     public String getCompanyId() {
         return companyId;
     }

    public void setCompanyName(String companyName) {
         this.companyName = companyName;
     }
     public String getCompanyName() {
         return companyName;
     }
	@Override
	public String toString() {
		return "InsuranceChangZhouPay [paymentDate=" + paymentDate + ", insuranceCode=" + insuranceCode
				+ ", paymentType=" + paymentType + ", paymentBase=" + paymentBase + ", personalPayable="
				+ personalPayable + ", personalActualPay=" + personalActualPay + ", companyPayable=" + companyPayable
				+ ", companyActualPay=" + companyActualPay + ", creditAccount=" + creditAccount + ", totalPayment="
				+ totalPayment + ", insuranceID=" + insuranceID + ", personid=" + personid + ", grjfbl=" + grjfbl
				+ ", dwjfbl=" + dwjfbl + ", qfhj=" + qfhj + ", companyId=" + companyId + ", companyName=" + companyName
				+ ", taskId=" + taskId + "]";
	}

     
}