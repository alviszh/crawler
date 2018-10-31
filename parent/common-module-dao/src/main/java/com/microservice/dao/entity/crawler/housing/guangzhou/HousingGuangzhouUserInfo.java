package com.microservice.dao.entity.crawler.housing.guangzhou;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 广州公积金用户信息
 * @author: sln 
 * @date: 2017年9月29日 上午9:59:52 
 */
@Entity
@Table(name="housing_guangzhou_userinfo",indexes = {@Index(name = "index_housing_guangzhou_userinfo_taskid", columnList = "taskid")})
public class HousingGuangzhouUserInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = -8904475878472240469L;
	private String taskid;
//	姓名
	private String name;
//	证件类型
	private String credentialstype;
//	个人编号
	private String personalnum;
//	单位登记号
	private String compnum;
//	单位缴存比例（%）
	private String compchargescale;
//	当前余额
	private String thisyearbalance;
//	当年缴存金额（元）
	private String thisyearcharge;
//	上年结转余额（元）
	private String lastyearbalance;
//	公积金账号
	private String housingaccountnum;
//	证件号
	private String idnum;
//	最后业务日期
	private String lastbusinessdate;
//	单位名称
	private String compname;
//	个人缴存比例（%）
	private String perchargescale;
//	账号状态
	private String accountnumstatus;
//	当年提取金额（元）
	private String thisyeardrawmoney;
//	缴存基数（元）
	private String chargebasenum;
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
	public String getCredentialstype() {
		return credentialstype;
	}
	public void setCredentialstype(String credentialstype) {
		this.credentialstype = credentialstype;
	}
	public String getPersonalnum() {
		return personalnum;
	}
	public void setPersonalnum(String personalnum) {
		this.personalnum = personalnum;
	}
	public String getCompnum() {
		return compnum;
	}
	public void setCompnum(String compnum) {
		this.compnum = compnum;
	}
	public String getCompchargescale() {
		return compchargescale;
	}
	public void setCompchargescale(String compchargescale) {
		this.compchargescale = compchargescale;
	}
	public String getThisyearbalance() {
		return thisyearbalance;
	}
	public void setThisyearbalance(String thisyearbalance) {
		this.thisyearbalance = thisyearbalance;
	}
	public String getThisyearcharge() {
		return thisyearcharge;
	}
	public void setThisyearcharge(String thisyearcharge) {
		this.thisyearcharge = thisyearcharge;
	}
	public String getLastyearbalance() {
		return lastyearbalance;
	}
	public void setLastyearbalance(String lastyearbalance) {
		this.lastyearbalance = lastyearbalance;
	}
	public String getHousingaccountnum() {
		return housingaccountnum;
	}
	public void setHousingaccountnum(String housingaccountnum) {
		this.housingaccountnum = housingaccountnum;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getLastbusinessdate() {
		return lastbusinessdate;
	}
	public void setLastbusinessdate(String lastbusinessdate) {
		this.lastbusinessdate = lastbusinessdate;
	}
	public String getCompname() {
		return compname;
	}
	public void setCompname(String compname) {
		this.compname = compname;
	}
	public String getPerchargescale() {
		return perchargescale;
	}
	public void setPerchargescale(String perchargescale) {
		this.perchargescale = perchargescale;
	}
	public String getAccountnumstatus() {
		return accountnumstatus;
	}
	public void setAccountnumstatus(String accountnumstatus) {
		this.accountnumstatus = accountnumstatus;
	}
	public String getThisyeardrawmoney() {
		return thisyeardrawmoney;
	}
	public void setThisyeardrawmoney(String thisyeardrawmoney) {
		this.thisyeardrawmoney = thisyeardrawmoney;
	}
	public String getChargebasenum() {
		return chargebasenum;
	}
	public void setChargebasenum(String chargebasenum) {
		this.chargebasenum = chargebasenum;
	}
	public HousingGuangzhouUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public HousingGuangzhouUserInfo(String taskid, String name, String credentialstype, String personalnum,
			String compnum, String compchargescale, String thisyearbalance, String thisyearcharge,
			String lastyearbalance, String housingaccountnum, String idnum, String lastbusinessdate, String compname,
			String perchargescale, String accountnumstatus, String thisyeardrawmoney, String chargebasenum) {
		super();
		this.taskid = taskid;
		this.name = name;
		this.credentialstype = credentialstype;
		this.personalnum = personalnum;
		this.compnum = compnum;
		this.compchargescale = compchargescale;
		this.thisyearbalance = thisyearbalance;
		this.thisyearcharge = thisyearcharge;
		this.lastyearbalance = lastyearbalance;
		this.housingaccountnum = housingaccountnum;
		this.idnum = idnum;
		this.lastbusinessdate = lastbusinessdate;
		this.compname = compname;
		this.perchargescale = perchargescale;
		this.accountnumstatus = accountnumstatus;
		this.thisyeardrawmoney = thisyeardrawmoney;
		this.chargebasenum = chargebasenum;
	}
}
