package com.microservice.dao.entity.crawler.housing.lishui;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name="housing_lishui_userinfo",indexes = {@Index(name = "index_housing_lishui_userinfo_taskid", columnList = "taskid")})
public class HousingLiShuiUserInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = -5901921334712806791L;
	private String taskid;
//	姓名
	private String name;
//	公积金账号
	private String accnum;
//	身份证号码
	private String idnum;
//	缴存单位
	private String chargeunit;
//	账户余额
	private String accbalance;
//	账号状态
	private String accstatus;
//	本年度利息
	private String yearinterest;
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
	public String getAccnum() {
		return accnum;
	}
	public void setAccnum(String accnum) {
		this.accnum = accnum;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getChargeunit() {
		return chargeunit;
	}
	public void setChargeunit(String chargeunit) {
		this.chargeunit = chargeunit;
	}
	public String getAccbalance() {
		return accbalance;
	}
	public void setAccbalance(String accbalance) {
		this.accbalance = accbalance;
	}
	public String getAccstatus() {
		return accstatus;
	}
	public void setAccstatus(String accstatus) {
		this.accstatus = accstatus;
	}
	public String getYearinterest() {
		return yearinterest;
	}
	public void setYearinterest(String yearinterest) {
		this.yearinterest = yearinterest;
	}
	public HousingLiShuiUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public HousingLiShuiUserInfo(String taskid, String name, String accnum, String idnum, String chargeunit,
			String accbalance, String accstatus, String yearinterest) {
		super();
		this.taskid = taskid;
		this.name = name;
		this.accnum = accnum;
		this.idnum = idnum;
		this.chargeunit = chargeunit;
		this.accbalance = accbalance;
		this.accstatus = accstatus;
		this.yearinterest = yearinterest;
	}
}
