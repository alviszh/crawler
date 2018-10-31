package com.microservice.dao.entity.crawler.insurance.linyi;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 * 所有类型社保的缴费明细
 * @author sln
 *
 */
@Entity
@Table(name = "insurance_linyi_chargedetail",indexes = {@Index(name = "index_insurance_linyi_chargedetail_taskid", columnList = "taskid")})
public class InsuranceLinYiChargeDetail extends IdEntity implements Serializable{
	private static final long serialVersionUID = 5273473947864101643L;
	private String taskid;
//	缴费年月
	private String chargeyearmonth;
//	险种信息
	private String insurtype;
//	缴费类型
	private String chargetype;
//	缴费标志
	private String chargeflag;
//	缴费基数
	private String chargebasenum;
//	单位缴费金额
	private String unitcharge;
//	个人缴费金额
	private String percharge;
	
	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getChargeyearmonth() {
		return chargeyearmonth;
	}

	public void setChargeyearmonth(String chargeyearmonth) {
		this.chargeyearmonth = chargeyearmonth;
	}

	public String getInsurtype() {
		return insurtype;
	}

	public void setInsurtype(String insurtype) {
		this.insurtype = insurtype;
	}

	public String getChargetype() {
		return chargetype;
	}

	public void setChargetype(String chargetype) {
		this.chargetype = chargetype;
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

	public String getUnitcharge() {
		return unitcharge;
	}

	public void setUnitcharge(String unitcharge) {
		this.unitcharge = unitcharge;
	}

	public String getPercharge() {
		return percharge;
	}

	public void setPercharge(String percharge) {
		this.percharge = percharge;
	}

	public InsuranceLinYiChargeDetail() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
