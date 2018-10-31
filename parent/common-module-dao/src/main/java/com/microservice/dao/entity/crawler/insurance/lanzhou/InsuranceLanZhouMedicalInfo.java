package com.microservice.dao.entity.crawler.insurance.lanzhou;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "insurance_lanzhou_medicalinfo",indexes = {@Index(name = "index_insurance_lanzhou_medicalinfo_taskid", columnList = "taskid")})
public class InsuranceLanZhouMedicalInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = 5273473947864101643L;
	private String taskid;
//	个人编号
	private String pernum;
//	姓名
	private String name;
//	身份证号码
	private String idnum;
//	划账结算期
	private String accountsettleperiod;
//	缴费结算期
	private String chargesettleperiod;
//	单位名称
	private String unitname;
//	缴费基数
	private String chargebasenum;
//	个人缴费划账户
	private String percharge;
//	单位缴费划账户
	private String unitcharge;
//	合计
	private String totalamount;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getPernum() {
		return pernum;
	}
	public void setPernum(String pernum) {
		this.pernum = pernum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getAccountsettleperiod() {
		return accountsettleperiod;
	}
	public void setAccountsettleperiod(String accountsettleperiod) {
		this.accountsettleperiod = accountsettleperiod;
	}
	public String getChargesettleperiod() {
		return chargesettleperiod;
	}
	public void setChargesettleperiod(String chargesettleperiod) {
		this.chargesettleperiod = chargesettleperiod;
	}
	public String getUnitname() {
		return unitname;
	}
	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}
	public String getChargebasenum() {
		return chargebasenum;
	}
	public void setChargebasenum(String chargebasenum) {
		this.chargebasenum = chargebasenum;
	}
	public String getPercharge() {
		return percharge;
	}
	public void setPercharge(String percharge) {
		this.percharge = percharge;
	}
	public String getUnitcharge() {
		return unitcharge;
	}
	public void setUnitcharge(String unitcharge) {
		this.unitcharge = unitcharge;
	}
	public String getTotalamount() {
		return totalamount;
	}
	public void setTotalamount(String totalamount) {
		this.totalamount = totalamount;
	}
	public InsuranceLanZhouMedicalInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
}