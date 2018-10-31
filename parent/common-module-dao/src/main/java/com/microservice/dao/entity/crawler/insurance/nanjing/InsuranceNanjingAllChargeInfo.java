package com.microservice.dao.entity.crawler.insurance.nanjing;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description:
 * @author: sln 
 * @date: 2017年9月27日 上午9:29:52 
 */
@Entity
@Table(name = "insurance_nanjing_allchargeinfo",indexes = {@Index(name = "index_insurance_nanjing_allchargeinfo_taskid", columnList = "taskid")})
public class InsuranceNanjingAllChargeInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = 4581928125198379521L;
	private String taskid;
//	险种
	private String insurancetype;
//	缴费基数（元）
	private String chargebasenum;
//	缴费月份
	private String chargemonth;
//	单位应缴（元）
	private String compcharge;
//	个人应缴（元）
	private String personalcharge;
//	利息（元）
	private String  interest;
//	滞纳金（元）
	private String overduefine;
//	单位名称
	private String unitname;
//	单位编号
	private String unitnum;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getInsurancetype() {
		return insurancetype;
	}
	public void setInsurancetype(String insurancetype) {
		this.insurancetype = insurancetype;
	}
	public String getChargebasenum() {
		return chargebasenum;
	}
	public void setChargebasenum(String chargebasenum) {
		this.chargebasenum = chargebasenum;
	}
	public String getChargemonth() {
		return chargemonth;
	}
	public void setChargemonth(String chargemonth) {
		this.chargemonth = chargemonth;
	}
	public String getCompcharge() {
		return compcharge;
	}
	public void setCompcharge(String compcharge) {
		this.compcharge = compcharge;
	}
	public String getPersonalcharge() {
		return personalcharge;
	}
	public void setPersonalcharge(String personalcharge) {
		this.personalcharge = personalcharge;
	}
	public String getInterest() {
		return interest;
	}
	public void setInterest(String interest) {
		this.interest = interest;
	}
	public String getOverduefine() {
		return overduefine;
	}
	public void setOverduefine(String overduefine) {
		this.overduefine = overduefine;
	}
	public InsuranceNanjingAllChargeInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getUnitname() {
		return unitname;
	}
	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}
	public String getUnitnum() {
		return unitnum;
	}
	public void setUnitnum(String unitnum) {
		this.unitnum = unitnum;
	}
	public InsuranceNanjingAllChargeInfo(String taskid, String insurancetype, String chargebasenum, String chargemonth,
			String compcharge, String personalcharge, String interest, String overduefine, String unitname,
			String unitnum) {
		super();
		this.taskid = taskid;
		this.insurancetype = insurancetype;
		this.chargebasenum = chargebasenum;
		this.chargemonth = chargemonth;
		this.compcharge = compcharge;
		this.personalcharge = personalcharge;
		this.interest = interest;
		this.overduefine = overduefine;
		this.unitname = unitname;
		this.unitnum = unitnum;
	}
}
