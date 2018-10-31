package com.microservice.dao.entity.crawler.insurance.zhuhai;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

/**
 * 社保-珠海 五险详情表
 * @author zz
 *
 */
@Entity
@Table(name = "insurance_zhuhai_pension")
public class InsuranceZhuhaiUnify extends IdEntity {
	
	private String taskid;
	private String month;					//缴费月份
	private String companyName;				//单位名称
	private String payType;					//缴费状态
	private String paySalary;				//缴费工资
	private String payPerson;				//个人缴费
	private String payCompany;				//单位缴费
	private String dealDate;				//处理日期
	private String companyClassified;		//单位划入
	private String capitalSource;			//资金来源
	private String type;					//保险种类
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getPaySalary() {
		return paySalary;
	}
	public void setPaySalary(String paySalary) {
		this.paySalary = paySalary;
	}
	public String getPayPerson() {
		return payPerson;
	}
	public void setPayPerson(String payPerson) {
		this.payPerson = payPerson;
	}
	public String getPayCompany() {
		return payCompany;
	}
	public void setPayCompany(String payCompany) {
		this.payCompany = payCompany;
	}
	public String getDealDate() {
		return dealDate;
	}
	public void setDealDate(String dealDate) {
		this.dealDate = dealDate;
	}
	public String getCompanyClassified() {
		return companyClassified;
	}
	public void setCompanyClassified(String companyClassified) {
		this.companyClassified = companyClassified;
	}
	public String getCapitalSource() {
		return capitalSource;
	}
	@Override
	public String toString() {
		return "InsuranceZhuhaiPensioin [taskid=" + taskid + ", month=" + month
				+ ", companyName=" + companyName + ", payType=" + payType + ", paySalary=" + paySalary + ", payPerson="
				+ payPerson + ", payCompany=" + payCompany + ", dealDate=" + dealDate + ", companyClassified="
				+ companyClassified + ", capitalSource=" + capitalSource + "]";
	}
	public void setCapitalSource(String capitalSource) {
		this.capitalSource = capitalSource;
	}
	
}
