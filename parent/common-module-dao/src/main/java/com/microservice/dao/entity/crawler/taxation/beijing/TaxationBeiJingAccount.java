package com.microservice.dao.entity.crawler.taxation.beijing;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name="taxation_beijing_account",indexes = {@Index(name = "index_taxation_beijing_account_taskid", columnList = "taskid")}) 
public class TaxationBeiJingAccount extends IdEntity{
	
	private String taskid;
	
	private String getProject;//所得项目
	private String taxDate;//税款所属期
	private String getMoney;//申报收入额
	private String taxRatio;//实缴(退)税额
	private String inDate;//入(退)库时间
	private String company;//任职/受雇/投资单位
	private String govement;//征收机关
	@Override
	public String toString() {
		return "TaxationBeiJingAccount [taskid=" + taskid + ", getProject=" + getProject + ", taxDate=" + taxDate
				+ ", getMoney=" + getMoney + ", taxRatio=" + taxRatio + ", inDate=" + inDate + ", company=" + company
				+ ", govement=" + govement + "]";
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getGetProject() {
		return getProject;
	}
	public void setGetProject(String getProject) {
		this.getProject = getProject;
	}
	public String getTaxDate() {
		return taxDate;
	}
	public void setTaxDate(String taxDate) {
		this.taxDate = taxDate;
	}
	public String getGetMoney() {
		return getMoney;
	}
	public void setGetMoney(String getMoney) {
		this.getMoney = getMoney;
	}
	public String getTaxRatio() {
		return taxRatio;
	}
	public void setTaxRatio(String taxRatio) {
		this.taxRatio = taxRatio;
	}
	public String getInDate() {
		return inDate;
	}
	public void setInDate(String inDate) {
		this.inDate = inDate;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getGovement() {
		return govement;
	}
	public void setGovement(String govement) {
		this.govement = govement;
	}
	
	
}
