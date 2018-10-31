package com.microservice.dao.entity.crawler.housing.huzhou;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
//由于个人账号和个人姓名在个人基本信息中已经爬取完成，故此处不再重复爬取
@Entity
@Table(name="housing_huzhou_detailaccount",indexes = {@Index(name = "index_housing_huzhou_detailaccount_taskid", columnList = "taskid")})
public class HousingHuZhouDetailAccount extends IdEntity implements Serializable {
	private static final long serialVersionUID = 2044745879268491789L;
	private String taskid;
//	账户类型
	private String acctype;
//	单位账户账号
	private String unitaccnum;
//	借贷标志
	private String borrowingmarks;
//	发生额
	private String amount;
//	账户余额
	private String accbalance;
//	缴费年月（根据返回值发现的信息，故此处也加上）
	private String chargedate;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getAcctype() {
		return acctype;
	}
	public void setAcctype(String acctype) {
		this.acctype = acctype;
	}
	public String getUnitaccnum() {
		return unitaccnum;
	}
	public void setUnitaccnum(String unitaccnum) {
		this.unitaccnum = unitaccnum;
	}
	public String getBorrowingmarks() {
		return borrowingmarks;
	}
	public void setBorrowingmarks(String borrowingmarks) {
		this.borrowingmarks = borrowingmarks;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getAccbalance() {
		return accbalance;
	}
	public void setAccbalance(String accbalance) {
		this.accbalance = accbalance;
	}
	
	public String getChargedate() {
		return chargedate;
	}
	public void setChargedate(String chargedate) {
		this.chargedate = chargedate;
	}
	public HousingHuZhouDetailAccount() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
