package com.microservice.dao.entity.crawler.housing.eerduosi;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 * 由于姓名、身份证号、个人账号已经在用户信息中进行了爬取，故该实体中不再爬取
 * @author sln
 *
 */
@Entity
@Table(name="housing_eerduosi_flowinfo",indexes = {@Index(name = "index_housing_eerduosi_flowinfo_taskid", columnList = "taskid")})
public class HousingEErDuoSiFlowInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = 351229914049113222L;
	private String taskid;
//	管理部
	private String managedept;
//	单位名称
	private  String unitname;
//	单位账号
	private String unitaccnum;
//	业务类型
	private String businesstype;
//	摘要
	private String summary;
//	发生日期
	private String date;
//	发生金额
	private String amount;
//	发生利息
	private String interest;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getManagedept() {
		return managedept;
	}
	public void setManagedept(String managedept) {
		this.managedept = managedept;
	}
	public String getUnitname() {
		return unitname;
	}
	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}
	public String getUnitaccnum() {
		return unitaccnum;
	}
	public void setUnitaccnum(String unitaccnum) {
		this.unitaccnum = unitaccnum;
	}
	public String getBusinesstype() {
		return businesstype;
	}
	public void setBusinesstype(String businesstype) {
		this.businesstype = businesstype;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getInterest() {
		return interest;
	}
	public void setInterest(String interest) {
		this.interest = interest;
	}
	public HousingEErDuoSiFlowInfo() {
		super();
	}
	public HousingEErDuoSiFlowInfo(String taskid, String managedept, String unitname, String unitaccnum,
			String businesstype, String summary, String date, String amount, String interest) {
		super();
		this.taskid = taskid;
		this.managedept = managedept;
		this.unitname = unitname;
		this.unitaccnum = unitaccnum;
		this.businesstype = businesstype;
		this.summary = summary;
		this.date = date;
		this.amount = amount;
		this.interest = interest;
	}
	
}
