package com.microservice.dao.entity.crawler.housing.hulunbeier;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "housing_hulunbeier_pay",indexes = {@Index(name = "index_housing_hulunbeier_pay_taskid", columnList = "taskid")})
public class HousingHuLunBeiErPay extends IdEntity implements Serializable{

	private String taskid;

	private String accnum;//个人帐号

	private String amt;//发生额

	private String ywtype;//业务类型

	private String unitaccname;//公司名称

	private String trandate;//日期

	private String unitaccnum;//单位帐号

	private String accname;//姓名

	private String bal;//余额

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getAccnum() {
		return accnum;
	}

	public void setAccnum(String accnum) {
		this.accnum = accnum;
	}

	public String getAmt() {
		return amt;
	}

	public void setAmt(String amt) {
		this.amt = amt;
	}

	public String getYwtype() {
		return ywtype;
	}

	public void setYwtype(String ywtype) {
		this.ywtype = ywtype;
	}

	public String getUnitaccname() {
		return unitaccname;
	}

	public void setUnitaccname(String unitaccname) {
		this.unitaccname = unitaccname;
	}

	public String getTrandate() {
		return trandate;
	}

	public void setTrandate(String trandate) {
		this.trandate = trandate;
	}

	public String getUnitaccnum() {
		return unitaccnum;
	}

	public void setUnitaccnum(String unitaccnum) {
		this.unitaccnum = unitaccnum;
	}

	public String getAccname() {
		return accname;
	}

	public void setAccname(String accname) {
		this.accname = accname;
	}

	public String getBal() {
		return bal;
	}

	public void setBal(String bal) {
		this.bal = bal;
	}

	public HousingHuLunBeiErPay(String taskid, String accnum, String amt, String ywtype, String unitaccname,
			String trandate, String unitaccnum, String accname, String bal) {
		super();
		this.taskid = taskid;
		this.accnum = accnum;
		this.amt = amt;
		this.ywtype = ywtype;
		this.unitaccname = unitaccname;
		this.trandate = trandate;
		this.unitaccnum = unitaccnum;
		this.accname = accname;
		this.bal = bal;
	}

	public HousingHuLunBeiErPay() {
		super();
	}

	@Override
	public String toString() {
		return "HousingHuLunBeiErPay [taskid=" + taskid + ", accnum=" + accnum + ", amt=" + amt + ", ywtype=" + ywtype
				+ ", unitaccname=" + unitaccname + ", trandate=" + trandate + ", unitaccnum=" + unitaccnum
				+ ", accname=" + accname + ", bal=" + bal + "]";
	}

	
}
