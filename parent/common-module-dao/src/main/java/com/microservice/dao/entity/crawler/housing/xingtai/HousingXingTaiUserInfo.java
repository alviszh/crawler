package com.microservice.dao.entity.crawler.housing.xingtai;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 邢台市公积金个人基本信息
 * @author: sln 
 * @date: 
 */
@Entity
@Table(name="housing_xingtai_userinfo",indexes = {@Index(name = "index_housing_xingtai_userinfo_taskid", columnList = "taskid")})
public class HousingXingTaiUserInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = -4523112843732781341L;
	private String taskid;
//	个人姓名
	private String accname;
//	个人账号
	private String accnum;
//	身份证号
	private String certinum;
//	开户日期
	private String opendate;
//	公积金余额
	private String balance;
//	单位账号
	private String unitaccnum;
//	单位名称
	private String unitaccname;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getAccname() {
		return accname;
	}
	public void setAccname(String accname) {
		this.accname = accname;
	}
	public String getAccnum() {
		return accnum;
	}
	public void setAccnum(String accnum) {
		this.accnum = accnum;
	}
	public String getCertinum() {
		return certinum;
	}
	public void setCertinum(String certinum) {
		this.certinum = certinum;
	}
	public String getOpendate() {
		return opendate;
	}
	public void setOpendate(String opendate) {
		this.opendate = opendate;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getUnitaccnum() {
		return unitaccnum;
	}
	public void setUnitaccnum(String unitaccnum) {
		this.unitaccnum = unitaccnum;
	}
	public String getUnitaccname() {
		return unitaccname;
	}
	public void setUnitaccname(String unitaccname) {
		this.unitaccname = unitaccname;
	}
	public HousingXingTaiUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
}
