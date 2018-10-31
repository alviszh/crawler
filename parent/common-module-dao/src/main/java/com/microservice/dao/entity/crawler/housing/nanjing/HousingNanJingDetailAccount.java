package com.microservice.dao.entity.crawler.housing.nanjing;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 个人明细
 * @author: sln 
 * @date: 2017年10月26日 下午2:41:41 
 */
@Entity
@Table(name="housing_nanjing_detailaccount",indexes = {@Index(name = "index_housing_nanjing_detailaccount_taskid", columnList = "taskid")})
public class HousingNanJingDetailAccount extends IdEntity implements Serializable {
	private static final long serialVersionUID = 2044745879268491789L;
	private String taskid;
//	行号
	private Integer rownum;
//	单位账号
	private String unitnum;
//	单位名称
	private String unitname;
//	个人账号
	private String personalaccountnum;
//	姓名
	private String name;
//	身份证号码
	private String idnum;
//	交易日期
	private String transdate;
//	业务种类
	private String businesstype;
//	发生额
	private String amount;
//	余额
	private String balance;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public Integer getRownum() {
		return rownum;
	}
	public void setRownum(Integer rownum) {
		this.rownum = rownum;
	}
	public String getUnitnum() {
		return unitnum;
	}
	public void setUnitnum(String unitnum) {
		this.unitnum = unitnum;
	}
	public String getUnitname() {
		return unitname;
	}
	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}
	public String getPersonalaccountnum() {
		return personalaccountnum;
	}
	public void setPersonalaccountnum(String personalaccountnum) {
		this.personalaccountnum = personalaccountnum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getTransdate() {
		return transdate;
	}
	public void setTransdate(String transdate) {
		this.transdate = transdate;
	}
	public String getBusinesstype() {
		return businesstype;
	}
	public void setBusinesstype(String businesstype) {
		this.businesstype = businesstype;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public HousingNanJingDetailAccount() {
		super();
		// TODO Auto-generated constructor stub
	}
	public HousingNanJingDetailAccount(String taskid, Integer rownum, String unitnum, String unitname,
			String personalaccountnum, String name, String idnum, String transdate, String businesstype, String amount,
			String balance) {
		super();
		this.taskid = taskid;
		this.rownum = rownum;
		this.unitnum = unitnum;
		this.unitname = unitname;
		this.personalaccountnum = personalaccountnum;
		this.name = name;
		this.idnum = idnum;
		this.transdate = transdate;
		this.businesstype = businesstype;
		this.amount = amount;
		this.balance = balance;
	}
}
