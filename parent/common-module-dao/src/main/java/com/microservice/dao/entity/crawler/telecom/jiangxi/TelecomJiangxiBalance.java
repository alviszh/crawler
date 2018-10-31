package com.microservice.dao.entity.crawler.telecom.jiangxi;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 * 
 * @Description: 当前余额实体
 * @author sln
 * @date 2017年9月16日
 */
@Entity
@Table(name = "telecom_jiangxi_balance",indexes = {@Index(name = "index_telecom_jiangxi_balance_taskid", columnList = "taskid")})
public class TelecomJiangxiBalance extends IdEntity implements Serializable {
	private static final long serialVersionUID = -324109488783585266L;
	private String taskid;
//	当前时间
	private String querytime;
//	当前余额
	private String balance;
	
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getQuerytime() {
		return querytime;
	}
	public void setQuerytime(String querytime) {
		this.querytime = querytime;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public TelecomJiangxiBalance() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TelecomJiangxiBalance(String taskid, String querytime, String balance) {
		super();
		this.taskid = taskid;
		this.querytime = querytime;
		this.balance = balance;
	}
	
	
}
