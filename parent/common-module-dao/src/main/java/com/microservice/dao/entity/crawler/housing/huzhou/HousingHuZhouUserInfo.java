package com.microservice.dao.entity.crawler.housing.huzhou;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name="housing_huzhou_userinfo",indexes = {@Index(name = "index_housing_huzhou_userinfo_taskid", columnList = "taskid")})
public class HousingHuZhouUserInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = -5901921334712806791L;
	private String taskid;
//	姓名
	private String name;
//	个人账号
	private String peraccnum;
//	证件类型
	private String idtype;
//	证件号
	private String idnum;
//	开户日期
	private String opendate;
//	个人状态
	private String perstate;
//	单位账号
	private String unitaccnum;
//	单位名称
	private String unitname;
//	单位比例
	private String unitprop;
//	个人比例
	private String indiprop;
//	补贴比例
	private String subprop;
//	工资基数
	private String wagebase;
//	补贴基数
	private String subbase;
//	个人月缴
	private String indimonthcharge;
//	单位月缴
	private String unitmonthcharge;
//	补贴月缴
	private String submonthcharge;
//	合计月缴
	private String totalmonthcharge;
//	一般公积金余额
	private String generalbalance;
//	住房公积金补贴余额
	private String housingfundsubalance;
//	住房补贴余额
	private String housingsubalance;
//	合计余额
	private String totalbalance;
//	欠缴金额
	private String arrearsamount;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPeraccnum() {
		return peraccnum;
	}
	public void setPeraccnum(String peraccnum) {
		this.peraccnum = peraccnum;
	}
	public String getIdtype() {
		return idtype;
	}
	public void setIdtype(String idtype) {
		this.idtype = idtype;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getOpendate() {
		return opendate;
	}
	public void setOpendate(String opendate) {
		this.opendate = opendate;
	}
	public String getPerstate() {
		return perstate;
	}
	public void setPerstate(String perstate) {
		this.perstate = perstate;
	}
	public String getUnitaccnum() {
		return unitaccnum;
	}
	public void setUnitaccnum(String unitaccnum) {
		this.unitaccnum = unitaccnum;
	}
	public String getUnitname() {
		return unitname;
	}
	public void setUnitname(String unitname) {
		this.unitname = unitname;
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
	public String getSubprop() {
		return subprop;
	}
	public void setSubprop(String subprop) {
		this.subprop = subprop;
	}
	public String getWagebase() {
		return wagebase;
	}
	public void setWagebase(String wagebase) {
		this.wagebase = wagebase;
	}
	public String getSubbase() {
		return subbase;
	}
	public void setSubbase(String subbase) {
		this.subbase = subbase;
	}
	public String getIndimonthcharge() {
		return indimonthcharge;
	}
	public void setIndimonthcharge(String indimonthcharge) {
		this.indimonthcharge = indimonthcharge;
	}
	public String getUnitmonthcharge() {
		return unitmonthcharge;
	}
	public void setUnitmonthcharge(String unitmonthcharge) {
		this.unitmonthcharge = unitmonthcharge;
	}
	public String getSubmonthcharge() {
		return submonthcharge;
	}
	public void setSubmonthcharge(String submonthcharge) {
		this.submonthcharge = submonthcharge;
	}
	public String getTotalmonthcharge() {
		return totalmonthcharge;
	}
	public void setTotalmonthcharge(String totalmonthcharge) {
		this.totalmonthcharge = totalmonthcharge;
	}
	public String getGeneralbalance() {
		return generalbalance;
	}
	public void setGeneralbalance(String generalbalance) {
		this.generalbalance = generalbalance;
	}
	public String getHousingfundsubalance() {
		return housingfundsubalance;
	}
	public void setHousingfundsubalance(String housingfundsubalance) {
		this.housingfundsubalance = housingfundsubalance;
	}
	public String getHousingsubalance() {
		return housingsubalance;
	}
	public void setHousingsubalance(String housingsubalance) {
		this.housingsubalance = housingsubalance;
	}
	public String getTotalbalance() {
		return totalbalance;
	}
	public void setTotalbalance(String totalbalance) {
		this.totalbalance = totalbalance;
	}
	public String getArrearsamount() {
		return arrearsamount;
	}
	public void setArrearsamount(String arrearsamount) {
		this.arrearsamount = arrearsamount;
	}
	public HousingHuZhouUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public HousingHuZhouUserInfo(String taskid, String name, String peraccnum, String idtype, String idnum,
			String opendate, String perstate, String unitaccnum, String unitname, String unitprop, String indiprop,
			String subprop, String wagebase, String subbase, String indimonthcharge, String unitmonthcharge,
			String submonthcharge, String totalmonthcharge, String generalbalance, String housingfundsubalance,
			String housingsubalance, String totalbalance, String arrearsamount) {
		super();
		this.taskid = taskid;
		this.name = name;
		this.peraccnum = peraccnum;
		this.idtype = idtype;
		this.idnum = idnum;
		this.opendate = opendate;
		this.perstate = perstate;
		this.unitaccnum = unitaccnum;
		this.unitname = unitname;
		this.unitprop = unitprop;
		this.indiprop = indiprop;
		this.subprop = subprop;
		this.wagebase = wagebase;
		this.subbase = subbase;
		this.indimonthcharge = indimonthcharge;
		this.unitmonthcharge = unitmonthcharge;
		this.submonthcharge = submonthcharge;
		this.totalmonthcharge = totalmonthcharge;
		this.generalbalance = generalbalance;
		this.housingfundsubalance = housingfundsubalance;
		this.housingsubalance = housingsubalance;
		this.totalbalance = totalbalance;
		this.arrearsamount = arrearsamount;
	}
}
