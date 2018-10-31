package com.microservice.dao.entity.crawler.insurance.taian;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 * 泰安市所有的缴费信息
 * @author sln
 *
 */
@Entity
@Table(name = "insurance_taian_chargedetail",indexes = {@Index(name = "index_insurance_taian_chargedetail_taskid", columnList = "taskid")})
public class InsuranceTaiAnChargeDetail extends IdEntity implements Serializable{
	private static final long serialVersionUID = 5273473947864101643L;
	private String taskid;
//	缴费年月
	private String chargeyearmonth;
//	缴费基数
	private String chargebasenum;
//	险种标志
	private String insurtype;
//	单位缴费基数
	private String unitchargebasenum;
//	单位缴费额
	private String unitcharge;
//	个人缴费基数
	private String perchargebasenum;
//	个人缴费额
	private String percharge;
//	单位名称
	private String unitname;
	
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

	public String getChargebasenum() {
		return chargebasenum;
	}

	public void setChargebasenum(String chargebasenum) {
		this.chargebasenum = chargebasenum;
	}

	public String getInsurtype() {
		return insurtype;
	}

	public void setInsurtype(String insurtype) {
		this.insurtype = insurtype;
	}

	public String getUnitchargebasenum() {
		return unitchargebasenum;
	}

	public void setUnitchargebasenum(String unitchargebasenum) {
		this.unitchargebasenum = unitchargebasenum;
	}

	public String getUnitcharge() {
		return unitcharge;
	}

	public void setUnitcharge(String unitcharge) {
		this.unitcharge = unitcharge;
	}

	public String getPerchargebasenum() {
		return perchargebasenum;
	}

	public void setPerchargebasenum(String perchargebasenum) {
		this.perchargebasenum = perchargebasenum;
	}

	public String getPercharge() {
		return percharge;
	}

	public void setPercharge(String percharge) {
		this.percharge = percharge;
	}

	public String getUnitname() {
		return unitname;
	}

	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}

	public InsuranceTaiAnChargeDetail() {
		super();
		// TODO Auto-generated constructor stub
	}
}
