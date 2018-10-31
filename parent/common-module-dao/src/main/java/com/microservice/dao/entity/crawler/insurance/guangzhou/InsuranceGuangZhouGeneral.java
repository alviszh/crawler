package com.microservice.dao.entity.crawler.insurance.guangzhou;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/*
 * 社保综合实体类（养老、失业、工伤、生育）
 */
@Entity
@Table(name="insurance_guangzhou_general")
public class InsuranceGuangZhouGeneral extends IdEntity{

//	
	private String payStartDate;				//开始缴费日期
	private String payEndDate;					//终止缴费日期
	private String monthCum;					//累计月数
	private String payBase;						//缴费基数
	private String pensionCompanyPay;			//养老单位缴费
	private String pensionPersonalPay;			//养老个人缴费
	private String unemploymentCompanyPay;		//失业单位缴费
	private String unemploymentPersonalPay;		//失业个人缴费
	private String accidentsPay;				//工伤
	private String maternityPay;				//生育
	private String organizationnum;				//单位编号
	private String organizationname;			//单位名称
	private String checkmode;					//核定方式
	private String taskid;
	public String getPayStartDate() {
		return payStartDate;
	}
	public void setPayStartDate(String payStartDate) {
		this.payStartDate = payStartDate;
	}
	public String getPayEndDate() {
		return payEndDate;
	}
	public void setPayEndDate(String payEndDate) {
		this.payEndDate = payEndDate;
	}
	public String getMonthCum() {
		return monthCum;
	}
	public void setMonthCum(String monthCum) {
		this.monthCum = monthCum;
	}
	public String getPayBase() {
		return payBase;
	}
	public void setPayBase(String payBase) {
		this.payBase = payBase;
	}
	public String getPensionCompanyPay() {
		return pensionCompanyPay;
	}
	public void setPensionCompanyPay(String pensionCompanyPay) {
		this.pensionCompanyPay = pensionCompanyPay;
	}
	public String getPensionPersonalPay() {
		return pensionPersonalPay;
	}
	public void setPensionPersonalPay(String pensionPersonalPay) {
		this.pensionPersonalPay = pensionPersonalPay;
	}
	public String getUnemploymentCompanyPay() {
		return unemploymentCompanyPay;
	}
	public void setUnemploymentCompanyPay(String unemploymentCompanyPay) {
		this.unemploymentCompanyPay = unemploymentCompanyPay;
	}
	public String getUnemploymentPersonalPay() {
		return unemploymentPersonalPay;
	}
	public void setUnemploymentPersonalPay(String unemploymentPersonalPay) {
		this.unemploymentPersonalPay = unemploymentPersonalPay;
	}
	public String getAccidentsPay() {
		return accidentsPay;
	}
	public void setAccidentsPay(String accidentsPay) {
		this.accidentsPay = accidentsPay;
	}
	public String getMaternityPay() {
		return maternityPay;
	}
	public void setMaternityPay(String maternityPay) {
		this.maternityPay = maternityPay;
	}
	public String getOrganizationnum() {
		return organizationnum;
	}
	public void setOrganizationnum(String organizationnum) {
		this.organizationnum = organizationnum;
	}
	public String getOrganizationname() {
		return organizationname;
	}
	public void setOrganizationname(String organizationname) {
		this.organizationname = organizationname;
	}
	public String getCheckmode() {
		return checkmode;
	}
	public void setCheckmode(String checkmode) {
		this.checkmode = checkmode;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Override
	public String toString() {
		return "InsuranceGuangZhouGeneral [payStartDate=" + payStartDate + ", payEndDate=" + payEndDate + ", monthCum="
				+ monthCum + ", payBase=" + payBase + ", pensionCompanyPay=" + pensionCompanyPay
				+ ", pensionPersonalPay=" + pensionPersonalPay + ", unemploymentCompanyPay=" + unemploymentCompanyPay
				+ ", unemploymentPersonalPay=" + unemploymentPersonalPay + ", accidentsPay=" + accidentsPay
				+ ", maternityPay=" + maternityPay + ", organizationnum=" + organizationnum + ", organizationname="
				+ organizationname + ", checkmode=" + checkmode + ", taskid=" + taskid + "]";
	} 
	
	
}
