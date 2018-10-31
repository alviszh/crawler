package com.microservice.dao.entity.crawler.insurance.qingdao;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @Description: 青岛医疗保险信息实体
 * @author sln
 * @date 2017年8月9日
 */
@Entity
@Table(name = "insurance_qingdao_medical",indexes = {@Index(name = "index_insurance_qingdao_medical_taskid", columnList = "taskid")})
public class InsuranceQingdaoMedical extends IdEntity implements Serializable {
	private static final long serialVersionUID = -9169590602959163696L;
	private String taskid;
	// 缴费年月
	private String paydate;
	// 应属年月
	private String belongdate;
	// 单位名称
	private String compname;
	// 缴费类别
	private String paytype;
	// 缴费基数
	private String paybasenum;
	// 个人缴费
	private String perpay;
	// 单位划入账户
	private String unitpay;
	// 大额救助
	private String largerelief;
	// 共划入账户
	private String accountsum;
	// 公务员补助
	private String subsidy;
	// 缴费标志
	private String paystatus;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getPaydate() {
		return paydate;
	}
	public void setPaydate(String paydate) {
		this.paydate = paydate;
	}
	public String getBelongdate() {
		return belongdate;
	}
	public void setBelongdate(String belongdate) {
		this.belongdate = belongdate;
	}
	public String getCompname() {
		return compname;
	}
	public void setCompname(String compname) {
		this.compname = compname;
	}
	public String getPaytype() {
		return paytype;
	}
	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}
	public String getPaybasenum() {
		return paybasenum;
	}
	public void setPaybasenum(String paybasenum) {
		this.paybasenum = paybasenum;
	}
	public String getPerpay() {
		return perpay;
	}
	public void setPerpay(String perpay) {
		this.perpay = perpay;
	}
	public String getUnitpay() {
		return unitpay;
	}
	public void setUnitpay(String unitpay) {
		this.unitpay = unitpay;
	}
	public String getLargerelief() {
		return largerelief;
	}
	public void setLargerelief(String largerelief) {
		this.largerelief = largerelief;
	}
	public String getAccountsum() {
		return accountsum;
	}
	public void setAccountsum(String accountsum) {
		this.accountsum = accountsum;
	}
	public String getSubsidy() {
		return subsidy;
	}
	public void setSubsidy(String subsidy) {
		this.subsidy = subsidy;
	}
	public String getPaystatus() {
		return paystatus;
	}
	public void setPaystatus(String paystatus) {
		this.paystatus = paystatus;
	}
	public InsuranceQingdaoMedical() {
		super();
	}
	public InsuranceQingdaoMedical(String taskid, String paydate, String belongdate, String compname, String paytype,
			String paybasenum, String perpay, String unitpay, String largerelief, String accountsum, String subsidy,
			String paystatus) {
		super();
		this.taskid = taskid;
		this.paydate = paydate;
		this.belongdate = belongdate;
		this.compname = compname;
		this.paytype = paytype;
		this.paybasenum = paybasenum;
		this.perpay = perpay;
		this.unitpay = unitpay;
		this.largerelief = largerelief;
		this.accountsum = accountsum;
		this.subsidy = subsidy;
		this.paystatus = paystatus;
	}
}
