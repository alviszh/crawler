package com.microservice.dao.entity.crawler.housing.dandong;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "housing_dandong_userinfo",indexes = {@Index(name = "index_housing_dandong_userinfo_taskid", columnList = "taskid")})
public class HousingDanDongUserinfo extends IdEntity implements Serializable{

	private String taskid;
	
	private String dwnum;
	
	private String dwname;
	
	private String num;
	
	private String name;
	
	private String certificate_type;
	
	private String certificate_num;
	
	private String phone;
	
	private String monthmon;
	
	private String dw_month;
	
	private String gr_month;
	
	private String year_pay;
	
	private String year_get;
	
	private String capital;//本金
	
	private String interest;//利息
	
	private String practical_pay;//实际缴纳
	


	public HousingDanDongUserinfo() {
		super();
	}

	public HousingDanDongUserinfo(String taskid, String dwnum, String dwname, String num, String name,
			String certificate_type, String certificate_num, String phone, String monthmon, String dw_month,
			String gr_month, String year_pay, String year_get, String capital, String interest, String practical_pay) {
		super();
		this.taskid = taskid;
		this.dwnum = dwnum;
		this.dwname = dwname;
		this.num = num;
		this.name = name;
		this.certificate_type = certificate_type;
		this.certificate_num = certificate_num;
		this.phone = phone;
		this.monthmon = monthmon;
		this.dw_month = dw_month;
		this.gr_month = gr_month;
		this.year_pay = year_pay;
		this.year_get = year_get;
		this.capital = capital;
		this.interest = interest;
		this.practical_pay = practical_pay;
	}

	public String getMonthmon() {
		return monthmon;
	}

	public void setMonthmon(String monthmon) {
		this.monthmon = monthmon;
	}

	public String getDw_month() {
		return dw_month;
	}

	public void setDw_month(String dw_month) {
		this.dw_month = dw_month;
	}

	public String getGr_month() {
		return gr_month;
	}

	public void setGr_month(String gr_month) {
		this.gr_month = gr_month;
	}

	public String getYear_pay() {
		return year_pay;
	}

	public void setYear_pay(String year_pay) {
		this.year_pay = year_pay;
	}

	public String getYear_get() {
		return year_get;
	}

	public void setYear_get(String year_get) {
		this.year_get = year_get;
	}

	public String getCapital() {
		return capital;
	}

	public void setCapital(String capital) {
		this.capital = capital;
	}

	public String getInterest() {
		return interest;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}

	public String getPractical_pay() {
		return practical_pay;
	}

	public void setPractical_pay(String practical_pay) {
		this.practical_pay = practical_pay;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getDwnum() {
		return dwnum;
	}

	public void setDwnum(String dwnum) {
		this.dwnum = dwnum;
	}

	public String getDwname() {
		return dwname;
	}

	public void setDwname(String dwname) {
		this.dwname = dwname;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCertificate_type() {
		return certificate_type;
	}

	public void setCertificate_type(String certificate_type) {
		this.certificate_type = certificate_type;
	}

	public String getCertificate_num() {
		return certificate_num;
	}

	public void setCertificate_num(String certificate_num) {
		this.certificate_num = certificate_num;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
}
