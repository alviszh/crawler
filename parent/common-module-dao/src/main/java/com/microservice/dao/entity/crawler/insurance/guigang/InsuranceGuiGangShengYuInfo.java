package com.microservice.dao.entity.crawler.insurance.guigang;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "insurance_guigang_shengyuinfo",indexes = {@Index(name = "index_insurance_guigang_shengyuinfo_taskid", columnList = "taskid")})
public class InsuranceGuiGangShengYuInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = -7225639204374657354L;
	private String taskid;
	private String gs_name;//公司名称
	private String name;
	private String issue;//期号
	private String pay_salary;//缴费工资
	private String dw_pay;//单位缴费
	private String gr_pay;//个人缴费
	private String receiveddate;//到账日期
	private String duty;//职务类型
	private String type;

	public String getTaskid() {
		return taskid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getGs_name() {
		return gs_name;
	}
	public void setGs_name(String gs_name) {
		this.gs_name = gs_name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}
	public String getPay_salary() {
		return pay_salary;
	}
	public void setPay_salary(String pay_salary) {
		this.pay_salary = pay_salary;
	}
	public String getDw_pay() {
		return dw_pay;
	}
	public void setDw_pay(String dw_pay) {
		this.dw_pay = dw_pay;
	}
	public String getGr_pay() {
		return gr_pay;
	}
	public void setGr_pay(String gr_pay) {
		this.gr_pay = gr_pay;
	}
	public String getReceiveddate() {
		return receiveddate;
	}
	public void setReceiveddate(String receiveddate) {
		this.receiveddate = receiveddate;
	}
	public String getDuty() {
		return duty;
	}
	public void setDuty(String duty) {
		this.duty = duty;
	}
	
	public InsuranceGuiGangShengYuInfo(String taskid, String gs_name, String name, String issue, String pay_salary,
			String dw_pay, String gr_pay, String receiveddate, String duty, String type) {
		super();
		this.taskid = taskid;
		this.gs_name = gs_name;
		this.name = name;
		this.issue = issue;
		this.pay_salary = pay_salary;
		this.dw_pay = dw_pay;
		this.gr_pay = gr_pay;
		this.receiveddate = receiveddate;
		this.duty = duty;
		this.type = type;
	}
	public InsuranceGuiGangShengYuInfo() {
		super();
	}
	@Override
	public String toString() {
		return "InsuranceGuiGangGongShangInfo [taskid=" + taskid + ", gs_name=" + gs_name + ", name=" + name
				+ ", issue=" + issue + ", pay_salary=" + pay_salary + ", dw_pay=" + dw_pay + ", gr_pay=" + gr_pay
				+ ", receiveddate=" + receiveddate + ", duty=" + duty + "]";
	}
	
	
	
	
}
