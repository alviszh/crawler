package com.microservice.dao.entity.crawler.insurance.anqing;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;
/**
 * 安庆保险缴费明细
 * @author zcx
 *
 */
@Entity
@Table(name="insurance_anqing_paydetails")
public class InsuranceAnQingPaydetails extends IdEntity {

	private String type;//险种类型
	private String beginmonth;//建账年月
	private String accountbeginmonth;//账目起始年月
	private String accountendmonth;//账目截止年月
	private String personalbase;//个人缴费基数
	private String companyratio;//单位缴费比例
	private String companyamount;//单位缴费金额
	private String personalratio;//个人缴费比例
	private String personalamount;//个人缴费金额
	private String overduefine;//滞纳金
	private String interest;//利息
	private String checksign;//核实标志
	private String checktime;//核实时间

	private String taskid;//
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBeginmonth() {
		return beginmonth;
	}
	public void setBeginmonth(String beginmonth) {
		this.beginmonth = beginmonth;
	}
	public String getAccountbeginmonth() {
		return accountbeginmonth;
	}
	public void setAccountbeginmonth(String accountbeginmonth) {
		this.accountbeginmonth = accountbeginmonth;
	}
	public String getAccountendmonth() {
		return accountendmonth;
	}
	public void setAccountendmonth(String accountendmonth) {
		this.accountendmonth = accountendmonth;
	}
	public String getPersonalbase() {
		return personalbase;
	}
	public void setPersonalbase(String personalbase) {
		this.personalbase = personalbase;
	}
    
	public String getCompanyratio() {
		return companyratio;
	}
	public void setCompanyratio(String companyratio) {
		this.companyratio = companyratio;
	}
	public String getCompanyamount() {
		return companyamount;
	}
	public void setCompanyamount(String companyamount) {
		this.companyamount = companyamount;
	}
	public String getPersonalratio() {
		return personalratio;
	}
	public void setPersonalratio(String personalratio) {
		this.personalratio = personalratio;
	}
	public String getPersonalamount() {
		return personalamount;
	}
	public void setPersonalamount(String personalamount) {
		this.personalamount = personalamount;
	}
	public String getOverduefine() {
		return overduefine;
	}
	public void setOverduefine(String overduefine) {
		this.overduefine = overduefine;
	}
	public String getInterest() {
		return interest;
	}
	public void setInterest(String interest) {
		this.interest = interest;
	}
	public String getChecksign() {
		return checksign;
	}
	public void setChecksign(String checksign) {
		this.checksign = checksign;
	}
	public String getChecktime() {
		return checktime;
	}
	public void setChecktime(String checktime) {
		this.checktime = checktime;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	@Override
	public String toString() {
		return "InsuranceAnQingPaydetails [type=" + type + ", beginmonth=" + beginmonth + ", accountbeginmonth="
				+ accountbeginmonth + ", accountendmonth=" + accountendmonth + ", personalbase=" + personalbase
				+ ", companyratio=" + companyratio + ", companyamount=" + companyamount + ", personalratio="
				+ personalratio + ", personalamount=" + personalamount + ", overduefine=" + overduefine + ", interest="
				+ interest + ", checksign=" + checksign + ", checktime=" + checktime + ", taskid=" + taskid + "]";
	}
	public InsuranceAnQingPaydetails() {
		super();
		// TODO Auto-generated constructor stub
	}
	public InsuranceAnQingPaydetails(String type, String beginmonth, String accountbeginmonth, String accountendmonth,
			String personalbase, String companyratio, String companyamount, String personalratio, String personalamount,
			String overduefine, String interest, String checksign, String checktime, String taskid) {
		super();
		this.type = type;
		this.beginmonth = beginmonth;
		this.accountbeginmonth = accountbeginmonth;
		this.accountendmonth = accountendmonth;
		this.personalbase = personalbase;
		this.companyratio = companyratio;
		this.companyamount = companyamount;
		this.personalratio = personalratio;
		this.personalamount = personalamount;
		this.overduefine = overduefine;
		this.interest = interest;
		this.checksign = checksign;
		this.checktime = checktime;
		this.taskid = taskid;
	}
}
