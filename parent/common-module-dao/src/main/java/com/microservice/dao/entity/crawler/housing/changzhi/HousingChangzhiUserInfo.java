package com.microservice.dao.entity.crawler.housing.changzhi;

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
@Table(name = "housing_changzhi_userinfo" ,indexes = {@Index(name = "index_housing_changzhi_userinfo_taskid", columnList = "taskid")})
public class HousingChangzhiUserInfo extends IdEntity {

	/**
	 * taskid uuid 前端通过uuid访问状态结果
	 */
	private String taskid;

	/**
	 * 当前余额
	 */
	private String balance;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public HousingChangzhiUserInfo(String taskid, String balance) {
		super();
		this.taskid = taskid;
		this.balance = balance;
	}

	public HousingChangzhiUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
