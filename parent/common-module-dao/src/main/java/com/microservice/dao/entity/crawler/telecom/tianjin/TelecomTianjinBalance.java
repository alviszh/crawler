package com.microservice.dao.entity.crawler.telecom.tianjin;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 * 
 * @Description: 当前余额实体
 * @author sln
 * @date 2017年9月11日
 */
@Entity
@Table(name = "telecom_tianjin_balance",indexes = {@Index(name = "index_telecom_tianjin_balance_taskid", columnList = "taskid")})
public class TelecomTianjinBalance extends IdEntity implements Serializable {
	private static final long serialVersionUID = 6267897265619497249L;
	private String taskid;
//	当前余额
	private String querytime;
//	当前时间
	private String accountBalance;
	
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
	public String getAccountBalance() {
		return accountBalance;
	}
	public void setAccountBalance(String accountBalance) {
		this.accountBalance = accountBalance;
	}
	public TelecomTianjinBalance() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TelecomTianjinBalance(String taskid, String querytime, String accountBalance) {
		super();
		this.taskid = taskid;
		this.querytime = querytime;
		this.accountBalance = accountBalance;
	}
}
