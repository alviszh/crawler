package com.microservice.dao.entity.crawler.insurance.rizhao;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 *	日照所有的缴费信息
 * @author sln
 *
 */
@Entity
@Table(name = "insurance_rizhao_chargedetail",indexes = {@Index(name = "index_insurance_rizhao_chargedetail_taskid", columnList = "taskid")})
public class InsuranceRiZhaoChargeDetail extends IdEntity implements Serializable{
	private static final long serialVersionUID = 5273473947864101643L;
	private String taskid;
//	单位名称
	private String unitname;
//	结算期
	private String accountdate;
//	缴费基数
	private String chargebasenum;
//	个人缴纳金额
	private String percharge;
//	单位缴纳统筹金额
	private String unitoverallcharge;
//	缴费标志
	private String chargeflag;
//	单位缴纳个人账户金额
	private String unitcharge;
//	险种类型
	private String insurtype;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getUnitname() {
		return unitname;
	}
	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}
	public String getAccountdate() {
		return accountdate;
	}
	public void setAccountdate(String accountdate) {
		this.accountdate = accountdate;
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
	public String getUnitoverallcharge() {
		return unitoverallcharge;
	}
	public void setUnitoverallcharge(String unitoverallcharge) {
		this.unitoverallcharge = unitoverallcharge;
	}
	public String getChargeflag() {
		return chargeflag;
	}
	public void setChargeflag(String chargeflag) {
		this.chargeflag = chargeflag;
	}
	public String getUnitcharge() {
		return unitcharge;
	}
	public void setUnitcharge(String unitcharge) {
		this.unitcharge = unitcharge;
	}
	public String getInsurtype() {
		return insurtype;
	}
	public void setInsurtype(String insurtype) {
		this.insurtype = insurtype;
	}
	
}