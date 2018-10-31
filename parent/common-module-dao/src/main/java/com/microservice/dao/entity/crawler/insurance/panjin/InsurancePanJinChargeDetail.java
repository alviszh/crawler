package com.microservice.dao.entity.crawler.insurance.panjin;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description:
 * @author: sln 
 */
@Entity
@Table(name = "insurance_panjin_chargedetail",indexes = {@Index(name = "index_insurance_panjin_chargedetail_taskid", columnList = "taskid")})
public class InsurancePanJinChargeDetail extends IdEntity implements Serializable{
	private static final long serialVersionUID = 1208044439567427753L;
	private String taskid;
//	险种类别
	private String insurtype;
//	单位编号
	private String unitnum;
//	单位名称 
	private String unitname;
//	缴费类型
	private String chargetype;
//	缴费年月
	private String chargeyearmonth;
//	缴费标志
	private String chargeflag;
//	缴费基数
	private String chargebasenum;
//	个人缴费
	private String percharge;
//	单位缴费
	private String unitcharge;
//	到账日期
	private String accountdate;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getInsurtype() {
		return insurtype;
	}
	public void setInsurtype(String insurtype) {
		this.insurtype = insurtype;
	}
	public String getUnitnum() {
		return unitnum;
	}
	public void setUnitnum(String unitnum) {
		this.unitnum = unitnum;
	}
	public String getUnitname() {
		return unitname;
	}
	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}
	public String getChargetype() {
		return chargetype;
	}
	public void setChargetype(String chargetype) {
		this.chargetype = chargetype;
	}
	public String getChargeyearmonth() {
		return chargeyearmonth;
	}
	public void setChargeyearmonth(String chargeyearmonth) {
		this.chargeyearmonth = chargeyearmonth;
	}
	public String getChargeflag() {
		return chargeflag;
	}
	public void setChargeflag(String chargeflag) {
		this.chargeflag = chargeflag;
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
	public String getAccountdate() {
		return accountdate;
	}
	public void setAccountdate(String accountdate) {
		this.accountdate = accountdate;
	}
	public InsurancePanJinChargeDetail() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
