package com.microservice.dao.entity.crawler.insurance.sz.neimenggu;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 * 参保信息
 * @author sln
 *
 */
@Entity
@Table(name = "insurance_sz_neimenggu_insurinfo",indexes = {@Index(name = "index_insurance_sz_neimenggu_insurinfo_taskid", columnList = "taskid")})
public class InsuranceSZNeiMengGuInsurInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = 5463733733681486246L;
	private String taskid;
//	险种类型
	private String insurtype;
//	参保状态
	private String insurstatus;
//	首次参保日期
	private String firstinsurdate;
//	本次参保日期
	private String thisinsurdate;
//	参保单位
	private String insurunit;
//	参保地
	private String insuraddr;
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
	public String getInsurstatus() {
		return insurstatus;
	}
	public void setInsurstatus(String insurstatus) {
		this.insurstatus = insurstatus;
	}
	public String getFirstinsurdate() {
		return firstinsurdate;
	}
	public void setFirstinsurdate(String firstinsurdate) {
		this.firstinsurdate = firstinsurdate;
	}
	public String getThisinsurdate() {
		return thisinsurdate;
	}
	public void setThisinsurdate(String thisinsurdate) {
		this.thisinsurdate = thisinsurdate;
	}
	public String getInsurunit() {
		return insurunit;
	}
	public void setInsurunit(String insurunit) {
		this.insurunit = insurunit;
	}
	public String getInsuraddr() {
		return insuraddr;
	}
	public void setInsuraddr(String insuraddr) {
		this.insuraddr = insuraddr;
	}
	public InsuranceSZNeiMengGuInsurInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
}
