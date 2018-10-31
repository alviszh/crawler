package com.microservice.dao.entity.crawler.housing.lishui;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_lishui_detailaccount",indexes = {@Index(name = "index_housing_lishui_detailaccount_taskid", columnList = "taskid")})
public class HousingLiShuiDetailAccount extends IdEntity implements Serializable {
	private static final long serialVersionUID = 2044745879268491789L;
	private String taskid;
//	缴存月份
	private String chargemonth;
//	缴存日期
	private String chargedate;
//	缴存额
	private String chargeamount;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getChargemonth() {
		return chargemonth;
	}
	public void setChargemonth(String chargemonth) {
		this.chargemonth = chargemonth;
	}
	public String getChargedate() {
		return chargedate;
	}
	public void setChargedate(String chargedate) {
		this.chargedate = chargedate;
	}
	public String getChargeamount() {
		return chargeamount;
	}
	public void setChargeamount(String chargeamount) {
		this.chargeamount = chargeamount;
	}
	public HousingLiShuiDetailAccount() {
		super();
		// TODO Auto-generated constructor stub
	}
	public HousingLiShuiDetailAccount(String taskid, String chargemonth, String chargedate, String chargeamount) {
		super();
		this.taskid = taskid;
		this.chargemonth = chargemonth;
		this.chargedate = chargedate;
		this.chargeamount = chargeamount;
	}
	
}
