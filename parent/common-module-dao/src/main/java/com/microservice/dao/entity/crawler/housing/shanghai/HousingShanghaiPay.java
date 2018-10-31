package com.microservice.dao.entity.crawler.housing.shanghai;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

/**
 * 缴存明细
 * @author Administrator
 *
 */
@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "housing_shanghai_pay" ,indexes = {@Index(name = "index_housing_shanghai_pay_taskid", columnList = "taskid")})
public class HousingShanghaiPay extends IdEntity {

	/**
	 * taskid uuid 前端通过uuid访问状态结果
	 */
	private String taskid;

	/**
	 * 日期
	 */
	private String date;

	/**
	 * 单位名称
	 */
	private String company;

	/**
	 * 金额（元）
	 */
	private String money;

	/**
	 * 业务描述
	 */
	private String description;

	/**
	 * 业务原因
	 */
	private String reason;

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

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Override
	public String toString() {
		return "HousingShanghaiPay [taskid=" + taskid + ", date=" + date + ", company=" + company + ", money=" + money
				+ ", description=" + description + ", reason=" + reason + "]";
	}

	public HousingShanghaiPay(String taskid, String date, String company, String money, String description,
			String reason) {
		super();
		this.taskid = taskid;
		this.date = date;
		this.company = company;
		this.money = money;
		this.description = description;
		this.reason = reason;
	}

	public HousingShanghaiPay() {
		super();
		// TODO Auto-generated constructor stub
	}


}
