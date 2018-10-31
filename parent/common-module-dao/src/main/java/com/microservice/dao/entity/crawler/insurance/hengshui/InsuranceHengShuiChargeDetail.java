package com.microservice.dao.entity.crawler.insurance.hengshui;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 * 衡水市的社保缴费信息中没有生育缴费，故此表存储的是其他四种缴费信息
 * @author sln
 *
 */
@Entity
@Table(name = "insurance_hengshui_chargedetail",indexes = {@Index(name = "index_insurance_hengshui_chargedetail_taskid", columnList = "taskid")})
public class InsuranceHengShuiChargeDetail extends IdEntity implements Serializable{
	private static final long serialVersionUID = 5273473947864101643L;
	private String taskid;
//	险种类型
	private String insurtype;
//	费款年月
	private String chargeyearmonth;
//	缴费类型(枚举类过多，决定去掉)
//	private String chargetype;
//	缴费基数
	private String chargebasenum;
//	应缴金额
	private String shouldcharge;
//	单位缴费金额
	private String unitcharge;
//	个人缴费金额
	private String percharge;
//	划账户金额
	private String delimitaccount;
//	缴费标志
	private String chargeflag;
//	缴费月数
	private String chargemonths;
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
	public String getShouldcharge() {
		return shouldcharge;
	}
	public void setShouldcharge(String shouldcharge) {
		this.shouldcharge = shouldcharge;
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
	public String getDelimitaccount() {
		return delimitaccount;
	}
	public void setDelimitaccount(String delimitaccount) {
		this.delimitaccount = delimitaccount;
	}
	public String getChargeflag() {
		return chargeflag;
	}
	public void setChargeflag(String chargeflag) {
		this.chargeflag = chargeflag;
	}
	public String getChargemonths() {
		return chargemonths;
	}
	public void setChargemonths(String chargemonths) {
		this.chargemonths = chargemonths;
	}
	public InsuranceHengShuiChargeDetail() {
		super();
		// TODO Auto-generated constructor stub
	}
}
