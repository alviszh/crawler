package com.microservice.dao.entity.crawler.telecom.chongqing;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;


/**
 * 重庆用户余额
 * @author tz
 *
 */
@Entity
@Table(name = "telecom_chongqing_balance" ,indexes = {@Index(name = "index_telecom_chongqing_balance_taskid", columnList = "taskid")})
public class TelecomChongqingBalance extends IdEntity {
	
	/**
	 * taskid
	 */
	private String taskid;
	
	/**
	 * 时间
	 */
	private String time;
	
	/**
	 * 余额(元)
	 */
	private String money;
	
	/**
	 * 余额类别
	 */
	private String type;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "TelecomChongqingBalance [taskid=" + taskid + ", time=" + time + ", money=" + money + ", type=" + type
				+ "]";
	}

	public TelecomChongqingBalance(String taskid, String time, String money, String type) {
		super();
		this.taskid = taskid;
		this.time = time;
		this.money = money;
		this.type = type;
	}

	public TelecomChongqingBalance() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}