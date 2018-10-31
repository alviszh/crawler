package com.microservice.dao.entity.crawler.housing.qingdao;
/**
 * @description:
 * @author: sln 
 * @date: 2017年10月25日 下午1:38:38 
 */

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_qingdao_chargeinfo",indexes = {@Index(name = "index_housing_qingdao_chargeinfo_taskid", columnList = "taskid")})
public class HousingQingDaoChargeInfo extends IdEntity implements Serializable  {
	private static final long serialVersionUID = -877212474018580108L;
	private String taskid;
//	产生日期
	private String productiondate;
//	所属年月
	private String belongtoyearmonth;
//	单位金额
	private String compcharge;
//	个人金额
	private String personalcharge;
//	缴交单位
	private String paymentunit;
//	缴交原因
	private String paymentreason;
//	单据状态
	private String documentstatus;
//	结算方式
	private String accountmethod;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getProductiondate() {
		return productiondate;
	}
	public void setProductiondate(String productiondate) {
		this.productiondate = productiondate;
	}
	public String getBelongtoyearmonth() {
		return belongtoyearmonth;
	}
	public void setBelongtoyearmonth(String belongtoyearmonth) {
		this.belongtoyearmonth = belongtoyearmonth;
	}
	public String getCompcharge() {
		return compcharge;
	}
	public void setCompcharge(String compcharge) {
		this.compcharge = compcharge;
	}
	public String getPersonalcharge() {
		return personalcharge;
	}
	public void setPersonalcharge(String personalcharge) {
		this.personalcharge = personalcharge;
	}
	public String getPaymentunit() {
		return paymentunit;
	}
	public void setPaymentunit(String paymentunit) {
		this.paymentunit = paymentunit;
	}
	public String getDocumentstatus() {
		return documentstatus;
	}
	public void setDocumentstatus(String documentstatus) {
		this.documentstatus = documentstatus;
	}
	public String getAccountmethod() {
		return accountmethod;
	}
	public void setAccountmethod(String accountmethod) {
		this.accountmethod = accountmethod;
	}
	public HousingQingDaoChargeInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public String getPaymentreason() {
		return paymentreason;
	}
	public void setPaymentreason(String paymentreason) {
		this.paymentreason = paymentreason;
	}
	public HousingQingDaoChargeInfo(String taskid, String productiondate, String belongtoyearmonth, String compcharge,
			String personalcharge, String paymentunit, String paymentreason, String documentstatus,
			String accountmethod) {
		super();
		this.taskid = taskid;
		this.productiondate = productiondate;
		this.belongtoyearmonth = belongtoyearmonth;
		this.compcharge = compcharge;
		this.personalcharge = personalcharge;
		this.paymentunit = paymentunit;
		this.paymentreason = paymentreason;
		this.documentstatus = documentstatus;
		this.accountmethod = accountmethod;
	}
	
}
