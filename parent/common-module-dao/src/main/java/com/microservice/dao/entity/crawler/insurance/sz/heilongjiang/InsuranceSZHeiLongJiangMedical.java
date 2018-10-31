package com.microservice.dao.entity.crawler.insurance.sz.heilongjiang;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name="insurance_sz_heilongjiang_medical",indexes = {@Index(name = "index_insurance_sz_heilongjiang_medical_taskid", columnList = "taskid")}) 
public class InsuranceSZHeiLongJiangMedical extends IdEntity{

	private String taskid;
	
	private String payDate;//缴费属期
	
	private String payType;//缴费类别
	
	private String payBase;//缴费基数
	
	private String mustSum;//应缴合计
	
	private String mustPersonal;//应缴个人
	
	private String mustCompany;//应缴单位
	
	private String factSum;//实缴合计
	
	private String factPersonal;//实缴个人
	
	private String factCompany;//实缴单位
	
	private String payStatus;//缴费状态
	
	private String peopleStatus;//人员类别

	@Override
	public String toString() {
		return "InsuranceSZHeiLongJiangMedical [taskid=" + taskid + ", payDate=" + payDate + ", payType=" + payType
				+ ", payBase=" + payBase + ", mustSum=" + mustSum + ", mustPersonal=" + mustPersonal + ", mustCompany="
				+ mustCompany + ", factSum=" + factSum + ", factPersonal=" + factPersonal + ", factCompany="
				+ factCompany + ", payStatus=" + payStatus + ", peopleStatus=" + peopleStatus + "]";
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getPayDate() {
		return payDate;
	}

	public void setPayDate(String payDate) {
		this.payDate = payDate;
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

	public String getMustSum() {
		return mustSum;
	}

	public void setMustSum(String mustSum) {
		this.mustSum = mustSum;
	}

	public String getMustPersonal() {
		return mustPersonal;
	}

	public void setMustPersonal(String mustPersonal) {
		this.mustPersonal = mustPersonal;
	}

	public String getMustCompany() {
		return mustCompany;
	}

	public void setMustCompany(String mustCompany) {
		this.mustCompany = mustCompany;
	}

	public String getFactSum() {
		return factSum;
	}

	public void setFactSum(String factSum) {
		this.factSum = factSum;
	}

	public String getFactPersonal() {
		return factPersonal;
	}

	public void setFactPersonal(String factPersonal) {
		this.factPersonal = factPersonal;
	}

	public String getFactCompany() {
		return factCompany;
	}

	public void setFactCompany(String factCompany) {
		this.factCompany = factCompany;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public String getPeopleStatus() {
		return peopleStatus;
	}

	public void setPeopleStatus(String peopleStatus) {
		this.peopleStatus = peopleStatus;
	}
	
	
}
