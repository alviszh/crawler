package com.microservice.dao.entity.crawler.housing.qingdao;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 个人信息
 * @author: sln 
 * @date: 2017年10月25日 上午11:42:39 
 */
@Entity
@Table(name="housing_qingdao_userinfo",indexes = {@Index(name = "index_housing_qingdao_userinfo_taskid", columnList = "taskid")})
public class HousingQingDaoUserInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = -4523112843732781341L;
	private String taskid;
//	职工编号
	private String staffnum;
//	姓名
	private String name;
//	账户状态
	private String accountstate;
//	身份证
	private String idnum;
//	手机号码
	private String phonenum;
//	开户日期
	private String opendate;
//	月工资额
	private String monthwage;
//	单位比例(%)
	private String compscale;
//	个人比例(%)
	private String personalscale;
//	个人月汇缴额
	private String personalcharge;
//	单位月汇缴额
	private String compcharge;
//	账户余额
	private String accountbalance;
//	联名卡号
	private String jointnumber;
//	发卡行
	private String issuingbank;
//	登记日期
	private String registdate;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getStaffnum() {
		return staffnum;
	}
	public void setStaffnum(String staffnum) {
		this.staffnum = staffnum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAccountstate() {
		return accountstate;
	}
	public void setAccountstate(String accountstate) {
		this.accountstate = accountstate;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getPhonenum() {
		return phonenum;
	}
	public void setPhonenum(String phonenum) {
		this.phonenum = phonenum;
	}
	public String getOpendate() {
		return opendate;
	}
	public void setOpendate(String opendate) {
		this.opendate = opendate;
	}
	public String getMonthwage() {
		return monthwage;
	}
	public void setMonthwage(String monthwage) {
		this.monthwage = monthwage;
	}
	public String getCompscale() {
		return compscale;
	}
	public void setCompscale(String compscale) {
		this.compscale = compscale;
	}
	public String getPersonalscale() {
		return personalscale;
	}
	public void setPersonalscale(String personalscale) {
		this.personalscale = personalscale;
	}
	public String getPersonalcharge() {
		return personalcharge;
	}
	public void setPersonalcharge(String personalcharge) {
		this.personalcharge = personalcharge;
	}
	public String getCompcharge() {
		return compcharge;
	}
	public void setCompcharge(String compcharge) {
		this.compcharge = compcharge;
	}
	public String getAccountbalance() {
		return accountbalance;
	}
	public void setAccountbalance(String accountbalance) {
		this.accountbalance = accountbalance;
	}
	public String getJointnumber() {
		return jointnumber;
	}
	public void setJointnumber(String jointnumber) {
		this.jointnumber = jointnumber;
	}
	public String getIssuingbank() {
		return issuingbank;
	}
	public void setIssuingbank(String issuingbank) {
		this.issuingbank = issuingbank;
	}
	public String getRegistdate() {
		return registdate;
	}
	public void setRegistdate(String registdate) {
		this.registdate = registdate;
	}
	public HousingQingDaoUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public HousingQingDaoUserInfo(String taskid, String staffnum, String name, String accountstate, String idnum,
			String phonenum, String opendate, String monthwage, String compscale, String personalscale,
			String personalcharge, String compcharge, String accountbalance, String jointnumber, String issuingbank,
			String registdate) {
		super();
		this.taskid = taskid;
		this.staffnum = staffnum;
		this.name = name;
		this.accountstate = accountstate;
		this.idnum = idnum;
		this.phonenum = phonenum;
		this.opendate = opendate;
		this.monthwage = monthwage;
		this.compscale = compscale;
		this.personalscale = personalscale;
		this.personalcharge = personalcharge;
		this.compcharge = compcharge;
		this.accountbalance = accountbalance;
		this.jointnumber = jointnumber;
		this.issuingbank = issuingbank;
		this.registdate = registdate;
	}
}
