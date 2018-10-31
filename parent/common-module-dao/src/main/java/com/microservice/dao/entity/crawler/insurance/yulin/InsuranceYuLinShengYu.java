package com.microservice.dao.entity.crawler.insurance.yulin;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "insurance_yulin_shengyu",indexes = {@Index(name = "index_insurance_yulin_shengyu_taskid", columnList = "taskid")})
public class InsuranceYuLinShengYu extends IdEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	private String taskid;
	
	private String companyname;
	
	private String settlement_period;
	
	private String fkdate;
	
	private String insurance_type;
	
	private String pay_type;
	
	private String base;
	
	private String total;
	
	private String company_amount_receivable;
	
	private String personage_amount_receivable;
	
	private String state;
	private int pagenum;

	public int getPagenum() {
		return pagenum;
	}

	public void setPagenum(int pagenum) {
		this.pagenum = pagenum;
	}
	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}

	public String getSettlement_period() {
		return settlement_period;
	}

	public void setSettlement_period(String settlement_period) {
		this.settlement_period = settlement_period;
	}

	public String getFkdate() {
		return fkdate;
	}

	public void setFkdate(String fkdate) {
		this.fkdate = fkdate;
	}

	public String getInsurance_type() {
		return insurance_type;
	}

	public void setInsurance_type(String insurance_type) {
		this.insurance_type = insurance_type;
	}

	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getCompany_amount_receivable() {
		return company_amount_receivable;
	}

	public void setCompany_amount_receivable(String company_amount_receivable) {
		this.company_amount_receivable = company_amount_receivable;
	}

	public String getPersonage_amount_receivable() {
		return personage_amount_receivable;
	}

	public void setPersonage_amount_receivable(String personage_amount_receivable) {
		this.personage_amount_receivable = personage_amount_receivable;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public InsuranceYuLinShengYu(String taskid, String companyname, String settlement_period, String fkdate,
			String insurance_type, String pay_type, String base, String total, String company_amount_receivable,
			String personage_amount_receivable, String state) {
		super();
		this.taskid = taskid;
		this.companyname = companyname;
		this.settlement_period = settlement_period;
		this.fkdate = fkdate;
		this.insurance_type = insurance_type;
		this.pay_type = pay_type;
		this.base = base;
		this.total = total;
		this.company_amount_receivable = company_amount_receivable;
		this.personage_amount_receivable = personage_amount_receivable;
		this.state = state;
	}

	public InsuranceYuLinShengYu() {
		super();
	}

	@Override
	public String toString() {
		return "InsuranceYuLinYangLao [taskid=" + taskid + ", companyname=" + companyname + ", settlement_period="
				+ settlement_period + ", fkdate=" + fkdate + ", insurance_type=" + insurance_type + ", pay_type="
				+ pay_type + ", base=" + base + ", total=" + total + ", company_amount_receivable="
				+ company_amount_receivable + ", personage_amount_receivable=" + personage_amount_receivable
				+ ", state=" + state + "]";
	}
	
	
}
