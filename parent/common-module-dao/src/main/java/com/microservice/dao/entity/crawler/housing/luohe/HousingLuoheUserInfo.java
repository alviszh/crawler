package com.microservice.dao.entity.crawler.housing.luohe;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

/**
 * 用户信息
 * @author tz
 *
 */
@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "housing_luohe_userinfo" ,indexes = {@Index(name = "index_housing_luohe_userinfo_taskid", columnList = "taskid")})
public class HousingLuoheUserInfo extends IdEntity {

	/**
	 * taskid uuid 前端通过uuid访问状态结果
	 */
	private String taskid;

	/**
	 * 姓名
	 */
	private String name;

	/**
	 * 工资基数
	 */
	private String base;

	/**
	 * 月缴额
	 */
	private String monthly_pay;

	/**
	 * 缴存余额
	 */
	private String balance;

	/**
	 * 开户日期
	 */
	private String open_date;

	/**
	 * 缴至年月
	 */
	private String pay_year;

	/**
	 * 账户状态
	 */
	private String state;

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

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getMonthly_pay() {
		return monthly_pay;
	}

	public void setMonthly_pay(String monthly_pay) {
		this.monthly_pay = monthly_pay;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getOpen_date() {
		return open_date;
	}

	public void setOpen_date(String open_date) {
		this.open_date = open_date;
	}

	public String getPay_year() {
		return pay_year;
	}

	public void setPay_year(String pay_year) {
		this.pay_year = pay_year;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public HousingLuoheUserInfo(String taskid, String name, String base, String monthly_pay, String balance,
			String open_date, String pay_year, String state) {
		super();
		this.taskid = taskid;
		this.name = name;
		this.base = base;
		this.monthly_pay = monthly_pay;
		this.balance = balance;
		this.open_date = open_date;
		this.pay_year = pay_year;
		this.state = state;
	}

	public HousingLuoheUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

}
