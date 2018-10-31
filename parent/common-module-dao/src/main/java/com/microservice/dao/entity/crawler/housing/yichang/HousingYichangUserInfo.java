package com.microservice.dao.entity.crawler.housing.yichang;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 用户信息
 * @author tz
 *
 */
@Entity
@Table(name = "housing_yichang_userinfo" ,indexes = {@Index(name = "index_housing_yichang_userinfo_taskid", columnList = "taskid")})
public class HousingYichangUserInfo extends IdEntity {

	/**
	 * taskid uuid 前端通过uuid访问状态结果
	 */
	private String taskid;

	/**
	 * 个人账户
	 */
	private String gr_num;
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 缴存单位
	 */
	private String company;
	/**
	 * 公积金总额
	 */
	private String total;
	/**
	 * 每月应缴纳金额
	 */
	private String monthly_payment;
	/**
	 * 缴至年月
	 */
	private String year_month;
	/**
	 * 单位月缴额
	 */
	private String dw_monthly_pay;
	
	/**
	 * 个人月缴额
	 */
	private String gr_Monthly_Pay;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getGr_num() {
		return gr_num;
	}

	public void setGr_num(String gr_num) {
		this.gr_num = gr_num;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getMonthly_payment() {
		return monthly_payment;
	}

	public void setMonthly_payment(String monthly_payment) {
		this.monthly_payment = monthly_payment;
	}

	public String getYear_month() {
		return year_month;
	}

	public void setYear_month(String year_month) {
		this.year_month = year_month;
	}

	public String getDw_monthly_pay() {
		return dw_monthly_pay;
	}

	public void setDw_monthly_pay(String dw_monthly_pay) {
		this.dw_monthly_pay = dw_monthly_pay;
	}

	public String getGr_Monthly_Pay() {
		return gr_Monthly_Pay;
	}

	public void setGr_Monthly_Pay(String gr_Monthly_Pay) {
		this.gr_Monthly_Pay = gr_Monthly_Pay;
	}

	public HousingYichangUserInfo() {
		super();
	}

	public HousingYichangUserInfo(String taskid, String gr_num, String name, String company, String total,
			String monthly_payment, String year_month, String dw_monthly_pay, String gr_Monthly_Pay) {
		super();
		this.taskid = taskid;
		this.gr_num = gr_num;
		this.name = name;
		this.company = company;
		this.total = total;
		this.monthly_payment = monthly_payment;
		this.year_month = year_month;
		this.dw_monthly_pay = dw_monthly_pay;
		this.gr_Monthly_Pay = gr_Monthly_Pay;
	}
	
}
