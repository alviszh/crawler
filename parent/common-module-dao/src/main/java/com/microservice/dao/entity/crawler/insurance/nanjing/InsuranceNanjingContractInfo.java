package com.microservice.dao.entity.crawler.insurance.nanjing;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 合同备案信息
 * @author: sln 
 * @date: 2017年9月28日 上午9:19:40
 */
@Entity
@Table(name = "insurance_nanjing_contractinfo",indexes = {@Index(name = "index_insurance_nanjing_contractinfo_taskid", columnList = "taskid")})
public class InsuranceNanjingContractInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = -6613155467475202619L;
	private String taskid;
//	单位名称
	private String compname;
//	合同类别
	private String contractype;
//	合同开始期
	private String contractstart;
//	合同截止期
	private String contractend;
//	期限类型
	private String termtype;
//	日工作时间
	private String dayworktime;
//	备案机构
	private String filingmechanism;
//	终解日期
	private String finalsolutiondate;
//	终解原因
	private String finalsolutionreasion;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getCompname() {
		return compname;
	}
	public void setCompname(String compname) {
		this.compname = compname;
	}
	public String getContractype() {
		return contractype;
	}
	public void setContractype(String contractype) {
		this.contractype = contractype;
	}
	public String getContractstart() {
		return contractstart;
	}
	public void setContractstart(String contractstart) {
		this.contractstart = contractstart;
	}
	public String getContractend() {
		return contractend;
	}
	public void setContractend(String contractend) {
		this.contractend = contractend;
	}
	public String getTermtype() {
		return termtype;
	}
	public void setTermtype(String termtype) {
		this.termtype = termtype;
	}
	public String getDayworktime() {
		return dayworktime;
	}
	public void setDayworktime(String dayworktime) {
		this.dayworktime = dayworktime;
	}
	public String getFilingmechanism() {
		return filingmechanism;
	}
	public void setFilingmechanism(String filingmechanism) {
		this.filingmechanism = filingmechanism;
	}
	public String getFinalsolutiondate() {
		return finalsolutiondate;
	}
	public void setFinalsolutiondate(String finalsolutiondate) {
		this.finalsolutiondate = finalsolutiondate;
	}
	public String getFinalsolutionreasion() {
		return finalsolutionreasion;
	}
	public void setFinalsolutionreasion(String finalsolutionreasion) {
		this.finalsolutionreasion = finalsolutionreasion;
	}
	public InsuranceNanjingContractInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public InsuranceNanjingContractInfo(String taskid, String compname, String contractype, String contractstart,
			String contractend, String termtype, String dayworktime, String filingmechanism, String finalsolutiondate,
			String finalsolutionreasion) {
		super();
		this.taskid = taskid;
		this.compname = compname;
		this.contractype = contractype;
		this.contractstart = contractstart;
		this.contractend = contractend;
		this.termtype = termtype;
		this.dayworktime = dayworktime;
		this.filingmechanism = filingmechanism;
		this.finalsolutiondate = finalsolutiondate;
		this.finalsolutionreasion = finalsolutionreasion;
	}
	
}
