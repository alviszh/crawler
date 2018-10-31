package com.microservice.dao.entity.crawler.insurance.zhenjiang;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 * 镇江所有的缴费信息
 * @author sln
 *
 */
@Entity
@Table(name = "insurance_zhenjiang_chargedetail",indexes = {@Index(name = "index_insurance_zhenjiang_chargedetail_taskid", columnList = "taskid")})
public class InsuranceZhenJiangChargeDetail extends IdEntity implements Serializable{
	private static final long serialVersionUID = 5273473947864101643L;
	private String taskid;
//	单位名称
	private String unitname;
//	所属期
	private String belongdate;
//	险种类型
	private String insurtype;
//	人员缴费基数
	private String chargebasenum;
//	单位应缴金额
	private String unitcharge;
//	个人应缴金额
	private String percharge;
//	是否到账
	private String isreachacc;
//	到账年月
	private String reachdate;
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
	public String getBelongdate() {
		return belongdate;
	}
	public void setBelongdate(String belongdate) {
		this.belongdate = belongdate;
	}
	public String getInsurtype() {
		return insurtype;
	}
	public void setInsurtype(String insurtype) {
		this.insurtype = insurtype;
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
	public String getIsreachacc() {
		return isreachacc;
	}
	public void setIsreachacc(String isreachacc) {
		this.isreachacc = isreachacc;
	}
	public String getReachdate() {
		return reachdate;
	}
	public void setReachdate(String reachdate) {
		this.reachdate = reachdate;
	}
	public InsuranceZhenJiangChargeDetail() {
		super();
		// TODO Auto-generated constructor stub
	}
}