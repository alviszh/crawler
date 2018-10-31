package com.microservice.dao.entity.crawler.insurance.sz.jilin;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "insurance_sz_jilin_soldout",indexes = {@Index(name = "index_insurance_sz_jilin_soldout_taskid", columnList = "taskid")})
public class InsuranceSZJiLinSoldout extends IdEntity{

	private String taskid;
	private String years;//年度
	private String issue;//期号
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getYears() {
		return years;
	}
	public void setYears(String years) {
		this.years = years;
	}
	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}
	public InsuranceSZJiLinSoldout(String taskid, String years, String issue) {
		super();
		this.taskid = taskid;
		this.years = years;
		this.issue = issue;
	}
	public InsuranceSZJiLinSoldout() {
		super();
	}
	@Override
	public String toString() {
		return "InsuranceBaiShanSoldout [taskid=" + taskid + ", years=" + years + ", issue=" + issue + "]";
	}
	
	
}
