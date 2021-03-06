package com.microservice.dao.entity.crawler.insurance.jiyuan;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "insurance_jiyuan_gongshanginfo",indexes = {@Index(name = "index_insurance_jiyuan_gongshanginfo_taskid", columnList = "taskid")})
public class InsuranceJiYuanGongShangInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = -7225639204374657354L;
	private String taskid;
	
	private String abstracts;//摘要
	private String total;//合计
	private String pay_num;//缴费基数
	private String gr_pay;//个人缴费
	private String dw_pay;//单位缴费
	private String arrivetime;//到账日期
	private String pay_sign;//缴费标志
	private String write_gr;//记入个人部分
	private String impose_month;//征缴月份
	private String plan_month;//计划月份
	private String dw_name;//单位名称
	private String dw_num;//单位编号
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getAbstracts() {
		return abstracts;
	}
	public void setAbstracts(String abstracts) {
		this.abstracts = abstracts;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getPay_num() {
		return pay_num;
	}
	public void setPay_num(String pay_num) {
		this.pay_num = pay_num;
	}
	public String getGr_pay() {
		return gr_pay;
	}
	public void setGr_pay(String gr_pay) {
		this.gr_pay = gr_pay;
	}
	public String getDw_pay() {
		return dw_pay;
	}
	public void setDw_pay(String dw_pay) {
		this.dw_pay = dw_pay;
	}
	public String getArrivetime() {
		return arrivetime;
	}
	public void setArrivetime(String arrivetime) {
		this.arrivetime = arrivetime;
	}
	public String getPay_sign() {
		return pay_sign;
	}
	public void setPay_sign(String pay_sign) {
		this.pay_sign = pay_sign;
	}
	public String getWrite_gr() {
		return write_gr;
	}
	public void setWrite_gr(String write_gr) {
		this.write_gr = write_gr;
	}
	public String getImpose_month() {
		return impose_month;
	}
	public void setImpose_month(String impose_month) {
		this.impose_month = impose_month;
	}
	public String getPlan_month() {
		return plan_month;
	}
	public void setPlan_month(String plan_month) {
		this.plan_month = plan_month;
	}
	public String getDw_name() {
		return dw_name;
	}
	public void setDw_name(String dw_name) {
		this.dw_name = dw_name;
	}
	public String getDw_num() {
		return dw_num;
	}
	public void setDw_num(String dw_num) {
		this.dw_num = dw_num;
	}
	public InsuranceJiYuanGongShangInfo(String taskid, String abstracts, String total, String pay_num, String gr_pay,
			String dw_pay, String arrivetime, String pay_sign, String write_gr, String impose_month, String plan_month,
			String dw_name, String dw_num) {
		super();
		this.taskid = taskid;
		this.abstracts = abstracts;
		this.total = total;
		this.pay_num = pay_num;
		this.gr_pay = gr_pay;
		this.dw_pay = dw_pay;
		this.arrivetime = arrivetime;
		this.pay_sign = pay_sign;
		this.write_gr = write_gr;
		this.impose_month = impose_month;
		this.plan_month = plan_month;
		this.dw_name = dw_name;
		this.dw_num = dw_num;
	}
	public InsuranceJiYuanGongShangInfo() {
		super();
	}
	@Override
	public String toString() {
		return "InsuranceJiYuanYangLaoInfo [taskid=" + taskid + ", abstracts=" + abstracts + ", total=" + total
				+ ", pay_num=" + pay_num + ", gr_pay=" + gr_pay + ", dw_pay=" + dw_pay + ", arrivetime=" + arrivetime
				+ ", pay_sign=" + pay_sign + ", write_gr=" + write_gr + ", impose_month=" + impose_month
				+ ", plan_month=" + plan_month + ", dw_name=" + dw_name + ", dw_num=" + dw_num + "]";
	}
	
	
	
}
