package com.microservice.dao.entity.crawler.insurance.zhangzhou;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_zhangzhou_maternity",indexes = {@Index(name = "index_insurance_zhangzhou_maternity_taskid", columnList = "taskid")})
public class InsuranceZhangZhouMaternity extends IdEntity{


	private String datea;//建账年月
	private String companyPay;//单位缴费金额
	private String base;//缴费基数
	private String companyRatio;//单位缴费比例
	private String taskid;
	@Override
	public String toString() {
		return "InsuranceZhangZhouMaternity [datea=" + datea + ", companyPay=" + companyPay + ", base=" + base
				+ ", companyRatio=" + companyRatio + ", taskid=" + taskid + "]";
	}
	public String getDatea() {
		return datea;
	}
	public void setDatea(String datea) {
		this.datea = datea;
	}
	public String getCompanyPay() {
		return companyPay;
	}
	public void setCompanyPay(String companyPay) {
		this.companyPay = companyPay;
	}
	public String getBase() {
		return base;
	}
	public void setBase(String base) {
		this.base = base;
	}
	public String getCompanyRatio() {
		return companyRatio;
	}
	public void setCompanyRatio(String companyRatio) {
		this.companyRatio = companyRatio;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	

}
