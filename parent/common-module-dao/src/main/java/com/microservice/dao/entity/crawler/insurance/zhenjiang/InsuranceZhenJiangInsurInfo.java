package com.microservice.dao.entity.crawler.insurance.zhenjiang;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 *	镇江市参保信息
 * @author sln
 *
 */
@Entity
@Table(name = "insurance_zhenjiang_insurinfo",indexes = {@Index(name = "index_insurance_zhenjiang_insurinfo_taskid", columnList = "taskid")})
public class InsuranceZhenJiangInsurInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = 5273473947864101643L;
	private String taskid;
//	参保险种类型
	private String insurtype;
//	单位名称
	private String unitname;
//	缴费状态
	private String chargestate;
//	参保身份
	private String insuredidentity;
//	个人缴费基数
	private String chargebasenum;
//	单位缴费比例
	private String unitprop;
//	个人缴费比例
	private String indiprop;
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
	public String getUnitname() {
		return unitname;
	}
	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}
	public String getChargestate() {
		return chargestate;
	}
	public void setChargestate(String chargestate) {
		this.chargestate = chargestate;
	}
	public String getInsuredidentity() {
		return insuredidentity;
	}
	public void setInsuredidentity(String insuredidentity) {
		this.insuredidentity = insuredidentity;
	}
	public String getChargebasenum() {
		return chargebasenum;
	}
	public void setChargebasenum(String chargebasenum) {
		this.chargebasenum = chargebasenum;
	}
	public String getUnitprop() {
		return unitprop;
	}
	public void setUnitprop(String unitprop) {
		this.unitprop = unitprop;
	}
	public String getIndiprop() {
		return indiprop;
	}
	public void setIndiprop(String indiprop) {
		this.indiprop = indiprop;
	}
	public InsuranceZhenJiangInsurInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public InsuranceZhenJiangInsurInfo(String taskid, String insurtype, String unitname, String chargestate,
			String insuredidentity, String chargebasenum, String unitprop, String indiprop) {
		super();
		this.taskid = taskid;
		this.insurtype = insurtype;
		this.unitname = unitname;
		this.chargestate = chargestate;
		this.insuredidentity = insuredidentity;
		this.chargebasenum = chargebasenum;
		this.unitprop = unitprop;
		this.indiprop = indiprop;
	}
	
}