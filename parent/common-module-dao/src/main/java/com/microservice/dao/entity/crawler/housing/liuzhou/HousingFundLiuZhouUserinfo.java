package com.microservice.dao.entity.crawler.housing.liuzhou;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "housing_liuzhou_userinfo",indexes = {@Index(name = "index_housing_liuzhou_userinfo_taskid", columnList = "taskid")})
public class HousingFundLiuZhouUserinfo extends IdEntity implements Serializable{

	private String taskid;
	private String name;//姓名
	private String num;//个人编号
	private String pay_state;//缴存状态
	private String dw_num;//单位帐号
	private String idcard;//身份证
	private String dw_name;//单位名称
	private String wd_name;//所属网点名称
	private String mon_paybase;//月缴存基数
	private String dw_payratio;//单位缴存比例(%)
	private String gr_payratio;//个人缴存比例(%)
	private String dw_pay;//单位月缴存额
	private String gr_pay;//个人月缴存额
	private String total;//合计月缴存额
	private String extract;//提取额
	private String balance;//账户余额
	private String time;//当前缴至年月
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getPay_state() {
		return pay_state;
	}
	public void setPay_state(String pay_state) {
		this.pay_state = pay_state;
	}
	public String getDw_num() {
		return dw_num;
	}
	public void setDw_num(String dw_num) {
		this.dw_num = dw_num;
	}
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public String getDw_name() {
		return dw_name;
	}
	public void setDw_name(String dw_name) {
		this.dw_name = dw_name;
	}
	public String getWd_name() {
		return wd_name;
	}
	public void setWd_name(String wd_name) {
		this.wd_name = wd_name;
	}
	public String getMon_paybase() {
		return mon_paybase;
	}
	public void setMon_paybase(String mon_paybase) {
		this.mon_paybase = mon_paybase;
	}
	public String getDw_payratio() {
		return dw_payratio;
	}
	public void setDw_payratio(String dw_payratio) {
		this.dw_payratio = dw_payratio;
	}
	public String getGr_payratio() {
		return gr_payratio;
	}
	public void setGr_payratio(String gr_payratio) {
		this.gr_payratio = gr_payratio;
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
	public String getExtract() {
		return extract;
	}
	public void setExtract(String extract) {
		this.extract = extract;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "HousingFundLiuZhouUserinfo [taskid=" + taskid + ", name=" + name + ", num=" + num + ", pay_state="
				+ pay_state + ", dw_num=" + dw_num + ", idcard=" + idcard + ", dw_name=" + dw_name + ", wd_name="
				+ wd_name + ", mon_paybase=" + mon_paybase + ", dw_payratio=" + dw_payratio + ", gr_payratio="
				+ gr_payratio + ", dw_pay=" + dw_pay + ", gr_pay=" + gr_pay + ", total=" + total + ", extract="
				+ extract + ", balance=" + balance + ", time=" + time + "]";
	}
	public HousingFundLiuZhouUserinfo(String taskid, String name, String num, String pay_state, String dw_num,
			String idcard, String dw_name, String wd_name, String mon_paybase, String dw_payratio, String gr_payratio,
			String dw_pay, String gr_pay, String total, String extract, String balance, String time) {
		super();
		this.taskid = taskid;
		this.name = name;
		this.num = num;
		this.pay_state = pay_state;
		this.dw_num = dw_num;
		this.idcard = idcard;
		this.dw_name = dw_name;
		this.wd_name = wd_name;
		this.mon_paybase = mon_paybase;
		this.dw_payratio = dw_payratio;
		this.gr_payratio = gr_payratio;
		this.dw_pay = dw_pay;
		this.gr_pay = gr_pay;
		this.total = total;
		this.extract = extract;
		this.balance = balance;
		this.time = time;
	}
	public HousingFundLiuZhouUserinfo() {
		super();
	}
	
	
}
