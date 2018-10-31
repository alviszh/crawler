package com.microservice.dao.entity.crawler.insurance.huzhou;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 * 湖州社保
 * @author zhaochunxiang
 *
 */
@Entity
@Table(name="insurance_huzhou_records",indexes = {@Index(name = "index_insurance_huzhou_records_taskid", columnList = "taskid")})
public class InsuranceHuzhouRecords extends IdEntity {

	private String planarea; // 统筹区
	private String companyname; // 单位名称
	private String type; // 险种名称
	private String payBase; // 缴费基数
	private String paymonth; // 缴费年月
	private String payamount; // 缴费金额
	private String companyamount;//单位应缴金额
	private String personamount;//个人应缴金额
	private String sign;//到账标识
	private String accountmonth;//到账年月
	private String taskid;
	
	public String getPlanarea() {
		return planarea;
	}
	public void setPlanarea(String planarea) {
		this.planarea = planarea;
	}
	public String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPayBase() {
		return payBase;
	}
	public void setPayBase(String payBase) {
		this.payBase = payBase;
	}
	public String getPaymonth() {
		return paymonth;
	}
	public void setPaymonth(String paymonth) {
		this.paymonth = paymonth;
	}
	public String getPayamount() {
		return payamount;
	}
	public void setPayamount(String payamount) {
		this.payamount = payamount;
	}
	public String getCompanyamount() {
		return companyamount;
	}
	public void setCompanyamount(String companyamount) {
		this.companyamount = companyamount;
	}
	public String getPersonamount() {
		return personamount;
	}
	public void setPersonamount(String personamount) {
		this.personamount = personamount;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getAccountmonth() {
		return accountmonth;
	}
	public void setAccountmonth(String accountmonth) {
		this.accountmonth = accountmonth;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public InsuranceHuzhouRecords() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public InsuranceHuzhouRecords(String planarea, String companyname, String type, String payBase, String paymonth,
			String payamount, String companyamount, String personamount, String sign,
			String accountmonth, String taskid) {
		super();
		this.planarea = planarea;
		this.companyname = companyname;
		this.type = type;
		this.payBase = payBase;
		this.paymonth = paymonth;
		this.payamount = payamount;
		this.companyamount = companyamount;
		this.personamount = personamount;
		this.sign = sign;
		this.accountmonth = accountmonth;
		this.taskid = taskid;
	}
	@Override
	public String toString() {
		return "InsuranceHuzhouRecords [planarea=" + planarea + ", companyname=" + companyname + ", type=" + type
				+ ", payBase=" + payBase + ", paymonth=" + paymonth + ", payamount=" + payamount + ", companyamount="
				+ companyamount + ", personamount=" + personamount + ", sign=" + sign
				+ ", accountmonth=" + accountmonth + ", taskid=" + taskid + "]";
	}
}
