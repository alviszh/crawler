package com.microservice.dao.entity.crawler.insurance.lanzhou;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "insurance_lanzhou_pensioninfo",indexes = {@Index(name = "index_insurance_lanzhou_pensioninfo_taskid", columnList = "taskid")})
public class InsuranceLanZhouPensionInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = 5273473947864101643L;
	private String taskid;
//	费款所属期
	private String periodofpay;
//	缴费基数
	private String chargebasenum;
//	社平工资
	private String socialavgwage;
//	单位划账户
	private String unitaccount;
//	个人划账户
	private String peraccount;
//	合计金额
	private String totalamount;
//	缴费月数
	private String chargemohths;
//	账户记账期
	private String accountdate;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getPeriodofpay() {
		return periodofpay;
	}
	public void setPeriodofpay(String periodofpay) {
		this.periodofpay = periodofpay;
	}
	public String getChargebasenum() {
		return chargebasenum;
	}
	public void setChargebasenum(String chargebasenum) {
		this.chargebasenum = chargebasenum;
	}
	public String getSocialavgwage() {
		return socialavgwage;
	}
	public void setSocialavgwage(String socialavgwage) {
		this.socialavgwage = socialavgwage;
	}
	public String getUnitaccount() {
		return unitaccount;
	}
	public void setUnitaccount(String unitaccount) {
		this.unitaccount = unitaccount;
	}
	public String getPeraccount() {
		return peraccount;
	}
	public void setPeraccount(String peraccount) {
		this.peraccount = peraccount;
	}
	public String getTotalamount() {
		return totalamount;
	}
	public void setTotalamount(String totalamount) {
		this.totalamount = totalamount;
	}
	public String getChargemohths() {
		return chargemohths;
	}
	public void setChargemohths(String chargemohths) {
		this.chargemohths = chargemohths;
	}
	public String getAccountdate() {
		return accountdate;
	}
	public void setAccountdate(String accountdate) {
		this.accountdate = accountdate;
	}
	public InsuranceLanZhouPensionInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
}