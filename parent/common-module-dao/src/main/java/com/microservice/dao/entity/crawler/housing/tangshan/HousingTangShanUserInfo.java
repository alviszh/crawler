package com.microservice.dao.entity.crawler.housing.tangshan;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "housing_tangshan_userinfo",indexes = {@Index(name = "index_housing_tangshan_userinfo_taskid", columnList = "taskid")})
public class HousingTangShanUserInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = 1L;

	private String taskid;
	
	private Integer userid;
	
	private String accname;
	
	private String	certinum;
	
	private String	accnum;
	
	private String	unitaccname;
	
	private String	grjcjs;
	
	private String	monpaysum;
	
	private String	unitprop;
	
	private String	indiprop;
	
	private String	dwyjce;
	
	private String	gryjce;
	
	private String	balance;
	
	private String lastpaydate;
	
	private String opendate;
	
	private String grzhzt;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getAccname() {
		return accname;
	}

	public void setAccname(String accname) {
		this.accname = accname;
	}

	public String getCertinum() {
		return certinum;
	}

	public void setCertinum(String certinum) {
		this.certinum = certinum;
	}

	public String getAccnum() {
		return accnum;
	}

	public void setAccnum(String accnum) {
		this.accnum = accnum;
	}

	public String getUnitaccname() {
		return unitaccname;
	}

	public void setUnitaccname(String unitaccname) {
		this.unitaccname = unitaccname;
	}

	public String getGrjcjs() {
		return grjcjs;
	}

	public void setGrjcjs(String grjcjs) {
		this.grjcjs = grjcjs;
	}

	public String getMonpaysum() {
		return monpaysum;
	}

	public void setMonpaysum(String monpaysum) {
		this.monpaysum = monpaysum;
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

	public String getDwyjce() {
		return dwyjce;
	}

	public void setDwyjce(String dwyjce) {
		this.dwyjce = dwyjce;
	}

	public String getGryjce() {
		return gryjce;
	}

	public void setGryjce(String gryjce) {
		this.gryjce = gryjce;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getLastpaydate() {
		return lastpaydate;
	}

	public void setLastpaydate(String lastpaydate) {
		this.lastpaydate = lastpaydate;
	}

	public String getOpendate() {
		return opendate;
	}

	public void setOpendate(String opendate) {
		this.opendate = opendate;
	}

	public String getGrzhzt() {
		return grzhzt;
	}

	public void setGrzhzt(String grzhzt) {
		this.grzhzt = grzhzt;
	}

	public HousingTangShanUserInfo(String taskid, Integer userid, String accname, String certinum, String accnum,
			String unitaccname, String grjcjs, String monpaysum, String unitprop, String indiprop, String dwyjce,
			String gryjce, String balance, String lastpaydate, String opendate, String grzhzt) {
		super();
		this.taskid = taskid;
		this.userid = userid;
		this.accname = accname;
		this.certinum = certinum;
		this.accnum = accnum;
		this.unitaccname = unitaccname;
		this.grjcjs = grjcjs;
		this.monpaysum = monpaysum;
		this.unitprop = unitprop;
		this.indiprop = indiprop;
		this.dwyjce = dwyjce;
		this.gryjce = gryjce;
		this.balance = balance;
		this.lastpaydate = lastpaydate;
		this.opendate = opendate;
		this.grzhzt = grzhzt;
	}

	public HousingTangShanUserInfo() {
		super();
	}

	
}
