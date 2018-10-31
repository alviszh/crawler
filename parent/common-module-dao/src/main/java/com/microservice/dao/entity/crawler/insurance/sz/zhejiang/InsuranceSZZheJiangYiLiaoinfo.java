package com.microservice.dao.entity.crawler.insurance.sz.zhejiang;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "insurance_sz_zhejiang_yiliaoinfo",indexes = {@Index(name = "index_insurance_sz_zhejiang_yiliaoinfo_taskid", columnList = "taskid")})
public class InsuranceSZZheJiangYiLiaoinfo extends IdEntity{

	private String taskid;
	private String month;//缴费年月
	private String paybase;//缴费基数
	private String perpaymoney;//个人缴费金额
	private String dw_name;//公司名称
	private String paysign;//缴费类型
	private String state;//缴纳状态
	private String insurancetype;//险种类型
	private String bdate;//变动日期
	private String beikao;//备考
	private String expenditure;//支出金额
	private String income;//收入金额
	private String ei_type;//收支类型
	
	public InsuranceSZZheJiangYiLiaoinfo(String taskid, String insurancetype, String bdate, String beikao,
			String expenditure, String income, String ei_type) {
		super();
		this.taskid = taskid;
		this.insurancetype = insurancetype;
		this.bdate = bdate;
		this.beikao = beikao;
		this.expenditure = expenditure;
		this.income = income;
		this.ei_type = ei_type;
	}
	public String getBdate() {
		return bdate;
	}
	public void setBdate(String bdate) {
		this.bdate = bdate;
	}
	public String getBeikao() {
		return beikao;
	}
	public void setBeikao(String beikao) {
		this.beikao = beikao;
	}
	public String getExpenditure() {
		return expenditure;
	}
	public void setExpenditure(String expenditure) {
		this.expenditure = expenditure;
	}
	public String getIncome() {
		return income;
	}
	public void setIncome(String income) {
		this.income = income;
	}
	public String getEi_type() {
		return ei_type;
	}
	public void setEi_type(String ei_type) {
		this.ei_type = ei_type;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getPaybase() {
		return paybase;
	}
	public void setPaybase(String paybase) {
		this.paybase = paybase;
	}
	public String getPerpaymoney() {
		return perpaymoney;
	}
	public void setPerpaymoney(String perpaymoney) {
		this.perpaymoney = perpaymoney;
	}
	public String getDw_name() {
		return dw_name;
	}
	public void setDw_name(String dw_name) {
		this.dw_name = dw_name;
	}
	public String getPaysign() {
		return paysign;
	}
	public void setPaysign(String paysign) {
		this.paysign = paysign;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getInsurancetype() {
		return insurancetype;
	}
	public void setInsurancetype(String insurancetype) {
		this.insurancetype = insurancetype;
	}
	public InsuranceSZZheJiangYiLiaoinfo() {
		super();
	}
	public InsuranceSZZheJiangYiLiaoinfo(String taskid, String month, String paybase, String perpaymoney,
			String dw_name, String paysign, String state, String insurancetype) {
		super();
		this.taskid = taskid;
		this.month = month;
		this.paybase = paybase;
		this.perpaymoney = perpaymoney;
		this.dw_name = dw_name;
		this.paysign = paysign;
		this.state = state;
		this.insurancetype = insurancetype;
	}
	@Override
	public String toString() {
		return "InsuranceSZZheJiangYiLiaoinfo [taskid=" + taskid + ", month=" + month + ", paybase=" + paybase
				+ ", perpaymoney=" + perpaymoney + ", dw_name=" + dw_name + ", paysign=" + paysign + ", state=" + state
				+ ", insurancetype=" + insurancetype + "]";
	}
	
}
