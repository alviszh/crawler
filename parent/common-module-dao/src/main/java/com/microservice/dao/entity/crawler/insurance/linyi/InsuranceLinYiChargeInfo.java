package com.microservice.dao.entity.crawler.insurance.linyi;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 个人参保大体信息（参保险种等相关信息）
 * @author: sln 
 * @date: 2017年12月7日 下午6:09:41 
 */
@Entity
@Table(name = "insurance_linyi_chargeinfo",indexes = {@Index(name = "index_insurance_linyi_chargeinfo_taskid", columnList = "taskid")})
public class InsuranceLinYiChargeInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = -8625036411379792198L;
	private String taskid;
//	单位编号
	private String unitnum;
//	单位名称
	private String unitname;
//	个人编号
	private String personalnum;
//	险种类型
	private String insurtype;
//	参保状态
	private String insurstatus;
//	本单位参保日期
	private String thisunitinsurdate;
//	参保时间
	private String insurdate;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
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
	public String getPersonalnum() {
		return personalnum;
	}
	public void setPersonalnum(String personalnum) {
		this.personalnum = personalnum;
	}
	public String getInsurtype() {
		return insurtype;
	}
	public void setInsurtype(String insurtype) {
		this.insurtype = insurtype;
	}
	public String getInsurstatus() {
		return insurstatus;
	}
	public void setInsurstatus(String insurstatus) {
		this.insurstatus = insurstatus;
	}
	public String getThisunitinsurdate() {
		return thisunitinsurdate;
	}
	public void setThisunitinsurdate(String thisunitinsurdate) {
		this.thisunitinsurdate = thisunitinsurdate;
	}
	public String getInsurdate() {
		return insurdate;
	}
	public void setInsurdate(String insurdate) {
		this.insurdate = insurdate;
	}
	public InsuranceLinYiChargeInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
}
