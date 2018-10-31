package com.microservice.dao.entity.crawler.housing.hulunbeier;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "housing_hulunbeier_userinfo",indexes = {@Index(name = "index_housing_hulunbeier_userinfo_taskid", columnList = "taskid")})
public class HousingHuLunBeiErUserInfo extends IdEntity implements Serializable {

	private String taskid;
	
	private String lasttransdate;//最后交易日期
	
	private String certinum;//身份证号
	
	private String opnaccdate;//开户日期
	
	private String unitpayamt;//单位缴存额
	 
	private String accnum;//个人公积金帐号
	
	private String accname1;//账户状态
	
	private String unitprop;//单位存缴比例
	
	private String unitaccname;//单位名称
	
	private String unitaccnum;//单位帐号
	
	private String indipayamt;//个人存缴额
	
	private String accname;//姓名
	
	private String indiprop;//个人存缴比例
	
	private String indipaysum;//月缴存额
	
	private String bal;//缴存余额

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getLasttransdate() {
		return lasttransdate;
	}

	public void setLasttransdate(String lasttransdate) {
		this.lasttransdate = lasttransdate;
	}

	public String getCertinum() {
		return certinum;
	}

	public void setCertinum(String certinum) {
		this.certinum = certinum;
	}

	public String getOpnaccdate() {
		return opnaccdate;
	}

	public void setOpnaccdate(String opnaccdate) {
		this.opnaccdate = opnaccdate;
	}

	public String getUnitpayamt() {
		return unitpayamt;
	}

	public void setUnitpayamt(String unitpayamt) {
		this.unitpayamt = unitpayamt;
	}

	public String getAccnum() {
		return accnum;
	}

	public void setAccnum(String accnum) {
		this.accnum = accnum;
	}

	public String getAccname1() {
		return accname1;
	}

	public void setAccname1(String accname1) {
		this.accname1 = accname1;
	}

	public String getUnitprop() {
		return unitprop;
	}

	public void setUnitprop(String unitprop) {
		this.unitprop = unitprop;
	}

	public String getUnitaccname() {
		return unitaccname;
	}

	public void setUnitaccname(String unitaccname) {
		this.unitaccname = unitaccname;
	}

	public String getUnitaccnum() {
		return unitaccnum;
	}

	public void setUnitaccnum(String unitaccnum) {
		this.unitaccnum = unitaccnum;
	}

	public String getIndipayamt() {
		return indipayamt;
	}

	public void setIndipayamt(String indipayamt) {
		this.indipayamt = indipayamt;
	}

	public String getAccname() {
		return accname;
	}

	public void setAccname(String accname) {
		this.accname = accname;
	}

	public String getIndiprop() {
		return indiprop;
	}

	public void setIndiprop(String indiprop) {
		this.indiprop = indiprop;
	}

	public String getIndipaysum() {
		return indipaysum;
	}

	public void setIndipaysum(String indipaysum) {
		this.indipaysum = indipaysum;
	}

	public String getBal() {
		return bal;
	}

	public void setBal(String bal) {
		this.bal = bal;
	}

	public HousingHuLunBeiErUserInfo(String taskid, String lasttransdate, String certinum, String opnaccdate,
			String unitpayamt, String accnum, String accname1, String unitprop, String unitaccname, String unitaccnum,
			String indipayamt, String accname, String indiprop, String indipaysum, String bal) {
		super();
		this.taskid = taskid;
		this.lasttransdate = lasttransdate;
		this.certinum = certinum;
		this.opnaccdate = opnaccdate;
		this.unitpayamt = unitpayamt;
		this.accnum = accnum;
		this.accname1 = accname1;
		this.unitprop = unitprop;
		this.unitaccname = unitaccname;
		this.unitaccnum = unitaccnum;
		this.indipayamt = indipayamt;
		this.accname = accname;
		this.indiprop = indiprop;
		this.indipaysum = indipaysum;
		this.bal = bal;
	}

	public HousingHuLunBeiErUserInfo() {
		super();
	}

	@Override
	public String toString() {
		return "HousingHuLunBeiErUserInfo [taskid=" + taskid + ", lasttransdate=" + lasttransdate + ", certinum="
				+ certinum + ", opnaccdate=" + opnaccdate + ", unitpayamt=" + unitpayamt + ", accnum=" + accnum
				+ ", accname1=" + accname1 + ", unitprop=" + unitprop + ", unitaccname=" + unitaccname + ", unitaccnum="
				+ unitaccnum + ", indipayamt=" + indipayamt + ", accname=" + accname + ", indiprop=" + indiprop
				+ ", indipaysum=" + indipaysum + ", bal=" + bal + "]";
	}
	
	
}
