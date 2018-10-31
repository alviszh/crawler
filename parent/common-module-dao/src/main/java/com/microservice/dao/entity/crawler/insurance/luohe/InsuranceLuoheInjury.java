package com.microservice.dao.entity.crawler.insurance.luohe;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;


/**
 * 工伤
 * @author tz
 *
 */
@Entity
@Table(name = "insurance_luohe_injury" ,indexes = {@Index(name = "index_insurance_luohe_injury_taskid", columnList = "taskid")})
public class InsuranceLuoheInjury extends IdEntity implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -3461048441103039458L;
	private String taskid;
	/**
	 * 计划月份
	 */
	private String plan_month;
	/**
	 * 征缴月份
	 */
	private String impose_month;
	/**
	 * 缴费基数
	 */
	private String pay_num;
	/**
	 * 单位缴费
	 */
	private String dw_pay;
	/**
	 * 个人缴费
	 */
	private String gr_pay;
	/**
	 * 合计
	 */
	private String total;
	/**
	 * 记入个人部分
	 */
	private String write_gr;
	/**
	 * 缴费标志
	 */
	private String pay_sign;
	/**
	 * 到账日期
	 */
	private String arrivetime;
	/**
	 * 单位名称
	 */
	private String dw_name;
	/**
	 * 单位编号
	 */
	private String dw_num;
	/**
	 * 摘要
	 */
	private String abstracts;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getPlan_month() {
		return plan_month;
	}
	public void setPlan_month(String plan_month) {
		this.plan_month = plan_month;
	}
	public String getImpose_month() {
		return impose_month;
	}
	public void setImpose_month(String impose_month) {
		this.impose_month = impose_month;
	}
	public String getPay_num() {
		return pay_num;
	}
	public void setPay_num(String pay_num) {
		this.pay_num = pay_num;
	}
	public String getDw_pay() {
		return dw_pay;
	}
	public void setDw_pay(String dw_pay) {
		this.dw_pay = dw_pay;
	}
	public String getGr_pay() {
		return gr_pay;
	}
	public void setGr_pay(String gr_pay) {
		this.gr_pay = gr_pay;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getWrite_gr() {
		return write_gr;
	}
	public void setWrite_gr(String write_gr) {
		this.write_gr = write_gr;
	}
	public String getPay_sign() {
		return pay_sign;
	}
	public void setPay_sign(String pay_sign) {
		this.pay_sign = pay_sign;
	}
	public String getArrivetime() {
		return arrivetime;
	}
	public void setArrivetime(String arrivetime) {
		this.arrivetime = arrivetime;
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
	public String getAbstracts() {
		return abstracts;
	}
	public void setAbstracts(String abstracts) {
		this.abstracts = abstracts;
	}
	public InsuranceLuoheInjury(String taskid, String plan_month, String impose_month, String pay_num, String dw_pay,
			String gr_pay, String total, String write_gr, String pay_sign, String arrivetime, String dw_name,
			String dw_num, String abstracts) {
		super();
		this.taskid = taskid;
		this.plan_month = plan_month;
		this.impose_month = impose_month;
		this.pay_num = pay_num;
		this.dw_pay = dw_pay;
		this.gr_pay = gr_pay;
		this.total = total;
		this.write_gr = write_gr;
		this.pay_sign = pay_sign;
		this.arrivetime = arrivetime;
		this.dw_name = dw_name;
		this.dw_num = dw_num;
		this.abstracts = abstracts;
	}
	public InsuranceLuoheInjury() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
