package com.microservice.dao.entity.crawler.insurance.qingdao;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 * @Description: 青岛养老保险信息实体
 * @author sln
 * @date 2017年8月9日
 */
@Entity
@Table(name = "insurance_qingdao_pension",indexes = {@Index(name = "index_insurance_qingdao_pension_taskid", columnList = "taskid")})
public class InsuranceQingdaoPension extends IdEntity implements Serializable{
	private static final long serialVersionUID = 5273473947864101643L;
	private String taskid;
//	缴费年月
	private String paydate;
//	应属年月
	private String belongdate;
//	单位名称
	private String compname;
//	缴费类别
	private String paytype;
//	个人基数
	private String perpaybasenum;
//	个人缴费
	private String perpay;
//	单位划入账户
	private String unitpay;
//	社平划入
	private String societypayavg;
//	缴费标志
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
	public String getPerpaybasenum() {
		return perpaybasenum;
	}
	public void setPerpaybasenum(String perpaybasenum) {
		this.perpaybasenum = perpaybasenum;
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
	public String getSocietypayavg() {
		return societypayavg;
	}
	public void setSocietypayavg(String societypayavg) {
		this.societypayavg = societypayavg;
	}
	public String getPaystatus() {
		return paystatus;
	}
	public void setPaystatus(String paystatus) {
		this.paystatus = paystatus;
	}
	public InsuranceQingdaoPension() {
		super();
	}
	public InsuranceQingdaoPension(String taskid, String paydate, String belongdate, String compname, String paytype,
			String perpaybasenum, String perpay, String unitpay, String societypayavg, String paystatus) {
		super();
		this.taskid = taskid;
		this.paydate = paydate;
		this.belongdate = belongdate;
		this.compname = compname;
		this.paytype = paytype;
		this.perpaybasenum = perpaybasenum;
		this.perpay = perpay;
		this.unitpay = unitpay;
		this.societypayavg = societypayavg;
		this.paystatus = paystatus;
	}
}
