package com.microservice.dao.entity.crawler.insurance.shijiazhuang;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "insurance_shijiazhuang_aged",indexes = {@Index(name = "index_insurance_shijiazhuang_aged_taskid", columnList = "taskid")})
public class StreamAgedShiJiaZhuang extends IdEntity implements Serializable{
	private static final long serialVersionUID = -8027379993812936680L;
	private String taskid;
	private String aged_insur_name; //  缴费类型（保险名称）
	private String aged_insur_pay_base;	 //	缴纳基数(当年缴费基数)
	private String aged_insur_pay_per; //	个人缴费金额（无单位缴费明细）
	private String aged_insur_thisyearmonth; // 当年缴费月数
	private String aged_insuer_allyearmonth;  //多年缴费累计月数
	private String aged_insur_year; //	统计年份
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getAged_insur_name() {
		return aged_insur_name;
	}
	public void setAged_insur_name(String aged_insur_name) {
		this.aged_insur_name = aged_insur_name;
	}
	public String getAged_insur_pay_base() {
		return aged_insur_pay_base;
	}
	public void setAged_insur_pay_base(String aged_insur_pay_base) {
		this.aged_insur_pay_base = aged_insur_pay_base;
	}
	public String getAged_insur_pay_per() {
		return aged_insur_pay_per;
	}
	public void setAged_insur_pay_per(String aged_insur_pay_per) {
		this.aged_insur_pay_per = aged_insur_pay_per;
	}
	public String getAged_insur_thisyearmonth() {
		return aged_insur_thisyearmonth;
	}
	public void setAged_insur_thisyearmonth(String aged_insur_thisyearmonth) {
		this.aged_insur_thisyearmonth = aged_insur_thisyearmonth;
	}
	public String getAged_insuer_allyearmonth() {
		return aged_insuer_allyearmonth;
	}
	public void setAged_insuer_allyearmonth(String aged_insuer_allyearmonth) {
		this.aged_insuer_allyearmonth = aged_insuer_allyearmonth;
	}
	public String getAged_insur_year() {
		return aged_insur_year;
	}
	public void setAged_insur_year(String aged_insur_year) {
		this.aged_insur_year = aged_insur_year;
	}
	public StreamAgedShiJiaZhuang() {
		super();
		// TODO Auto-generated constructor stub
	}
	public StreamAgedShiJiaZhuang(String taskid, String aged_insur_name, String aged_insur_pay_base,
			String aged_insur_pay_per, String aged_insur_thisyearmonth, String aged_insuer_allyearmonth,
			String aged_insur_year) {
		super();
		this.taskid = taskid;
		this.aged_insur_name = aged_insur_name;
		this.aged_insur_pay_base = aged_insur_pay_base;
		this.aged_insur_pay_per = aged_insur_pay_per;
		this.aged_insur_thisyearmonth = aged_insur_thisyearmonth;
		this.aged_insuer_allyearmonth = aged_insuer_allyearmonth;
		this.aged_insur_year = aged_insur_year;
	}
	
	
}
