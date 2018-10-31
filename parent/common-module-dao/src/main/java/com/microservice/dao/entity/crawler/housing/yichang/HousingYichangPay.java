package com.microservice.dao.entity.crawler.housing.yichang;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 缴存明细
 * @author tz
 *
 */
@Entity
@Table(name = "housing_yichang_pay" ,indexes = {@Index(name = "index_housing_yichang_pay_taskid", columnList = "taskid")})
public class HousingYichangPay extends IdEntity {

	/**
	 * taskid uuid 前端通过uuid访问状态结果
	 */
	private String taskid;

	/**
	 * 记账日期
	 */
	private String date;
	
	/**
	 * 发生额
	 */
	private String money;
	
	/**
	 * 业务类型
	 */
	private String business_type;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getBusiness_type() {
		return business_type;
	}

	public void setBusiness_type(String business_type) {
		this.business_type = business_type;
	}

	public HousingYichangPay() {
		super();
	}

	public HousingYichangPay(String taskid, String date, String money, String business_type) {
		super();
		this.taskid = taskid;
		this.date = date;
		this.money = money;
		this.business_type = business_type;
	}
	
	
}
