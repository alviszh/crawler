package com.microservice.dao.entity.crawler.insurance.anqing;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

/**
 * 安庆社保参保情况
 * @author zcx
 *
 */
@Entity
@Table(name="insurance_anqing_info")
public class InsuranceAnQingInfo extends IdEntity {

	private String type; // 险种类型
	private String companynum; // 单位
	private String companyname; // 单位名称
	private String insuredtype; // 比例类型
	private String insuredway; // 人员参保关系
	private String paybase; // 缴费基数
	private String insuredstate;//个人参保状态
	private String insureddate;//个人参保日期
	private String taskid;

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCompanynum() {
		return companynum;
	}
	public void setCompanynum(String companynum) {
		this.companynum = companynum;
	}
	public String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
	public String getInsuredtype() {
		return insuredtype;
	}
	public void setInsuredtype(String insuredtype) {
		this.insuredtype = insuredtype;
	}
	public String getInsuredway() {
		return insuredway;
	}
	public void setInsuredway(String insuredway) {
		this.insuredway = insuredway;
	}
	public String getPaybase() {
		return paybase;
	}
	public void setPaybase(String paybase) {
		this.paybase = paybase;
	}
	public String getInsuredstate() {
		return insuredstate;
	}
	public void setInsuredstate(String insuredstate) {
		this.insuredstate = insuredstate;
	}
	public String getInsureddate() {
		return insureddate;
	}
	public void setInsureddate(String insureddate) {
		this.insureddate = insureddate;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Override
	public String toString() {
		return "InsuranceAnQingInfo [type=" + type + ", companynum=" + companynum + ", companyname=" + companyname
				+ ", insuredtype=" + insuredtype + ", insuredway=" + insuredway + ", paybase=" + paybase
				+ ", insuredstate=" + insuredstate + ", insureddate=" + insureddate + ", taskid=" + taskid + "]";
	}
	public InsuranceAnQingInfo(String type, String companynum, String companyname, String insuredtype,
			String insuredway, String paybase, String insuredstate, String insureddate, String taskid) {
		super();
		this.type = type;
		this.companynum = companynum;
		this.companyname = companyname;
		this.insuredtype = insuredtype;
		this.insuredway = insuredway;
		this.paybase = paybase;
		this.insuredstate = insuredstate;
		this.insureddate = insureddate;
		this.taskid = taskid;
	}
	public InsuranceAnQingInfo() {
		super();
	}
}
