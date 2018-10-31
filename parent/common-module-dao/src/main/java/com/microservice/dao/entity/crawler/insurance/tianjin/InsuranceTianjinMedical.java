package com.microservice.dao.entity.crawler.insurance.tianjin;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_tianjin_medical",indexes = {@Index(name = "index_insurance_tianjin_medical_taskid", columnList = "taskid")}) 
public class InsuranceTianjinMedical extends IdEntity{

	private String taskid;
	private String payCompany;   //缴费公司
	private String payDate;   //缴费年月
	private String payTime;  //费款所属年月
	private String payBase;  //缴费基数
	private String companyOverallPay; //单位划入统筹
	private String companyPay; //单位划入个账
	private String personOverallPay;  //个人缴纳统筹
	private String personPay;   //个人缴纳金额
	private String payCount;    //缴费合计
	private String accountIncome;  //划入个人账户
	@Override
	public String toString() {
		return "InsuranceTianjinUnemployment [taskid=" + taskid + ", payCompany=" + payCompany + ", payDate=" + payDate
				+ ", payTime=" + payTime + ", payBase=" + payBase + ", companyOverallPay=" + companyOverallPay
				+ ", companyPay=" + companyPay + ", personOverallPay=" + personOverallPay + ", personPay=" + personPay
				+ ", payCount=" + payCount + ", accountIncome=" + accountIncome + "]";
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getPayCompany() {
		return payCompany;
	}
	public void setPayCompany(String payCompany) {
		this.payCompany = payCompany;
	}
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	public String getPayBase() {
		return payBase;
	}
	public void setPayBase(String payBase) {
		this.payBase = payBase;
	}
	public String getCompanyOverallPay() {
		return companyOverallPay;
	}
	public void setCompanyOverallPay(String companyOverallPay) {
		this.companyOverallPay = companyOverallPay;
	}
	public String getCompanyPay() {
		return companyPay;
	}
	public void setCompanyPay(String companyPay) {
		this.companyPay = companyPay;
	}
	public String getPersonOverallPay() {
		return personOverallPay;
	}
	public void setPersonOverallPay(String personOverallPay) {
		this.personOverallPay = personOverallPay;
	}
	public String getPersonPay() {
		return personPay;
	}
	public void setPersonPay(String personPay) {
		this.personPay = personPay;
	}
	public String getPayCount() {
		return payCount;
	}
	public void setPayCount(String payCount) {
		this.payCount = payCount;
	}
	public String getAccountIncome() {
		return accountIncome;
	}
	public void setAccountIncome(String accountIncome) {
		this.accountIncome = accountIncome;
	}
	
	
}
