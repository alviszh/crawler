package com.microservice.dao.entity.crawler.insurance.sz.jilin;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name = "insurance_sz_jilin_paymentbase",indexes = {@Index(name = "index_insurance_sz_jilin_paymentbase_taskid", columnList = "taskid")})
public class InsuranceSZJiLinPaymentBase extends IdEntity{

	private String taskid;
	private String name;
	private String idcard;
	private String began_issue;//开始期号
	private String end_issue;//结束期号
	private String declare_wages;//申报工资
	private String payment_base;//缴费基数
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public String getBegan_issue() {
		return began_issue;
	}
	public void setBegan_issue(String began_issue) {
		this.began_issue = began_issue;
	}
	public String getEnd_issue() {
		return end_issue;
	}
	public void setEnd_issue(String end_issue) {
		this.end_issue = end_issue;
	}
	public String getDeclare_wages() {
		return declare_wages;
	}
	public void setDeclare_wages(String declare_wages) {
		this.declare_wages = declare_wages;
	}
	public String getPayment_base() {
		return payment_base;
	}
	public void setPayment_base(String payment_base) {
		this.payment_base = payment_base;
	}
	public InsuranceSZJiLinPaymentBase(String taskid, String name, String idcard, String began_issue, String end_issue,
			String declare_wages, String payment_base) {
		super();
		this.taskid = taskid;
		this.name = name;
		this.idcard = idcard;
		this.began_issue = began_issue;
		this.end_issue = end_issue;
		this.declare_wages = declare_wages;
		this.payment_base = payment_base;
	}
	public InsuranceSZJiLinPaymentBase() {
		super();
	}
	@Override
	public String toString() {
		return "InsuranceBaiShanPaymentBase [taskid=" + taskid + ", name=" + name + ", idcard=" + idcard
				+ ", began_issue=" + began_issue + ", end_issue=" + end_issue + ", declare_wages=" + declare_wages
				+ ", payment_base=" + payment_base + "]";
	}
	
	
	
}
